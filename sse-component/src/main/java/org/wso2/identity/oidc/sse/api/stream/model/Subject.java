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

/**
 * Subject details model.
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Subject {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String format;
    private String email;

    public Subject(String format, String email) {

        this.format = format;
        this.email = email;
    }

    /**
     * Get format used to identify the subject.
     *
     * @return format name
     */
    public String getFormat() {

        return format;
    }

    /**
     * Set format of the subject.
     *
     * @param format Format name.
     */
    public void setFormat(String format) {

        this.format = format;
    }

    /**
     * Get subject email.
     *
     * @return Subject email.
     */
    public String getEmail() {

        return email;
    }

    /**
     * Set email to identify subject.
     *
     * @param email Subject email.
     */
    public void setEmail(String email) {

        this.email = email;
    }
}
