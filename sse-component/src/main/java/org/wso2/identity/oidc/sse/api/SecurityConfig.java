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

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Security layer configuration.
 */
@Configuration
@Slf4j
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value(Constants.INTROSPECTION_URI)
    String introspectionUri;

    @Value(Constants.CLIENT_ID)
    String clientId;

    @Value(Constants.CLIENT_SECRET)
    String clientSecret;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests(authz -> {
            try {
                authz.antMatchers(HttpMethod.GET, Constants.Path.STATUS).hasAuthority(Constants.Scope.STATUS_READ)

                        .antMatchers(HttpMethod.POST, Constants.Path.STATUS).hasAuthority(Constants.Scope.STATUS_WRITE)

                        .antMatchers(HttpMethod.GET, Constants.Path.STREAM).hasAuthority(Constants.Scope.STREAM_READ)

                        .antMatchers(HttpMethod.POST, Constants.Path.STREAM).hasAuthority(Constants.Scope.STREAM_WRITE)

                        .antMatchers(HttpMethod.DELETE, Constants.Path.STREAM).hasAuthority(Constants.Scope.STREAM_WRITE)

                        .antMatchers(HttpMethod.POST, Constants.Path.ADD_SUBJECT).hasAuthority(Constants.Scope.ADD_SUBJECT)

                        .antMatchers(HttpMethod.POST, Constants.Path.REMOVE_SUBJECT).hasAuthority(Constants.Scope.REMOVE_SUBJECT)

                        .antMatchers(HttpMethod.POST, Constants.Path.VERIFY).hasAuthority(Constants.Scope.VERIFY)

                        .antMatchers(Constants.Path.WELL_KNOWN).permitAll()

                        .and().csrf().disable().authorizeRequests().antMatchers(Constants.Path.EVENT).permitAll();
            } catch (Exception e) {
                //TODO add error logs
                log.error(e.toString());
            }
        }).oauth2ResourceServer(oauth2 -> oauth2.opaqueToken(token -> token.introspectionUri(this.introspectionUri).introspectionClientCredentials(this.clientId, this.clientSecret)));
    }
}
