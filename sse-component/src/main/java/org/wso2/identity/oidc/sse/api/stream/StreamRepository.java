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

package org.wso2.identity.oidc.sse.api.stream;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.wso2.identity.oidc.sse.api.stream.model.Stream;

import java.util.List;

/**
 * Represents data access layer of management API.
 */
@Repository
public interface StreamRepository extends MongoRepository<Stream, String> {

    List<Stream> findBySubjects(String email);

    @Query(value = "{ 'eventsRequested' : ?0, 'subjects.email' : ?1 }")
    List<Stream> findByEventsRequestedAndSubjectsEmail(String event, String email);
}
