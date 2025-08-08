package com.anas.gameLibrary.gameDomainTests;

import com.anas.gameLibrary.game.*;
import com.anas.gameLibrary.game.GameRepository;
import com.anas.gameLibrary.game.Platform;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    private Game sampleGame;
    private List<Game> mockGames;

    @BeforeEach
    void setUp() {
        sampleGame = new Game("1", "Elden Ring", "RPG", Platform.PC, LocalDate.of(2022, 1, 1));
        mockGames = List.of(sampleGame);
    }

    @Test
    void testGetAllGames() {
        when(gameRepository.findAll()).thenReturn(mockGames);

        List<Game> result = gameService.getAllGames();

        assertEquals(mockGames.size(), result.size());
        verify(gameRepository).findAll();
    }

    @Test
    void testGetGameByIdFound() {
        when(gameRepository.findById("1")).thenReturn(Optional.of(sampleGame));

        Optional<Game> result = gameService.getGameById("1");

        assertTrue(result.isPresent());
        assertEquals(sampleGame.title(), result.get().title());
        verify(gameRepository).findById("1");
    }

    @Test
    void testGetGameByIdNotFound() {
        when(gameRepository.findById("invalid")).thenReturn(Optional.empty());

        Optional<Game> result = gameService.getGameById("invalid");

        assertTrue(result.isEmpty());
        verify(gameRepository).findById("invalid");
    }


    @Test
    void testSaveGame() {
        when(gameRepository.save(sampleGame)).thenReturn(sampleGame);

        Game result = gameService.saveGame(sampleGame);

        assertEquals(sampleGame.title(), result.title());
        verify(gameRepository).save(sampleGame);
    }

    @Test
    void testUpdateGameFound() {
        Game updatedGame = new Game("1", "Elden Ring Updated", "RPG",Platform.XBOX, LocalDate.of(2022, 1, 1));
        when(gameRepository.findById("1")).thenReturn(Optional.of(sampleGame));
        when(gameRepository.save(any(Game.class))).thenReturn(updatedGame);

        Optional<Game> result = gameService.updateGame("1", updatedGame);

        assertTrue(result.isPresent());
        assertEquals(updatedGame.title(), result.get().title());
        assertEquals(updatedGame.platform(), result.get().platform());
        verify(gameRepository).save(any(Game.class));
    }

    @Test
    void testUpdateGameNotFound() {
        when(gameRepository.findById("404")).thenReturn(Optional.empty());

        Game updatedGame = new Game("404", "Ghost", "Horror",Platform.SWITCH,  LocalDate.of(2025, 1, 1));

        Optional<Game> result = gameService.updateGame("404", updatedGame);

        assertTrue(result.isEmpty());
        verify(gameRepository).findById("404");
        verify(gameRepository, never()).save(any());
    }

    @Test
    void testDeleteGameExists() {
        when(gameRepository.existsById("1")).thenReturn(true);

        boolean result = gameService.deleteGame("1");

        assertTrue(result);
        verify(gameRepository).existsById("1");
        verify(gameRepository).deleteById("1");
    }

    @Test
    void testDeleteGameNotExists() {
        when(gameRepository.existsById("404")).thenReturn(false);

        boolean result = gameService.deleteGame("404");

        assertFalse(result);
        verify(gameRepository).existsById("404");
        verify(gameRepository, never()).deleteById(anyString());
    }
}
