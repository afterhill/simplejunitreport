package simple.junit.reporter;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

import simple.junit.reporter.GUITests;


public final class GUITests {

	public static boolean isGUITest(Class<?> type, Method method) {
		if (isGUITest(type))
			return true;
		if (isGUITest(method))
			return true;
		return (isSuperclassGUITest(type, method));
	}

	private static boolean isSuperclassGUITest(Class<?> type, Method method) {
		Class<?> superclass = type.getSuperclass();
		while (superclass != null) {
			if (isGUITest(superclass))
				return true;
			Method overriden = method(superclass, method.getName(),
					method.getParameterTypes());
			if (overriden != null && isGUITest(overriden))
				return true;
			superclass = superclass.getSuperclass();
		}
		return false;
	}

	private static Method method(Class<?> superclass, String methodName,
			Class<?>[] parameterTypes) {
		try {
			return superclass.getDeclaredMethod(methodName, parameterTypes);
		} catch (Exception e) {
			return null;
		}
	}

	private static boolean isGUITest(AnnotatedElement annotatedElement) {
		GUITest annotation = annotatedElement.getAnnotation(GUITest.class);
		return annotation != null;
	}

	private GUITests() {
	}
}
