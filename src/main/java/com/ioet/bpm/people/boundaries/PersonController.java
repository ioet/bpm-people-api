package com.ioet.bpm.people.boundaries;

import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.repositories.PersonRepository;
import com.ioet.bpm.people.services.PasswordManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;


@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonRepository personRepository;

    @Autowired
    private PasswordManagementService passwordManagementService;

    public PersonController(PersonRepository personRepository, PasswordManagementService passwordManagementService) {
        this.personRepository = personRepository;
        this.passwordManagementService = passwordManagementService;
    }

    @GetMapping
    public ResponseEntity<Iterable> getAllPersons() {
        Iterable<Person> persons = this.personRepository.findAll();
        return new ResponseEntity<>(persons, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable(value = "id") String personId) {
        Optional<Person> personOptional = personRepository.findById(personId);
        return personOptional.map(
                person -> new ResponseEntity<>(person, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {

        person.setPassword(passwordManagementService.generatePassword(person.getPassword()));
        Person personCreated = personRepository.save(person);
        return new ResponseEntity<>(personCreated, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Person> deletePerson(@PathVariable(value = "id") String personId) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isPresent()) {
            personRepository.delete(person.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
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

    @PostMapping("/change-password/{id}")
    public ResponseEntity<Person> changePassword(@PathVariable(value = "id") String personId,
                                                 @Valid @RequestBody Person personToUpdate) {

        Optional<Person> personFound = personRepository.findById(personId);
        if (personFound.isPresent()) {
            personToUpdate.setId(personFound.get().getId());
            personToUpdate.setPassword(passwordManagementService.generatePassword(personToUpdate.getPassword()));
            Person updatedPerson = personRepository.save(personToUpdate);
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
