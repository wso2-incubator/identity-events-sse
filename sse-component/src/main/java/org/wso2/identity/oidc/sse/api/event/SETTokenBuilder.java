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

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.wso2.identity.oidc.sse.api.Constants;
import springfox.documentation.builders.RequestHandlerSelectors;

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

@Slf4j
/**
 * Create jwt and get private key from keystore, then sign the jwt using private key to generate SET token.
 */ public class SETTokenBuilder {

    /**
     * Get private key from the keystore and sign jwt.
     *
     * @param event    event object instance
     * @param audience list of audience in the stream
     * @return signed JWT (SET)
     */
    public String buildToken(Event event, List<String> audience) {

        RSAPrivateKey privateKey = null;
        try {
            privateKey = getPrivateKey();
        } catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException e) {
            e.printStackTrace();
        }
        String signedJwtToken = createJWTAndSign(event, privateKey, audience);

        if (log.isDebugEnabled()) {
            log.debug("JWT Token:" + signedJwtToken);
        }
        return signedJwtToken;
    }

    /**
     * Create JWT and Sign it using the private key.
     *
     * @param event      event object instance
     * @param privateKey private key
     * @param audience   audience of the stream
     * @return signed jwt
     */
    private static String createJWTAndSign(Event event, RSAPrivateKey privateKey, List<String> audience) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;

        long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        Date iat = new Date(currentTimeInMillis);

        JwtBuilder builder =
                Jwts.builder().setSubject(event.getSubject()).setAudience(String.valueOf(audience)).setIssuer(event.getSubject()).setIssuedAt(iat).signWith(signatureAlgorithm, privateKey);

        return builder.compact();
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
    private RSAPrivateKey getPrivateKey() throws IOException, NoSuchAlgorithmException, CertificateException,
            KeyStoreException, UnrecoverableKeyException {

        File ks =
                new File("C:\\dev\\identity-events-sse\\sse-component\\src\\main\\java\\org\\wso2\\identity\\oidc\\sse\\api\\event\\wso2carbon.jks");
        KeyStore keyStore = loadKeyStore(ks);

        return (RSAPrivateKey) keyStore.getKey(Constants.Keystore.ALIAS, Constants.Keystore.PASSWORD.toCharArray());
    }

    private static KeyStore loadKeyStore(final File keystoreFile) throws KeyStoreException, IOException,
            NoSuchAlgorithmException, CertificateException {

        if (null == keystoreFile) {
            throw new IllegalArgumentException("Keystore url may not be null");
        }

        final URI keystoreUri = keystoreFile.toURI();
        final URL keystoreUrl = keystoreUri.toURL();
        final KeyStore keystore = KeyStore.getInstance(Constants.Keystore.KEYSTORE_TYPE);
        InputStream is = keystoreUrl.openStream();
        keystore.load(is, Constants.Keystore.PASSWORD.toCharArray());

        return keystore;
    }
}
