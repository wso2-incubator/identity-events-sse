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

package org.wso2.custom.event.handler;

/**
 * Contains the constants related to OIDC sse
 */
public class OIDCSSEConstants {

    public static final String SUBJECT = "subject";
    public static final String EVENT_NAME = "name";
    public static final String SSE_URL = "sse.event.handler.url";
    public static final String EVENT_HANDLER_NAME = "sse.event.handler";
    public static final String ISSUER = "issuer";
    public static final String ISSUER_URL="https://idp.sse.com/sse-issuer";
}
