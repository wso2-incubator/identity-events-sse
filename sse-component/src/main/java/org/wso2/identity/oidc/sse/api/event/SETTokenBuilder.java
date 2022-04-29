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
import org.springframework.stereotype.Component;
import org.wso2.identity.oidc.sse.api.Constants;
import org.wso2.identity.oidc.sse.api.exception.OIDCSSEException;

import java.security.interfaces.RSAPrivateKey;
import java.util.List;

/**
 * Build the security event token.
 */
@Slf4j
@Component
public class SETTokenBuilder {

    @Value(Constants.Keystore.SSE_SIGNATURE_ALGORITHM)
    private String sseSignatureAlgorithm;
    private static String SSE_SIGNATURE_ALGORITHM;

    @Value(Constants.Keystore.SSE_SIGNATURE_ALGORITHM)
    public void setSseSignatureAlgorithmStatic(String sseSignatureAlgorithm) {

        SETTokenBuilder.SSE_SIGNATURE_ALGORITHM = sseSignatureAlgorithm;
    }

    @Value(Constants.Keystore.KEYSTORE_PATH)
    private String keystorePath;
    private static String KEYSTORE_PATH_STATIC;

    @Value(Constants.Keystore.KEYSTORE_PATH)
    public void setKeystorePath(String keystorePath) {

        SETTokenBuilder.KEYSTORE_PATH_STATIC = keystorePath;
    }

    @Value(Constants.Keystore.ALIAS)
    private String alias;
    private static String ALIAS_STATIC;

    @Value(Constants.Keystore.ALIAS)
    public void setAliasStatic(String alias) {

        SETTokenBuilder.ALIAS_STATIC = alias;
    }

    @Value(Constants.Keystore.PASSWORD)
    private String password;
    private static String PASSWORD_STATIC;

    @Value(Constants.Keystore.PASSWORD)
    public void setPasswordStatic(String password) {

        SETTokenBuilder.PASSWORD_STATIC = password;
    }

    @Value(Constants.Keystore.KEYSTORE_TYPE)
    private String keystoreType;
    private static String KEYSTORE_TYPE_STATIC;

    @Value(Constants.Keystore.KEYSTORE_TYPE)
    public void setKeystoreTypeStatic(String keystoreType) {

        SETTokenBuilder.KEYSTORE_TYPE_STATIC = keystoreType;
    }

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
            RSAPrivateKey privateKey = SSEUtil.getPrivateKey(KEYSTORE_PATH_STATIC, ALIAS_STATIC, PASSWORD_STATIC,
                    KEYSTORE_TYPE_STATIC);
            return SSEUtil.signJwtWithRSA(jwtClaimsSet, SSE_SIGNATURE_ALGORITHM, privateKey);
        }
    }
}
