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

package org.wso2.identity.oidc.sse.api.stream;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.wso2.identity.oidc.sse.api.Constants;
import org.wso2.identity.oidc.sse.api.exception.OIDCSSEException;
import org.wso2.identity.oidc.sse.api.stream.model.Stream;
import org.wso2.identity.oidc.sse.api.stream.model.Subject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * Controller class for Management API endpoints.
 */
@RestController
@Slf4j
@RequestMapping(path = "sse")
@Api(description = "Stream Controller", tags = "Stream Management API ")
public class StreamController {

    @Value(Constants.Introspection.INTROSPECTION_URI)
    String introspectionUri;

    @Value(Constants.Introspection.CLIENT_ID)
    String username;

    @Value(Constants.Introspection.CLIENT_SECRET)
    String password;

    @Value(Constants.Data.EVENTS_SUPPORTED)
    String[] EVENTS_SUPPORTED;

    private final StreamRepository streamRepository;

    public StreamController(StreamRepository streamRepository) {

        this.streamRepository = streamRepository;
    }

    /**
     * Get stream configuration.
     *
     * @param accessToken Authorization token.
     * @return Stream configuration with status code in a response entity , if authorization success and stream exists.
     */
    @GetMapping("stream")
    @ApiOperation(value = "", notes = "Retrieve current stream configuration")
    public ResponseEntity<?> getConfiguration(@RequestHeader(value = AUTHORIZATION) String accessToken) {

        if (!validateAccessToken(accessToken)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        String id = getClientId(accessToken);
        Optional<Stream> stream = streamRepository.findById(id);
        if (stream.isPresent()) {
            Stream getStream = new Stream(stream.get().getIss(), stream.get().getAud(), stream.get().getDelivery(),
                    stream.get().getEventsSupported(), stream.get().getEventsRequested(),
                    stream.get().getEventsDelivered());

            return new ResponseEntity<>(getStream, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Update the stream ,if stream already exists
     * else create a new stream.
     *
     * @param config      Stream instance.
     * @param accessToken Authorization token.
     * @return Stream configuration.
     */
    @PostMapping("stream")
    @ApiOperation(value = "", notes =
            "Update current stream configuration / create a new stream if stream is not " + "available")
    public ResponseEntity<?> updateConfiguration(@RequestBody Stream config,
                                                 @RequestHeader(value = AUTHORIZATION) String accessToken) {

        if (!validateAccessToken(accessToken)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        String id = getClientId(accessToken);
        Optional<Stream> stream = streamRepository.findById(id);
        if (stream.isPresent()) {
            Stream streamUpdate = stream.get();
            streamUpdate.setId(id);
            streamUpdate.setEventsRequested(config.getEventsRequested());
            streamUpdate.setDelivery(config.getDelivery());
            streamRepository.save(streamUpdate);
            Stream updatedStream = new Stream(streamUpdate.getIss(), streamUpdate.getAud(),
                    streamUpdate.getDelivery(), streamUpdate.getEventsSupported(), streamUpdate.getEventsRequested(),
                    streamUpdate.getEventsDelivered());
            return new ResponseEntity<>(updatedStream, HttpStatus.OK);
        }
        return createStream(config, id);
    }

    /**
     * Create a new stream.
     *
     * @param config Stream configuration.
     * @param id     Stream Id.
     * @return Stream configuration , if stream created.
     */
    private ResponseEntity<?> createStream(Stream config, String id) {

        try {
            config.setId(id);
            config.setStatus(Constants.Data.DEFAULT_STATUS);
            config.setEventsSupported(Arrays.asList(EVENTS_SUPPORTED));
            config.setAud(Constants.Data.AUD);
            config.setIss(Constants.Data.ISS);
            config.setEventsDelivered(Arrays.asList());
            streamRepository.save(config);
            Stream createdStream = new Stream(config.getIss(), config.getAud(), config.getDelivery(),
                    config.getEventsSupported(), config.getEventsRequested(), config.getEventsDelivered());
            return new ResponseEntity<>(createdStream, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Delete stream configuration.
     *
     * @param accessToken Authorization token.
     * @return Response entity with status code.
     */
    @DeleteMapping("stream")
    @ApiOperation(value = "", notes = "Resets to the streams default configuration. All subjects that have been " +
            "added to the stream, events that have been enqueued in the stream, or status that has been set on " +
            "the stream will also be deleted.")
    public ResponseEntity<?> deleteConfiguration(@RequestHeader(value = AUTHORIZATION) String accessToken) {

        if (!validateAccessToken(accessToken)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        String id = getClientId(accessToken);
        Optional<Stream> stream = streamRepository.findById(id);
        if (stream.isPresent()) {
            Stream streamDelete = stream.get();
            streamDelete.setId(id);
            streamDelete.setEventsRequested(null);
            streamDelete.setDelivery(null);
            streamRepository.save(streamDelete);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Get current status of the stream.
     *
     * @param accessToken Authorization token.
     * @return Current stream status , if stream exists and authorization success.
     */
    @GetMapping("status")
    @ApiOperation(value = "", notes = "Checks the current status of an event stream ")
    public ResponseEntity<Stream> getStatus(@RequestHeader(value = AUTHORIZATION) String accessToken) {

        if (!validateAccessToken(accessToken)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        String id = getClientId(accessToken);
        Optional<Stream> stream = streamRepository.findById(id);
        if (stream.isPresent()) {
            Stream status = new Stream(stream.get().getStatus());
            return new ResponseEntity<>(status, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Update stream status.
     *
     * @param status      New status.
     * @param accessToken Authorization token.
     * @return Updated status, if stream exists and authorization success.
     */
    @PostMapping("status")
    @ApiOperation(value = "", notes = "Updates the current status of a stream")
    public ResponseEntity<Stream> updateStatus(@RequestBody Stream status,
                                               @RequestHeader(value = AUTHORIZATION) String accessToken) {

        if (!validateAccessToken(accessToken)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        String id = getClientId(accessToken);
        Optional<Stream> stream = streamRepository.findById(id);
        if (stream.isPresent()) {
            Stream streamUpdate = stream.get();
            streamUpdate.setStatus(status.getStatus());
            streamRepository.save(streamUpdate);
            return new ResponseEntity<>(status, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Add subject to the stream.
     *
     * @param subject     Subject instance.
     * @param accessToken Authorization token.
     * @return Response entity with status code.
     */
    @PostMapping("subjects:add")
    @ApiOperation(value = "", notes = "Retrieve current stream configuration")
    public ResponseEntity addSubject(@RequestBody Subject subject,
                                     @RequestHeader(value = AUTHORIZATION) String accessToken) {

        if (!validateAccessToken(accessToken)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        String id = getClientId(accessToken);
        Optional<Stream> stream = streamRepository.findById(id);

        if (stream.isPresent()) {
            Stream streamUpdate = stream.get();
            List<Subject> newSubjects = new ArrayList<>();

            if (streamUpdate.getSubjects() != null) {
                newSubjects.addAll(streamUpdate.getSubjects());
            }
            newSubjects.add(subject);
            streamUpdate.setSubjects(newSubjects);
            streamRepository.save(streamUpdate);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Remove subject to the stream.
     *
     * @param removeSubject Subject instance.
     * @param accessToken   Authorization token.
     * @return Response entity with status code.
     */
    @PostMapping("subjects:remove")
    @ApiOperation(value = "", notes = "Add new subject to the stream")
    public ResponseEntity<?> removeSubject(@RequestBody Subject removeSubject,
                                           @RequestHeader(value = AUTHORIZATION) String accessToken)
            throws OIDCSSEException {

        if (!validateAccessToken(accessToken)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        String id = getClientId(accessToken);
        Optional<Stream> stream = streamRepository.findById(id);

        if (stream.isPresent()) {
            Stream streamSelect = stream.get();
            List<Subject> newSubjects = new ArrayList<>();
            try {
                newSubjects.addAll(streamSelect.getSubjects());
            } catch (Exception e) {
                throw new OIDCSSEException("No streams selected");
            }
            for (int i = 0; i < newSubjects.size(); i++) {
                if (StringUtils.equals(removeSubject.getFormat(), newSubjects.get(i).getFormat())) {
                    if (StringUtils.equals(removeSubject.getEmail(), newSubjects.get(i).getEmail())) {
                        newSubjects.remove(i);
                        streamSelect.setSubjects(newSubjects);
                        streamRepository.save(streamSelect);
                        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
                    }
                }
            }
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    /**
     * Verify if the stream is working properly.
     *
     * @param accessToken Authorization token.
     * @return Stream configurations.
     */
    @PostMapping("verify")
    @ApiOperation(value = "", notes = "Remove existing subject from the stream")
    public ResponseEntity<? extends Object> verification(@RequestHeader(value = AUTHORIZATION) String accessToken) {


        if (!validateAccessToken(accessToken)) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        String id = getClientId(accessToken);
        Optional<Stream> stream = streamRepository.findById(id);
        if (stream.isPresent()) {
            Stream getStream = new Stream(stream.get().getIss(), stream.get().getAud(),
                    stream.get().getEventsDelivered());
            return new ResponseEntity<>(getStream, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    /**
     * Validate access token and get id
     *
     * @param accessToken Authorization token.
     * @return stream id
     */
    private Boolean validateAccessToken(String accessToken) {

        if (accessToken.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Get client Id from authorization token.
     *
     * @param accessToken Authorization token.
     * @return Client Id
     */
    private String getClientId(String accessToken) {

        String requestBody = Constants.TOKEN + "=" + accessToken.substring(7);
        if (log.isDebugEnabled()) {
            log.debug(requestBody);
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(username, password);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
        String result = restTemplate.postForObject(introspectionUri, entity, String.class);
        String temp = result.substring(result.indexOf(Constants.CLIENT_ID) + 12);
        return temp.substring(0, temp.indexOf("\""));
    }
}
