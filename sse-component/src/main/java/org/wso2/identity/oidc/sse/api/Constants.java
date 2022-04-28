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

package org.wso2.identity.oidc.sse.api;

import java.util.Arrays;
import java.util.List;

/**
 * Contains the constants related to sse component
 */
public class Constants {

    public class Scope {

        public static final String STATUS_READ = "SCOPE_status_read";
        public static final String STATUS_WRITE = "SCOPE_status_write";
        public static final String STREAM_READ = "SCOPE_stream_read";
        public static final String STREAM_WRITE = "SCOPE_stream_write";
        public static final String ADD_SUBJECT = "SCOPE_add_subject";
        public static final String REMOVE_SUBJECT = "SCOPE_remove_subject";
        public static final String VERIFY = "SCOPE_verify";
    }

    public class Path {

        public static final String STATUS = "/sse/status";
        public static final String STREAM = "/sse/stream";
        public static final String ADD_SUBJECT = "/sse/subjects:add";
        public static final String REMOVE_SUBJECT = "/sse/subjects:remove";
        public static final String VERIFY = "/sse/verify";
        public static final String WELL_KNOWN = "/.well-known/sse-configuration";
        public static final String EVENT = "/event";
    }

    public static class Introspection {

        public static final String INTROSPECTION_URI = "${spring.security.oauth2.resourceserver.opaque" +
                ".introspection-uri}";
        public static final String CLIENT_ID = "${spring.security.oauth2.resourceserver.opaque.introspection" +
                "-client-id}";
        public static final String CLIENT_SECRET = "${spring.security.oauth2.resourceserver.opaque.introspection" +
                "-client-secret}";
    }

    public static class Data {

        public static final String DEFAULT_STATUS = "enabled";
        //TODO move to applications.properties - add comment that it should be added to deployment.toml
        public static final List<String> EVENTS_SUPPORTED = Arrays.asList("POST_UPDATE_CREDENTIAL_BY_ADMIN",
                "POST_UPDATE_CREDENTIAL");
    }

    public static class Keystore{
        public static final String ALIAS ="wso2carbon";
        public static final String PASSWORD ="wso2carbon";
        public static final String KEYSTORE_TYPE="jks";
    }

    public static final String CLIENT_ID = "client_id";
    public static final String TOKEN = "token";
}
