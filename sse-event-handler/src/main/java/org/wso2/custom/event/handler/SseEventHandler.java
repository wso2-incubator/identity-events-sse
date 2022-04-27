/*
 *
 *  * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.com).
 *  *
 *  * WSO2 Inc. licenses this file to you under the Apache License,
 *  * Version 2.0 (the "License"); you may not use this file except
 *  * in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  * http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing,
 *  * software distributed under the License is distributed on an
 *  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  * KIND, either express or implied.  See the License for the
 *  * specific language governing permissions and limitations
 *  * under the License.
 *
 */

package org.wso2.custom.event.handler;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTClaimsSet;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
import org.wso2.carbon.identity.event.IdentityEventConstants;
import org.wso2.carbon.identity.event.IdentityEventException;
import org.wso2.carbon.identity.event.event.Event;
import org.wso2.carbon.identity.event.handler.AbstractEventHandler;
import org.wso2.carbon.identity.oauth2.IdentityOAuth2Exception;
import org.wso2.carbon.identity.oauth2.util.OAuth2Util;
import org.wso2.carbon.utils.multitenancy.MultitenantConstants;
import org.wso2.custom.event.handler.exception.OIDCSSEClientException;
import org.wso2.custom.event.handler.exception.OIDCSSEServerException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Receive internal events in IS and convert them to SSE format and send them to the SSE component.
 */
public class SseEventHandler extends AbstractEventHandler {

    private static final Log LOG = LogFactory.getLog(SseEventHandler.class);

    @Override
    public void handleEvent(Event event) throws IdentityEventException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("SSE event handler received event: " + event.getEventName());
        }

        Map<String, Object> eventProperties = event.getEventProperties();
        String userName = (String) eventProperties.get(IdentityEventConstants.EventProperty.USER_NAME);

        if (StringUtils.isEmpty(userName)) {
            throw new OIDCSSEServerException("Username not available in event: " + event.getEventName());
        }
        //TODO constant issuer value -uri
        JWTClaimsSet claimsSet =
                new JWTClaimsSet.Builder().issuer(userName).audience(OIDCSSEConstants.AUDIENCE).subject(userName).build();

        //        String securityEventToken;
        //        try {
        //            securityEventToken = OAuth2Util.signJWTWithRSA(claimsSet, JWSAlgorithm.RS256,
        //                    MultitenantConstants.SUPER_TENANT_DOMAIN_NAME).serialize();
        //        } catch (IdentityOAuth2Exception e) {
        //            throw new OIDCSSEServerException("Error while generating token", e);
        //        }

        //        if (LOG.isDebugEnabled()) {
        //            LOG.debug("Generated SET token: " + securityEventToken);
        //        }

        String url = this.configs.getModuleProperties().getProperty(OIDCSSEConstants.SSE_URL);
        if (StringUtils.isEmpty(url)) {
            throw new OIDCSSEServerException("Url not available in event: " + event.getEventName());
        }

        //        try {
        //            sendNotification(url, securityEventToken, userName, event.getEventName());
        //        } catch (OIDCSSEServerException | OIDCSSEClientException e) {
        //            if (LOG.isErrorEnabled()) {
        //                LOG.error(e.getMessage());
        //            }
        //        }
    //    sendEvent(url, userName, event.getEventName(), time, issuer);
    }

    private void sendEvent(String url, String subject, String eventName, String time, String issuer) throws
            OIDCSSEServerException {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);

        JSONObject jsonObject = new JSONObject();
        //        jsonObject.accumulate(OIDCSSEConstants.TOKEN, token);
        //        jsonObject.accumulate(OIDCSSEConstants.SUBJECT, userName);
        //        jsonObject.accumulate(OIDCSSEConstants.EVENT, eventName);

        String json = jsonObject.toString();

        StringEntity se;
        try {
            se = new StringEntity(json);
        } catch (UnsupportedEncodingException e) {
            throw new OIDCSSEServerException("Error while creating String entity");
        }

        httpPost.setEntity(se);
        httpPost.setHeader(HTTPConstants.HEADER_ACCEPT, HTTPConstants.MEDIA_TYPE_APPLICATION_JSON);
        httpPost.setHeader(HTTPConstants.HEADER_CONTENT_TYPE, HTTPConstants.MEDIA_TYPE_APPLICATION_JSON);

        try {
            client.execute(httpPost);
        } catch (IOException e) {
            throw new OIDCSSEServerException("Error while sending SET for event : " + eventName + "with " + url, e);
        }
    }

    @Override
    public String getName() {

        return OIDCSSEConstants.EVENT_HANDLER_NAME;
    }
}
