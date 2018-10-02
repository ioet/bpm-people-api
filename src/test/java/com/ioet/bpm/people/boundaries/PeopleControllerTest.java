package com.ioet.bpm.people.boundaries;

import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.repositories.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PeopleControllerTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonController personController;

    @Test
    public void peopleAreListedUsingTheRepository() {

        ResponseEntity<Iterable> persons = personController.getAllPersons();

        assertEquals(HttpStatus.OK, persons.getStatusCode());
        verify(personRepository, times(1)).findAll();
    }

    @Test
    public void whenAPersonIsCreatedTheNewPersonIsReturned() {
        Person personToCreate = mock(Person.class);
        Person personCreated = mock(Person.class);
        when(personRepository.save(personToCreate)).thenReturn(personCreated);

        ResponseEntity<Person> personCreatedResponse = personController.createPerson(personToCreate);

        assertEquals(personCreated, personCreatedResponse.getBody());
        assertEquals(HttpStatus.CREATED, personCreatedResponse.getStatusCode());
        verify(personRepository, times(1)).save(personToCreate);
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
        assertEquals(HttpStatus.NO_CONTENT, deletedPersonResponse.getStatusCode());
        verify(personRepository, times(1)).delete(personToDelete);
    }

    @Test
    public void ifThePersonToUpdateDoesNotExistA404IsReturned() {
        String id = "id";
        when(personRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity response = personController.updatePerson(id, mock(Person.class));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void whenAPersonIsUpdatedTheUpdatedPersonIsReturned() {

        String idPersonToUpdate = "id";
        Person personToUpdate = mock(Person.class);
        Person updatedPerson = mock(Person.class);

        when(personRepository.findById(idPersonToUpdate)).thenReturn(Optional.of(mock(Person.class)));
        when(personRepository.save(personToUpdate)).thenReturn(updatedPerson);

        ResponseEntity<Person> updatedPersonResponse = personController.updatePerson(idPersonToUpdate, personToUpdate);

        assertEquals(updatedPerson, updatedPersonResponse.getBody());
        assertEquals(HttpStatus.OK, updatedPersonResponse.getStatusCode());
        verify(personRepository, times(1)).save(personToUpdate);
    }
}
