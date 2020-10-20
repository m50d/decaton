/*
 * Copyright 2020 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.decaton.processor;

import com.linecorp.decaton.processor.runtime.TracingProvider.TraceHandle;

public class TestTraceHandle implements TraceHandle {
    private final String traceId;

    public TestTraceHandle(String traceId) {
        this.traceId = traceId;
        TestTracingProvider.openTraces.add(traceId);
    }

    @Override
    public void processingStart() {
        TestTracingProvider.traceId.set(traceId);
    }

    @Override
    public void processingReturn() {
        TestTracingProvider.traceId.remove();
    }

    @Override
    public void processingCompletion() {
        TestTracingProvider.openTraces.remove(traceId);
    }

    @Override
    public TraceHandle childFor(DecatonProcessor<?> processor) {
        final String childTraceId = traceId + "-" + processor.name();
        return new TestTraceHandle(childTraceId);
    }
}