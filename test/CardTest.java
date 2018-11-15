import org.junit.Test;

import static org.junit.Assert.*;

public class CardTest {

    @Test
    public void testGetFaceVal() {
        Card tester = new Card(1);
        assertEquals(tester.getVal(), 1);
    }
}