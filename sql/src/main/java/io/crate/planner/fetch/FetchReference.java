/*
 * Licensed to Crate under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Crate licenses this file
 * to you under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial
 * agreement.
 */

package io.crate.planner.fetch;

import io.crate.analyze.symbol.Reference;
import io.crate.analyze.symbol.Symbol;
import io.crate.analyze.symbol.SymbolType;
import io.crate.analyze.symbol.SymbolVisitor;
import io.crate.types.DataType;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import javax.annotation.Nullable;
import java.io.IOException;

public class FetchReference extends Symbol {

    public static final com.google.common.base.Function<FetchReference, Reference>
            REF_FUNCTION = new com.google.common.base.Function<FetchReference, Reference>() {
        @Nullable
        @Override
        public Reference apply(@Nullable FetchReference input) {
            assert input != null;
            return input.ref();
        }
    };

    private final Symbol docId;
    private final Reference ref;

    public FetchReference(Symbol docId, Reference ref) {
        this.docId = docId;
        this.ref = ref;
    }

    @Override
    public SymbolType symbolType() {
        return SymbolType.FETCH_REFERENCE;
    }

    @Override
    public <C, R> R accept(SymbolVisitor<C, R> visitor, C context) {
        return visitor.visitFetchReference(this, context);
    }

    @Override
    public DataType valueType() {
        return ref.valueType();
    }

    public Symbol docId() {
        return docId;
    }

    public Reference ref() {
        return ref;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        throw new UnsupportedOperationException("FetchReference cannot be streamed");
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        throw new UnsupportedOperationException("FetchReference cannot be streamed");
    }
}
