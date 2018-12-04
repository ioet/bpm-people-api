package com.ioet.bpm.people.services;

import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.repositories.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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
}
