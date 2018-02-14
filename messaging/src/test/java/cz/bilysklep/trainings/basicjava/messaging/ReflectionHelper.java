package cz.bilysklep.trainings.basicjava.messaging;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

public class ReflectionHelper<T> {

    private final Class<T> clazz;

    public ReflectionHelper(Class<T> type) {
        this.clazz = type;
    }

    public <R> R callPublicMethod(T object, String methodName,
            Class<R> returnType, Object... parameters) throws Throwable {

        Class<?>[] parameterTypes = getParameterTypes(parameters);

        Method method = getMethod(methodName, parameterTypes);
        assertTrue("Method " + toString(method) + " is not public",
                Modifier.isPublic(method.getModifiers()));
        assertEquals("Method " + toString(method) + " has wrong return type, ",
                returnType, method.getReturnType());
        try {
            return cast(returnType, method.invoke(object, parameters));
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Error when trying to call method "
                    + toString(method)
                    + ", it is probably not public", ex);
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }

    }

    public T newInstance(Object... parameters) throws Throwable {

        Class<?>[] parameterTypes = getParameterTypes(parameters);
        return newInstance(parameterTypes, parameters);
    }

    public T newInstance(Class<?>[] parameterTypes, Object... parameters) throws Throwable {

        Constructor<T> constructor = getConstructor(parameterTypes);
        assertTrue("Constructor " + toString(constructor) + " is not public",
                Modifier.isPublic(constructor.getModifiers()));
        try {
            return constructor.newInstance(parameters);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Error when trying to call constructor "
                    + toString(constructor) + ", it is probably not public", ex);
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        } catch (InstantiationException ex) {
            throw new AssertionError("Error when trying to call constructor"
                    + toString(constructor) + ", class is probably abstract", ex);
        }

    }

    public Method getMethod(String methodName, Class<?>... parameterTypes) {

        try {
            return clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException ex) {
            throw new AssertionError("Class " + clazz.getSimpleName()
                    + " does not have a method " + methodName
                    + toString(parameterTypes), ex);

        }
    }

    public Constructor<T> getConstructor(Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredConstructor(parameterTypes);
        } catch (NoSuchMethodException ex) {
            throw new AssertionError("Class " + clazz.getSimpleName()
                    + " does not have a constructor "
                    + clazz.getSimpleName() + toString(parameterTypes), ex);
        }
    }

    private static Class<?>[] getParameterTypes(Object[] parameters) {
        List<Class<?>> parameterTypes = Stream.of(parameters)
                .map(Object::getClass)
                .collect(Collectors.toList());
        return parameterTypes.toArray(new Class[parameterTypes.size()]);
    }

    private static String toString(Class<?>[] parameterTypes) {
        return Stream.of(parameterTypes)
                .map(Class::getName)
                .collect(Collectors.joining(",", "(", ")"));
    }

    private static String toString(Method method) {
        return method.getName() + toString(method.getParameterTypes());
    }

    private static String toString(Constructor constructor) {
        return constructor.getDeclaringClass().getSimpleName()
                + toString(constructor.getParameterTypes());
    }

    public <R> void assertHasPrivateField(Class<R> fieldType, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            assertEquals("Field " + fieldName + " has wrong type, ", fieldType, field.getType());
            assertTrue("Field " + fieldName + " is not private",
                    Modifier.isPrivate(field.getModifiers()));
        } catch (NoSuchFieldException ex) {
            fail("Class " + clazz.getSimpleName()
                    + " does not have a field with name " + fieldName);
        }
    }

    public <R> void assertHasPrivateFinalField(Class<R> fieldType, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            assertEquals("Field " + fieldName + " has wrong type, ", fieldType, field.getType());
            assertTrue("Field " + fieldName + " is not private",
                    Modifier.isPrivate(field.getModifiers()));
            assertTrue("Field " + fieldName + " is not final",
                    Modifier.isFinal(field.getModifiers()));
        } catch (NoSuchFieldException ex) {
            fail("Class " + clazz.getSimpleName()
                    + " does not have a field with name " + fieldName);
        }
    }

    public void assertMethodDeclaredInAncestor(Class ancestorClass,
            String methodName) {

        try {
            Method method = clazz.getMethod(methodName);
            assertTrue("Class " + clazz.getName() + " should not override or "
                    + "implement method " + methodName + "(), because this "
                    + "method should be inherited from class " + ancestorClass.getName(),
                    ancestorClass.equals(method.getDeclaringClass()));
        } catch (NoSuchMethodException ex) {
            fail("Class " + clazz.getName() + " does not have method " + methodName + "()");
        }
    }

    public void assertFieldsHaveCorrectIdentifiersAndAccessModifiers() {

        for (Field field : clazz.getDeclaredFields()) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
                // constant
                assertValidConstantIdentifier(field.getName());
            } else {
                // regular attribute
                assertTrue("Field " + field.getName() + " in class "
                        + clazz.getName() + " is not private", Modifier.isPrivate(mod));
                assertValidMethodOrVariableIdentifier(field.getName());
            }
        }
    }

    public void assertMethodsHaveCorrectIdentifier() {

        for (Method method : clazz.getDeclaredMethods()) {
            assertValidMethodOrVariableIdentifier(method.getName());
        }
    }

    public <R> R getFieldValue(T object, Class<R> fieldType, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            assertEquals("Field " + fieldName + " has wrong type, ", fieldType, field.getType());
            field.setAccessible(true);
            return cast(fieldType, field.get(object));
        } catch (NoSuchFieldException ex) {
            throw new AssertionError("Class " + clazz.getSimpleName()
                    + " does not have a field with name " + fieldName);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Error when trying to access field, "
                    + "accessible flag is probably not set", ex);
        }
    }

    private <T> T cast(Class<T> targetType, Object value) {

        if (targetType.isPrimitive()) {
            return getObjectWrapperClass(targetType).cast(value);
        } else {
            return targetType.cast(value);
        }

    }

    private <T> Class<T> getObjectWrapperClass(Class<T> primitiveClass) {
        if (primitiveClass.equals(byte.class)) {
            return (Class<T>) Byte.class;
        }
        if (primitiveClass.equals(short.class)) {
            return (Class<T>) Short.class;
        }
        if (primitiveClass.equals(int.class)) {
            return (Class<T>) Integer.class;
        }
        if (primitiveClass.equals(long.class)) {
            return (Class<T>) Long.class;
        }
        if (primitiveClass.equals(double.class)) {
            return (Class<T>) Double.class;
        }
        if (primitiveClass.equals(float.class)) {
            return (Class<T>) Float.class;
        }
        if (primitiveClass.equals(char.class)) {
            return (Class<T>) Character.class;
        }
        if (primitiveClass.equals(boolean.class)) {
            return (Class<T>) Boolean.class;
        }
        if (primitiveClass.equals(void.class)) {
            return (Class<T>) Void.class;
        }
        throw new IllegalArgumentException("Not primitive type : " + primitiveClass);
    }

    private static void assertValidMethodOrVariableIdentifier(String identifier) {
        assertTrue("Identifier " + identifier + " starts with capital letter",
                identifier.charAt(0) == identifier.toLowerCase().charAt(0));
        assertFalse("Identifier " + identifier + " contains character '_' (underscore)",
                identifier.contains("_"));
    }

    private static void assertValidConstantIdentifier(String identifier) {
        assertTrue("Constant identifier " + identifier + " contains small letters",
                identifier.equals(identifier.toUpperCase()));
    }

}
