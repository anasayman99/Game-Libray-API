package com.anas.gameLibrary.player;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for accessing and managing {@link PlayerGame} documents in MongoDB.
 * Provides standard CRUD operations and custom queries for player-game relationships.
 */
public interface PlayerGameRepository extends MongoRepository<PlayerGame, String> {

    /**
     * Retrieves all PlayerGame records for a given player.
     *
     * @param playerId the ID of the player
     * @return a list of PlayerGame records associated with the player
     */
    List<PlayerGame> findByPlayerId(String playerId);

    /**
     * Retrieves all PlayerGame records for a given game.
     *
     * @param gameId the ID of the game
     * @return a list of PlayerGame records associated with the game
     */
    List<PlayerGame> findByGameId(String gameId);

    /**
     * Retrieves a specific PlayerGame record by player ID and game ID.
     *
     * @param playerId the ID of the player
     * @param gameId the ID of the game
     * @return an Optional containing the PlayerGame if found, or empty if not
     */
    Optional<PlayerGame> findByPlayerIdAndGameId(String playerId, String gameId);
}
