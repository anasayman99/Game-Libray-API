package com.anas.gameLibrary.player;

import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/**
 * Represents a named collection of games owned by a specific player.
 * Used to group multiple games under a collection (e.g., Favorites, Wishlist).
 *
 * @param id the unique identifier for the collection (must not be blank)
 * @param name the name of the collection (e.g., "Favorites", "Backlog")
 * @param playerId the ID of the player who owns this collection (must not be blank)
 * @param gameIds the list of game IDs included in the collection (must not be empty)
 */
@Document
public record GameCollection(
        @Id @NotBlank String id,
        @NotBlank String name,
        @NotBlank String playerId, // owner of the collection
        @NotEmpty List<String> gameIds // list of Games IDs
) {}
