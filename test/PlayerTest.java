import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test
    public void testCheckIfFinishedTrue() throws IOException {
        BlockingDeque<Card> drawFrom = new LinkedBlockingDeque<>();
        BlockingDeque<Card> dropTo = new LinkedBlockingDeque<>();
        ArrayList<Card> hand = new ArrayList<>();

        hand.add(new Card(1));
        hand.add(new Card(1));
        hand.add(new Card(1));
        hand.add(new Card(1));

        Player tester = new Player("Player 1", 1, drawFrom, dropTo, hand, 4);
        PrintWriter playerPrinter = tester.createPlayerFile();

        assertEquals(tester.checkIfFinished(playerPrinter), true);
    }

    @Test
    public void testCheckIfFinishedFalse() throws IOException {
        BlockingDeque<Card> drawFrom = new LinkedBlockingDeque<>();
        BlockingDeque<Card> dropTo = new LinkedBlockingDeque<>();
        ArrayList<Card> hand = new ArrayList<>();

        hand.add(new Card(1));
        hand.add(new Card(2));
        hand.add(new Card(1));
        hand.add(new Card(3));

        Player tester = new Player("Player 1", 1, drawFrom, dropTo, hand, 4);
        PrintWriter playerPrinter = tester.createPlayerFile();

        assertEquals(tester.checkIfFinished(playerPrinter), false);
    }

    @Test
    public void testCheckIfFinishedBoundary() throws IOException {
        BlockingDeque<Card> drawFrom = new LinkedBlockingDeque<>();
        BlockingDeque<Card> dropTo = new LinkedBlockingDeque<>();
        ArrayList<Card> hand = new ArrayList<>();

        hand.add(new Card(1));
        hand.add(new Card(2));
        hand.add(new Card(1));
        hand.add(new Card(1));

        Player tester = new Player("Player 1", 1, drawFrom, dropTo, hand, 4);
        PrintWriter playerPrinter = tester.createPlayerFile();

        assertEquals(tester.checkIfFinished(playerPrinter), false);
    }

    @Test
    public void testCheckIfWinnerExistsTrue() throws IOException {
        BlockingDeque<Card> drawFrom = new LinkedBlockingDeque<>();
        BlockingDeque<Card> dropTo = new LinkedBlockingDeque<>();
        ArrayList<Card> hand = new ArrayList<>();

        Player tester = new Player("Player 1", 1, drawFrom, dropTo, hand, 4);
        PrintWriter playerPrinter = tester.createPlayerFile();

        CardGame.gameWinner = 2;

        assertEquals(tester.checkIfWinnerExists(playerPrinter), true);
    }

    @Test
    public void testCheckIfWinnerExistsFalse() throws IOException {
        BlockingDeque<Card> drawFrom = new LinkedBlockingDeque<>();
        BlockingDeque<Card> dropTo = new LinkedBlockingDeque<>();
        ArrayList<Card> hand = new ArrayList<>();

        Player tester = new Player("Player 1", 1, drawFrom, dropTo, hand, 4);
        PrintWriter playerPrinter = tester.createPlayerFile();

        CardGame.gameWinner = 0;

        assertEquals(tester.checkIfWinnerExists(playerPrinter), false);
    }
}