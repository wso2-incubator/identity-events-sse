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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Defines endpoints related to transmitter.
 */
@RestController
@RequestMapping(path = ".well-known/sse-configuration")
@Api(description = "Transmitter Controller", tags = "Transmitter configuration")
public class TransmitterController {

    private final TransmitterRepository transmitterRepository;

    @Autowired
    public TransmitterController(TransmitterRepository transmitterRepository) {

        this.transmitterRepository = transmitterRepository;
    }

    /**
     * Retrieve transmitter configuration information.
     *
     * @return Transmitter configuration if exists.
     */
    @GetMapping
    @ApiOperation(value = "", notes = "Retrieve transmitter Configuration information.")
    public ResponseEntity<?> getConfiguration() {

        List<Transmitter> transmittersList = transmitterRepository.findAll();

        if (transmittersList.size() == 1) {
            return new ResponseEntity<>(transmittersList.get(0), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }
}
