package com.ioet.bpm.people.boundaries;

import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.repositories.PersonRepository;
import com.ioet.bpm.people.services.PasswordManagementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@AllArgsConstructor
@RestController
@RequestMapping("/people")
@Api(value="/people", description="Manage People", produces ="application/json")
public class PersonController {

    private final PersonRepository personRepository;

    private PasswordManagementService passwordManagementService;

    @ApiOperation(value = "Return a list of all persons", response = Person.class, responseContainer = "List")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Persons successfully returned")
    })
    @GetMapping(produces = "application/json")
    public ResponseEntity<Iterable> getAllPersons() {
        Iterable<Person> persons = this.personRepository.findAll();
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    @ApiOperation(value = "Return one person", response = Person.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Person successfully returned")
    })
    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Person> getPerson(@PathVariable(value = "id") String personId) {
        Optional<Person> personOptional = personRepository.findById(personId);
        return personOptional.map(
                person -> new ResponseEntity<>(person, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @ApiOperation(value = "Create a new person", response = Person.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Person successfully created")
    })
    @PostMapping(produces = "application/json")
    public ResponseEntity<?> createPerson(@RequestBody Person person) {
        Optional<Person> personWithGivenAuthenticationIdentity
                = personRepository.findPersonByAuthenticationIdentity(person.getAuthenticationIdentity());
        if (personWithGivenAuthenticationIdentity.isPresent()) {
            return new ResponseEntity<>("This email is already in use.", HttpStatus.CONFLICT);
        }
        person.setPassword(passwordManagementService.generatePassword(person.getPassword()));
        Person personCreated = personRepository.save(person);
        return new ResponseEntity<>(personCreated, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Delete a person")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Person successfully deleted"),
            @ApiResponse(code = 404, message = "The person to delete was not found")
    })
    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Person> deletePerson(@PathVariable(value = "id") String personId) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isPresent()) {
            personRepository.delete(person.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Update a person", response = Person.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Person successfully updated"),
            @ApiResponse(code = 404, message = "The person to update was not found")
    })
    @PutMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Person> updatePerson(@PathVariable(value = "id") String personId,
                                               @Valid @RequestBody Person personToUpdate) {

        Optional<Person> personFound = personRepository.findById(personId);
        if (personFound.isPresent()) {
            personToUpdate.setId(personFound.get().getId());
            personToUpdate.setPassword(personFound.get().getPassword());
            Person updatedPerson = personRepository.save(personToUpdate);
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ApiOperation(value = "Change the password of one person", response = Person.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Persons password successfully updated"),
            @ApiResponse(code = 404, message = "The person to change the password was not found")
    })
    @PostMapping(path = "/change-password/{id}", produces = "application/json")
    public ResponseEntity<Person> changePassword(@PathVariable(value = "id") String personId,
                                                 @Valid @RequestBody Person personToUpdate) {

        Optional<Person> personFound = personRepository.findById(personId);
        if (personFound.isPresent()) {

            personToUpdate.setId(personFound.get().getId());
            personToUpdate.setPassword(passwordManagementService.generatePassword(personToUpdate.getPassword()));
            Person updatedPerson = personRepository.save(personToUpdate);

            passwordManagementService.recordPasswordHistory(personToUpdate);

            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
