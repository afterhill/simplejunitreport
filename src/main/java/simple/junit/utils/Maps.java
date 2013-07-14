package simple.junit.utils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static simple.junit.utils.ToString.toStringOf;

/**
 * Utility methods related to {@code java.util.Map}s.
 * 
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class Maps {
	private Maps() {
	}

	/**
	 * Returns a <em>mutable</em> {@code HashMap}.
	 * 
	 * @return the created {@code Map}.
	 */
	public static @Nonnull
	<K, V> Map<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	/**
	 * Returns a <em>mutable</em> {@code ConcurrentMap}.
	 * 
	 * @return the created {@code Map}.
	 */
	public static @Nonnull
	<K, V> ConcurrentMap<K, V> newConcurrentHashMap() {
		return new ConcurrentHashMap<K, V>();
	}

	/**
	 * Returns a <em>mutable</em> {@code WeakHashMap}.
	 * 
	 * @return the created {@code Map}.
	 */
	public static @Nonnull
	<K, V> WeakHashMap<K, V> newWeakHashMap() {
		return new WeakHashMap<K, V>();
	}

	/**
	 * Indicates whether the given {@code Map} is {@code null} or empty.
	 * 
	 * @param map
	 *            the map to check.
	 * @return {@code true} if the given {@code Map} is {@code null} or empty,
	 *         otherwise {@code false}.
	 */
	public static boolean isNullOrEmpty(@Nullable Map<?, ?> map) {
		return map == null || map.isEmpty();
	}

	/**
	 * Returns the {@code String} representation of the given map, or
	 * {@code null} if the given map is {@code null}.
	 * 
	 * @param map
	 *            the map to format.
	 * @return the {@code String} representation of the given map.
	 */
	public static @Nullable
	String format(@Nullable Map<?, ?> map) {
		if (map == null) {
			return null;
		}
		Iterator<?> i = map.entrySet().iterator();
		if (!i.hasNext()) {
			return "{}";
		}
		StringBuilder buffer = new StringBuilder();
		buffer.append("{");
		for (;;) {
			Entry<?, ?> e = (Entry<?, ?>) i.next();
			buffer.append(format(map, e.getKey()));
			buffer.append('=');
			buffer.append(format(map, e.getValue()));
			if (!i.hasNext()) {
				return buffer.append("}").toString();
			}
			buffer.append(", ");
		}
	}

	private static @Nullable
	Object format(@Nonnull Map<?, ?> map, @Nullable Object o) {
		return o == map ? "(this Map)" : toStringOf(o);
	}
}
