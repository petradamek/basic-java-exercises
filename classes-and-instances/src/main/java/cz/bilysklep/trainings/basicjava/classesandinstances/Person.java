package cz.bilysklep.trainings.basicjava.classesandinstances;

import java.time.LocalDate;
import java.time.Period;

/**
 * This class represents a Person.
 *
 * @author petr.adamek@bilysklep.cz
 */
public class Person {

    // a1. add attribute "name" with type "String"

    // a2. add attribute "dateOfBirth" with type "LocalDate"

    // a3. implement constructor which will initialize both attributes

    // a4. implement method getName() which returns person name

    // a5. implement method setName(String name) which change person name to new value

    // a6. implement method getDateOfBirth() which returns dateOfBirth

    // a7. implement method getAge(LocalDate date) which returns age of person at given date.
    //     Difference between two dates in years can be calculated this way:
    //
    //     int age = Period.between(dateOfBirth, date).getYears();

    // a8. implement method toString() which returns string describing the
    //     person with this format:
    //
    //     <name>, born <dateOfBirth>

    // a9. implement setName(String name) method


    // b1. add attribute "dateOfDeath" with type "LocalDate"

    // b2. implement new constructor (keep the old one) which will initialize all three attributes

    // b3. implement method getDateOfDeath() which returns dateOfDeath

    // b4. modify constructors to check that these invariants are fullfiled
    //     A) name is not null (otherwise throw NullPointerException with message "name is null")
    //     B) name is not empty string (otherwise throw IllegalArgumentException with message "name is empty")
    //     C) dateOfBirth is not null (otherwise throw NullPointerException with message "dateOfBirth is null")
    //     D) if the person is dead (dateOfDeath is not null), dateOfDeath is not before dateOfBirth
    //        (otherwise throw IllegalArgumentException with message
    //        "dateOfDeath (<dateOfDeath>) is before dateOfBirth (<dateOfBirth>)")

    // b5. implement method isDead() which will return true if the person is dead
    //     (i.e. the dateOfDeath is not null)

    // b6. modify toString() method to include also date of death (if the person is dead)
    //
    //     <name>, born <dateOfBirth>, died <dateOfDeath>

    // b7. make the class Person immutable

}
