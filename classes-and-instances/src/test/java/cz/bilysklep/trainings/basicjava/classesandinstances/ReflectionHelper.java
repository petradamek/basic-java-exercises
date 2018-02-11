package cz.bilysklep.trainings.basicjava.classesandinstances;

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
import static org.junit.Assert.fail;

public class ReflectionHelper<T> {

    private final Class<T> clazz;

    public ReflectionHelper(Class<T> type) {
        this.clazz = type;
    }

    public <R> R callPublicMethod(T object, String methodName,
            Class<R> returnType, Object ... parameters) throws Throwable {

        Class<?>[] parametersType = getParametersType(parameters);

        try {
            Method method = clazz.getDeclaredMethod(methodName, parametersType);
            assertTrue("Method " + methodName + " is not public",
                    Modifier.isPublic(method.getModifiers()));
            assertEquals("Method " + methodName + " has wrong return type, ",
                    returnType, method.getReturnType());
            return cast(returnType, method.invoke(object, parameters));
        } catch (NoSuchMethodException ex) {
            throw new AssertionError("Class " + clazz.getSimpleName()
                    + " does not have a " + methodName + " with arguments"
                    + " of these types " + toString(parametersType), ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Error when trying to call method "
                    + methodName + ", it is probably not public", ex);
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        }

    }

    public T newInstance(Object ... parameters) throws Throwable {

        Class<?>[] parametersType = getParametersType(parameters);
        return newInstance(parametersType, parameters);
    }

    public T newInstance(Class<?>[] parametersType, Object ... parameters) throws Throwable {


        try {
            Constructor<T> constructor
                    = clazz.getDeclaredConstructor(parametersType);
            assertTrue("Constructor is not public",
                    Modifier.isPublic(constructor.getModifiers()));
            return constructor.newInstance(parameters);

        } catch (NoSuchMethodException ex) {
            throw new AssertionError("Class " + Person.class.getSimpleName()
                    + " does not have a constructor with arguments"
                    + " of these types " + toString(parametersType), ex);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("Error when trying to call constructor, "
                    + "it is probably not public", ex);
        } catch (InvocationTargetException ex) {
            throw ex.getCause();
        } catch (InstantiationException ex) {
            throw new AssertionError("Error when trying to call constructor, "
                    + "class is probably abstract", ex);
        }

    }

    private static Class<?>[] getParametersType(Object[] parameters) {
        List<Class<?>> parameterTypes = Stream.of(parameters)
                .map(Object::getClass)
                .collect(Collectors.toList());
        return parameterTypes.toArray(new Class[parameterTypes.size()]);
    }

    private static String toString(Class<?>[] parametersType) {
        return Stream.of(parametersType)
                .map(Class::getName)
                .collect(Collectors.joining(",", "[", "]"));
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


}
