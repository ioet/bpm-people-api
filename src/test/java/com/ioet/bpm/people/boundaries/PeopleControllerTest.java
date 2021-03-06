package com.ioet.bpm.people.boundaries;

import com.ioet.bpm.people.boundaries.PersonController;
import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.domain.UpdatePassword;
import com.ioet.bpm.people.repositories.PersonRepository;
import com.ioet.bpm.people.services.PasswordManagementService;
import com.ioet.bpm.people.services.PersonService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Iterator;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PeopleControllerTest {

    @Mock
    private PersonRepository personRepository;
    @Mock
    private PersonService personService;
    @Mock
    private PasswordManagementService passwordManagementService;

    @InjectMocks
    private PersonController personController;

    private Iterable<Person> getIterable(boolean hasContent) {
        return () -> new Iterator<Person>() {
            @Override
            public boolean hasNext() {
                return hasContent;
            }

            @Override
            public Person next() {
                return null;
            }
        };
    }

    @Test
    public void peopleAreListedUsingTheRepository() {
        Iterable<Person> personFound = getIterable(true);
        when(personService.findPeopleByEmail(null)).thenReturn(personFound);

        ResponseEntity<Iterable> persons = (ResponseEntity<Iterable>) personController.findPeople(null);

        assertEquals(HttpStatus.OK, persons.getStatusCode());
        verify(personService, times(1)).findPeopleByEmail(null);
    }

    @Test
    public void aPersonIsFoundByEmail() {
        String email = "some_email";
        Iterable<Person> personFound = getIterable(true);
        when(personService.findPeopleByEmail(email)).thenReturn(personFound);

        personController.findPeople(email);

        verify(personService, times(1)).findPeopleByEmail(email);
    }

    @Test
    public void theBodyContainsThePersonFoundByEmail() {
        String email = "some_email";
        Iterable<Person> personFound = getIterable(true);
        when(personService.findPeopleByEmail(email)).thenReturn(personFound);

        ResponseEntity<?> existingPersonResponse = personController.findPeople(email);

        assertEquals(personFound, existingPersonResponse.getBody());
        assertEquals(HttpStatus.OK, existingPersonResponse.getStatusCode());
    }

    @Test
    public void thePersonWasNotFoundByEmail() {
        String email = "some_email";
        when(personService.findPeopleByEmail(email)).thenReturn(getIterable(false));

        ResponseEntity<Person> existingPersonResponse = (ResponseEntity<Person>) personController.findPeople(email);

        assertEquals(HttpStatus.NOT_FOUND, existingPersonResponse.getStatusCode());
    }

    @Test
    public void whenAPersonIsCreatedTheNewPersonIsReturned() {
        Person personCreated = mock(Person.class);
        Person personToCreate = new Person();
        personToCreate.setPassword("ioet");
        personToCreate.setName("Jorge Malla");
        personToCreate.setAuthenticationIdentity("jmalla@ioet.com");
        when(personRepository.save(personToCreate)).thenReturn(personCreated);

        ResponseEntity<Person> personCreatedResponse;
        personCreatedResponse = (ResponseEntity<Person>) personController.createPerson(personToCreate);

        assertEquals(personCreated, personCreatedResponse.getBody());
        assertEquals(HttpStatus.CREATED, personCreatedResponse.getStatusCode());
        verify(personRepository, times(1)).save(personToCreate);
        verify(passwordManagementService, times(1)).encryptPassword(anyString());
    }

    @Test
    public void whenAPersonIsCreatedWithoutPasswordTheNewPersonIsReturned() {
        Person personCreated = mock(Person.class);
        Person personToCreate = new Person();
        personToCreate.setName("Jorge Malla");
        personToCreate.setAuthenticationIdentity("jmalla@ioet.com");
        when(personRepository.save(personToCreate)).thenReturn(personCreated);

        ResponseEntity<Person> personCreatedResponse;
        personCreatedResponse = (ResponseEntity<Person>) personController.createPerson(personToCreate);

        assertEquals(personCreated, personCreatedResponse.getBody());
        assertEquals(HttpStatus.CREATED, personCreatedResponse.getStatusCode());
        verify(personRepository, times(1)).save(personToCreate);
        verify(passwordManagementService, never()).encryptPassword(anyString());
    }

    @Test
    public void whenAPersonIsCreatedWithAnExistingAuthenticationIdentityAnErrorMessageIsReturned() {
        Person personToCreate = new Person();
        personToCreate.setPassword("ioet");
        personToCreate.setName("Test Person Name");
        personToCreate.setAuthenticationIdentity("test@ioet.com");
        when(personService.authenticationIdentityExists(personToCreate.getAuthenticationIdentity()))
                .thenReturn(true);

        ResponseEntity<String> errorMessageResponse;
        errorMessageResponse = (ResponseEntity<String>) personController.createPerson(personToCreate);

        assertEquals(HttpStatus.CONFLICT, errorMessageResponse.getStatusCode());
        verify(personRepository, never()).save(any());
        verify(passwordManagementService, never()).encryptPassword(anyString());
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
        Person personUpdated = mock(Person.class);
        Optional<Person> personFound = Optional.of(mock(Person.class));
        String idPersonToUpdate = "id";
        Person personToUpdate = mock(Person.class);
        when(personRepository.findById(idPersonToUpdate)).thenReturn(personFound);
        when(personService.mergePersonToUpdateIntoExistingPerson(personToUpdate, personFound.get()))
                .thenReturn(personFound.get());
        when(personRepository.save(personFound.get())).thenReturn(personUpdated);

        ResponseEntity<Person> updatedPersonResponse =
                (ResponseEntity<Person>) personController.updatePerson(idPersonToUpdate, personToUpdate);

        assertEquals(personUpdated, updatedPersonResponse.getBody());
        assertEquals(HttpStatus.OK, updatedPersonResponse.getStatusCode());
        verify(personRepository, times(1)).save(personFound.get());
    }

    @Test
    public void whenAPersonIsUpdatedWithAnEmailThatAlreadyExistsAnErrorMessageIsReturned() {
        Optional<Person> personFound = Optional.of(mock(Person.class));
        String idPersonToUpdate = "id";
        Person personToUpdate = mock(Person.class);
        when(personRepository.findById(idPersonToUpdate)).thenReturn(personFound);
        when(personService.emailChanged(any(), any())).thenReturn(true);
        when(personService.authenticationIdentityExists(personToUpdate.getAuthenticationIdentity()))
                .thenReturn(true);

        ResponseEntity<Person> updatedPersonResponse =
                (ResponseEntity<Person>) personController.updatePerson(idPersonToUpdate, personToUpdate);

        assertEquals(HttpStatus.CONFLICT, updatedPersonResponse.getStatusCode());
        verify(personRepository, never()).save(any());
    }


    @Test
    public void whenThePasswordIsChangeThePersonWithTheEncryptedPasswordIsSaved() {
        String idPersonToUpdate = "id";
        Person personToUpdate = new Person();
        UpdatePassword updatePass = new UpdatePassword();
        personToUpdate.setPassword("ioet");
        when(personRepository.findById(idPersonToUpdate)).thenReturn(Optional.of(personToUpdate));
        when(passwordManagementService.isProvidedPasswordCorrect(personToUpdate, updatePass)).thenReturn(true);
        String encryptedPassword = "acds";
        when(passwordManagementService.encryptPassword(updatePass.getNewPassword())).thenReturn(encryptedPassword);

        personController.changePassword(idPersonToUpdate, updatePass);
        ArgumentCaptor<Person> personCaptor = ArgumentCaptor.forClass(Person.class);

        verify(personRepository).save(personCaptor.capture());
        assertEquals(encryptedPassword, personCaptor.getValue().getPassword());
    }

    @Test
    public void whenThePasswordIsChangedThenTheRecordPasswordHistoryIsCalled(){
        String idPersonToUpdate = "id";
        Person personToUpdate = new Person();
        UpdatePassword updatePass = new UpdatePassword();
        personToUpdate.setPassword("ioet");
        when(personRepository.findById(idPersonToUpdate)).thenReturn(Optional.of(personToUpdate));
        when(passwordManagementService.isProvidedPasswordCorrect(personToUpdate, updatePass)).thenReturn(true);

        personController.changePassword(idPersonToUpdate, updatePass);

        verify(passwordManagementService).recordPasswordHistory(personToUpdate);
    }

    @Test
    public void whenAPasswordIsChangedThenA204IsReturned() {
        String idPersonToUpdate = "id";
        Person personToUpdate = new Person();
        UpdatePassword updatePass = new UpdatePassword();
        personToUpdate.setPassword("ioet");
        when(personRepository.findById(idPersonToUpdate)).thenReturn(Optional.of(personToUpdate));
        when(passwordManagementService.isProvidedPasswordCorrect(personToUpdate, updatePass)).thenReturn(true);

        ResponseEntity response = personController.changePassword(idPersonToUpdate, updatePass);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void whenThePasswordIsChangedThenTheServiceEncryptsTheProvidedPassword() {
        String idPersonToUpdate = "id";
        Person personToUpdate = new Person();
        UpdatePassword updatePass = new UpdatePassword();
        personToUpdate.setPassword("ioet");
        when(personRepository.findById(idPersonToUpdate)).thenReturn(Optional.of(personToUpdate));
        when(passwordManagementService.isProvidedPasswordCorrect(personToUpdate, updatePass)).thenReturn(true);

        personController.changePassword(idPersonToUpdate, updatePass);

        verify(passwordManagementService).encryptPassword(updatePass.getNewPassword());

    }

    @Test
    public void whenNewPasswordAndNewPasswordConfirmationNotTheSame() {
        UpdatePassword updatePassword = mock(UpdatePassword.class);
        String idPersonToUpdate = "id";
        Person personToUpdate = mock(Person.class);
        when(personRepository.findById(idPersonToUpdate)).thenReturn(Optional.of(personToUpdate));
        when(passwordManagementService.isProvidedPasswordCorrect(personToUpdate, updatePassword)).thenReturn(false);

        ResponseEntity<Person> updatedPersonResponse = personController.changePassword(idPersonToUpdate, updatePassword);

        assertEquals(HttpStatus.BAD_REQUEST, updatedPersonResponse.getStatusCode());
    }

    @Test
    public void ifThePersonDoesNotFoundReturn404() {
        UpdatePassword updatePassword = mock(UpdatePassword.class);
        String idPersonToUpdate = "id";
        when(personRepository.findById(idPersonToUpdate)).thenReturn(Optional.empty());

        ResponseEntity<Person> updatedPersonResponse = personController.changePassword(idPersonToUpdate, updatePassword);

        assertEquals(HttpStatus.NOT_FOUND, updatedPersonResponse.getStatusCode());
    }
}
