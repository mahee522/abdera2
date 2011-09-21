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
package org.apache.abdera2.protocol.server.model;

import org.apache.abdera2.common.protocol.RequestContext;
import org.apache.abdera2.common.protocol.CollectionInfo;
import org.apache.abdera2.model.Collection;

/**
 * Metadata interface used by WorkspaceManager and Provider implementations to construct Atompub Service Documents. The
 * CollectionInfo interface provides information used to construct an app:collection element
 */
public interface AtompubCollectionInfo
  extends CollectionInfo {

    /**
     * Return the collection of CategoriesInfo objects for the app:collection element's app:categories elements. These
     * tell a client which atom:category elements are defined for use in the collections atom:entries
     */
    AtompubCategoriesInfo[] getCategoriesInfo(RequestContext request);

    /**
     * Converts this to an instance of the FOM Collection interface
     */
    Collection asCollectionElement(RequestContext request);

}