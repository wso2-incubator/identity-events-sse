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

    private String token;
    private String subject;
    private String event;

    public Event() {

    }

    public Event(String subject, String event, String token) {

        this.token = token;
        this.subject = subject;
        this.event = event;
    }

    /**
     * Get subject name
     *
     * @return subject name
     */
    public String getSubject() {

        return subject;
    }

    //TODO remove unused methods

    /**
     * Set subject name
     *
     * @param subject subject name
     */
    public void setSubject(String subject) {

        this.subject = subject;
    }

    /**
     * Get token value
     *
     * @return token
     */
    public String getToken() {

        return token;
    }

    /**
     * Set token value
     *
     * @param token token value
     */
    public void setToken(String token) {

        this.token = token;
    }

    /**
     * Get event name
     *
     * @return event name
     */
    public String getEvent() {

        return event;
    }

    /**
     * Set event name
     *
     * @param event event name
     */
    public void setEvent(String event) {

        this.event = event;
    }
}

