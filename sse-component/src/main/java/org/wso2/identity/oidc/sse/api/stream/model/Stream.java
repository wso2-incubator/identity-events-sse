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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Stream details model.
 */
@Document(collection = "Stream")
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Stream {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String iss;
    private String status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Id
    private String id;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> aud;
    private List<String> eventsSupported;
    private List<String> eventsRequested;
    private List<String> eventsDelivered;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Subject> subjects;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Delivery delivery;

    public Stream() {

    }

    public Stream(String status) {

        this.status = status;
    }

    public Stream(String iss, List<String> aud, Delivery delivery) {

        this.iss = iss;
        this.aud = aud;
        this.delivery = delivery;
    }

    public Stream(String id, String iss, String status, List<String> aud, Delivery delivery,
                  List<String> eventsSupported, List<String> eventsRequested, List<String> eventsDelivered,
                  List<Subject> subjects) {

        this.id = id;
        this.iss = iss;
        this.status = status;
        this.aud = aud;
        this.delivery = delivery;
        this.eventsSupported = eventsSupported;
        this.eventsRequested = eventsRequested;
        this.eventsDelivered = eventsDelivered;
        this.subjects = subjects;
    }

    public Stream(String iss, List<String> aud, Delivery delivery, List<String> eventsSupported,
                  List<String> eventsRequested, List<String> eventsDelivered) {

        this.iss = iss;
        this.aud = aud;
        this.delivery = delivery;
        this.eventsSupported = eventsSupported;
        this.eventsRequested = eventsRequested;
        this.eventsDelivered = eventsDelivered;
    }

    public Stream(String id, String iss, List<String> aud, Delivery delivery, List<String> eventsRequested) {

        this.id = id;
        this.iss = iss;
        this.aud = aud;
        this.delivery = delivery;
        this.eventsRequested = eventsRequested;
    }

    public Stream(String id, String iss, String status, List<String> aud, Delivery delivery,
                  List<String> eventsSupported, List<String> eventsRequested, List<String> eventsDelivered) {

        this.id = id;
        this.iss = iss;
        this.status = status;
        this.aud = aud;
        this.delivery = delivery;
        this.eventsSupported = eventsSupported;
        this.eventsRequested = eventsRequested;
        this.eventsDelivered = eventsDelivered;
    }

    public Stream(String iss, List<String> aud, List<String> eventsDelivered) {

        this.iss = iss;
        this.aud = aud;
        this.eventsDelivered = eventsDelivered;
    }

    /**
     * Set supported event list.
     *
     * @param eventsSupported Supported event list.
     */
    public void setEventsSupported(List<String> eventsSupported) {

        this.eventsSupported = eventsSupported;
    }

    /**
     * Set events delivered.
     *
     * @param eventsDelivered Delivered event list.
     */
    public void setEventsDelivered(List<String> eventsDelivered) {

        this.eventsDelivered = eventsDelivered;
    }

    /**
     * Get stream id.
     *
     * @return stream id.
     */
    public String getId() {

        return id;
    }

    /**
     * Set stream id.
     *
     * @param id Stream id.
     */
    public void setId(String id) {

        this.id = id;
    }

    /**
     * Get issuer name.
     *
     * @return issuer name.
     */
    public String getIss() {

        return iss;
    }

    /**
     * Get current status of the stream.
     *
     * @return current status
     */
    public String getStatus() {

        return status;
    }

    /**
     * Set stream status.
     *
     * @param status new status.
     */
    public void setStatus(String status) {

        this.status = status;
    }

    /**
     * Get audience list of the stream.
     *
     * @return Audience list.
     */
    public List<String> getAud() {

        return aud;
    }

    /**
     * Get delivery method of the stream.
     *
     * @return delivery  object instance.
     */
    public Delivery getDelivery() {

        return delivery;
    }

    /**
     * Set delivery method of the stream.
     *
     * @param delivery delivery instance.
     */
    public void setDelivery(Delivery delivery) {

        this.delivery = delivery;
    }

    /**
     * Get supported events of the stream.
     *
     * @return supported event list.
     */
    public List<String> getEventsSupported() {

        return eventsSupported;
    }

    /**
     * Get requested events list.
     *
     * @return Requested events list.
     */
    public List<String> getEventsRequested() {

        return eventsRequested;
    }

    /**
     * Set events requested
     *
     * @param eventsRequested New requested events list.
     */
    public void setEventsRequested(List<String> eventsRequested) {

        this.eventsRequested = eventsRequested;
    }

    /**
     * Get events delivered by the transmitter.
     *
     * @return Delivered events list.
     */
    public List<String> getEventsDelivered() {

        return eventsDelivered;
    }

    /**
     * Get subjects in the stream.
     *
     * @return Subjects list.
     */
    public List<Subject> getSubjects() {

        return subjects;
    }

    /**
     * Set subjects to the stream.
     *
     * @param subjects Subject instance list.
     */
    public void setSubjects(List<Subject> subjects) {

        this.subjects = subjects;
    }
}
