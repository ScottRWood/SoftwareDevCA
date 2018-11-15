import java.util.ArrayList;
import java.util.Deque;
import java.io.*;


public class Player extends Thread {

    private Deque<Card> drawFrom;
    private Deque<Card> dropTo;
    private ArrayList<Card> hand;
    private int playerNo;
    private int players;
    private String name;
    private volatile boolean running = true;

    public Player(String name, int playerNo, CardDeck drawFrom, CardDeck dropTo, ArrayList<Card> hand, int players) {
        this.drawFrom = drawFrom;
        this.dropTo = dropTo;
        this.hand = hand;
        this.name = name;
        this.playerNo = playerNo;
        this.players = players;
    }


    /**
     * Executes a turn in the game
     */
    public synchronized void takeTurn(PrintWriter printer){
        System.out.println(this.name + " has taken a " + drawFrom.getLast().getVal());
        boolean checkCardNum = true;
        Card card1 = drawFrom.get((int)(Math.random() * (CardGame.NUMBER_OF_CARDS_PER_HAND + 1)))
        Card card2 = null;
        int deckNum = ((this.playerNo)+1);

        hand.add(card1);
        drawFrom.remove(card1)

        while (checkCardNum) {
            card2 = hand.get((int)(Math.random() * (CardGame.NUMBER_OF_CARDS_PER_HAND + 1)));

            if (card2.getVal() == this.playerNo) {
                checkCardNum = true;
            }

            else {
                checkCardNum = false;
            }

        }

        dropTo.add(card2);
        hand.remove(card2);

        printer.println(this.name + " draws a " + card1.getVal() + " from deck " + (this.playerNo));
        printer.println(this.name + " discards a " + card2.getVal() + " to deck " + deckNum);
        printer.print(this.name + "'s current hand is ");
        for (Card c : hand) {printer.print(c.getVal() + " ");};
        printer.print("\n");
    }

    public PrintWriter createPlayerFile() throws IOException {
        File playerFile = new File(this.name + "_output.txt");
        FileWriter writer = new FileWriter(playerFile);
        PrintWriter printer = new PrintWriter(writer);

        return printer;

    }

    public boolean checkIfWinnerExists(PrintWriter printer) {
        boolean response = false;

        for (Player p : CardGame.playersList) {
            if (!p.isAlive()) {
                 printer.println(p.name + " has informed " + this.name + " that " + 
                    p.name  + " has won");
                 response = true;
            }
        }    

        return response;

    }

    private boolean checkIfFinished(PrintWriter printer){
        boolean allEqual = false;

        //for (Card c : hand) {
            //if (!(c.getVal() == hand.get(0).getVal())){
                //allEqual = true;
            //}
        //}

        allEqual = hand.stream().distinct().limit(2).count() <= 1;

        if (allEqual == true) {
            printer.println(this.name + " wins");
            System.out.println(this.name + " wins");
            return allEqual;
        }

        else {
            return allEqual;
        }

    }

    public void playGame(PrintWriter printer){
        boolean finished = false;

        while(!finished){
            finished = checkIfFinished(printer);
            if (finished == true) {
                break;
            }

            takeTurn(printer);

            finished = checkIfWinnerExists(printer);
            if (finished == true) {
                break;
            }
           
            
        }

    
        this.shutdown();

        
    }

    public void shutdown() {
        running = false;
    }

    public void run() {
        PrintWriter printer;
        try {
            printer = createPlayerFile();
            printer.print(this.name + "'s initial hand is ");
            for (Card c : hand) {printer.print(c.getVal() + " ");};
            printer.print("\n");

            while (running) {
                //System.out.println(Thread.currentThread().getName());
                playGame(printer);

            }

            printer.println(this.name  + " exits");
            printer.println(this.name + "'s final hand: ");
            for (Card c : hand) {printer.print(c.getVal() + " ");};
            printer.print("\n");
            printer.close();
        }
        catch (IOException e) {
            System.out.println("No file found");
        }
        
    }
}
