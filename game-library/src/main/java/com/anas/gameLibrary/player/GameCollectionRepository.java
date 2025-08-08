package com.anas.gameLibrary.player;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repository interface for accessing and managing {@link GameCollection} documents in MongoDB.
 * Provides methods to perform standard CRUD operations and custom queries.
 */
public interface GameCollectionRepository extends MongoRepository<GameCollection, String> {

    /**
     * Finds all game collections associated with a specific player.
     *
     * @param playerId the ID of the player
     * @return a list of collections owned by the player
     */
    List<GameCollection> findByPlayerId(String playerId);

}