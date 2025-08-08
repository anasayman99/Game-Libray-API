package com.anas.gameLibrary.player;

import com.anas.gameLibrary.game.GameRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for managing {@link PlayerGame} entities.
 * Handles business logic related to tracking a player's game progress,
 * enforcing uniqueness, verifying existence of players and games, and updating statuses.
 */
@Service
public class PlayerGameService {
    private static final Logger log = LoggerFactory.getLogger(PlayerGameService.class);

    private final PlayerGameRepository playerGameRepository;
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;

    /**
     * Constructs a PlayerGameService with the required repositories.
     *
     * @param playerGameRepository repository for player-game records
     * @param playerRepository repository for player records
     * @param gameRepository repository for game records
     */
    public PlayerGameService(PlayerGameRepository playerGameRepository,
                             PlayerRepository playerRepository,
                             GameRepository gameRepository) {
        this.playerGameRepository = playerGameRepository;
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
    }

    /**
     * Retrieves all PlayerGame entries for a specific player.
     *
     * @param playerId the ID of the player
     * @return a list of PlayerGame entries
     */
    public List<PlayerGame> getGamesByPlayer(String playerId) {
        log.info("Fetching games for playerId={}", playerId);
        return playerGameRepository.findByPlayerId(playerId);
    }

    /**
     * Retrieves all PlayerGame entries associated with a specific game.
     *
     * @param gameId the ID of the game
     * @return a list of PlayerGame entries
     */
    public List<PlayerGame> getPlayerGameEntriesByGame(String gameId) {
        log.info("Fetching player-game entries for gameId={}", gameId);
        return playerGameRepository.findByGameId(gameId);
    }

    /**
     * Saves a new PlayerGame entry after verifying player and game existence
     * and ensuring no duplicate entry exists.
     *
     * @param pg the PlayerGame entry to save
     * @return an Optional containing the saved entry, or empty if it already exists
     * @throws IllegalArgumentException if player or game does not exist
     */
    public Optional<PlayerGame> savePlayerGame(PlayerGame pg) {
        if (!playerRepository.existsById(pg.playerId())) {
            throw new IllegalArgumentException("Player does not exist");
        }
        if (!gameRepository.existsById(pg.gameId())) {
            throw new IllegalArgumentException("Game does not exist");
        }

        String customId = pg.playerId() + "-" + pg.gameId();

        Optional<PlayerGame> existing = playerGameRepository.findById(customId);
        if (existing.isPresent()) {
            return Optional.empty();
        }

        PlayerGame withCustomId = new PlayerGame(customId, pg.playerId(), pg.gameId(), pg.status());

        return Optional.of(playerGameRepository.save(withCustomId));
    }


    /**
     * Retrieves a list of players who are tracking a specific game.
     *
     * @param gameId the ID of the game
     * @return a list of {@link Player} records
     */
    public List<Player> getPlayersByGame(String gameId) {
        log.info("Fetching players who are tracking gameId={}", gameId);

        List<String> playerIds = playerGameRepository.findByGameId(gameId).stream()
                .map(PlayerGame::playerId)
                .distinct()
                .toList();

        return playerRepository.findAllById(playerIds);
    }

    /**
     * Retrieves game IDs for a player filtered by a specific status.
     *
     * @param playerId the ID of the player
     * @param status the status to filter by
     * @return a list of game IDs matching the given status
     */
    public List<String> getGamesByStatus(String playerId, GameStatus status) {
        log.info("Fetching games for playerId={} with status={}", playerId, status);

        return playerGameRepository.findByPlayerId(playerId).stream()
                .filter(pg -> pg.status().equals(status))
                .map(PlayerGame::gameId)
                .distinct()
                .toList();
    }

    /**
     * Updates the status of a PlayerGame entry.
     *
     * @param playerId the ID of the player
     * @param gameId the ID of the game
     * @param newStatus the new status to apply
     * @return the updated PlayerGame entry if found, or empty if not
     */
    public Optional<PlayerGame> updateStatus(String playerId, String gameId, GameStatus newStatus) {
        log.info("Attempting to update status for playerId={} and gameId={} to {}", playerId, gameId, newStatus);

        Optional<PlayerGame> existing = playerGameRepository.findByPlayerIdAndGameId(playerId, gameId);
        if (existing.isEmpty()) {
            log.warn("No PlayerGame entry found for playerId={} and gameId={}", playerId, gameId);
            return Optional.empty();
        }

        PlayerGame updated = new PlayerGame(
                existing.get().id(),
                existing.get().playerId(),
                existing.get().gameId(),
                newStatus
        );

        return Optional.of(playerGameRepository.save(updated));
    }

    /**
     * Deletes a PlayerGame entry by its ID.
     *
     * @param id the ID of the PlayerGame entry to delete
     * @return true if deleted, false if not found
     */
    public boolean deletePlayerGame(String id) {
        log.info("Attempting to delete PlayerGame with id={}", id);

        if (playerGameRepository.existsById(id)) {
            playerGameRepository.deleteById(id);
            log.info("PlayerGame with id={} deleted successfully", id);
            return true;
        }

        log.warn("PlayerGame with id={} not found. Delete operation skipped.", id);
        return false;
    }
}


