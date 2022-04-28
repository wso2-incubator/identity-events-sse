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

package org.wso2.identity.oidc.sse.api.event;

import com.nimbusds.jwt.JWTClaimsSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.wso2.identity.oidc.sse.api.Constants;
import org.wso2.identity.oidc.sse.api.exception.OIDCSSEException;

import java.security.interfaces.RSAPrivateKey;
import java.util.List;

/**
 * Build the security event token.
 */
@Slf4j
public class SETTokenBuilder {

    @Value(Constants.Keystore.sseSignatureAlgorithm)
    static String sseSignatureAlgorithm;

    @Value(Constants.Keystore.keystorePath)
    static String keystorePath;

    @Value(Constants.Keystore.alias)
    static String alias;

    @Value(Constants.Keystore.password)
    static String password;

    @Value(Constants.Keystore.keystoreType)
    static String keystoreType;

    public static class Builder {

        private List<String> audience;
        private Event event;

        public Builder() {

        }

        public Builder event(Event event) {

            this.event = event;
            return this;
        }

        public Builder audience(List<String> audience) {

            this.audience = audience;
            return this;
        }

        public String build() throws OIDCSSEException {

            JWTClaimsSet jwtClaimsSet = SSEUtil.getJWT(event, audience);
            RSAPrivateKey privateKey = SSEUtil.getPrivateKey(keystorePath, alias, password, keystoreType);
            return SSEUtil.signJwtWithRSA(jwtClaimsSet, sseSignatureAlgorithm, privateKey);
        }
    }
}
