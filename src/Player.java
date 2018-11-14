import java.util.ArrayList;
import java.util.Deque;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Player extends Thread {

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
    public synchronized void takeTurn(PrintWriter printer){
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

        printer.print("Player " + this.playerNo + " draws a " + card1.getVal() + " from deck " + this.playerNo);
        printer.print("Player " + this.playerNo + " discards a " + card2.getVal() + " to deck " + deckNum);
        printer.print("Player " + this.playerNo + "'s current hand is " + hand);
    }

    public File createPlayerFile() {
        File playerFile = new File("player_" + this.playerNo + ".txt");
        return playerFile;

    }

    public boolean checkIfWinnerExists(PrintWriter printer); {
        boolean response = false

        for (Player p : CardGame.playersList) {
            if (!p.isAlive()) {
                 printer.print("Player " + p.playerNo + " has informed " + this.playerNo + " that Player " + 
                    p.playerNo + " has won" );
                 response = true;
            }
        }    

        return response;

    }

    private boolean checkIfFinished(PrintWriter printer){
        boolean allEqual = true;

        for (Card c : hand) {
            if (!(c.getVal() == hand.get(0).getVal())){
                allEqual = false;
                printer.print("Player " + this.playerNo + " wins");
                System.out.println("Player " + this.playerNo + " wins");
            }
        }

        return allEqual;
    }

    public void playGame(PrintWriter printer){
        boolean finished = false;

        while(!finished){
            finished = checkIfFinished(printer);
            finished = checkIfWinnerExists(printer);
            takeTurn(printer);
        }

        //for (Player p : CardGame.playersList) {
            //this.interrupt();
        //}

        
    }

    public void run() {
        FileWriter writer = new FileWriter(createPlayerFile());
        PrintWriter printer = new PrintWriter(writer);

        printer.print("Player " + this.playerNo + "'s initial hand is " + hand);

        while (true) {
            System.out.println(Thread.currentThread().getName());
        }

        //playGame(printer);


        printer.print("Player " + this.playerNo + " exits");
        printer.print("Player " + this.playerNo + "'s final hand: " + hand);
        printer.close();
    }
}
