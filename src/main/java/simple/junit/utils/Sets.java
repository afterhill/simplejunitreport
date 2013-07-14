package simple.junit.utils;

import javax.annotation.Nonnull;
import java.util.HashSet;
import java.util.LinkedHashSet;

import static java.util.Collections.addAll;
import static simple.junit.utils.Preconditions.checkNotNull;

/**
 * Utility methods related to {@code java.util.Set}s.
 * 
 * @author Alex Ruiz
 */
public final class Sets {
	private Sets() {
	}

	/**
	 * Creates a <em>mutable</em> {@code HashSet}.
	 * 
	 * @param <T>
	 *            the generic type of the {@code HashSet} to create.
	 * @return the created {@code HashSet}.
	 * @since 1.2.3
	 */
	public static @Nonnull
	<T> HashSet<T> newHashSet() {
		return new HashSet<T>();
	}

	/**
	 * Creates a <em>mutable</em> {@code HashSet} containing the given elements.
	 * 
	 * @param <T>
	 *            the generic type of the {@code HashSet} to create.
	 * @param elements
	 *            the elements to store in the {@code HashSet}.
	 * @return the created {@code HashSet}.
	 * @throws NullPointerException
	 *             if the given {@code Iterable} is {@code null}.
	 * @since 1.2.3
	 */
	public static @Nonnull
	<T> HashSet<T> newHashSet(@Nonnull Iterable<? extends T> elements) {
		HashSet<T> set = newHashSet();
		for (T e : elements) {
			set.add(e);
		}
		return set;
	}

	/**
	 * Creates a <em>mutable</em> {@code LinkedHashSet}.
	 * 
	 * @param <T>
	 *            the generic type of the {@code LinkedHashSet} to create.
	 * @return the created {@code LinkedHashSet}.
	 * @since 1.2.3
	 */
	public static @Nonnull
	<T> LinkedHashSet<T> newLinkedHashSet() {
		return new LinkedHashSet<T>();
	}

	/**
	 * Creates a <em>mutable</em> {@link LinkedHashSet} containing the given
	 * elements.
	 * 
	 * @param <T>
	 *            the generic type of the {@code LinkedHashSet} to create.
	 * @param elements
	 *            the elements to store in the {@code LinkedHashSet}.
	 * @return the created {@code LinkedHashSet}.
	 * @throws NullPointerException
	 *             if the given array is {@code null}.
	 * @since 1.2.3
	 */
	public static @Nonnull
	<T> LinkedHashSet<T> newLinkedHashSet(@Nonnull T... elements) {
		checkNotNull(elements);
		LinkedHashSet<T> set = new LinkedHashSet<T>();
		addAll(set, elements);
		return set;
	}
}
