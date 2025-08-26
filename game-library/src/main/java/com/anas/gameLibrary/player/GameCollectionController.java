package com.anas.gameLibrary.player;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link GameCollection} resources.
 * Provides endpoints to create, retrieve, update, and delete game collections
 * as well as add or remove games from a specific collection.
 */
@RestController
@RequestMapping("/api/collections")
@Tag (name = "Collections", description = "Endpoints for managing game collection entities")
public class GameCollectionController {

    private static final Logger log = LoggerFactory.getLogger(GameCollectionController.class);

    private final GameCollectionService gameCollectionService;

    /**
     * Constructs a new GameCollectionController with the given service.
     *
     * @param gameCollectionService the service handling game collection logic
     */
    public GameCollectionController(GameCollectionService gameCollectionService) {
        this.gameCollectionService = gameCollectionService;
    }

    /**
     * Handles HTTP GET requests to retrieve all game collections.
     *
     * @return ResponseEntity containing a list of all {@link GameCollection} entities,
     *         or 204 No Content if no collections exist.
     */
    @GetMapping
    @Operation(summary = "Get all collections", description = "Returns a list of all game collections")
    public ResponseEntity<List<GameCollection>> getAllCollections() {
        List<GameCollection> collections = gameCollectionService.getAllCollections();
        return collections.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(collections);
    }


    /**
     * Retrieves all collections belonging to a specific player.
     *
     * @param playerId the ID of the player
     * @return a list of collections owned by the player
     */
    @GetMapping("/player/{playerId}")
    @Operation(summary = "Get collections for player", description = "Returns a list of all collections for a player")
    public ResponseEntity<List<GameCollection>> getCollectionsByPlayer(@PathVariable String playerId) {
        log.info("Fetching collections for player {}", playerId);
        return ResponseEntity.ok(gameCollectionService.getCollectionsByPlayer(playerId));
    }

    /**
     * Retrieves a specific collection by its ID.
     *
     * @param id the ID of the collection
     * @return the collection if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get collection by ID", description = "Returns a single collection by its ID")
    public ResponseEntity<GameCollection> getCollectionById(@PathVariable String id) {
        log.info("Fetching collection by ID {}", id);
        Optional<GameCollection> collection = gameCollectionService.getById(id);

        return collection.map(gc -> {
            log.info("Collection found with ID: {}", id);
            return ResponseEntity.ok(gc);
        }).orElseGet(() -> {
            log.warn("Collection was not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        });
    }

    /**
     * Creates a new game collection.
     *
     * @param collection the collection to create
     * @return the created collection
     */
    @PostMapping
    @Operation(summary = "Create collection", description = "Creates a new game collection entity")
    public ResponseEntity<GameCollection> createCollection(@Valid @RequestBody GameCollection collection) {
        log.info("Creating new collection for player ID: {}", collection.playerId());
        GameCollection saved = gameCollectionService.saveCollection(collection);
        log.info("Collection created with ID: {}", saved.id());
        return ResponseEntity.ok(saved);
    }

    /**
     * Adds a game to a specific collection by ID.
     *
     * @param collectionId the ID of the collection
     * @param gameId the ID of the game to add
     * @return the updated collection or 404 Not Found
     */
    @PutMapping("/{collectionId}/add/{gameId}")
    @Operation(summary = "Add game to collection", description = "Adds a game to a specific game collection")
    public ResponseEntity<GameCollection> addGameToCollection(
            @PathVariable String collectionId,
            @PathVariable String gameId) {

        return gameCollectionService.addGameToCollection(collectionId, gameId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Removes a game from a specific collection by ID.
     *
     * @param collectionId the ID of the collection
     * @param gameId the ID of the game to remove
     * @return the updated collection or 404 Not Found
     */
    @PutMapping("/{collectionId}/remove/{gameId}")
    @Operation(summary = "Remove game from collection", description = "Removes a game from a specific game collection")
    public ResponseEntity<GameCollection> removeGameFromCollection(
            @PathVariable String collectionId,
            @PathVariable String gameId) {

        return gameCollectionService.removeGameFromCollection(collectionId, gameId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    /**
     * Deletes a game collection by ID.
     *
     * @param id the ID of the collection
     * @return 204 No Content if deleted, or 404 Not Found if not found
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete collection", description = "Deletes a game collection by its ID")
    public ResponseEntity<Void> deleteCollection(@PathVariable String id) {
        log.info("Deleting collection {}", id);
        if (gameCollectionService.deleteCollection(id)) {
            log.info("Collection deleted with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Collection not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
