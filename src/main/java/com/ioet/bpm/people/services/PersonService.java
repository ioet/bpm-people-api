package com.ioet.bpm.people.services;

import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PersonService {

    private final PersonRepository personRepository;

    private DozerBeanMapper mapper;

    @PostConstruct
    public void init() {
        mapper = new DozerBeanMapper();
        mapper.addMapping(new BeanMappingBuilder() {

            @Override
            protected void configure() {
                mapping(Person.class, Person.class, TypeMappingOptions.mapNull(false));
            }
        });
    }

    public Person mergePersonToUpdateIntoExistingPerson(Person source, Person destination) {
        mapper.map(source, destination);
        return destination;
    }

    public boolean authenticationIdentityExists(String authenticationIdentity) {
        Optional<Person> personWithGivenAuthenticationIdentity
                = personRepository.findPersonByAuthenticationIdentity(authenticationIdentity);
        return personWithGivenAuthenticationIdentity.isPresent();
    }

    public boolean emailChanged(Person oldPerson, Person personToUpdate) {
        return !oldPerson.getAuthenticationIdentity().equals(personToUpdate.getAuthenticationIdentity());
    }

    public Iterable<Person> findPeopleByEmail(String email) {
        Iterable<Person> persons;
        if (email == null) {
            persons = this.personRepository.findAll();
        } else {
            Optional<Person> personOptional = personRepository.findPersonByAuthenticationIdentity(email);
            if (personOptional.isPresent()) {
                ArrayList<Person> personList = (new ArrayList<>(1));
                personList.add(personOptional.get());
                return personList;
            } else {
                return null;
            }
        }

        return persons;
    }
}
