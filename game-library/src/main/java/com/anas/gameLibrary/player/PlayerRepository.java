package com.anas.gameLibrary.player;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;


/**
 * Repository interface for accessing and managing {@link Player} documents in MongoDB.
 * Extends {@link MongoRepository} to provide standard CRUD operations and custom queries.
 */
public interface PlayerRepository extends MongoRepository<Player, String> {

    /**
     * Finds a player by their username.
     *
     * @param username the username to search for
     * @return an Optional containing the matching Player, or empty if not found
     */
    Optional<Player> findByUsername(String username);
}
