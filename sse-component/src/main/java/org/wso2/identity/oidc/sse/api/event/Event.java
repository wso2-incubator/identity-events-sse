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

package org.wso2.identity.oidc.sse.api.event;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Event details model.
 */
@Document(collection = "Event")
public class Event {

    private String subject;
    private String issuer;
    private String name;

    public Event() {

    }

    public Event(String subject, String issuer, String name) {

        this.subject = subject;
        this.issuer = issuer;
        this.name = name;
    }

    /**
     * Get subject name
     *
     * @return subject name
     */
    public String getSubject() {

        return subject;
    }

    /**
     * Set subject name
     *
     * @param subject subject name
     */
    public void setSubject(String subject) {

        this.subject = subject;
    }

    /**
     * Get the issuer of the event.
     *
     * @return issuer url
     */
    public String getIssuer() {

        return issuer;
    }

    /**
     * Set the issuer of the event.
     *
     * @param issuer issuer url
     */
    public void setIssuer(String issuer) {

        this.issuer = issuer;
    }

    /**
     * Get event name
     *
     * @return event name
     */
    public String getName() {

        return name;
    }

    /**
     * Set event name
     *
     * @param name event name
     */
    public void setName(String name) {

        this.name = name;
    }
}
