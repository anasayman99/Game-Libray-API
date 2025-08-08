package com.anas.gameLibrary.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing {@link Player} entities.
 * Handles business logic related to player creation, retrieval, update, and deletion.
 */
@Service
public class PlayerService {
    private static final Logger log = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;

    /**
     * Constructs a PlayerService with the given PlayerRepository.
     *
     * @param playerRepository the repository used to access player data
     */
    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Retrieves all players.
     *
     * @return a list of all players
     */
    public List<Player> getAllPlayers() {
        log.info("Fetching all players");

        return playerRepository.findAll();
    }

    /**
     * Retrieves a player by their ID.
     *
     * @param id the player's ID
     * @return an Optional containing the player if found, or empty if not
     */
    public Optional<Player> getPlayerById(String id) {
        log.info("Fetching player with id={}", id);

        return playerRepository.findById(id);
    }

    /**
     * Saves a new player, enforcing unique username.
     *
     * @param player the player to save
     * @return the saved player
     * @throws IllegalArgumentException if the username already exists
     */
    public Player savePlayer(Player player) {
        log.info("Attempting to save player: {}", player.username());

        if (playerRepository.findByUsername(player.username()).isPresent()) {
            log.warn("Username '{}' is already taken", player.username());
            throw new IllegalArgumentException("Username already exists");
        }

        return playerRepository.save(player);
    }

    /**
     * Updates an existing player's information.
     *
     * @param id the ID of the player to update
     * @param updatedPlayer the new player data
     * @return the updated player if found, or empty if not found
     */
    public Optional<Player> updatePlayer(String id, Player updatedPlayer) {
        log.info("Attempting to update player with ID: {}", id);

        return playerRepository.findById(id).map(existing -> {
            log.info("Found player with ID: {}. Updating details.", id);
            Player playerToSave = new Player(
                    id,
                    updatedPlayer.username(),
                    updatedPlayer.email(),
                    updatedPlayer.birthDate()
            );
            Player savedPlayer = playerRepository.save(playerToSave);
            log.info("Player updated successfully: {}", savedPlayer.username());
            return savedPlayer;
        });
    }

    /**
     * Deletes a player by their ID.
     *
     * @param id the ID of the player to delete
     * @return true if deletion was successful, false if player was not found
     */
    public boolean deletePlayer(String id) {
        log.info("Attempting to delete player with id={}", id);

        if (playerRepository.existsById(id)) {
            playerRepository.deleteById(id);
            log.info("Player with id={} deleted", id);
            return true;
        }

        log.warn("Player with id={} not found. Delete skipped.", id);
        return false;
    }
}
