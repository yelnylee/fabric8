/**
 * Copyright (C) FuseSource, Inc.
 * http://fusesource.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.fusesource.gateway.handlers.detecting;

import org.fusesource.gateway.SocketWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vertx.java.core.Handler;
import org.vertx.java.core.http.ServerWebSocket;

import java.util.concurrent.atomic.AtomicReference;

/**
 */
public class DetectingGatewayWebSocketHandler implements Handler<ServerWebSocket> {

    private static final transient Logger LOG = LoggerFactory.getLogger(DetectingGatewayWebSocketHandler.class);
    private final AtomicReference<DetectingGatewayProtocolHandler> handler = new AtomicReference<DetectingGatewayProtocolHandler>();
    private String pathPrefix;

    @Override
    public void handle(final ServerWebSocket socket) {
        DetectingGatewayProtocolHandler handler = this.handler.get();
        if ( handler==null || !socket.path().startsWith(pathPrefix) ) {
          socket.reject();
          return;
        }
        handler.handle(SocketWrapper.wrap(socket));
    }

    public DetectingGatewayProtocolHandler getHandler() {
        return handler.get();
    }
    public void setHandler(DetectingGatewayProtocolHandler value) {
        handler.set(value);
    }

    public String getPathPrefix() {
        return pathPrefix;
    }

    public void setPathPrefix(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }
}
