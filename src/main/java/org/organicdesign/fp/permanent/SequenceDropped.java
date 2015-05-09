// Copyright 2015-04-05 PlanBase Inc. & Glen Peterson
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.organicdesign.fp.permanent;

import org.organicdesign.fp.Lazy;
import org.organicdesign.fp.Option;

public class SequenceDropped<T> implements Sequence<T> {
    private final Lazy.Ref<Sequence<T>> laz;

    SequenceDropped(Sequence<T> v, long n) {
        laz = Lazy.Ref.of(() -> {
            Sequence<T> seq = v;
            for (long i = n; i > 0; i--) {
                if (!seq.head().isSome()) { return Sequence.emptySequence(); }
                seq = seq.tail();
            }
            return seq;
        });
    }

    public static <T> Sequence<T> of(Sequence<T> v, long numItems) {
        if (numItems < 0) { throw new IllegalArgumentException("You can only drop a non-negative number of items"); }
        if (numItems == 0) { return v; }
        if ( (v == null) || (EMPTY_SEQUENCE == v) ) { return Sequence.emptySequence(); }
        return new SequenceDropped<>(v, numItems);
    }

    @Override public Option<T> head() { return laz.get().head(); }

    @Override public Sequence<T> tail() { return laz.get().tail(); }

//    @Override public int hashCode() { return Sequence.hashCode(this); }
//
//    @Override public boolean equals(Object o) {
//        if (this == o) { return true; }
//        if ( (o == null) || !(o instanceof Sequence) ) { return false; }
//        return Sequence.equals(this, (Sequence) o);
//    }
//
//    @Override public String toString() {
//        return "SequenceDropped(" + (laz.isRealizedYet() ? laz.get().head() : "*lazy*") + ",...)";
//    }
}
