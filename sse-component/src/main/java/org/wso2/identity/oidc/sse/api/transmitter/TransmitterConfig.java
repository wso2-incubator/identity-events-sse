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

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * Loads transmitter configuration to the database.
 */
@Configuration
public class TransmitterConfig implements CommandLineRunner {

    private TransmitterRepository transmitterRepository;

    public TransmitterConfig(TransmitterRepository transmitterRepository) {

        this.transmitterRepository = transmitterRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        this.transmitterRepository.deleteAll();

        this.transmitterRepository.save((new Transmitter("https://tr.example.com", "https://tr.example.com/jwks.json"
                , "http://localhost:8080/sse/subjects:add", "http://localhost:8080/sse/subjects:remove", "http" +
                "://localhost:8080/sse/verify", "https://tr.example.com/sse/mgmt/status", "http://localhost:8080/sse" +
                "/stream", Arrays.asList("tenant", "user"), Arrays.asList("https" + "://schemas.openid" +
                ".net/secevent/risc/delivery-method/push", "https://schemas.openid" +
                ".net/secevent/risc/delivery-method/poll"))));
    }
}
