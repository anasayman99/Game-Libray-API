package com.anas.gameLibrary.playerGame;

import com.anas.gameLibrary.player.GameStatus;
import com.anas.gameLibrary.player.Player;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing player-game relationships.
 * Provides endpoints to create, retrieve, update, and delete {@link PlayerGame} records,
 * track game status for players, and query games or players based on their relationships.
 */
@RestController
@RequestMapping("/api/player-games")
@Tag(name = "PlayerGames", description = "Endpoints for managing player-game relationships")
public class PlayerGameController {

    private static final Logger log = LoggerFactory.getLogger(PlayerGameController.class);

    private final PlayerGameService playerGameService;

    /**
     * Constructs a PlayerGameController with the provided service.
     *
     * @param playerGameService the service layer handling player-game logic
     */
    public PlayerGameController(PlayerGameService playerGameService) {
        this.playerGameService = playerGameService;
    }

    /**
     * Retrieves all games associated with a specific player.
     *
     * @param playerId the ID of the player
     * @return a list of {@link PlayerGame} entries for the player
     */
    @GetMapping("/player/{playerId}")
    @Operation (summary = "Get games for player", description = "Returns a list of all games for a player")
    public ResponseEntity<List<PlayerGame>> getGamesByPlayer(@PathVariable String playerId) {
        log.info("Fetching games for player {}", playerId);
        return ResponseEntity.ok(playerGameService.getGamesByPlayer(playerId));
    }

    /**
     * Retrieves all player-game entries associated with a specific game.
     *
     * @param gameId the ID of the game
     * @return a list of {@link PlayerGame} entries for the game
     */
    @GetMapping("/game/{gameId}")
    @Operation (summary = "Get entries for game", description = "Returns a list of all entries for a game")
    public ResponseEntity<List<PlayerGame>> getPlayerGameEntriesByGame(@PathVariable String gameId) {
        log.info("Fetching entries for game {}", gameId);
        return ResponseEntity.ok(playerGameService.getPlayerGameEntriesByGame(gameId));
    }

    /**
     * Saves a new player-game entry.
     *
     * @param playerGame the player-game data to save
     * @return the saved entry, or 400 Bad Request if the player or game does not exist
     */
    @PostMapping
    @Operation (summary = "Save player-game entry", description = "Creates a new player-game entry")
    public ResponseEntity<PlayerGame> savePlayerGame(@Valid @RequestBody PlayerGame playerGame) {
        log.info("Saving game '{}' for player '{}'", playerGame.gameId(), playerGame.playerId());

        return playerGameService.savePlayerGame(playerGame)
                .map(saved -> {
                    log.info("PlayerGame saved with ID: {}", saved.id());
                    return ResponseEntity.ok(saved);
                })
                .orElseGet(() -> {
                    log.warn("Failed to save PlayerGame for player '{}'", playerGame.playerId());
                    return ResponseEntity.badRequest().build();
                });
    }

    /**
     * Retrieves a list of players who have played a specific game.
     *
     * @param gameId the ID of the game
     * @return a list of {@link Player} entries who played the game
     */
    @GetMapping("/players-by-game/{gameId}")
    @Operation (summary = "Get players for game", description = "Returns a list of all players for a game")
    public ResponseEntity<List<Player>> getPlayersByGame(@PathVariable String gameId) {
        log.info("Fetching players who played this game {}", gameId);
        return ResponseEntity.ok(playerGameService.getPlayersByGame(gameId));
    }

    /**
     * Retrieves game IDs for a player filtered by status.
     *
     * @param playerId the ID of the player
     * @param status the status to filter by
     * @return a list of game IDs matching the given status
     */
    @GetMapping("/status/{playerId}")
    @Operation (summary = "Get games for player by status", description = "Returns a list of all games for a player filtered by status")
    public ResponseEntity<List<String>> getGamesByStatus(
            @PathVariable String playerId,
            @RequestParam GameStatus status) {
        log.info("Fetching games for player {} with status {}", playerId, status);
        return ResponseEntity.ok(playerGameService.getGamesByStatus(playerId, status));
    }

    /**
     * Updates the status of a specific game for a player.
     *
     * @param playerId the ID of the player
     * @param gameId the ID of the game
     * @param status the new status to set
     * @return the updated {@link PlayerGame} record, or 404 if not found
     */
    @PutMapping("/status")
    @Operation (summary = "Update status for player-game entry", description = "Updates the status of a player-game entry")
    public ResponseEntity<PlayerGame> updateStatus(
            @RequestParam String playerId,
            @RequestParam String gameId,
            @RequestParam GameStatus status) {
        log.info("Updating status for player={}, game={}", playerId, gameId);

        return playerGameService.updateStatus(playerId, gameId, status)
                .map(updated -> {
                    log.info("Status updated successfully for playerGame ID: {}", updated.id());
                    return ResponseEntity.ok(updated);
                })
                .orElseGet(() -> {
                    log.warn("Failed to update status for player={}, game={}", playerId, gameId);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Deletes a player-game entry by ID.
     *
     * @param id the ID of the player-game entry to delete
     * @return 204 No Content if deleted, or 404 Not Found if not found
     */
    @DeleteMapping("/{id}")
    @Operation (summary = "Delete player-game entry", description = "Deletes a player-game entry by its ID")
    public ResponseEntity<Void> deletePlayerGame(@PathVariable String id) {
        log.info("Deleting PlayerGame {}", id);

        if (playerGameService.deletePlayerGame(id)) {
            log.info("PlayerGame deleted with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("PlayerGame not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
