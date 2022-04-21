/*
 * Copyright (c) 2022, WSO2 Inc. (http://www.wso2.com).
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.identity.oidc.sse.event.handler;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jwt.JWTClaimsSet;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
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
import org.wso2.identity.oidc.sse.event.handler.exception.OIDCSSEClientException;
import org.wso2.identity.oidc.sse.event.handler.exception.OIDCSSEServerException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Handle SSE events.
 */
public class SseEventHandler extends AbstractEventHandler {

    private static Log LOG = LogFactory.getLog(SseEventHandler.class);

    @Override
    public void handleEvent(Event event) throws IdentityEventException {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Custom event handler received events successfully.");
        }

        if (StringUtils.equals(IdentityEventConstants.Event.POST_UPDATE_CREDENTIAL_BY_ADMIN, event.getEventName())) {
            Map<String, Object> eventProperties = event.getEventProperties();
            String userName = (String) eventProperties.get(IdentityEventConstants.EventProperty.USER_NAME);

            //TODO check userNAme null use StringUtils.isEmpty if it is empty throw exception and token ,url

            if (StringUtils.isEmpty(userName)) {

            }
            JWTClaimsSet claimsSet =
                    new JWTClaimsSet.Builder().issuer(userName).audience(OIDCSSEConstants.AUDIENCE).subject(OIDCSSEConstants.TENANT_ID).build();

            try {
                String token = OAuth2Util.signJWTWithRSA(claimsSet, JWSAlgorithm.RS256,
                        MultitenantConstants.SUPER_TENANT_DOMAIN_NAME).serialize();

                if (LOG.isDebugEnabled()) {
                    LOG.debug(token);
                }

                String url = this.configs.getModuleProperties().getProperty(OIDCSSEConstants.URL);
                sendNotification(url, token, userName, event.getEventName());
            } catch (IdentityOAuth2Exception e) {
                //TODO add debug logs and throw the exceptions
                if (LOG.isDebugEnabled()) {
                    LOG.debug(e.toString());
                }
            }
        }
    }

    private void sendNotification(String url, String token, String userName, String eventName) {

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost httpPost = new HttpPost(url);

        JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate(OIDCSSEConstants.TOKEN, token);
        jsonObject.accumulate(OIDCSSEConstants.SUBJECT, userName);
        jsonObject.accumulate(OIDCSSEConstants.EVENT, eventName);

        String json = jsonObject.toString();

        //TODO throw server exception
        StringEntity se = null;
        try {
            se = new StringEntity(json);
            throw new OIDCSSEServerException();
        } catch (OIDCSSEServerException e) {

        } catch (UnsupportedEncodingException e) {
           // e.printStackTrace();.....................
        }

        httpPost.setEntity(se);
        httpPost.setHeader(HTTPConstants.HEADER_ACCEPT, HTTPConstants.MEDIA_TYPE_APPLICATION_JSON);
        httpPost.setHeader(HTTPConstants.HEADER_CONTENT_TYPE, HTTPConstants.MEDIA_TYPE_APPLICATION_JSON);

        //TODO throw client exception
        HttpResponse response = null;
        try {
            response = client.execute(httpPost);
            throw new OIDCSSEClientException();
        } catch (IOException | OIDCSSEClientException e) {
           // e.printStackTrace();.................................
        }
        if (LOG.isDebugEnabled()) {
            LOG.debug(response.getStatusLine().getStatusCode());
        }
    }

    @Override
    public String getName() {

        return OIDCSSEConstants.EVENT_NAME;
    }
}


