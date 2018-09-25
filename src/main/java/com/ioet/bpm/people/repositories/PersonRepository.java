package com.ioet.bpm.people.repositories;

import com.ioet.bpm.people.domain.Person;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface PersonRepository extends CrudRepository<Person, String> {

}
