package com.ioet.bpm.people.services;

import com.ioet.bpm.people.domain.Person;
import com.ioet.bpm.people.repositories.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.TypeMappingOptions;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
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
}
