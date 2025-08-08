package com.anas.gameLibrary.game;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Represents a video game entity stored in the MongoDB database.
 * Each game has an ID, title, genre, platform, and release date.
 * Used within the game library tracking application for managing
 * game entries across various players' collections.
 *
 * @param id the unique identifier of the game (must not be blank)
 * @param title the name of the game (must not be blank)
 * @param genre the genre category of the game (must not be blank)
 * @param platform the platform the game runs on (e.g., PC, Xbox)
 * @param releaseDate the game's release date (must be in the past or present)
 */
@Document
public record Game(
        @Id
        @NotBlank String id,
        @NotBlank String title,
        @NotBlank String genre,
        @NotNull Platform platform,
        @PastOrPresent LocalDate releaseDate
) { }
