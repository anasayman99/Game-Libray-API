package com.anas.gameLibrary;

import com.anas.gameLibrary.game.Game;
import com.anas.gameLibrary.game.GameRepository;
import com.anas.gameLibrary.player.*;
import com.anas.gameLibrary.playerGame.PlayerGame;
import com.anas.gameLibrary.playerGame.PlayerGameRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * Main entry point for the Game Library Spring Boot application.
 *
 * This class boots the application and contains a data-loading routine
 * that runs on startup. The routine seeds the database with initial
 * data from JSON files located in the resources folder, but only if
 * the relevant tables are empty.
 *
 * The seeding process works as follows:
 * - If there are no players, the file players.json is loaded.
 * - If there are no games, the file games.json is loaded.
 * - If there are no collections, the file collections.json is loaded.
 * - If there are no player-game entries, the file playersGames.json is loaded.
 *
 * Logging is provided throughout to make it clear whether data
 * was inserted or skipped.
 *
 * Author: Anas Sadek
 */
@SpringBootApplication
public class GameLibraryApplication {

	private static final Logger log = LoggerFactory.getLogger(GameLibraryApplication.class);

	/**
	 * Starts the Spring Boot application.
	 *
	 * @param args command line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(GameLibraryApplication.class, args);
	}

	/**
	 * Loads initial data into the database on startup if the repositories
	 * are empty. Data is read from JSON files on the classpath and saved
	 * through the corresponding repositories.
	 *
	 * The JSON files are deserialized with Jackson's ObjectMapper and mapped
	 * into lists of entity objects.
	 *
	 * @param playerRepo repository for Player entities
	 * @param gameRepo repository for Game entities
	 * @param collectionRepo repository for GameCollection entities
	 * @param playerGameRepo repository for PlayerGame entities
	 * @param objectMapper Jackson mapper used to parse JSON files
	 * @return a CommandLineRunner that performs the seeding logic
	 */
	@Bean
	CommandLineRunner loadData(
			PlayerRepository playerRepo,
			GameRepository gameRepo,
			GameCollectionRepository collectionRepo,
			PlayerGameRepository playerGameRepo,
			ObjectMapper objectMapper) {
		return args -> {

			if (playerRepo.count() == 0) {
				log.info("No players found, loading players...");
				var players = objectMapper.readValue(
						getClass().getResourceAsStream("/players.json"),
						new TypeReference<List<Player>>() {});
				playerRepo.saveAll(players);
				log.info("Loaded {} players", players.size());
			} else {
				log.info("Players already exist, skipping players.json");
			}

			if (gameRepo.count() == 0) {
				log.info("No games found, loading games...");
				var games = objectMapper.readValue(
						getClass().getResourceAsStream("/games.json"),
						new TypeReference<List<Game>>() {});
				gameRepo.saveAll(games);
				log.info("Loaded {} games", games.size());
			} else {
				log.info("Games already exist, skipping games.json");
			}

			if (collectionRepo.count() == 0) {
				log.info("No game collections found, loading gameCollections...");
				var collections = objectMapper.readValue(
						getClass().getResourceAsStream("/collections.json"),
						new TypeReference<List<GameCollection>>() {});
				collectionRepo.saveAll(collections);
				log.info("Loaded {} game collections", collections.size());
			} else {
				log.info("Game collections already exist, skipping gameCollections.json");
			}

			if (playerGameRepo.count() == 0) {
				log.info("No player-game entries found, loading playerGames...");
				var playerGames = objectMapper.readValue(
						getClass().getResourceAsStream("/playersGames.json"),
						new TypeReference<List<PlayerGame>>() {});
				playerGameRepo.saveAll(playerGames);
				log.info("Loaded {} player-game entries", playerGames.size());
			} else {
				log.info("Player-game entries already exist, skipping playerGames.json");
			}

			log.info("Data loading completed.");
		};
	}

}
