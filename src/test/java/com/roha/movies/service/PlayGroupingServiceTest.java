package com.roha.movies.service;

import com.roha.movies.domain.Movie;
import com.roha.movies.domain.Play;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayGroupingServiceTest {

    @Test
    void groupPlaysByDay_groupsPlaysCorrectly() {
        // Arrange
        PlayGroupingService service = new PlayGroupingService();
        
        // Create test data
        Movie movie1 = new Movie("1", "Test Movie", null,null,null,"imagehref",42,null,"");
        Play play1 = new Play(
            "movie1_play1",
                movie1,
            LocalDateTime.of(2023, 6, 1, 10, 0), // Thursday
            LocalDateTime.of(2023, 6, 1, 10, 0), // Thursday
            "http://example.com/ticket1",
                "Cinema 1",""
        );
        
        Play play2 = new Play(
            "movie1_play2",
                movie1,
            LocalDateTime.of(2023, 6, 1, 14, 0), // Same day
            LocalDateTime.of(2023, 6, 1, 14, 0), // Same day
            "http://example.com/ticket2",
                "Cinema 1",""
        );
        
        Play play3 = new Play(
            "movie1",
            movie1,
            LocalDateTime.of(2023, 6, 2, 10, 0), // Friday
                null,
            "http://example.com/ticket3",
            "Cinema 2",""
        );
        Play play4 = new Play(
            "movie1",
            movie1,
            LocalDateTime.of(2023, 6, 3, 10, 0), // zaterdag
                null,
            "http://example.com/ticket3",
            "Cinema 2",""
        );
        
        Movie movie = new Movie("1", "Test Movie","","rating",null,"imagehref",42, List.of(play1, play2, play3,play4),"");
        
        // Act
        var result = service.groupPlaysByDay(movie, LocalDateTime.of(2023, 6, 1, 14, 0));
        
        // Assert
        assertNotNull(result);
        assertEquals(7, result.playsByDay().size());
        assertEquals(2, result.cinemas().size());
        assertTrue(result.playsByDay().containsKey("vandaag"));
        assertTrue(result.playsByDay().containsKey("morgen"));
        assertTrue(result.playsByDay().containsKey("zaterdag"));
        assertEquals(2, result.playsByDay().get("vandaag").size());
        assertEquals(1, result.playsByDay().get("morgen").size());
        assertEquals(1, result.playsByDay().get("zaterdag").size());
        assertEquals(0, result.playsByDay().get("zondag").size());
    }
}
