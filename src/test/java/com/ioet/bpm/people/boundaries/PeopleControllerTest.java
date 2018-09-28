package com.ioet.bpm.people.boundaries;

import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.repositories.PersonRepository;
import com.ioet.bpm.people.services.PasswordManagementService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PeopleControllerTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private PasswordManagementService passwordManagementService;

    @InjectMocks
    private PersonController personController;

    @Test
    public void peopleAreListedUsingTheRepository() {

        ResponseEntity<Iterable> persons = personController.getAllPersons();

        assertEquals(HttpStatus.OK, persons.getStatusCode());
        verify(personRepository, times(1)).findAll();
    }

    @Test
    public void whenAPersonIsCreatedTheNewPersonIsReturned() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Person personCreated = mock(Person.class);

        Person personToCreate = new Person();
        personToCreate.setPassword("ioet");
        personToCreate.setName("Jorge Malla");
        personToCreate.setAuthenticationIdentity("jmalla@ioet.com");

        when(personRepository.save(personToCreate)).thenReturn(personCreated);

        ResponseEntity<Person> personCreatedResponse = personController.createPerson(personToCreate);

        assertEquals(personCreated, personCreatedResponse.getBody());
        assertEquals(HttpStatus.CREATED, personCreatedResponse.getStatusCode());
        verify(personRepository, times(1)).save(personToCreate);
        verify(passwordManagementService, times(1)).generatePassword(anyString());

    }

    @Test
    public void whenAPersonDoesNotExistA404IsExpected() {
        ResponseEntity<Person> notExistingPersonResponse = personController.getPerson("not_existing_id");

        assertNull(notExistingPersonResponse.getBody());
        assertEquals(HttpStatus.NOT_FOUND, notExistingPersonResponse.getStatusCode());
    }

    @Test
    public void aPersonIsFoundUsingTheRepository() {
        String personId = "personId";

        personController.getPerson(personId);

        verify(personRepository, times(1)).findById(personId);
    }

    @Test
    public void theBodyContainsThePersonFromTheRepository() {
        String personIdToFind = "id";
        Person personFound = mock(Person.class);
        when(personRepository.findById(personIdToFind)).thenReturn(Optional.of(personFound));

        ResponseEntity<Person> existingPersonResponse = personController.getPerson(personIdToFind);

        assertEquals(personFound, existingPersonResponse.getBody());
        assertEquals(HttpStatus.OK, existingPersonResponse.getStatusCode());
    }

    @Test
    public void ifThePersonToDeleteDoesNotExistA404IsReturned() {
        String id = "id";
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity response = personController.deletePerson(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenDeletingAPersonTheResponseIsEmpty() {
        String personIdToDelete = "id";
        Person personToDelete = mock(Person.class);
        when(personRepository.findById(personIdToDelete)).thenReturn(Optional.of(personToDelete));

        ResponseEntity<?> deletedPersonResponse = personController.deletePerson(personIdToDelete);

        assertNull(deletedPersonResponse.getBody());
        assertEquals(HttpStatus.OK, deletedPersonResponse.getStatusCode());
        verify(personRepository, times(1)).delete(personToDelete);
    }

    @Test
    public void ifThePersonToUpdateDoesNotExistA404IsReturned() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        String id = "id";
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity response = personController.updatePerson(id, mock(Person.class));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenAPersonIsUpdatedTheUpdatedPersonIsReturned() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Person personUpdated = mock(Person.class);
        Optional<Person> personFound = Optional.of(mock(Person.class));

        String idPersonToUpdate = "id";
        Person personToUpdate = mock(Person.class);

        when(personRepository.findById(idPersonToUpdate)).thenReturn(personFound);
        when(personRepository.save(personToUpdate)).thenReturn(personUpdated);

        ResponseEntity<Person> updatedPersonResponse = personController.updatePerson(idPersonToUpdate, personToUpdate);

        assertEquals(personUpdated, updatedPersonResponse.getBody());
        assertEquals(HttpStatus.OK, updatedPersonResponse.getStatusCode());
        verify(personRepository, times(1)).save(personToUpdate);
    }

    @Test
    public void whenAPersonIsUpdatedTheChangePasswordIsReturned() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        Person personUpdated = mock(Person.class);
        Optional<Person> personFound = Optional.of(mock(Person.class));

        String idPersonToUpdate = "id";
        Person personToUpdate = new Person();
        personToUpdate.setPassword("ioet");

        when(personRepository.findById(idPersonToUpdate)).thenReturn(personFound);
        when(passwordManagementService.generatePassword(personToUpdate.getPassword())).thenReturn(personToUpdate.getPassword());
        when(personRepository.save(personToUpdate)).thenReturn(personUpdated);

        ResponseEntity<Person> updatedPersonResponse = personController.changePassword(idPersonToUpdate, personToUpdate);

        assertEquals(personUpdated, updatedPersonResponse.getBody());
        assertEquals(HttpStatus.OK, updatedPersonResponse.getStatusCode());
        verify(personRepository, times(1)).save(personToUpdate);
        verify(passwordManagementService, times(1)).generatePassword(personToUpdate.getPassword());

    }

}
