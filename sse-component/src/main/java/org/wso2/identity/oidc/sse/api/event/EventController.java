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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.wso2.identity.oidc.sse.api.Constants;
import org.wso2.identity.oidc.sse.api.exception.OIDCSSEException;
import org.wso2.identity.oidc.sse.api.stream.StreamRepository;
import org.wso2.identity.oidc.sse.api.stream.model.Stream;

import java.util.List;

/**
 * Controller class for event delivery endpoints.
 */
@RestController
@RequestMapping(path = "event")
@Slf4j
@Api(description = "Events Controller", tags = "Event Control")
public class EventController {

    private final EventRepository eventRepository;
    private final StreamRepository streamRepository;

    @Autowired
    public EventController(EventRepository eventRepository, StreamRepository streamRepository) {

        this.eventRepository = eventRepository;
        this.streamRepository = streamRepository;
    }

    /**
     * Add event to the database.
     *
     * @param event event instance
     */
    @PostMapping
    @ApiOperation(value = "", notes = "Store events in database and send event to subscribers.")
    public void newEvent(@RequestBody Event event) {

        eventRepository.save(event);
        try {
            sendEvent(event);
        } catch (OIDCSSEException e) {
            log.error("Error while sending event :" + event.getName(), e);
        }
    }

    /**
     * Send events to subscribed audiences.
     *
     * @param event event instance
     */
    private void sendEvent(Event event) throws OIDCSSEException {

        List<Stream> streams = streamRepository.findByEventsRequestedAndSubjectsEmail(event.getName(),
                event.getSubject());
        if (streams.isEmpty()) {
            if (log.isDebugEnabled()) {
                log.debug("There are no streams subscribed to event:" + event.getName() + "of subject:"
                        + event.getSubject());
            }
        } else {
            MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
            for (Stream stream : streams) {
                List<String> aud = stream.getAud();
                String securityEventToken = new SETTokenBuilder.Builder().audience(aud).event(event).build();
                map.add(Constants.TOKEN, securityEventToken);
                for (int i = 0; stream.getAud().size() > i; i++) {
                    String uri = stream.getAud().get(i);
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.postForObject(uri, map, String.class);
                    if (log.isDebugEnabled()) {
                        log.debug("Session invalidated successfully for the audience: " + uri);
                    }
                }
            }
        }
    }
}
