package com.ioet.bpm.people.services;

import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.repositories.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PersonServiceTest {

    @InjectMocks
    private PersonService personService;

    @Mock
    private PersonRepository personRepository;

    @Test
    public void whenPersonToUpdateIsMergedIntoExistingPersonExistingPersonIsReturned() {
        personService.init();
        Person personToUpdate = mock(Person.class);
        Person existingPerson = mock(Person.class);
        Person returnedExistingPerson =
                personService.mergePersonToUpdateIntoExistingPerson(personToUpdate, existingPerson);
        assertEquals(existingPerson, returnedExistingPerson);
    }

    @Test
    public void whenAuthenticationIdentityExistsAlreadyReturnTrue() {
        String existingAuthenticationIdentity = "existing@ioet.com";

        when(personRepository.findPersonByAuthenticationIdentity(existingAuthenticationIdentity))
                .thenReturn(Optional.of(mock(Person.class)));

        assertTrue(personService.authenticationIdentityExists(existingAuthenticationIdentity));
        verify(personRepository).findPersonByAuthenticationIdentity(existingAuthenticationIdentity);
    }

    @Test
    public void whenEmailIsDifferentReturnTrue() {
        Person oldPerson = Person.builder().authenticationIdentity("old@ioet.com").build();
        Person personToUpdate = Person.builder().authenticationIdentity("new@ioet.com").build();
        assertTrue(personService.emailChanged(oldPerson, personToUpdate));
    }

    @Test
    public void findPeopleByEmailUsesPersonRepository() {
        Iterable<Person> personIterable = mock(Iterable.class);
        Optional<Person> personOptional = Optional.of(mock(Person.class));
        when(personRepository.findAll()).thenReturn(personIterable);
        when(personRepository.findPersonByAuthenticationIdentity("some_email")).thenReturn(personOptional);
        personService.findPeopleByEmail(null);
        personService.findPeopleByEmail("some_email");
        verify(personRepository, times(1)).findAll();
        verify(personRepository, times(1)).findPersonByAuthenticationIdentity("some_email");
    }

    @Test
    public void whenNullEmailIsPassedReturnIterable() {
        Iterable<Person> personExpected = mock(Iterable.class);
        when(personRepository.findAll()).thenReturn(personExpected);
        Iterable<Person> personFound = personService.findPeopleByEmail(null);
        assertEquals(personExpected, personFound);
    }

    @Test
    public void whenEmailIsFoundReturnIterable() {
        String email = "some_email";
        Optional<Person> personExpected = Optional.of(mock(Person.class));
        when(personRepository.findPersonByAuthenticationIdentity(email)).thenReturn(personExpected);
        ArrayList<Person> personFound = (ArrayList<Person>) personService.findPeopleByEmail(email);
        assertEquals(personExpected.get(), personFound.get(0));
    }

    @Test
    public void whenEmailIsNotFoundReturnNull() {
        String email = "some_email";
        Iterable<Person> personFound = personService.findPeopleByEmail(email);
        assertFalse(personFound.iterator().hasNext());
    }
}
