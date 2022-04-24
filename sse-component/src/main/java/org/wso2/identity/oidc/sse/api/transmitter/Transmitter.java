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

package org.wso2.identity.oidc.sse.api.transmitter;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Represents transmitter model.
 */
@Document("Transmitter")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Transmitter {

    private String issuer;
    private String jwksUri;
    private String addSubjectEndpoint;
    private String removeSubjectEndpoint;
    private String verificationEndpoint;
    private String statusEndpoint;
    private String configurationEndpoint;

    private List<String> criticalSubjectMembers;
    private List<String> deliveryMethodsSupported;

    public Transmitter() {

    }

    public Transmitter(String issuer, String jwksUri, String addSubjectEndpoint, String removeSubjectEndpoint,
                       String verificationEndpoint, String statusEndpoint, String configurationEndpoint,
                       List<String> criticalSubjectMembers, List<String> deliveryMethodsSupported) {

        this.issuer = issuer;
        this.jwksUri = jwksUri;
        this.addSubjectEndpoint = addSubjectEndpoint;
        this.removeSubjectEndpoint = removeSubjectEndpoint;
        this.verificationEndpoint = verificationEndpoint;
        this.statusEndpoint = statusEndpoint;
        this.configurationEndpoint = configurationEndpoint;
        this.criticalSubjectMembers = criticalSubjectMembers;
        this.deliveryMethodsSupported = deliveryMethodsSupported;
    }

    /**
     * Get issuer name.
     *
     * @return issuer name.
     */
    public String getIssuer() {

        return issuer;
    }

    /**
     * Set issuer name.
     *
     * @param issuer name.
     */
    public void setIssuer(String issuer) {

        this.issuer = issuer;
    }

    /**
     * Get Jwks Uri
     *
     * @return Jwks Uri.
     */
    public String getJwksUri() {

        return jwksUri;
    }

    /**
     * Set Jwks Uri.
     *
     * @param jwksUri
     */
    public void setJwksUri(String jwksUri) {

        this.jwksUri = jwksUri;
    }

    /**
     * Get add subject endpoint url.
     *
     * @return Add subject endpoint url.
     */
    public String getAddSubjectEndpoint() {

        return addSubjectEndpoint;
    }

    /**
     * Set add subject endpoint url.
     *
     * @param addSubjectEndpoint
     */
    public void setAddSubjectEndpoint(String addSubjectEndpoint) {

        this.addSubjectEndpoint = addSubjectEndpoint;
    }

    /**
     * Get remove subject endpoint url.
     *
     * @return Remove subject endpoint url.
     */
    public String getRemoveSubjectEndpoint() {

        return removeSubjectEndpoint;
    }

    /**
     * Set remove subject endpoint url.
     *
     * @param removeSubjectEndpoint
     */
    public void setRemoveSubjectEndpoint(String removeSubjectEndpoint) {

        this.removeSubjectEndpoint = removeSubjectEndpoint;
    }

    /**
     * Get verification endpoint url.
     *
     * @return Get verification endpoint url.
     */
    public String getVerificationEndpoint() {

        return verificationEndpoint;
    }

    /**
     * Set verfication endpoint url.
     *
     * @param verificationEndpoint
     */
    public void setVerificationEndpoint(String verificationEndpoint) {

        this.verificationEndpoint = verificationEndpoint;
    }

    /**
     * Get Stream status endpoint url.
     *
     * @return Stream status endpoint url.
     */
    public String getStatusEndpoint() {

        return statusEndpoint;
    }

    /**
     * Set stream status endpoint url
     *
     * @param statusEndpoint
     */
    public void setStatusEndpoint(String statusEndpoint) {

        this.statusEndpoint = statusEndpoint;
    }

    /**
     * Get critical subject members list.
     *
     * @return critical subject members list.
     */
    public List<String> getCriticalSubjectMembers() {

        return criticalSubjectMembers;
    }

    /**
     * Set critical subject members list.
     *
     * @param criticalSubjectMembers
     */
    public void setCriticalSubjectMembers(List<String> criticalSubjectMembers) {

        this.criticalSubjectMembers = criticalSubjectMembers;
    }

    /**
     * Get supported delivery methods list.
     *
     * @return Supported delivery methods list.
     */
    public List<String> getDeliveryMethodsSupported() {

        return deliveryMethodsSupported;
    }

    /**
     * Set supported delivery methods list.
     *
     * @param deliveryMethodsSupported
     */
    public void setDeliveryMethodsSupported(List<String> deliveryMethodsSupported) {

        this.deliveryMethodsSupported = deliveryMethodsSupported;
    }

    /**
     * Get configuration endpoint url.
     *
     * @return configuration endpoint url.
     */
    public String getConfigurationEndpoint() {

        return configurationEndpoint;
    }

    /**
     * Set configuration endpoint url.
     *
     * @param configurationEndpoint
     */
    public void setConfigurationEndpoint(String configurationEndpoint) {

        this.configurationEndpoint = configurationEndpoint;
    }

    @Override
    public String toString() {

        return "Transmitter{" + "issuer='" + issuer + '\'' + ", jwksUri='" + jwksUri + '\'' + ", addSubjectEndpoint" +
                "='" + addSubjectEndpoint + '\'' + ", removeSubjectEndpoint='" + removeSubjectEndpoint + '\'' + ", " +
                "verificationEndpoint='" + verificationEndpoint + '\'' + ", statusEndpoint='" + statusEndpoint + '\'' +
                ", configurationEndpoint='" + configurationEndpoint + '\'' + ", criticalSubjectMembers=" +
                criticalSubjectMembers + ", deliveryMethodsSupported=" + deliveryMethodsSupported + '}';
    }
}
