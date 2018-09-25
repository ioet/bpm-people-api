package com.ioet.bpm.people.boundaries;

import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.repositories.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/people")
public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping
    public ResponseEntity<Iterable> listAll() {
        Iterable<Person> skills = this.personRepository.findAll();
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Person> create(@RequestBody Person person) {
        Person skillCreated = personRepository.save(person);
        return new ResponseEntity<>(skillCreated, HttpStatus.CREATED);
    }
}
