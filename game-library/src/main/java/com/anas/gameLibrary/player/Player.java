package com.anas.gameLibrary.player;


import jakarta.validation.constraints.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

/**
 * Represents a player profile in the game library system.
 * Each player has a unique ID, username, email, and date of birth.
 *
 * @param id the unique identifier of the player (must not be blank)
 * @param username the player's chosen username (must not be blank)
 * @param email the player's email address (must be valid and not blank)
 * @param birthDate the player's birth date (must be in the past)
 */
@Document
public record Player(
        @Id @NotBlank String id,
        @NotBlank String username,
        @Email @NotBlank String email,
        @Past LocalDate birthDate
) {}
