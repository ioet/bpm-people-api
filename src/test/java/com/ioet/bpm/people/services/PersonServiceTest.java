package com.ioet.bpm.people.services;

import com.ioet.bpm.people.domain.Person;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Test
    public void whenPersonToUpdateIsMergedIntoExistingPersonExistingPersonIsReturned() {
        personService.init();
        Person personToUpdate = mock(Person.class);
        Person existingPerson = mock(Person.class);
        Person returnedExistingPerson =
                personService.mergePersonToUpdateIntoExistingPerson(personToUpdate, existingPerson);
        assertEquals(existingPerson, returnedExistingPerson);
    }
}
