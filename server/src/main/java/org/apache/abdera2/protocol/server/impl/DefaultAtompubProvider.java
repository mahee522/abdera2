/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  The ASF licenses this file to You
 * under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.  For additional information regarding
 * copyright in this work, please see the NOTICE file in the top level
 * directory of this distribution.
 */
package org.apache.abdera2.protocol.server.impl;

import java.util.Collection;

import javax.security.auth.Subject;

import org.apache.abdera2.common.protocol.DefaultWorkspaceManager;
import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.Request;
import org.apache.abdera2.common.protocol.RouteManager;
import org.apache.abdera2.common.protocol.Target;
import org.apache.abdera2.common.protocol.TargetBuilder;
import org.apache.abdera2.common.protocol.TargetType;
import org.apache.abdera2.common.protocol.WorkspaceInfo;
import org.apache.abdera2.common.protocol.WorkspaceManager;

import com.google.common.base.Function;

/**
 * The DefaultProvider is the default Provider implementation for Abdera. It supports multiple collections and assumes a
 * simple http://.../{collection}/{entry} URL structure. Media-link entries are not supported.
 */
public class DefaultAtompubProvider 
  extends AbstractAtompubProvider {

    protected Function<RequestContext,Target> targetResolver;
    protected Function<Request,Subject> subjectResolver;
    protected TargetBuilder<?> targetBuilder;
    protected RouteManager<TargetType,RequestContext> routeManager;

    public DefaultAtompubProvider() {
        this("/");
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public DefaultAtompubProvider(String base) {
      super(new DefaultWorkspaceManager());
      if (base == null)
        base = "/";
      routeManager =
          new RouteManager()
            .addRoute("service", base, TargetType.TYPE_SERVICE)
            .addRoute("feed", base + ":collection", TargetType.TYPE_COLLECTION)
            .addRoute("entry", base + ":collection/:entry", TargetType.TYPE_ENTRY)
            .addRoute("categories", base + ":collection/:entry;categories", TargetType.TYPE_CATEGORIES);
      targetBuilder = routeManager;
      targetResolver = routeManager;
    }

    @SuppressWarnings("rawtypes")
    public RouteManager getRouteManager() {
        return routeManager;
    }

    protected Function<RequestContext,Target> getTargetResolver(RequestContext request) {
        return targetResolver;
    }

    public void setTargetResolver(Function<RequestContext,Target> targetResolver) {
        this.targetResolver = targetResolver;
    }

    protected Function<Request,Subject> getSubjectResolver(RequestContext request) {
        return subjectResolver;
    }

    public void setSubjectResolver(Function<Request,Subject> subjectResolver) {
        this.subjectResolver = subjectResolver;
    }

    public Function<RequestContext,Target> getTargetResolver() {
        return targetResolver;
    }

    public Function<Request,Subject> getSubjectResolver() {
        return subjectResolver;
    }

    public WorkspaceManager getWorkspaceManager() {
        return workspaceManager;
    }

    public void setWorkspaceManager(WorkspaceManager workspaceManager) {
        this.workspaceManager = workspaceManager;
    }

    @SuppressWarnings("rawtypes")
    public TargetBuilder getTargetBuilder() {
        return (TargetBuilder)targetBuilder;
    }

    public void setTargetBuilder(TargetBuilder<?> targetBuilder) {
        this.targetBuilder = targetBuilder;
    }

    public void addWorkspace(WorkspaceInfo workspace) {
        ((DefaultWorkspaceManager)getWorkspaceManager()).addWorkspace(workspace);
    }

    public void addWorkspaces(Collection<WorkspaceInfo> workspaces) {
        for (WorkspaceInfo w : workspaces) {
            ((DefaultWorkspaceManager)getWorkspaceManager()).addWorkspace(w);
        }
    }

    @SuppressWarnings("rawtypes")
    protected TargetBuilder getTargetBuilder(Request request) {
        return (TargetBuilder)targetBuilder;
    }
}
