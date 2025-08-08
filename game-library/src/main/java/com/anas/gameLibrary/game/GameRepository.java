package com.anas.gameLibrary.game;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Repository interface for accessing and managing {@link Game} entities in MongoDB.
 * Extends {@link MongoRepository} to provide basic CRUD operations.
 */
public interface GameRepository extends MongoRepository<Game, String> {

}
