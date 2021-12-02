package com.example.eventstore.storage.persitence;

import org.springframework.data.repository.CrudRepository;


public interface DomainEventsRepository extends CrudRepository<DomainEventsEntity, String> {

}
