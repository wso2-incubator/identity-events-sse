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

package org.wso2.identity.oidc.sse.api.stream.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * Delivery details model.
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Delivery {

    private String url;
    private String deliveryMethod;

    public Delivery() {

    }

    public Delivery(String url, String deliveryMethod) {

        this.url = url;
        this.deliveryMethod = deliveryMethod;
    }

    /**
     * Get delivery url.
     *
     * @return url
     */
    public String getUrl() {

        return url;
    }

    /**
     * Set delivery url.
     *
     * @param url
     */
    public void setUrl(String url) {

        this.url = url;
    }

    /**
     * Get delivery method.
     *
     * @return delivery method name.
     */
    public String getDeliveryMethod() {

        return deliveryMethod;
    }

    /**
     * Set delivery method.
     *
     * @param deliveryMethod delivery method name.
     */
    public void setDeliveryMethod(String deliveryMethod) {

        this.deliveryMethod = deliveryMethod;
    }
}
