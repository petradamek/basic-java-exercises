package cz.bilysklep.trainings.basicjava.classesandinstances;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.*;

@Ignore
public class PersonBTest {

    // *************************************************************************
    // Don't consider this class as an example how to write unit tests!
    // *************************************************************************
    // The purpose of this test is to check if the assigment was correctly
    // solved and if all relevant rules has been fullfiled. We are not testing
    // such things in real unit tests and we are not using Java Reflection API
    // for calling tested methods or accessing private fields.
    // *************************************************************************
    private final ReflectionHelper<Person> personClass
            = new ReflectionHelper<>(Person.class);

    private static final Class<?>[] TWO_ARGS_CONSTRUCTOR = {
        String.class, LocalDate.class
    };

    private static final Class<?>[] THREE_ARGS_CONSTRUCTOR = {
        String.class, LocalDate.class, LocalDate.class
    };

    @Test
    public void b1dateOfDeathField() {
        personClass.assertHasPrivateField(LocalDate.class, "dateOfDeath");
    }

    @Test
    public void b2constructor() throws Throwable {
        Person person = personClass.newInstance("Charles IV.",
                LocalDate.of(1316, Month.MAY, 14),
                LocalDate.of(1378, Month.NOVEMBER, 29));
        assertEquals("name is not properly set: ", "Charles IV.",
                personClass.getFieldValue(person, String.class, "name"));
        assertEquals("dateOfBirth is not properly set: ",
                LocalDate.of(1316, Month.MAY, 14),
                personClass.getFieldValue(person, LocalDate.class, "dateOfBirth"));
        assertEquals("dateOfDeath is not properly set: ",
                LocalDate.of(1378, Month.NOVEMBER, 29),
                personClass.getFieldValue(person, LocalDate.class, "dateOfDeath"));

    }

    @Test
    public void b3getDateOfDeath() throws Throwable {
        Person person = personClass.newInstance("Charles IV.",
                LocalDate.of(1316, Month.MAY, 14),
                LocalDate.of(1378, Month.NOVEMBER, 29));
        assertEquals("getDateOfDeath() returns wrong value: ",
                LocalDate.of(1378, Month.NOVEMBER, 29),
                personClass.callPublicMethod(person, "getDateOfDeath", LocalDate.class));
    }

    @Test
    public void b4NullNameOldConstructor() throws Throwable {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> {
                    personClass.newInstance(TWO_ARGS_CONSTRUCTOR,
                            null,
                            LocalDate.of(1316, Month.MAY, 14));
                })
                .withMessage("name is null");
    }

    @Test
    public void b4NullNameNewConstructor() throws Throwable {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> {
                    personClass.newInstance(THREE_ARGS_CONSTRUCTOR,
                            null,
                            LocalDate.of(1316, Month.MAY, 14),
                            LocalDate.of(1378, Month.NOVEMBER, 29));
                })
                .withMessage("name is null");
    }

    @Test
    public void b4EmptyNameOldConstructor() throws Throwable {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    personClass.newInstance("",
                            LocalDate.of(1316, Month.MAY, 14));
                })
                .withMessage("name is empty");
    }

    @Test
    public void b4EmptyNameNewConstructor() throws Throwable {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    personClass.newInstance("",
                            LocalDate.of(1316, Month.MAY, 14),
                            LocalDate.of(1378, Month.NOVEMBER, 29));
                })
                .withMessage("name is empty");
    }

    @Test
    public void b4NullDateOfBirthOldConstructor() throws Throwable {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> {
                    personClass.newInstance(TWO_ARGS_CONSTRUCTOR,
                            "Charles IV.",
                            null);
                })
                .withMessage("dateOfBirth is null");
    }

    @Test
    public void b4NullDateOfBirthNewConstructor() throws Throwable {
        Assertions.assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> {
                    personClass.newInstance(THREE_ARGS_CONSTRUCTOR,
                            "Charles IV.",
                            null,
                            LocalDate.of(1378, Month.NOVEMBER, 29));
                })
                .withMessage("dateOfBirth is null");
    }

    @Test
    public void b4NullDateOfDeath() throws Throwable {
        // this should work
        Person person = personClass.newInstance(THREE_ARGS_CONSTRUCTOR,
                "Charles IV.",
                LocalDate.of(1316, Month.MAY, 14),
                null);
        assertEquals("getDateOfDeath() returns wrong value: ",
                null,
                personClass.callPublicMethod(person, "getDateOfDeath", LocalDate.class));
    }

    @Test
    public void b4DateOfDeathBeforeDateOfBirth() throws Throwable {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> {
                    personClass.newInstance("Charles IV.",
                            LocalDate.of(1316, Month.MAY, 14),
                            LocalDate.of(1316, Month.MAY, 13));
                })
                .withMessage("dateOfDeath (1316-05-13) is before dateOfBirth (1316-05-14)");
    }

    @Test
    public void b4DateOfDeathSameAsDateOfBirth() throws Throwable {
        // this should work
        Person person = personClass.newInstance("Charles IV.",
                LocalDate.of(1316, Month.MAY, 14),
                LocalDate.of(1316, Month.MAY, 14));
        assertEquals("getDateOfDeath() returns wrong value: ",
                LocalDate.of(1316, Month.MAY, 14),
                personClass.callPublicMethod(person, "getDateOfDeath", LocalDate.class));
    }

    @Test
    public void b5isDeadTrue() throws Throwable {
        Person person = personClass.newInstance("Charles IV.",
                LocalDate.of(1316, Month.MAY, 14),
                LocalDate.of(1378, Month.NOVEMBER, 29));
        assertEquals("isDead() returns wrong value: ",
                true,
                personClass.callPublicMethod(person, "isDead", boolean.class));
    }

    @Test
    public void b5isDeadFalse() throws Throwable {
        Person person = personClass.newInstance(THREE_ARGS_CONSTRUCTOR,
                "Charles IV.",
                LocalDate.of(1316, Month.MAY, 14),
                null);
        assertEquals("isDead() returns wrong value: ",
                false,
                personClass.callPublicMethod(person, "isDead", boolean.class));
    }

    @Test
    public void b6toStringDeadPerson() throws Throwable {
        Person person = personClass.newInstance("Charles IV.",
                LocalDate.of(1316, Month.MAY, 14),
                LocalDate.of(1378, Month.NOVEMBER, 29));
        assertEquals("Charles IV., born 1316-05-14, died 1378-11-29",
                personClass.callPublicMethod(person, "toString", String.class));
    }

    @Test
    public void b6toStringAlivePerson() throws Throwable {
        Person person = personClass.newInstance(THREE_ARGS_CONSTRUCTOR,
                "Charles IV.",
                LocalDate.of(1316, Month.MAY, 14),
                null);
        assertEquals("Charles IV., born 1316-05-14",
                personClass.callPublicMethod(person, "toString", String.class));
    }

    @Test
    public void b7nameIsFinal() throws Throwable {
        personClass.assertHasPrivateFinalField(String.class, "name");
    }

    @Test
    public void b7dateOfBirthIsFinal() throws Throwable {
        personClass.assertHasPrivateFinalField(LocalDate.class, "dateOfBirth");
    }

    @Test
    public void b7dateOfDeathIsFinal() throws Throwable {
        personClass.assertHasPrivateFinalField(LocalDate.class, "dateOfDeath");
    }

    @Test
    public void b7ClassIsFinal() throws Throwable {
        assertTrue("class " + Person.class.getName() + " is not final",
                Modifier.isFinal(Person.class.getModifiers()));

    }

    @Test
    public void b7noSetMethod() throws Throwable {
        List<String> setMethods = Stream.of(Person.class.getDeclaredMethods())
                .map(Method::getName)
                .filter(name -> name.startsWith("set"))
                .collect(Collectors.toList());
        assertTrue("Class " + Person.class.getName() + " has these set methods: " + setMethods,
                setMethods.isEmpty());

    }

    @Test
    //@Ignore
    public void a5setName() throws Throwable {
        Person john = personClass.newInstance(
                "John Wick", LocalDate.of(1964, Month.SEPTEMBER, 2));
        personClass.callPublicMethod(john, "setName", void.class, "Keanu Reeves");
        assertEquals("Keanu Reeves",
                personClass.callPublicMethod(john, "getName", String.class));
    }

    @Test
    public void a6getDateOfBirth() throws Throwable {
        Person john = personClass.newInstance(
                "John Wick", LocalDate.of(1964, Month.SEPTEMBER, 2));
        assertEquals(LocalDate.of(1964, Month.SEPTEMBER, 2),
                personClass.callPublicMethod(john, "getDateOfBirth", LocalDate.class));
    }

    @Test
    public void a7getAge() throws Throwable {
        Person john = personClass.newInstance(
                "John Wick", LocalDate.of(1964, Month.SEPTEMBER, 2));
        assertEquals((Integer) 53,
                personClass.callPublicMethod(john, "getAge", int.class, LocalDate.of(2018, Month.FEBRUARY, 11)));
    }

    @Test
    public void a8toString() throws Throwable {
        Person john = personClass.newInstance(
                "John Wick", LocalDate.of(1964, Month.SEPTEMBER, 2));
        assertEquals("John Wick, born 1964-09-02",
                personClass.callPublicMethod(john, "toString", String.class));
    }

}
