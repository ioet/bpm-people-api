package com.ioet.bpm.people.repositories;

import com.ioet.bpm.people.domain.PasswordHistory;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

@EnableScan
public interface PasswordHistoryRepository extends CrudRepository<PasswordHistory, String> {
}
