package org.jatakasource.common.utils;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionUtils {
	/**
	 * Return property vale using reflection.
	 * 
	 * @return Object value
	 */
	public static <T> Object getPropertyValue(Class<T> beanClass, T target, String property,
			Class<?>... parameterTypes) {
		try {
			Method getter = new PropertyDescriptor(property, beanClass).getReadMethod();
			
			return invokeMethod(getter, target, null);
		} catch (SecurityException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": "
					+ ex.getMessage());
		} catch (IntrospectionException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": "
					+ ex.getMessage());
		}
	}

	/**
	 * Invoke the specified {@link Method} against the supplied target object
	 * with the supplied arguments. The target object can be <code>null</code>
	 * when invoking a static {@link Method}.
	 * <p>
	 * Thrown exceptions are handled via a call to
	 * {@link #handleReflectionException}.
	 * 
	 * @see #invokeMethod(java.lang.reflect.Method, Object, Object[])
	 */
	public static Object invokeMethod(Method method, Object target, Object[] args) {
		try {
			return method.invoke(target, args);
		} catch (IllegalAccessException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": "
					+ ex.getMessage());
		} catch (InvocationTargetException ex) {
			handleReflectionException(ex);
			throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": "
					+ ex.getMessage());
		}
	}

	/**
	 * Handle the given reflection exception. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>
	 * Throws the underlying RuntimeException or Error in case of an
	 * InvocationTargetException with such a root cause. Throws an
	 * IllegalStateException with an appropriate message else.
	 * 
	 * @param ex
	 *            the reflection exception to handle
	 */
	public static void handleReflectionException(Exception ex) {
		if (ex instanceof NoSuchMethodException) {
			throw new IllegalStateException("Method not found: " + ex.getMessage());
		}
		if (ex instanceof IllegalAccessException) {
			throw new IllegalStateException("Could not access method: " + ex.getMessage());
		}
		if (ex instanceof InvocationTargetException) {
			handleInvocationTargetException((InvocationTargetException) ex);
		}
		throw new IllegalStateException("Unexpected reflection exception - " + ex.getClass().getName() + ": "
				+ ex.getMessage());
	}

	/**
	 * Handle the given invocation target exception. Should only be called if no
	 * checked exception is expected to be thrown by the target method.
	 * <p>
	 * Throws the underlying RuntimeException or Error in case of such a root
	 * cause. Throws an IllegalStateException else.
	 * 
	 * @param ex
	 *            the invocation target exception to handle
	 */
	public static void handleInvocationTargetException(InvocationTargetException ex) {
		if (ex.getTargetException() instanceof RuntimeException) {
			throw (RuntimeException) ex.getTargetException();
		}
		if (ex.getTargetException() instanceof Error) {
			throw (Error) ex.getTargetException();
		}
		throw new IllegalStateException("Unexpected exception thrown by method - "
				+ ex.getTargetException().getClass().getName() + ": " + ex.getTargetException().getMessage());
	}

}
