package com.anas.gameLibrary.game;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Service layer for managing {@link Game} entities.
 * Handles business logic and delegates data access to {@link GameRepository}.
 */
@Service
public class GameService {
    private static final Logger log = LoggerFactory.getLogger(GameService.class);

    private final GameRepository repository;

    /**
     * Constructs a GameService with the given GameRepository.
     *
     * @param repository the repository used to access game data
     */
    public GameService(GameRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves all games from the database.
     *
     * @return a list of all games
     */
    public List<Game> getAllGames() {
        log.info("Fetching all games");

        return repository.findAll();
    }

    /**
     * Retrieves a game by its ID.
     *
     * @param id the ID of the game
     * @return an Optional containing the game if found, or empty if not
     */
    public Optional<Game> getGameById(String id) {
        log.info("Fetching game with ID: {}", id);

        return repository.findById(id);
    }

    /**
     * Saves a new game to the database.
     *
     * @param game the game to save
     * @return the saved game
     */
    public Game saveGame(Game game) {
        log.info("Saving game: {}", game.title());

        return repository.save(game);
    }

    /**
     * Updates an existing game with new data.
     *
     * @param id the ID of the game to update
     * @param updatedGame the new game data
     * @return an Optional containing the updated game, or empty if not found
     */
    public Optional<Game> updateGame(String id, Game updatedGame) {
        log.info("Updating game with ID: {}", id);

        return repository.findById(id).map(existing -> {
            Game newGame = new Game(
                    existing.id(), // keep original ID
                    updatedGame.title(),
                    updatedGame.genre(),
                    updatedGame.platform(),
                    updatedGame.releaseDate());
            return repository.save(newGame);
        });
    }


    /**
     * Deletes a game by its ID.
     *
     * @param id the ID of the game to delete
     * @return true if the game was deleted, false if it was not found
     */
    public boolean deleteGame(String id) {
        log.info("Attempting to delete game with ID: {}", id);

        if (!repository.existsById(id)) {
            log.warn("Game with ID {} not found. Cannot delete.", id);
            return false;
        }

        repository.deleteById(id);
        return true;
    }
}
