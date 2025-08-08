package com.anas.gameLibrary.playerDomainTests;

import com.anas.gameLibrary.player.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    private Player samplePlayer;
    private List<Player> mockPlayers;

    @BeforeEach
    void setUp() {
        samplePlayer = new Player("1", "anas_s", "anas@example.com", LocalDate.of(2000, 1, 1));
        mockPlayers = List.of(samplePlayer);
    }

    @Test
    void testGetAllPlayers() {
        when(playerRepository.findAll()).thenReturn(mockPlayers);

        List<Player> result = playerService.getAllPlayers();

        assertEquals(mockPlayers.size(), result.size());
        verify(playerRepository).findAll();
    }

    @Test
    void testGetPlayerByIdFound() {
        when(playerRepository.findById("1")).thenReturn(Optional.of(samplePlayer));

        Optional<Player> result = playerService.getPlayerById("1");

        assertTrue(result.isPresent());
        assertEquals(samplePlayer.username(), result.get().username());
        verify(playerRepository).findById("1");
    }

    @Test
    void testGetPlayerByIdNotFound() {
        when(playerRepository.findById("404")).thenReturn(Optional.empty());

        Optional<Player> result = playerService.getPlayerById("404");

        assertTrue(result.isEmpty());
        verify(playerRepository).findById("404");
    }

    @Test
    void testSavePlayer() {
        when(playerRepository.save(samplePlayer)).thenReturn(samplePlayer);

        Player result = playerService.savePlayer(samplePlayer);

        assertEquals(samplePlayer.username(), result.username());
        verify(playerRepository).save(samplePlayer);
    }

    @Test
    void testUpdatePlayerFound() {
        Player updatedPlayer = new Player("1", "anas_updated", "anas@updated.com", LocalDate.of(1999, 5, 5));
        when(playerRepository.findById("1")).thenReturn(Optional.of(samplePlayer));
        when(playerRepository.save(any(Player.class))).thenReturn(updatedPlayer);

        Optional<Player> result = playerService.updatePlayer("1", updatedPlayer);

        assertTrue(result.isPresent());
        assertEquals(updatedPlayer.username(), result.get().username());
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    void testUpdatePlayerNotFound() {
        Player updatedPlayer = new Player("404", "ghost", "ghost@example.com", LocalDate.of(1990, 1, 1));
        when(playerRepository.findById("404")).thenReturn(Optional.empty());

        Optional<Player> result = playerService.updatePlayer("404", updatedPlayer);

        assertTrue(result.isEmpty());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void testDeletePlayerExists() {
        when(playerRepository.existsById("1")).thenReturn(true);

        boolean result = playerService.deletePlayer("1");

        assertTrue(result);
        verify(playerRepository).existsById("1");
        verify(playerRepository).deleteById("1");
    }

    @Test
    void testDeletePlayerNotExists() {
        when(playerRepository.existsById("404")).thenReturn(false);

        boolean result = playerService.deletePlayer("404");

        assertFalse(result);
        verify(playerRepository).existsById("404");
        verify(playerRepository, never()).deleteById(any());
    }
}
