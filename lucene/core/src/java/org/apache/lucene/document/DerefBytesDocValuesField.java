package org.apache.lucene.document;

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.apache.lucene.index.DocValues;
import org.apache.lucene.util.BytesRef;

/**
 * <p>
 * This class provides a {@link Field} that enables storing
 * of a per-document {@link BytesRef} value.  The values are
 * stored indirectly, such that many documents sharing the
 * same value all point to a single copy of the value, which
 * is a good fit when the fields share values.  If values
 * are (mostly) unique it's better to use {@link
 * StraightBytesDocValuesField}.  Here's an example usage: 
 * 
 * <pre class="prettyprint">
 *   document.add(new DerefBytesDocValuesField(name, new BytesRef("hello")));
 * </pre>
 * 
 * <p>
 * If you also need to store the value, you should add a
 * separate {@link StoredField} instance.
 * 
 * @see DocValues
 * */

public class DerefBytesDocValuesField extends StoredField {

  // TODO: ideally indexer figures out var vs fixed on its own!?
  /**
   * Type for indirect bytes DocValues: all with the same length
   */
  public static final FieldType TYPE_FIXED_LEN = new FieldType();
  static {
    TYPE_FIXED_LEN.setDocValueType(DocValues.Type.BYTES_FIXED_DEREF);
    TYPE_FIXED_LEN.freeze();
  }

  /**
   * Type for indirect bytes DocValues: can have variable lengths
   */
  public static final FieldType TYPE_VAR_LEN = new FieldType();
  static {
    TYPE_VAR_LEN.setDocValueType(DocValues.Type.BYTES_VAR_DEREF);
    TYPE_VAR_LEN.freeze();
  }

  /**
   * Create a new variable-length indirect DocValues field.
   * <p>
   * This calls 
   * {@link DerefBytesDocValuesField#DerefBytesDocValuesField(String, BytesRef, boolean)
   *  DerefBytesDocValuesField(name, bytes, false}, meaning by default
   * it allows for values of different lengths. If your values are all 
   * the same length, use that constructor instead.
   * @param name field name
   * @param bytes binary content
   * @throws IllegalArgumentException if the field name is null
   */
  public DerefBytesDocValuesField(String name, BytesRef bytes) {
    super(name, TYPE_VAR_LEN);
    fieldsData = bytes;
  }

  /**
   * Create a new fixed or variable length indirect DocValues field.
   * <p>
   * @param name field name
   * @param bytes binary content
   * @param isFixedLength true if all values have the same length.
   * @throws IllegalArgumentException if the field name is null
   */
  public DerefBytesDocValuesField(String name, BytesRef bytes, boolean isFixedLength) {
    super(name, isFixedLength ? TYPE_FIXED_LEN : TYPE_VAR_LEN);
    fieldsData = bytes;
  }
}
