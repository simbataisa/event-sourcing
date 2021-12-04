package com.example.eventstore.storage.repository;

import com.example.eventstore.storage.model.DomainEventsEntity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Profile("h2")
@Repository
public interface DomainEventsRepository extends CrudRepository<DomainEventsEntity, String> {

}
