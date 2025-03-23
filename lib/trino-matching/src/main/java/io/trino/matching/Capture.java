/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.matching;

import java.util.concurrent.atomic.AtomicInteger;

public class Capture<T>
{
    private static final AtomicInteger sequenceCounter = new AtomicInteger(); // 静态变量，属于类，每生成一个实例都会增1

    private final String description; // 自定义描述，外部传入，方便人阅读

    public static <T> Capture<T> newCapture()
    {
        return newCapture("");
    }

    public static <T> Capture<T> newCapture(String description)
    {
        return new Capture<>(description + "@" + sequenceCounter.incrementAndGet());
    }

    private Capture(String description)
    {
        this.description = description;
    }

    public String description()
    {
        return description;
    }
}
