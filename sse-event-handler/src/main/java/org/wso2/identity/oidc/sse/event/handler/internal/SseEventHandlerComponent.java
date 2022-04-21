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

package org.wso2.identity.oidc.sse.event.handler.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Component;
import org.wso2.carbon.identity.event.handler.AbstractEventHandler;
import org.wso2.identity.oidc.sse.event.handler.SseEventHandler;

/**
 * Activate and deactivate SSE event handler.
 */
@Component(name = "custom.event.handler", immediate = true)
public class SseEventHandlerComponent {

    private static final Log LOG = LogFactory.getLog(SseEventHandlerComponent.class);

    protected void activate(ComponentContext ctxt) {

        SseEventHandler eventHandler = new SseEventHandler();
        // Register the custom listener as an OSGI service.
        ctxt.getBundleContext().registerService(AbstractEventHandler.class.getName(), eventHandler, null);
        LOG.info("Custom event handler activated successfully.");
    }

    protected void deactivate(ComponentContext ctxt) {

        if (LOG.isDebugEnabled()) {
            LOG.debug("Custom event handler is deactivated ");
        }
    }
}
