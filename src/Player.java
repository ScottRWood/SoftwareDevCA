import java.util.ArrayList;
import java.util.Deque;


public class Player implements Runnable{

    private Deque<Card> drawFrom;
    private Deque<Card> dropTo;
    private ArrayList<Card> hand;

    public Player(Deque<Card> drawFrom, Deque<Card> dropTo, ArrayList<Card> hand){
        this.drawFrom = drawFrom;
        this.dropTo = dropTo;
        this.hand = hand;
    }


    /**
     * Executes a turn in the game
     */
    public synchronized void takeTurn(){
        hand.add(drawFrom.getFirst());
        Card card = hand.get((int)(Math.random() * (CardGame.NUMBER_OF_CARDS_PER_HAND + 1)));
        dropTo.addLast(card);
        hand.remove(card);
    }

    public void playGame(){
    }

    public void run() {
        playGame();
    }
}
