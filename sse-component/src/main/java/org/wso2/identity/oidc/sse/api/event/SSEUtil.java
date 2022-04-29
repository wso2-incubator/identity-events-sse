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
import org.wso2.identity.oidc.sse.api.Constants;
import org.wso2.identity.oidc.sse.api.exception.OIDCSSEException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
import java.util.UUID;

/**
 * Utility methods for SET token builder implementation.
 */
@Slf4j
public class SSEUtil {

    private static final String ALLOW_WEAK_RSA_SIGNER_KEY = "allow_weak_rsa_signer_key";

    /**
     * Create a JWT token using the claims.
     *
     * @param event    event object instance.
     * @param audience audience of the stream.
     * @return created JWT claim set.
     */
    public static JWTClaimsSet getJWT(Event event, List<String> audience) {

        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        Date iat = new Date(currentTimeInMillis);
        JWTClaimsSet.Builder eventClaims = new JWTClaimsSet.Builder();
        eventClaims.claim(Constants.FORMAT, Constants.EMAIL);
        eventClaims.claim(Constants.EMAIL, event.getSubject());
        JWTClaimsSet.Builder jwtClaimsSetBuilder = new JWTClaimsSet.Builder();
        UUID jit = UUID.randomUUID();
        jwtClaimsSetBuilder.claim(Constants.JTI, jit.toString()).issueTime(iat).audience(audience).claim(event.getName()
                , eventClaims).issuer(event.getIssuer());
        return jwtClaimsSetBuilder.build();
    }

    /**
     * Sign JWT using private key of IS.
     *
     * @param jwtClaimsSet JWT claim set
     * @return Singed JWT token.
     * @throws OIDCSSEException Error when SSE signature algorithm is not supported.
     */
    public static String signJwtWithRSA(JWTClaimsSet jwtClaimsSet, String sseSignatureAlgorithm,
                                        RSAPrivateKey privateKey) throws OIDCSSEException {

        JWSAlgorithm signatureAlgorithm = null;
        if (StringUtils.isEmpty(sseSignatureAlgorithm)) {
            signatureAlgorithm = new JWSAlgorithm(Algorithm.NONE.getName());
        } else if (StringUtils.equalsIgnoreCase(sseSignatureAlgorithm, Constants.RS256)) {
            signatureAlgorithm = JWSAlgorithm.RS256;
        } else if (StringUtils.equalsIgnoreCase(sseSignatureAlgorithm, Constants.SHA512_WITH_HMAC)) {
            signatureAlgorithm = JWSAlgorithm.HS512;
        } else {
            throw new OIDCSSEException("Configured SSE signature algorithm: " + signatureAlgorithm + " is not " +
                    "supported");
        }
        JWSSigner signer = createJWSSigner(privateKey);
        JWSHeader.Builder headerBuilder = new JWSHeader.Builder(signatureAlgorithm);
        SignedJWT signedJWT = new SignedJWT(headerBuilder.build(), jwtClaimsSet);
        try {
            signedJWT.sign(signer);
            return signedJWT.serialize();
        } catch (JOSEException e) {
            throw new OIDCSSEException("Error while signing the token", e);
        }
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
     * @param keystorePath path where the keystore located.
     * @param alias        alias of the private key.
     * @param password     password of the keystore.
     * @param keystoreType type of the keystore.
     * @return private key
     * @throws OIDCSSEException Error when keystore path, alias , keystore password or keystore type is null.
     */
    public static RSAPrivateKey getPrivateKey(String keystorePath, String alias, String password,
                                              String keystoreType) throws OIDCSSEException {

        if (StringUtils.isEmpty(keystorePath)) {
            throw new OIDCSSEException("Keystore path cannot be null");
        }
        if (StringUtils.isEmpty(alias)) {
            throw new OIDCSSEException("alias cannot be null");
        }
        if (StringUtils.isEmpty(password)) {
            throw new OIDCSSEException("Keystore password cannot be null");
        }
        if (StringUtils.isEmpty(keystoreType)) {
            throw new OIDCSSEException("Keystore type cannot be null");
        }
        File keystoreFile = new File(keystorePath);
        try {
            KeyStore keyStore = loadKeyStore(keystoreFile, keystoreType, password);
            return (RSAPrivateKey) keyStore.getKey(alias, password.toCharArray());
        } catch (IOException e) {
            throw new OIDCSSEException("Error while loading keystore", e);
        } catch (UnrecoverableKeyException | NoSuchAlgorithmException | KeyStoreException e) {
            throw new OIDCSSEException("Error while getting private key", e);
        }
    }

    /**
     * Load the keystore.
     *
     * @param keystoreFile keystore file
     * @param keystoreType keystore type
     * @param password     keystore password
     * @return keystore
     * @throws OIDCSSEException Error when keystore file is null.
     * @throws IOException      Error when input stream closing.
     */
    public static KeyStore loadKeyStore(File keystoreFile, String keystoreType, String password) throws OIDCSSEException
            , IOException {

        if (keystoreFile == null) {
            throw new OIDCSSEException("Keystore file should not be null");
        }
        URI keystoreUri = keystoreFile.toURI();
        InputStream inputStream = null;
        try {
            URL keystoreUrl = keystoreUri.toURL();
            KeyStore keystore = KeyStore.getInstance(keystoreType);
            inputStream = keystoreUrl.openStream();
            keystore.load(inputStream, password.toCharArray());
            return keystore;
        } catch (CertificateException | NoSuchAlgorithmException | IOException | KeyStoreException e) {
            throw new OIDCSSEException("Error while getting keystore url", e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
