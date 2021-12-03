package com.example.eventstore.storage.persitence;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Profile("h2")
@Repository
public interface DomainEventsRepository extends CrudRepository<DomainEventsEntity, String> {

}
