package com.ioet.bpm.people.boundaries;

import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.repositories.PersonRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(MockitoJUnitRunner.class)
public class PeopleControllerTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonController personController;

    @Test
    public void peopleAreListedUsingTheRepository() throws Exception {

        ResponseEntity<Iterable> skills =  personController.getAllPersons();

        Assert.assertEquals(HttpStatus.OK, skills.getStatusCode());
        Mockito.verify(personRepository, Mockito.times(1)).findAll();
    }

    @Test
    public void whenAPersonIsCreatedTheNewPersonIsReturned() throws Exception {
        Person personToCreate= Mockito.mock(Person.class);
        Person personCreated= Mockito.mock(Person.class);
        Mockito.when(personRepository.save(personToCreate)).thenReturn(personCreated);

        ResponseEntity<Person> personCreatedResponse =  personController.createPerson(personToCreate);

        Assert.assertEquals(personCreated, personCreatedResponse.getBody());
        Assert.assertEquals(HttpStatus.CREATED, personCreatedResponse.getStatusCode());
        Mockito.verify(personRepository, Mockito.times(1)).save(personToCreate);
    }
}
