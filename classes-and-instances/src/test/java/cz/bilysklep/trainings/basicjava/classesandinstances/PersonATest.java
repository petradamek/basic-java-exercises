package cz.bilysklep.trainings.basicjava.classesandinstances;

import java.time.LocalDate;
import java.time.Month;
import org.junit.Test;
import org.junit.Ignore;

import static org.junit.Assert.*;


public class PersonATest {

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

    @Test
    public void a1nameField() {
        personClass.assertHasPrivateField(String.class, "name");
    }

    @Test
    public void a2dateOfBirthField() {
        personClass.assertHasPrivateField(LocalDate.class, "dateOfBirth");
    }

    @Test
    public void a3constructor() throws Throwable {
        Person person = personClass.newInstance(
                "John Wick", LocalDate.of(1964, Month.SEPTEMBER, 2));
        assertEquals("name is not properly set: ", "John Wick",
                personClass.getFieldValue(person, String.class, "name"));
        assertEquals("dateOfBirth is not properly set: ",
                LocalDate.of(1964, Month.SEPTEMBER, 2),
                personClass.getFieldValue(person, LocalDate.class, "dateOfBirth"));
    }

    @Test
    public void a4getName() throws Throwable {
        Person john = personClass.newInstance(
                "John Wick", LocalDate.of(1964, Month.SEPTEMBER, 2));
        assertEquals("getName() returns wrong value: ",
                "John Wick",
                personClass.callPublicMethod(john, "getName", String.class));
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
        assertEquals("getDateOfBirth() returns wrong value: ",
                LocalDate.of(1964, Month.SEPTEMBER, 2),
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
