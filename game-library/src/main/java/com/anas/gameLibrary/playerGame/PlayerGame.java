package com.anas.gameLibrary.playerGame;

import com.anas.gameLibrary.player.GameStatus;
import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Represents a relationship between a player and a specific game.
 * Tracks the player's progress with the game using a defined status.
 *
 * @param id the unique identifier for the player-game relationship
 * @param playerId the ID of the player who owns or is playing the game (must not be blank)
 * @param gameId the ID of the game being tracked (must not be blank)
 * @param status the current status of the game for the player (must not be null)
 */
@Document
public record PlayerGame(
        @Id String id,
        @NotBlank String playerId,
        @NotBlank String gameId,
        @NotNull GameStatus status
) {
}
