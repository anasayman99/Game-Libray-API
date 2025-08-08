package com.anas.gameLibrary.player;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing {@link Player} resources.
 * Provides endpoints to create, retrieve, update, and delete player profiles.
 */
@RestController
@RequestMapping("/api/players")
public class PlayerController {

    private static final Logger log = LoggerFactory.getLogger(PlayerController.class);

    private final PlayerService playerService;

    /**
     * Constructs a PlayerController with the given player service.
     *
     * @param playerService the service responsible for player operations
     */
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    /**
     * Retrieves all players in the system.
     *
     * @return a list of all player records
     */
    @GetMapping
    public ResponseEntity<List<Player>> getAllPlayers() {
        log.info("Received request to get all players");
        return ResponseEntity.ok(playerService.getAllPlayers());
    }

    /**
     * Retrieves a specific player by ID.
     *
     * @param id the ID of the player
     * @return the player record if found, or 404 Not Found
     */
    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable String id) {
        log.info("Received request to get player with ID: {}\"", id);
        return playerService.getPlayerById(id)
                .map(player -> {
                    log.info("Player found: {}", player.username());
                    return ResponseEntity.ok(player);
                })
                .orElseGet(() -> {
                    log.warn("Player not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Creates a new player.
     *
     * @param player the player data to create
     * @return the created player record
     */
    @PostMapping
    public ResponseEntity<Player> createPlayer(@Valid @RequestBody Player player) {
        log.info("Received request to create player: {}", player.username());
        return ResponseEntity.ok(playerService.savePlayer(player));
    }

    /**
     * Updates an existing player's information.
     *
     * @param id the ID of the player to update
     * @param updatedPlayer the new player data
     * @return the updated player if found, or 404 Not Found
     */
    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable String id, @Valid @RequestBody Player updatedPlayer) {
        log.info("Received request to update player with ID: {}\"", id);
        return playerService.updatePlayer(id, updatedPlayer)
                .map(player -> {
                    log.info("Player updated: {}", player.username());
                    return ResponseEntity.ok(player);
                })
                .orElseGet(() -> {
                    log.warn("Update failed, player not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    /**
     * Deletes a player by ID.
     *
     * @param id the ID of the player to delete
     * @return 204 No Content if deleted, or 404 Not Found if not found
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePlayer(@PathVariable String id) {
        log.info("Received request to delete player with ID: {}", id);
        if (playerService.deletePlayer(id)) {
            log.info("Player deleted with ID: {}", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Failed to delete, player not found with ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
