import java.util.ArrayList;
import java.util.Deque;


public class Player extends Thread{

    private Deque<Card> drawFrom;
    private Deque<Card> dropTo;
    private ArrayList<Card> hand;

    public Player(String name, Deque<Card> drawFrom, Deque<Card> dropTo, ArrayList<Card> hand){
        this.drawFrom = drawFrom;
        this.dropTo = dropTo;
        this.hand = hand;
        this.setName(name);
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

    private boolean checkIfFinished(){
        boolean allEqual = true;

        for (Card c : hand) {
            if (!(c.getVal() == hand.get(0).getVal())){
                allEqual = false;
            }
        }

        return allEqual;
    }

    public void playGame(){
        boolean finished = false;

        while(!finished){
            takeTurn();
            finished = checkIfFinished();
        }

        for (Player p : CardGame.playersList) {
            p.interrupt();
        }
    }

    public void run() {
        while (true) {
            System.out.println(Thread.currentThread().getName());
        }
        //playGame();
    }
}
