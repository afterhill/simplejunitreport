package simple.junit.utils;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static simple.junit.utils.Preconditions.checkNotNull;

public final class Lists {
	private Lists() {
	}

	/**
	 * Creates a <em>mutable</em> {@link ArrayList} containing the given
	 * elements.
	 * 
	 * @param <T>
	 *            the generic type of the {@code ArrayList} to create.
	 * @param elements
	 *            the elements to store in the {@code ArrayList}.
	 * @return the created {@code ArrayList}.
	 * @throws NullPointerException
	 *             if the given array is {@code null}.
	 */
	public static @Nonnull
	<T> ArrayList<T> newArrayList(@Nonnull T... elements) {
		checkNotNull(elements);
		ArrayList<T> list = newArrayList();
		for (T e : elements) {
			list.add(e);
		}
		return list;
	}

	/**
	 * Creates a <em>mutable</em> {@link ArrayList} containing the given
	 * elements.
	 * 
	 * @param <T>
	 *            the generic type of the {@code ArrayList} to create.
	 * @param elements
	 *            the elements to store in the {@code ArrayList}.
	 * @return the created {@code ArrayList}.
	 * @throws NullPointerException
	 *             if the given {@code Iterable} is {@code null}.
	 */
	public static @Nonnull
	<T> ArrayList<T> newArrayList(@Nonnull Iterable<? extends T> elements) {
		checkNotNull(elements);
		ArrayList<T> list = newArrayList();
		for (T e : elements) {
			list.add(e);
		}
		return list;
	}

	/**
	 * Creates a <em>mutable</em> {@link ArrayList} containing the given
	 * elements.
	 * 
	 * @param <T>
	 *            the generic type of the {@code ArrayList} to create.
	 * @param elements
	 *            the elements to store in the {@code ArrayList}.
	 * @return the created {@code ArrayList}.
	 * @throws NullPointerException
	 *             if the given {@code Iterable} is {@code null}.
	 */
	public static @Nonnull
	<T> ArrayList<T> newArrayList(@Nonnull Iterator<? extends T> elements) {
		checkNotNull(elements);
		ArrayList<T> list = newArrayList();
		while (elements.hasNext()) {
			list.add(elements.next());
		}
		return list;
	}

	/**
	 * Creates a <em>mutable</em> {@link ArrayList}.
	 * 
	 * @param <T>
	 *            the generic type of the {@code ArrayList} to create.
	 * @return the created {@code ArrayList}, of {@code null} if the given array
	 *         of elements is {@code null}.
	 */
	public static @Nonnull
	<T> ArrayList<T> newArrayList() {
		return new ArrayList<T>();
	}

	/**
	 * @return an empty, <em>immutable</em> {@code List}.
	 */
	public static @Nonnull
	<T> List<T> emptyList() {
		return Collections.emptyList();
	}
}
