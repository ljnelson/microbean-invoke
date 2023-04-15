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

import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.invoke.MethodType;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Useful constant bootstrap methods and methods that make sense to invoke from {@link
 * java.lang.invoke.ConstantBootstraps#invoke(Lookup, String, Class, MethodHandle, Object...)}.
 *
 * @author <a href="https://about.me/lairdnelson" target="_parent">Laird Nelson</a>
 *
 * @see <a href="https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/lang/invoke/package-summary.html"
 * target="_parent"><code>java.lang.invoke</code></a>
 */
public final class BootstrapMethods {


  /*
   * Constructors.
   */


  private BootstrapMethods() {
    super();
  }


  /*
   * public static methods.
   */


  /**
   * Returns a {@link ConstantCallSite} {@linkplain ConstantCallSite#getTarget() backed} by a {@linkplain
   * Lookup#findStaticSetter(Class, String, Class) static setter <code>MethodHandle</code>}.
   *
   * @param lookup a {@link Lookup}; will not be {@code null}
   *
   * @param fieldName the name of the static field to find; will not be {@code null}
   *
   * @param methodType a {@link MethodType}; will not be {@code null}
   *
   * @param targetClass the {@link Class} whose static field will be sought; will not be {@code null}
   *
   * @return a {@link ConstantCallSite} {@linkplain ConstantCallSite#getTarget() backed} by a {@linkplain
   * Lookup#findStaticSetter(Class, String, Class) static setter <code>MethodHandle</code>}
   *
   * @exception Throwable if an error occurs
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   *
   * @see Lookup#findStaticSetter(Class, String, Class)
   */
  public static final ConstantCallSite findStaticSetterCallSite(final Lookup lookup,
                                                                final String fieldName,
                                                                final MethodType methodType,
                                                                final Class<?> targetClass)
    throws Throwable {
    return new ConstantCallSite(findStaticSetterMethodHandle(lookup, fieldName, methodType.parameterType(0), targetClass));
  }

  /**
   * Returns a {@link ConstantCallSite} {@linkplain ConstantCallSite#getTarget() backed} by a {@linkplain
   * Lookup#findStatic(Class, String, MethodType) static <code>MethodHandle</code>} {@linkplain
   * MethodHandle#asSpreader(Class, int) adapted to be an <em>array-spreading</em> <code>MethodHandle</code>}.
   *
   * @param lookup a {@link Lookup}; will not be {@code null}
   *
   * @param methodName the name of the static field to find; will not be {@code null}
   *
   * @param methodType a {@link MethodType}; will not be {@code null}
   *
   * @param targetClass the {@link Class} whose static method will be sought; will not be {@code null}
   *
   * @return a {@link ConstantCallSite} {@linkplain ConstantCallSite#getTarget() backed} by a {@linkplain
   * Lookup#findStatic(Class, String, MethodType) static <code>MethodHandle</code>} {@linkplain
   * MethodHandle#asSpreader(Class, int) adapted to be an <em>array-spreading</em> <code>MethodHandle</code>}
   *
   * @exception Throwable if an error occurs
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   *
   * @see Lookup#findStatic(Class, String, MethodType)
   *
   * @see MethodHandle#asSpreader(Class, int)
   */
  public static final ConstantCallSite findStaticCallSiteAsSpreader(final Lookup lookup,
                                                                    final String methodName,
                                                                    final MethodType methodType,
                                                                    final Class<?> targetClass)
    throws Throwable {
    return new ConstantCallSite(findStaticMethodHandle(lookup, methodName, methodType, targetClass)
                                .asSpreader(Object[].class, methodType.parameterCount()));
  }

  /**
   * Returns a {@link ConstantCallSite} {@linkplain ConstantCallSite#getTarget() backed} by a {@linkplain
   * Lookup#findSetter(Class, String, Class) setter <code>MethodHandle</code>}.
   *
   * @param lookup a {@link Lookup}; will not be {@code null}
   *
   * @param fieldName the name of the field to find; will not be {@code null}
   *
   * @param methodType a {@link MethodType}; will not be {@code null}
   *
   * @param targetClass the {@link Class} whose instance field will be sought; will not be {@code null}
   *
   * @return a {@link ConstantCallSite} {@linkplain ConstantCallSite#getTarget() backed} by a {@linkplain
   * Lookup#findSetter(Class, String, Class) setter <code>MethodHandle</code>}
   *
   * @exception Throwable if an error occurs
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   *
   * @see Lookup#findSetter(Class, String, Class)
   */
  public static final ConstantCallSite findSetterCallSite(final Lookup lookup,
                                                          final String fieldName,
                                                          final MethodType methodType,
                                                          final Class<?> targetClass)
    throws Throwable {
    return new ConstantCallSite(findSetterMethodHandle(lookup, fieldName, methodType.parameterType(0), targetClass));
  }

  /**
   * Returns a {@link ConstantCallSite} {@linkplain ConstantCallSite#getTarget() backed} by a {@linkplain
   * Lookup#findVirtual(Class, String, MethodType) virtual <code>MethodHandle</code>}.
   *
   * @param lookup a {@link Lookup}; will not be {@code null}
   *
   * @param methodName the name of the method to find; will not be {@code null}
   *
   * @param methodType a {@link MethodType}; will not be {@code null}
   *
   * @param targetClass the {@link Class} whose instance field will be sought; will not be {@code null}
   *
   * @return a {@link ConstantCallSite} {@linkplain ConstantCallSite#getTarget() backed} by a {@linkplain
   * Lookup#findVirtual(Class, String, MethodType) virtual <code>MethodHandle</code>}
   *
   * @exception Throwable if an error occurs
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   *
   * @see Lookup#findVirtual(Class, String, MethodType)
   */
  public static final ConstantCallSite findVirtualCallSite(final Lookup lookup,
                                                           final String methodName,
                                                           final MethodType methodType,
                                                           final Class<?> targetClass)
    throws Throwable {
    return new ConstantCallSite(findVirtualMethodHandle(lookup, methodName, methodType, targetClass));
  }

  /**
   * Returns a {@link ConstantCallSite} {@linkplain ConstantCallSite#getTarget() backed} by a {@linkplain
   * Lookup#findConstructor(Class, MethodType) constructor <code>MethodHandle</code>}.
   *
   * @param lookup a {@link Lookup}; will not be {@code null}
   *
   * @param ignoredMethodName ignored
   *
   * @param methodType a {@link MethodType}; will not be {@code null}
   *
   * @param targetClass the {@link Class} whose instance field will be sought; will not be {@code null}
   *
   * @return a {@link ConstantCallSite} {@linkplain ConstantCallSite#getTarget() backed} by a {@linkplain
   * Lookup#findConstructor(Class, MethodType) constructor <code>MethodHandle</code>}
   *
   * @exception Throwable if an error occurs
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   *
   * @see Lookup#findConstructor(Class, MethodType)
   */
  public static final ConstantCallSite findConstructorCallSite(final Lookup lookup,
                                                               final String ignoredMethodName,
                                                               final MethodType methodType,
                                                               final Class<?> targetClass)
    throws Throwable {
    return new ConstantCallSite(findConstructorMethodHandle(lookup, methodType, targetClass));
  }

  /**
   * Given an {@link Iterable} of {@link Entry} instances, creates an immutable {@link Map} representing them that
   * preserves the iteration order.
   *
   * @param <K> the type borne by the returned {@link Map}'s {@linkplain Map#keySet() keys}
   *
   * @param <V> the type borne by the returned {@link Map}'s {@linkplain Map#values() values}
   *
   * @param entries the {@link Entry} instances used to build the {@link Map}; must not be {@code null}
   *
   * @return an immutable {@link Map} representing the supplied {@link Map} that preserves its iteration order
   *
   * @exception NullPointerException if {@code entries} is {@code null}
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   */
  public static final <K, V> Map<K, V> immutableOrderedMapOf(final Iterable<Entry<? extends K, ? extends V>> entries) {
    if (entries == null) {
      return Map.of();
    }
    final Iterator<Entry<? extends K, ? extends V>> i = entries.iterator();
    if (i.hasNext()) {
      final Map<K, V> m = new LinkedHashMap<>();
      Entry<? extends K, ? extends V> entry = i.next();
      m.put(entry.getKey(), entry.getValue());
      while (i.hasNext()) {
        entry = i.next();
        m.put(entry.getKey(), entry.getValue());
      }
      return Collections.unmodifiableMap(m);
    }
    return Map.of();
  }

  /**
   * Given an {@link Map}, creates an immutable {@link Map} representing it that preserves its iteration order.
   *
   * @param <K> the type borne by the returned {@link Map}'s {@linkplain Map#keySet() keys}
   *
   * @param <V> the type borne by the returned {@link Map}'s {@linkplain Map#values() values}
   *
   * @param map the {@link Map} to represent
   *
   * @return an immutable {@link Map} representing the supplied {@link Map} that preserves its iteration order
   *
   * @exception NullPointerException if {@code map} is {@code null}
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   */
  public static final <K, V> Map<K, V> immutableOrderedMapOf(final Map<? extends K, ? extends V> map) {
    if (map == null || map.isEmpty()) {
      return Map.of();
    }
    return Collections.unmodifiableMap(new LinkedHashMap<>(map));
  }

  /**
   * Given an {@link Iterable} of {@link Entry} instances, creates a {@link SortedMap} representing them that is
   * immutable.
   *
   * @param <K> the type borne by the returned {@link Map}'s {@linkplain Map#keySet() keys}
   *
   * @param <V> the type borne by the returned {@link Map}'s {@linkplain Map#values() values}
   *
   * @param entries the {@link Entry} instances used to build the {@link SortedMap}; must not be {@code null}
   *
   * @return an immutable {@link SortedMap} representing the supplied {@link Map}
   *
   * @exception NullPointerException if {@code map} is {@code null}
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   */
  @SuppressWarnings("unchecked")
  public static final <K, V> SortedMap<K, V> immutableSortedMapOf(final Iterable<Entry<? extends K, ? extends V>> entries) {
    if (entries == null) {
      return Collections.emptySortedMap();
    }
    final Iterator<Entry<? extends K, ? extends V>> i = entries.iterator();
    if (i.hasNext()) {
      final SortedMap<K, V> mutableSortedMap = new TreeMap<>();
      Entry<? extends K, ? extends V> entry = i.next();
      mutableSortedMap.put(entry.getKey(), entry.getValue());
      while (i.hasNext()) {
        entry = i.next();
        mutableSortedMap.put(entry.getKey(), entry.getValue());
      }
      return Collections.unmodifiableSortedMap(mutableSortedMap);
    }
    return Collections.emptySortedMap();
  }

  /**
   * Given an {@link Iterable} of {@link Entry} instances, creates a {@link SortedMap} representing them that is
   * immutable.
   *
   * @param <K> the type borne by the returned {@link Map}'s {@linkplain Map#keySet() keys}
   *
   * @param <V> the type borne by the returned {@link Map}'s {@linkplain Map#values() values}
   *
   * @param entries the {@link Entry} instances used to build the {@link SortedMap}; must not be {@code null}
   *
   * @param comparator the {@link Comparator} to use to order the supplied {@link Map}'s elements; may be {@code null}
   * to indicate natural order should be used in which case the supplied {@link Map}'s elements must implement {@link
   * Comparable}
   *
   * @return an immutable {@link SortedMap} representing the supplied {@link Map}
   *
   * @exception NullPointerException if {@code map} is {@code null}
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   */
  @SuppressWarnings("unchecked")
  public static final <K, V> SortedMap<K, V> immutableSortedMapOf(final Iterable<Entry<? extends K, ? extends V>> entries,
                                                                  final Comparator<? super K> comparator) {
    if (entries == null) {
      return immutableSortedMapOf(comparator);
    }
    final Iterator<Entry<? extends K, ? extends V>> i = entries.iterator();
    if (i.hasNext()) {
      final SortedMap<K, V> mutableSortedMap = new TreeMap<>(comparator);
      Entry<? extends K, ? extends V> entry = i.next();
      mutableSortedMap.put(entry.getKey(), entry.getValue());
      while (i.hasNext()) {
        entry = i.next();
        mutableSortedMap.put(entry.getKey(), entry.getValue());
      }
      return Collections.unmodifiableSortedMap(mutableSortedMap);
    }
    return immutableSortedMapOf(comparator);
  }

  /**
   * Given a {@link Map} or a {@link SortedMap}, returns a {@link SortedMap} representing it that is immutable.
   *
   * @param <K> the type borne by the returned {@link SortedMap}'s {@linkplain Map#keySet() keys}
   *
   * @param <V> the type borne by the returned {@link SortedMap}'s {@linkplain Map#values() values}
   *
   * @param map the {@link Map} to represent; must not be {@code null}
   *
   * @return an immutable {@link SortedMap} representing the supplied {@link Map}
   *
   * @exception NullPointerException if {@code map} is {@code null}
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   */
  @SuppressWarnings("unchecked")
  public static final <K, V> SortedMap<K, V> immutableSortedMapOf(final Map<? extends K, ? extends V> map) {
    return immutableSortedMapOf(map, null);
  }

  /**
   * Given a {@link Map} or a {@link SortedMap}, returns a {@link SortedMap} representing it that is immutable.
   *
   * @param <K> the type borne by the supplied {@link Map}'s {@linkplain Map#keySet() keys}
   *
   * @param <V> the type borne by the supplied {@link Map}'s {@linkplain Map#values() values}
   *
   * @param map the {@link Map} to represent; must not be {@code null}
   *
   * @param comparator the {@link Comparator} to use to order the supplied {@link Map}'s elements; may be {@code null}
   * to indicate natural order should be used in which case the supplied {@link Map}'s elements must implement {@link
   * Comparable}
   *
   * @return an immutable {@link SortedMap} representing the supplied {@link Map}
   *
   * @exception NullPointerException if {@code map} is {@code null}
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   */
  @SuppressWarnings("unchecked")
  public static final <K, V> SortedMap<K, V> immutableSortedMapOf(final Map<? extends K, ? extends V> map,
                                                                  final Comparator<? super K> comparator) {
    if (comparator == null) {
      if (map == null || map.isEmpty()) {
        return Collections.emptySortedMap();
      }
      return Collections.unmodifiableSortedMap(map instanceof SortedMap ? (SortedMap<K, V>)map : new TreeMap<>(map));
    }
    final SortedMap<K, V> mutableSortedMap = new TreeMap<>(comparator);
    if (map != null) {
      mutableSortedMap.putAll(map);
    }
    return Collections.unmodifiableSortedMap(mutableSortedMap);
  }

  /**
   * Given a {@link Comparator}, returns an empty, immutable {@link SortedMap} whose {@link SortedMap#comparator()}
   * method will return it.
   *
   * @param <K> the type borne by the returned {@link Map}'s {@linkplain Map#keySet() keys}
   *
   * @param <V> the type borne by the returned {@link Map}'s {@linkplain Map#values() values}
   *
   * @param comparator the {@link Comparator} in question; may be {@code null}
   *
   * @return an immutable, empty {@link SortedMap} whose {@link SortedMap#comparator()} method will return the supplied {@code
   * comparator}
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   */
  public static final <K, V> SortedMap<K, V> immutableSortedMapOf(final Comparator<? super K> comparator) {
    return comparator == null ? Collections.emptySortedMap() : Collections.unmodifiableSortedMap(new TreeMap<>(comparator));
  }

  /**
   * Given a {@link Iterable}, returns an immutable {@link Set} representing it that preserves its iteration order.
   *
   * @param <E> the type borne by the supplied {@link Iterable}'s elements
   *
   * @param elements the {@link Iterable} to represent; must not be {@code null}
   *
   * @return an immutable {@link Set} representing the supplied {@link Iterable} and preserving its iteration order
   *
   * @exception NullPointerException if {@code elements} is {@code null}
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   */
  public static final <E> Set<E> immutableOrderedSetOf(final Iterable<? extends E> elements) {
    if (elements != null) {
      final Iterator<? extends E> i = elements.iterator();
      if (i.hasNext()) {
        final Set<E> set = new LinkedHashSet<>();
        set.add(i.next());
        while (i.hasNext()) {
          set.add(i.next());
        }
        return Collections.unmodifiableSet(set);
      }
    }
    return Set.of();
  }

  /**
   * Given a {@link Iterable}, returns a {@link SortedSet} representing it that is immutable.
   *
   * @param <E> the type borne by the supplied {@link Iterable}'s elements
   *
   * @param set the {@link Iterable} to represent; must not be {@code null}
   *
   * @return an immutable {@link SortedSet} representing the supplied {@link Iterable}
   *
   * @exception NullPointerException if {@code set} is {@code null}
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   */
  @SuppressWarnings("unchecked")
  public static final <E> SortedSet<E> immutableSortedSetOf(final Iterable<? extends E> set) {
    return immutableSortedSetOf(set, null);
  }

  /**
   * Given a {@link Iterable}, returns a {@link SortedSet} representing it that is immutable.
   *
   * @param <E> the type borne by the supplied {@link Iterable}'s elements
   *
   * @param set the {@link Iterable} to represent; must not be {@code null}
   *
   * @param comparator the {@link Comparator} to use to order the supplied {@link Iterable}'s elements; may be {@code
   * null} to indicate natural order should be used in which case the supplied {@link Iterable}'s elements must
   * implement {@link Comparable}
   *
   * @return an immutable {@link SortedSet} representing the supplied {@link Iterable}
   *
   * @exception NullPointerException if {@code set} is {@code null}
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   */
  @SuppressWarnings("unchecked")
  public static final <E> SortedSet<E> immutableSortedSetOf(final Iterable<? extends E> set,
                                                            final Comparator<? super E> comparator) {
    if (comparator == null) {
      if (set == null) {
        return Collections.emptySortedSet();
      } else if (set instanceof SortedSet) {
        final SortedSet<E> s = (SortedSet<E>)set;
        if (s.isEmpty()) {
          return Collections.emptySortedSet();
        }
        return Collections.unmodifiableSortedSet(s);
      }
    }
    final SortedSet<E> mutableSortedSet = new TreeSet<>(comparator);
    if (set != null) {
      for (final E e : set) {
        mutableSortedSet.add(e);
      }
    }
    return Collections.unmodifiableSortedSet(mutableSortedSet);
  }

  /**
   * Given a {@link Comparator}, returns an empty, immutable {@link SortedSet} whose {@link SortedSet#comparator()} method will return it.
   *
   * @param <E> the type borne by the returned {@link SortedSet}'s elements
   *
   * @param comparator the {@link Comparator} in question; may be {@code null}
   *
   * @return an immutable, empty {@link SortedSet} whose {@link SortedSet#comparator()} method will return the supplied
   * {@code comparator}
   *
   * @nullability This method never returns {@code null}.
   *
   * @idempotency This method is idempotent and deterministic.
   *
   * @threadsafety This method is safe for concurrent use by multiple threads.
   */
  public static final <E> SortedSet<E> immutableSortedSetOf(final Comparator<? super E> comparator) {
    return comparator == null ? Collections.emptySortedSet() : Collections.unmodifiableSortedSet(new TreeSet<>(comparator));
  }


  /*
   * private static methods.
   */


  private static final MethodHandle findStaticMethodHandle(final Lookup lookup,
                                                           final String methodName,
                                                           final MethodType methodType,
                                                           final Class<?> targetClass)
    throws Throwable {
    return MethodHandles.privateLookupIn(targetClass, lookup).findStatic(targetClass, methodName, methodType);
  }

  private static final MethodHandle findStaticSetterMethodHandle(final Lookup lookup,
                                                                 final String fieldName,
                                                                 final Class<?> fieldType,
                                                                 final Class<?> targetClass)
    throws Throwable {
    return
      MethodHandles.privateLookupIn(targetClass, lookup).findStaticSetter(targetClass, fieldName, fieldType);
  }

  private static final MethodHandle findSetterMethodHandle(final Lookup lookup,
                                                           final String fieldName,
                                                           final Class<?> fieldType,
                                                           final Class<?> targetClass)
    throws Throwable {
    return
      MethodHandles.privateLookupIn(targetClass, lookup).findSetter(targetClass, fieldName, fieldType);
  }

  private static final MethodHandle findVirtualMethodHandle(final Lookup lookup,
                                                            final String methodName,
                                                            final MethodType methodType,
                                                            final Class<?> targetClass)
    throws Throwable {
    return MethodHandles.privateLookupIn(targetClass, lookup).findVirtual(targetClass, methodName, methodType);
  }

  private static final MethodHandle findConstructorMethodHandle(final Lookup lookup,
                                                                final MethodType methodType,
                                                                final Class<?> targetClass)
    throws Throwable {
    if (void.class != methodType.returnType()) {
      throw new IllegalArgumentException("void.class != methodType.returnType(); methodType: " + methodType);
    }
    return MethodHandles.privateLookupIn(targetClass, lookup).findConstructor(targetClass, methodType);
  }


}
