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
package org.apache.abdera2.test.server.custom;

import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.BaseRequestContextWrapper;
import org.apache.abdera2.common.protocol.ResponseContext;
import org.apache.abdera2.common.protocol.Filter;
import org.apache.abdera2.common.protocol.FilterChain;
import org.apache.abdera2.common.protocol.RegexTargetResolver;
import org.apache.abdera2.common.protocol.TargetType;
import org.apache.abdera2.common.protocol.TemplateManagerTargetBuilder;
import org.apache.abdera2.protocol.server.AtompubCollectionAdapter;
import org.apache.abdera2.protocol.server.impl.AbstractAtompubWorkspaceProvider;
import org.apache.abdera2.protocol.server.impl.SimpleWorkspaceInfo;

public class CustomProvider extends AbstractAtompubWorkspaceProvider {

    private final SimpleAdapter adapter;

    public CustomProvider() {

        this.adapter = new SimpleAdapter();

        RegexTargetResolver<RequestContext> resolver = 
          new RegexTargetResolver<RequestContext>()
            .setPattern("/atom(\\?[^#]*)?", TargetType.TYPE_SERVICE)
            .setPattern("/atom/([^/#?]+);categories", TargetType.TYPE_CATEGORIES, "collection")
            .setPattern("/atom/([^/#?;]+)(\\?[^#]*)?", TargetType.TYPE_COLLECTION, "collection")
            .setPattern("/atom/([^/#?]+)/([^/#?]+)(\\?[^#]*)?", TargetType.TYPE_ENTRY, "collection", "entry");
        setTargetResolver(resolver);

        TemplateManagerTargetBuilder<TargetType> tmb = 
          new TemplateManagerTargetBuilder<TargetType>();
        
        tmb.add(TargetType.TYPE_SERVICE, "{target_base}/atom");
        tmb.add(TargetType.TYPE_COLLECTION,
                         "{target_base}/atom/{collection}{?q,c,s,p,l,i,o}");
        tmb.add(TargetType.TYPE_CATEGORIES, "{target_base}/atom/{collection};categories");
        tmb.add(TargetType.TYPE_ENTRY, "{target_base}/atom/{collection}/{entry}");

        setTargetBuilder(tmb);
        
        SimpleWorkspaceInfo workspace = new SimpleWorkspaceInfo();
        workspace.setTitle("A Simple Workspace");
        workspace.addCollection(adapter);
        addWorkspace(workspace);

        addFilter(new SimpleFilter());
    }

    public AtompubCollectionAdapter getCollectionAdapter(RequestContext request) {
        return adapter;
    }

    public class SimpleFilter implements Filter {
        public <S extends ResponseContext>S filter(RequestContext request, FilterChain chain) {
            BaseRequestContextWrapper rcw = new BaseRequestContextWrapper(request);
            rcw.setAttribute("offset", 10);
            rcw.setAttribute("count", 10);
            return chain.next(rcw);
        }
    }

}