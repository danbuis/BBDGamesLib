package TestsGameEngine;

import BBDGameLibrary.GameEngine.Die;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDie {

    @Test
    public void testD6(){
        Die<Integer> test = new Die<>(6);
        assertTrue(test.isUnrolled());

        test.roll();
        assertFalse(test.isUnrolled());
    }

    @Test
    public void testSetToFace(){
        Die<Integer> test = new Die<>(6);
        test.setToFace(2);
        assertEquals(2, test.getCurrentFace());

        test.setToFace(99);
        assertEquals(2, test.getCurrentFace());
    }

    @Test
    public void testRolling(){
        Die<Integer> test = new Die<>(9999);
        test.roll();
        int firstRoll = test.getCurrentFace();
        test.roll();
        int secondRoll = test.getCurrentFace();

        if (secondRoll == firstRoll){
            test.roll();
            secondRoll = test.getCurrentFace();
        }

        assertNotEquals(firstRoll, secondRoll);
    }

    @Test
    public void diceObjects(){
        String[] faces = {"one", "two", "three", "four", "five", "Guac"};

        Die<String> test = new Die<>(faces);
        assertTrue(test.isUnrolled());

        test.setToFace("three");
        assertFalse(test.isUnrolled());

        assertEquals("three", test.getCurrentFace());
    }
}
