package com.anas.gameLibrary.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing {@link GameCollection} entities.
 * Handles business logic related to creating, retrieving, updating,
 * and deleting game collections, as well as modifying their contents.
 */
@Service
public class GameCollectionService {
    private static final Logger log = LoggerFactory.getLogger(GameCollectionService.class);

    private final GameCollectionRepository collectionRepository;
    private final PlayerRepository playerRepository;

    /**
     * Constructs a GameCollectionService with the required repositories.
     *
     * @param collectionRepository the repository used to manage collections
     * @param playerRepository the repository used to verify player existence
     */
    public GameCollectionService(GameCollectionRepository collectionRepository, PlayerRepository playerRepository) {
        this.collectionRepository = collectionRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * Retrieves all game collections.
     *
     * @return a list of all {@link GameCollection} entities
     */
    public List<GameCollection> getAllCollections() {
        log.info("Fetching all game collections");
        return collectionRepository.findAll();
    }

    /**
     * Retrieves all collections associated with a specific player.
     *
     * @param playerId the ID of the player
     * @return a list of the player's game collections
     */
    public List<GameCollection> getCollectionsByPlayer(String playerId) {
        log.info("Fetching collections for playerId={}", playerId);

        return collectionRepository.findByPlayerId(playerId);
    }

    /**
     * Retrieves a collection by its ID.
     *
     * @param id the collection ID
     * @return the matching collection, if found
     */
    public Optional<GameCollection> getById(String id) {
        log.info("Fetching collection with id={}", id);

        return collectionRepository.findById(id);
    }

    /**
     * Saves a new collection after verifying the player exists.
     *
     * @param collection the collection to save
     * @return the saved collection
     * @throws IllegalArgumentException if the player does not exist
     */
    public GameCollection saveCollection(GameCollection collection) {
        log.info("Attempting to save collection: {}", collection.name());

        if (!playerRepository.existsById(collection.playerId())) {
            log.warn("Cannot save collection — playerId {} does not exist", collection.playerId());
            throw new IllegalArgumentException("Player does not exist");
        }

        boolean nameExists = collectionRepository.findByPlayerId(collection.playerId()).stream()
                .anyMatch(c -> c.name().equalsIgnoreCase(collection.name()) && !c.id().equals(collection.id()));

        if (nameExists) {
            log.warn("Collection name '{}' already exists for playerId {}", collection.name(), collection.playerId());
            throw new IllegalArgumentException("Collection name already exists for this player");
        }

        return collectionRepository.save(collection);
    }


    /**
     * Adds a game to a specific collection if it's not already present.
     *
     * @param collectionId the ID of the collection
     * @param gameId the ID of the game to add
     * @return the updated collection, if found
     */
    public Optional<GameCollection> addGameToCollection(String collectionId, String gameId) {
        return collectionRepository.findById(collectionId).map(collection -> {
            List<String> updatedGames = new ArrayList<>(collection.gameIds());
            if (!updatedGames.contains(gameId)) {
                updatedGames.add(gameId);
            }
            GameCollection updated = new GameCollection(collection.id(), collection.playerId(), collection.name(), updatedGames);
            return collectionRepository.save(updated);
        });
    }

    /**
     * Removes a game from a specific collection if it exists in the list.
     *
     * @param collectionId the ID of the collection
     * @param gameId the ID of the game to remove
     * @return the updated collection, if found
     */
    public Optional<GameCollection> removeGameFromCollection(String collectionId, String gameId) {
        return collectionRepository.findById(collectionId).map(collection -> {
            List<String> updatedGames = new ArrayList<>(collection.gameIds());
            if (updatedGames.remove(gameId)) {
                GameCollection updated = new GameCollection(collection.id(), collection.playerId(), collection.name(), updatedGames);
                return collectionRepository.save(updated);
            }
            return collection; // unchanged
        });
    }

    /**
     * Deletes a collection by its ID.
     *
     * @param id the ID of the collection to delete
     * @return true if the collection was deleted, false if it didn't exist
     */
    public boolean deleteCollection(String id) {
        log.info("Attempting to delete collection with id={}", id);

        if (collectionRepository.existsById(id)) {
            collectionRepository.deleteById(id);
            log.info("Collection with id={} deleted", id);
            return true;
        }

        log.warn("Collection with id={} not found. Delete skipped.", id);
        return false;
    }
}
