package simple.junit.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.*;

import static simple.junit.utils.Arrays.isArray;
import static simple.junit.utils.Preconditions.checkNotNull;
import static simple.junit.utils.Strings.quote;

/**
 * Obtains the {@code toString} representation of an object.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 * @author Yvonne Wang
 */
public final class ToString {
	private ToString() {
	}

	/**
	 * Returns the {@code toString} representation of the given object. It may
	 * or not the object's own implementation of {@code toString}.
	 * 
	 * @param o
	 *            the given object.
	 * @return the {@code toString} representation of the given object.
	 */
	public static @Nullable
	String toStringOf(@Nullable Object o) {
		if (isArray(o)) {
			return Arrays.format(o);
		}
		if (o instanceof Calendar) {
			return toStringOf(o);
		}
		if (o instanceof Class<?>) {
			return toStringOf((Class<?>) o);
		}
		if (o instanceof Collection<?>) {
			return toStringOf((Collection<?>) o);
		}
		if (o instanceof Date) {
			return toStringOf(o);
		}
		if (o instanceof Float) {
			return toStringOf((Float) o);
		}
		if (o instanceof Long) {
			return toStringOf((Long) o);
		}
		if (o instanceof File) {
			return toStringOf((File) o);
		}
		if (o instanceof Map<?, ?>) {
			return toStringOf((Map<?, ?>) o);
		}
		if (o instanceof String) {
			return quote((String) o);
		}
		if (o instanceof Comparator) {
			return toStringOf((Comparator<?>) o);
		}
		return o == null ? null : o.toString();
	}

	private static @Nonnull
	String toStringOf(@Nonnull Comparator<?> comparator) {
		String typeName = comparator.getClass().getSimpleName();
		String toString = quote(!typeName.isEmpty() ? typeName
				: "Anonymous Comparator class");
		return checkNotNull(toString);
	}

	private static @Nonnull
	String toStringOf(@Nonnull Class<?> c) {
		return c.getCanonicalName();
	}

	private static @Nonnull
	String toStringOf(@Nonnull Collection<?> c) {
		return checkNotNull(Collections.format(c));
	}

	private static @Nonnull
	String toStringOf(@Nonnull Float f) {
		if (f.isNaN()) {
			return "NaN";
		}
		return String.format("%sf", f);
	}

	private static @Nonnull
	String toStringOf(@Nonnull Long l) {
		return String.format("%sL", l);
	}

	private static @Nullable
	String toStringOf(@Nonnull File f) {
		return f.getAbsolutePath();
	}

	private static @Nonnull
	String toStringOf(@Nonnull Map<?, ?> m) {
		return checkNotNull(Maps.format(m));
	}
}
