import java.util.ArrayList;
import java.util.Deque;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Player extends Thread{

    private Deque<Card> drawFrom;
    private Deque<Card> dropTo;
    private ArrayList<Card> hand;
    private int playerNo;
    private int players;

    public Player(String name, int playerNo, Deque<Card> drawFrom, Deque<Card> dropTo, ArrayList<Card> hand, int players) {
        this.drawFrom = drawFrom;
        this.dropTo = dropTo;
        this.hand = hand;
        this.setName(name);
        this.playerNo = playerNo;
        this.players = players;
    }


    /**
     * Executes a turn in the game
     */
    public synchronized void takeTurn(){
        boolean checkCardNum = true;
        Card card1 = drawFrom.getFirst();
        Card card2 = null;
        int deckNum = (this.playerNo+1)%this.players;

        hand.add(card1);

        while (checkCardNum) {
            card2 = hand.get((int)(Math.random() * (CardGame.NUMBER_OF_CARDS_PER_HAND + 1)));

            if (card2.getVal() == this.playerNo) {
                checkCardNum = true;
            }

            else {
                checkCardNum = false;
            }

        }

        dropTo.addLast(card2);
        hand.remove(card2);

        FileWriter writer = new FileWriter(someFile);
        PrintWriter printer = new PrintWriter(writer);

        printer.print("Player " + this.playerNo + " draws a " + card1.getVal() + " from deck " + this.playerNo);
        printer.print("Player " + this.playerNo + " discards a " + card2.getVal() + " to deck " + deckNum);
        printer.print("Player " + this.playerNo + "'s current hand is " + hand);
    }

    public void createFile() {
        File playerFile = new File("player_" + this.playerNo + ".txt");

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

        FileWriter writer = new FileWriter(someFile);
        PrintWriter printer = new PrintWriter(writer);

        printer.print("Player " + this.playerNo + "'s initial hand is " + hand);


        while(!finished){
            takeTurn();
            finished = checkIfFinished();
        }

        for (Player p : CardGame.playersList) {
            p.interrupt();
        }
    }

    public void run() {
        createFile();
        while (true) {
            System.out.println(Thread.currentThread().getName());
        }
        //playGame();
    }
}
