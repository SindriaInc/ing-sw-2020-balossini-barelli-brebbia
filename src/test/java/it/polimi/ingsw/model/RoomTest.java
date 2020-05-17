package it.polimi.ingsw.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {

    @Test
    void checkAll(){
        Player player1 = new Player("Carlo", 34);
        Player player2 = new Player("Luigi", 78);
        Player player3 = new Player("Erica", 23);
        Room room = new Room(player1,3, true);
        assertEquals(player1, room.getOwner());
        assertEquals(3, room.getMaxPlayers());
        assertTrue(room.isSimpleGame());
        room.addPlayer(player2);
        assertEquals(room.getOtherPlayers().size(), 1);
        assertEquals(room.getAllPlayers().size(), 2);
        assertFalse(room.isFull());
        room.removePlayer(player2);
        assertEquals(room.getOtherPlayers().size(), 0);
        assertEquals(room.getAllPlayers().size(), 1);
        assertFalse(room.isFull());
        room.addPlayer(player2);
        room.addPlayer(player3);
        assertEquals(room.getOtherPlayers().size(), 2);
        assertEquals(room.getAllPlayers().size(), 3);
        assertTrue(room.isFull());
    }

}