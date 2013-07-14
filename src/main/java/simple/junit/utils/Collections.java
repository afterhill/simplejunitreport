package simple.junit.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import static simple.junit.utils.Lists.emptyList;
import static simple.junit.utils.Sets.newHashSet;
import static simple.junit.utils.ToString.toStringOf;

/**
 * Utility methods related to {@code Collection}s.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
public final class Collections {
	private Collections() {
	}

	/**
	 * Returns any duplicate elements from the given {@code Collection}.
	 * 
	 * @param <T>
	 *            the generic type of the given {@code Collection}.
	 * @param c
	 *            the given {@code Collection} that might have duplicate
	 *            elements.
	 * @return a {@code Collection} containing the duplicate elements of the
	 *         given one. If the given {@code Collection} is {@code null} or if
	 *         no duplicates were found, an empty {@code Collection} is
	 *         returned.
	 */
	public static @Nonnull
	<T> Collection<T> duplicatesFrom(@Nullable Collection<T> c) {
		Set<T> duplicates = new LinkedHashSet<T>();
		if (c == null) {
			return duplicates;
		}
		Set<T> unique = newHashSet();
		for (T e : c) {
			if (unique.contains(e)) {
				duplicates.add(e);
				continue;
			}
			unique.add(e);
		}
		return duplicates;
	}

	/**
	 * Indicates whether the given {@code Collection} is {@code null} or empty.
	 * 
	 * @param c
	 *            the given {@code Collection}.
	 * @return {@code true} if the given {@code Collection} is {@code null} or
	 *         empty, otherwise {@code false}.
	 */
	public static boolean isNullOrEmpty(@Nullable Collection<?> c) {
		return c == null || c.isEmpty();
	}

	/**
	 * Returns the {@code String} representation of the given {@code Collection}
	 * , or {@code null} if the given {@code Collection} is {@code null}.
	 * 
	 * @param c
	 *            the {@code Collection} to format.
	 * @return the {@code String} representation of the given {@code Collection}
	 *         .
	 */
	public static @Nullable
	String format(@Nullable Collection<?> c) {
		if (c == null) {
			return null;
		}
		Iterator<?> i = c.iterator();
		if (!i.hasNext()) {
			return "[]";
		}
		StringBuilder b = new StringBuilder();
		b.append('[');
		for (;;) {
			Object e = i.next();
			b.append(e == c ? "(this Collection)" : toStringOf(e));
			if (!i.hasNext()) {
				return b.append(']').toString();
			}
			b.append(", ");
		}
	}

	/**
	 * Returns all the non-{@code null} elements in the given {@link Collection}
	 * .
	 * 
	 * @param <T>
	 *            the type of elements of the {@code Collection}.
	 * @param c
	 *            the given {@code Collection}.
	 * @return all the non-{@code null} elements in the given {@code Collection}
	 *         . An empty list is returned if the given {@code Collection} is
	 *         {@code null}.
	 * @since 1.1.3
	 */
	public static @Nonnull
	<T> List<T> nonNullElementsIn(@Nullable Collection<T> c) {
		if (c == null) {
			return emptyList();
		}
		List<T> nonNull = new ArrayList<T>();
		for (T element : c) {
			if (element != null) {
				nonNull.add(element);
			}
		}
		return nonNull;
	}
}
