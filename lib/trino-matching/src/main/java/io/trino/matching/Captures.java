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

import java.util.NoSuchElementException;
import java.util.Objects;

public class Captures
{
    private static final Captures NIL = new Captures(null, null, null);

    private final Capture<?> capture; // 表示捕获的键
    private final Object value; // 捕获的值，Object 类型也就是可以存任意类型的值
    private final Captures tail; // 这个变量名取的不好，应该叫 next，指向下一个 Captures 对象

    private Captures(Capture<?> capture, Object value, Captures tail)
    {
        this.capture = capture;
        this.value = value;
        this.tail = tail;
    }

    public static Captures empty()
    {
        return NIL;
    }

    public static <T> Captures ofNullable(Capture<T> capture, T value)
    {
        return capture == null ? empty() : new Captures(capture, value, NIL);
    }

    public Captures addAll(Captures other)
    {
        if (this == NIL) {
            return other;
        }
        return new Captures(capture, value, tail.addAll(other));
    }

    @SuppressWarnings("unchecked cast")
    public <T> T get(Capture<T> capture)
    {
        if (this.equals(NIL)) {
            throw new NoSuchElementException("Requested value for unknown Capture. Was it registered in the Pattern?");
        }
        if (this.capture.equals(capture)) { // 找到了，则返回对应的 value
            return (T) value;
        }
        return tail.get(capture); // 没找到则继续往下找
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Captures captures = (Captures) o;
        return Objects.equals(capture, captures.capture)
                && Objects.equals(value, captures.value)
                && Objects.equals(tail, captures.tail);
    }

    @Override
    public int hashCode()
    {
        int result = capture != null ? capture.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (tail != null ? tail.hashCode() : 0);
        return result;
    }
}
