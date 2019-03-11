package com.ioet.bpm.people.boundaries;

import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.domain.UpdatePassword;
import com.ioet.bpm.people.repositories.PersonRepository;
import com.ioet.bpm.people.services.PasswordManagementService;
import com.ioet.bpm.people.services.PersonService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@AllArgsConstructor
@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonRepository personRepository;

    private PersonService personService;

    private PasswordManagementService passwordManagementService;

    @GetMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Person> getPerson(@PathVariable(value = "id") String personId) {
        Optional<Person> personOptional = personRepository.findById(personId);
        return personOptional.map(
                person -> new ResponseEntity<>(person, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<?> findPeople(@RequestParam(value = "email", required = false) String email) {
        Iterable<Person> foundPeople = personService.findPeopleByEmail(email);
        if (foundPeople.iterator().hasNext()) {
            return new ResponseEntity<>(foundPeople, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<?> createPerson(@Valid @RequestBody Person person) {
        if (personService.authenticationIdentityExists(person.getAuthenticationIdentity())) {
            return new ResponseEntity<>("This email is already in use.", HttpStatus.CONFLICT);
        }

        person.setAuthenticationProvider("ioet.com");
        // at the moment the password functionality is not needed
        // it should be possible to create a person without a password
        if (person.getPassword() != null) {
            person.setPassword(passwordManagementService.encryptPassword(person.getPassword()));
        }
        Person personCreated = personRepository.save(person);
        return new ResponseEntity<>(personCreated, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<Person> deletePerson(@PathVariable(value = "id") String personId) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isPresent()) {
            personRepository.delete(person.get());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(path = "/{id}", produces = "application/json")
    public ResponseEntity<?> updatePerson(@PathVariable(value = "id") String personId,
                                          @Valid @RequestBody Person personToUpdate) {

        Optional<Person> personFoundOptional = personRepository.findById(personId);
        if (personFoundOptional.isPresent()) {
            if (personService.emailChanged(personFoundOptional.get(), personToUpdate)
                    && personService.authenticationIdentityExists(personToUpdate.getAuthenticationIdentity())) {
                return new ResponseEntity<>("This email is already in use.", HttpStatus.CONFLICT);
            }

            Person personToSave =
                    personService.mergePersonToUpdateIntoExistingPerson(personToUpdate, personFoundOptional.get());

            Person updatedPerson = personRepository.save(personToSave);
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(path = "/{id}/change-password", produces = "application/json")
    public ResponseEntity<Person> changePassword(@PathVariable(value = "id") String personId,
                                                 @Valid @RequestBody UpdatePassword updatePassword) {
        Optional<Person> personFound = personRepository.findById(personId);

        if (personFound.isPresent()) {
            Person personToUpdate = personFound.get();

            if (passwordManagementService.isProvidedPasswordCorrect(personToUpdate, updatePassword)) {
                personToUpdate.setPassword(passwordManagementService.encryptPassword(updatePassword.getNewPassword()));
                personRepository.save(personToUpdate);
                passwordManagementService.recordPasswordHistory(personToUpdate);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
