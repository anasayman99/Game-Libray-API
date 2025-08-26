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

@SpringBootApplication
public class GameLibraryApplication {

	private static final Logger log = LoggerFactory.getLogger(GameLibraryApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(GameLibraryApplication.class, args);
	}

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
