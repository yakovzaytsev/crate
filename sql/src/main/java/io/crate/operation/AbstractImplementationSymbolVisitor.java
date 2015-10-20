/*
 * Licensed to CRATE Technology GmbH ("Crate") under one or more contributor
 * license agreements.  See the NOTICE file distributed with this work for
 * additional information regarding copyright ownership.  Crate licenses
 * this file to you under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *  
 *   http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *  
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial agreement.
 */

package io.crate.operation;

import io.crate.analyze.symbol.Symbol;
import io.crate.metadata.Functions;
import io.crate.planner.node.dql.CollectPhase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractImplementationSymbolVisitor<C extends AbstractImplementationSymbolVisitor.Context>
        extends BaseImplementationSymbolVisitor<C> {

    public abstract static class Context {

        protected ArrayList<Input<?>> topLevelInputs = new ArrayList<>();

        public void add(Input<?> input) {
            topLevelInputs.add(input);
        }

        public List<Input<?>> topLevelInputs() {
            return topLevelInputs;
        }

    }

    protected abstract C newContext();

    public C extractImplementations(CollectPhase node) {
        C context = newContext();
        if (node.toCollect() != null) {
            for (Symbol symbol : node.toCollect()) {
                context.add(process(symbol, context));
            }
        }
        return context;
    }

    public C extractImplementations(Symbol... symbols) {
        C context = newContext();
        for (Symbol symbol : symbols) {
            context.add(process(symbol, context));
        }
        return context;
    }

    public C extractImplementations(Collection<? extends Symbol> symbols) {
        C context = newContext();
        for (Symbol symbol : symbols) {
            context.add(process(symbol, context));
        }
        return context;
    }

    public AbstractImplementationSymbolVisitor(Functions functions) {
        super(functions);
    }

}
