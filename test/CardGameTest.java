import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Stack;

import static org.junit.Assert.*;

public class CardGameTest {

    @Test(expected = Exception.class)
    public void testPackNegativePlayers() throws Exception {
        CardGame tester = new CardGame();
        tester.readPackFile(Paths.get("test").toAbsolutePath() + "/testFiles/LegalPack.txt.txt", -5);
    }

    @Test(expected = Exception.class)
    public void testPackBoundaryPlayers() throws Exception {
        CardGame tester = new CardGame();
        tester.readPackFile(Paths.get("test").toAbsolutePath() + "/testFiles/LegalPack.txt.txt", 0);
    }

    @Test(expected = IOException.class)
    public void testPackNonExistentFile() throws IOException, Exception {
        CardGame tester = new CardGame();
        tester.readPackFile(Paths.get("test").toAbsolutePath() + "/none.txt", 4);
    }

    @Test(expected = Exception.class)
    public void testPackLength() throws IOException, Exception {
        CardGame tester = new CardGame();
        tester.readPackFile(Paths.get("test").toAbsolutePath() + "/testFiles/IllegalPackLength.txt", 4);
    }

    @Test(expected = Exception.class)
    public void testPackValueNegative() throws IOException, Exception {
        CardGame tester = new CardGame();
        tester.readPackFile(Paths.get("test").toAbsolutePath() + "/testFiles/IllegalPackValueNegative.txt", 4);
    }

    @Test(expected = Exception.class)
    public void testPackValueType() throws IOException, Exception {
        CardGame tester = new CardGame();
        tester.readPackFile(Paths.get("test").toAbsolutePath() + "/testFiles/IllegalPackValueType.txt", 4);
    }

    @Test
    public void testPackLegal() throws IOException, Exception {
        CardGame tester = new CardGame();
        Stack<Card> pack = tester.readPackFile(Paths.get("test").toAbsolutePath() + "/testFiles/LegalPack.txt", 4);
        for (Card c : pack) {
            assertEquals(c.getVal(), 1);
        }
    }

    @Test
    public void testDeckFile() {
        CardGame tester = new CardGame();
        File deckFile = CardGame.createDeckFile(1);
        assertEquals(deckFile.getName(), "deck_1.txt");
    }
}