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

    public static class Keystore {

        public static final String SSE_SIGNATURE_ALGORITHM = "${sse.signature.algorithm}";
        public static final String KEYSTORE_PATH = "${sse.keystore.path}";
        public static final String ALIAS = "${sse.keystore.alias}";
        public static final String PASSWORD = "${sse.keystore.password}";
        public static final String KEYSTORE_TYPE = "${sse.keystore.type}";
    }

    public static class Data {

        public static final String DEFAULT_STATUS = "enabled";
        //TODO map events to sse format
        public static final String EVENTS_SUPPORTED = "${sse.supported.events}";
        public static final String ISS = "https://idp.sse.com/sse-issuer";
        public static final List<String> AUD = Arrays.asList("https://idp.sse.com/sse-aud");
    }

    public static final String CLIENT_ID = "client_id";
    public static final String FORMAT = "format";
    public static final String TOKEN = "token";
    public static final String EMAIL = "email";
    public static final String JTI = "jti";
    public static final String RS256 = "RS256";
    public static final String SHA512_WITH_HMAC = "SHA512_WITH_HMAC";
}
