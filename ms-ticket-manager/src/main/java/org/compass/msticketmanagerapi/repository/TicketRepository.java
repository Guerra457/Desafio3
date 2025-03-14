package org.compass.msticketmanagerapi.repository;

import org.compass.msticketmanagerapi.entity.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends MongoRepository<Ticket, String> {
    boolean existsByEventId(String eventId);
}