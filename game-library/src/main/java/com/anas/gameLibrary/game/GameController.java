package com.anas.gameLibrary.game;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * REST controller for managing game entities.
 * Handles HTTP requests related to game operations, including
 * retrieving, creating, updating, and deleting games from the database.
 */
@RestController
@RequestMapping("/api/games")
public class GameController {
    private static final Logger log = LoggerFactory.getLogger(GameController.class);

    private final GameService gameService;


    /**
     * Constructs a new GameController with the specified GameService.
     *
     * @param gameService the service layer for game operations
     */
    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    /**
     * Retrieves all games.
     *
     * @return a list of all games, or 204 No Content if none exist
     */
    @GetMapping
    public ResponseEntity<List<Game>> getAllGames() {
        log.info("Received request to get all games");

        List<Game> games = gameService.getAllGames();
        return games.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(games);
    }


    /**
     * Retrieves a game by its ID.
     *
     * @param id the ID of the game
     * @return the game if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable String id) {
        log.info("Received request to get game with ID: {}", id);

        return gameService.getGameById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Creates a new game.
     *
     * @param game the game to create
     * @return the created game
     */
    @PostMapping
    public ResponseEntity<Game> createGame(@Valid @RequestBody Game game) {
        log.info("Received request to create game: {}", game.title());

        return ResponseEntity.ok(gameService.saveGame(game));
    }

    /**
     * Updates an existing game by its ID.
     *
     * @param id   the ID of the game to update
     * @param game the updated game data
     * @return the updated game if found, or 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Game> updateGame(@PathVariable String id, @Valid @RequestBody Game game) {
        log.info("Received request to update game with ID: {}", id);

        return gameService.updateGame(id, game)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Deletes a game by its ID.
     *
     * @param id the ID of the game to delete
     * @return 204 No Content if deleted, or 404 Not Found if the game does not exist
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable String id) {
        log.info("DELETE /api/games/{} - Received request to delete game", id);

        if (gameService.deleteGame(id)) {
            log.info("Game deleted with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Game not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }


}
