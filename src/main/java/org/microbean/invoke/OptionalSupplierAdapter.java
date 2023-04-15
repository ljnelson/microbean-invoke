/* -*- mode: Java; c-basic-offset: 2; indent-tabs-mode: nil; coding: utf-8-unix -*-
 *
 * Copyright © 2022–2023 microBean™.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations under the License.
 */
package org.microbean.invoke;

import java.util.Objects;

import java.util.function.Supplier;

final class OptionalSupplierAdapter<T> implements OptionalSupplier<T> {

  private final Supplier<? extends Determinism> determinismSupplier;

  private final Supplier<? extends T> supplier;

  OptionalSupplierAdapter() {
    this(Determinism.ABSENT, Absence.of());
  }

  OptionalSupplierAdapter(Supplier<? extends T> supplier) {
    this(supplier == null ? Determinism.ABSENT : supplier instanceof OptionalSupplier<? extends T> os ? os.determinism() : Determinism.NON_DETERMINISTIC,
         supplier);
  }

  OptionalSupplierAdapter(final Determinism determinism, Supplier<? extends T> supplier) {
    super();
    if (supplier == null) {
      this.determinismSupplier = FixedValueSupplier.of(Determinism.ABSENT);
      this.supplier = Absence.of();
    } else {
      this.supplier = supplier;
      if (supplier instanceof OptionalSupplier<? extends T> os) {
        this.determinismSupplier = os::determinism;
      } else {
        this.determinismSupplier = FixedValueSupplier.of(Objects.requireNonNull(determinism, "determinism"));
      }
    }
  }

  @Override
  public final Determinism determinism() {
    return this.determinismSupplier.get();
  }

  @Override
  public final T get() {
    return this.supplier.get();
  }

}
