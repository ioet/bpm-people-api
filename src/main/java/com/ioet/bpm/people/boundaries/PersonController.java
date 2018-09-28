package com.ioet.bpm.people.boundaries;

import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.repositories.PersonRepository;
import com.ioet.bpm.people.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonRepository personRepository;

    @Autowired
    private PersonService personService;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
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
    public ResponseEntity<Person> createPerson(@RequestBody Person person) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        person.setPassword(personService.generatePassword(person.getPassword()));
        Person personCreated = personRepository.save(person);
        return new ResponseEntity<>(personCreated, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable(value = "id") String personId) {
        Optional<Person> person = personRepository.findById(personId);
        if (person.isPresent()) {
            personRepository.delete(person.get());
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Person> updatePerson(@PathVariable(value = "id") String personId,
                                               @Valid @RequestBody Person personToUpdate) throws UnsupportedEncodingException,
            NoSuchAlgorithmException {

        Optional<Person> personFound = personRepository.findById(personId);
        if (personFound.isPresent()) {
            personFound.get().setPassword(personService.generatePassword(personToUpdate.getPassword()));
            personFound.get().setName(personToUpdate.getName());
            personFound.get().setAuthenticationIdentity(personToUpdate.getAuthenticationIdentity());
            Person updatedPerson = personRepository.save(personFound.get());
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }
}
