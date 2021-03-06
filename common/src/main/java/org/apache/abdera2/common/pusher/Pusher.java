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
package org.apache.abdera2.common.pusher;

import com.google.common.base.Supplier;

/**
 * Used to push items into a channel. This will cause all registered
 * Listeners to receive notification of the item
 */
public interface Pusher<T> {

  void push(T t);
  
  void push(Supplier<? extends T> t);
  
  void pushAll(T... t);
  
  void pushAll(Supplier<? extends T>... t);

  void pushAll(Iterable<T> t);
  
}
