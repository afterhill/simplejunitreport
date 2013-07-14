package simple.junit.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

import static java.lang.reflect.Array.getLength;
import static simple.junit.utils.Arrays.isArray;
import static simple.junit.utils.Sets.newHashSet;
import static simple.junit.utils.ToString.toStringOf;

/**
 * Creates a {@code String} representation of an array.
 * 
 * @author Alex Ruiz
 * @author Joel Costigliola
 */
final class ArrayFormatter {
	private static final String NULL = "null";

	@Nullable
	String format(@Nullable Object o) {
		if (o == null || !isArray(o)) {
			return null;
		}
		return isObjectArray(o) ? formatObjectArray(o)
				: formatPrimitiveArray(o);
	}

	private @Nonnull
	String formatObjectArray(@Nonnull Object o) {
		Object[] array = (Object[]) o;
		int size = array.length;
		if (size == 0) {
			return "[]";
		}
		StringBuilder buffer = new StringBuilder((20 * (size - 1)));
		HashSet<Object[]> alreadyFormatted = newHashSet();
		deepToString(array, buffer, alreadyFormatted);
		return buffer.toString();
	}

	private void deepToString(@Nullable Object[] array,
			@Nonnull StringBuilder buffer,
			@Nonnull Set<Object[]> alreadyFormatted) {
		if (array == null) {
			buffer.append(NULL);
			return;
		}
		alreadyFormatted.add(array);
		buffer.append('[');
		int size = array.length;
		for (int i = 0; i < size; i++) {
			if (i != 0) {
				buffer.append(", ");
			}
			Object element = array[i];
			if (!isArray(element)) {
				buffer.append(element == null ? NULL : toStringOf(element));
				continue;
			}
			if (!isObjectArray(element)) {
				buffer.append(formatPrimitiveArray(element));
				continue;
			}
			if (alreadyFormatted.contains(element)) {
				buffer.append("[...]");
				continue;
			}
			deepToString((Object[]) element, buffer, alreadyFormatted);
		}
		buffer.append(']');
		alreadyFormatted.remove(array);
	}

	private boolean isObjectArray(@Nullable Object o) {
		return o != null && isArray(o) && !isArrayTypePrimitive(o);
	}

	private String formatPrimitiveArray(@Nullable Object o) {
		if (o == null || !isArray(o)) {
			return null;
		}
		if (!isArrayTypePrimitive(o)) {
			String msg = String.format("<%s> is not an array of primitives", o);
			throw new IllegalArgumentException(msg);
		}
		int size = getLength(o);
		if (size == 0) {
			return "[]";
		}
		StringBuilder buffer = new StringBuilder();
		buffer.append('[');
		buffer.append(toStringOf(Array.get(o, 0)));
		for (int i = 1; i < size; i++) {
			buffer.append(", ");
			buffer.append(toStringOf(Array.get(o, i)));
		}
		buffer.append("]");
		return buffer.toString();
	}

	private boolean isArrayTypePrimitive(@Nonnull Object o) {
		return o.getClass().getComponentType().isPrimitive();
	}
}
