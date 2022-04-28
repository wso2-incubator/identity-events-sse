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

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.wso2.identity.oidc.sse.api.exception.OIDCSSEException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Create jwt and get private key from keystore, then sign the jwt using private key to generate SET token.
 */
@Slf4j
public class SETTokenBuilder {

    @Value("${sse.signature.algorithm}")
    static String sseSignatureAlgorithm;

    @Value("${sse.keystore.path}")
    static String keystorePath;

    @Value("${sse.keystore.alias}")
    static String alias;

    @Value("${sse.keystore.password}")
    static String password;

    @Value("{ sse.keystore.type}")
    static String keystoreType;

    private static final String ALLOW_WEAK_RSA_SIGNER_KEY = "allow_weak_rsa_signer_key";

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

            JWTClaimsSet jwtClaimsSet = getJWT(event, audience);
            return signJwtWithRSA(jwtClaimsSet);
        }
    }

    /**
     * Get private key from the keystore and sign jwt.
     *
     * @param event    event object instance
     * @param audience list of audience in the stream
     * @return signed JWT (SET)
     */
    //    public String buildToken(Event event, List<String> audience) throws OIDCSSEException {
    //
    //
    //    }

    /**
     * Create JWT and Sign it using the private key.
     *
     * @param event    event object instance
     * @param audience audience of the stream
     * @return signed jwt
     */
    private static JWTClaimsSet getJWT(Event event, List<String> audience) throws OIDCSSEException {

        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        Date iat = new Date(currentTimeInMillis);
        JWTClaimsSet.Builder jwtClaimsSetBuilder = new JWTClaimsSet.Builder();
        jwtClaimsSetBuilder.issueTime(iat).subject(event.getSubject()).audience(audience).issuer(event.getIssuer());

        return jwtClaimsSetBuilder.build();
    }

    /**
     * Sign JWT with token.
     *
     * @param jwtClaimsSet JWT claim set
     * @return Singed JWT token.
     * @throws OIDCSSEException
     */
    public static String signJwtWithRSA(JWTClaimsSet jwtClaimsSet) throws OIDCSSEException {

        Algorithm signatureAlgorithm = new JWSAlgorithm(JWSAlgorithm.NONE.getName());

        if (StringUtils.equalsIgnoreCase(sseSignatureAlgorithm, "RS256")) {
            signatureAlgorithm = JWSAlgorithm.RS256;
        } else if (StringUtils.equalsIgnoreCase(sseSignatureAlgorithm, "SHA512_WITH_HMAC")) {
            //TODO
            signatureAlgorithm = JWSAlgorithm.RS256;
        } else {
            throw new OIDCSSEException("Configured SSE signature algorithm is not supported");
        }
        JWSSigner signer;
        signer = createJWSSigner(getPrivateKey());
        JWSHeader.Builder headerBuilder = new JWSHeader.Builder((JWSAlgorithm) signatureAlgorithm);
        SignedJWT signedJWT = new SignedJWT(headerBuilder.build(), jwtClaimsSet);
        try {
            signedJWT.sign(signer);
        } catch (JOSEException e) {
            throw new OIDCSSEException("Error while signing the token", e);
        }
        return signedJWT.serialize();
    }

    /**
     * Create JWT signer.
     *
     * @param privateKey private key
     * @return JWT singer
     */
    public static JWSSigner createJWSSigner(RSAPrivateKey privateKey) {

        boolean allowWeakKey = Boolean.parseBoolean(System.getProperty(ALLOW_WEAK_RSA_SIGNER_KEY));
        if (allowWeakKey && log.isDebugEnabled()) {
            log.debug("System flag 'allow_weak_rsa_signer_key' is  enabled. So weak keys (key length less than 2048) "
                    + " will be allowed for signing.");
        }
        return new RSASSASigner(privateKey, allowWeakKey);
    }

    /**
     * Extract private key from the keystore.
     *
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws CertificateException
     * @throws KeyStoreException
     * @throws UnrecoverableKeyException
     */
    private static RSAPrivateKey getPrivateKey() throws OIDCSSEException {

        File ks = new File(keystorePath);
        KeyStore keyStore = loadKeyStore(ks);

        RSAPrivateKey privateKey = null;
        try {
            privateKey = (RSAPrivateKey) keyStore.getKey(alias, password.toCharArray());
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new OIDCSSEException("Error while getting private key", e);
        }
        return privateKey;
    }

    /**
     * Load the keystore.
     *
     * @param keystoreFile keystore file
     * @return keystore
     * @throws OIDCSSEException
     */
    private static KeyStore loadKeyStore(final File keystoreFile) throws OIDCSSEException {

        if (null == keystoreFile) {
            throw new IllegalArgumentException("Keystore url may not be null");
        }

        final URI keystoreUri = keystoreFile.toURI();
        final URL keystoreUrl;
        try {
            keystoreUrl = keystoreUri.toURL();
        } catch (MalformedURLException e) {
            throw new OIDCSSEException("Error while getting keystore url", e);
        }
        final KeyStore keystore;
        try {
            keystore = KeyStore.getInstance(keystoreType);
        } catch (KeyStoreException e) {
            throw new OIDCSSEException("Error while getting keystore instance", e);
        }
        InputStream is = null;
        try {
            is = keystoreUrl.openStream();
        } catch (IOException e) {
            throw new OIDCSSEException("Error while creating stream", e);
        }
        try {
            keystore.load(is, password.toCharArray());
        } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
            throw new OIDCSSEException("Error while loading keystore", e);
        }
        return keystore;
    }
}
