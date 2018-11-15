import java.util.ArrayList;
import java.io.*;
import java.util.concurrent.BlockingDeque;
import java.util.function.Consumer;


public class Player extends Thread {

    private BlockingDeque<Card> drawFrom;
    private BlockingDeque<Card> dropTo;
    private ArrayList<Card> hand;
    private int playerNo;
    private int turnsTaken = 0;
    private String name;
    private volatile boolean running = true;


    public Player(String name, int playerNo, BlockingDeque<Card> drawFrom, BlockingDeque<Card> dropTo, ArrayList<Card> hand, int players) {
        this.drawFrom = drawFrom;
        this.dropTo = dropTo;
        this.hand = hand;
        this.name = name;
        this.playerNo = playerNo;
    }


    /**
     * Executes a turn in the game and writes to the file
     */
    public synchronized void takeTurn(PrintWriter printer) {
        boolean checkCardNum = true;

        try {
                if (!drawFrom.isEmpty()) {
                    Card card1 = drawFrom.takeFirst();
                    Card card2 = null;

                    int deckNum = ((this.playerNo) + 1);
                    int rand = 0;
                    hand.add(card1);

                    while (checkCardNum) {
                        rand = (int) (Math.random() * (CardGame.NUMBER_OF_CARDS_PER_HAND + 1));
                        card2 = hand.get(rand);

                        if (card2.getVal() == this.playerNo) {
                            checkCardNum = true;
                        } else {
                            checkCardNum = false;
                        }

                    }

                    dropTo.putLast(card2);
                    hand.remove(rand);

                    turnsTaken++;

                    printer.println(this.name + " draws a " + card1.getVal() + " from deck " + (this.playerNo));
                    printer.println(this.name + " discards a " + card2.getVal() + " to deck " + deckNum);
                    printer.print(this.name + "'s current hand is ");
                    for (Card c : hand) {
                        printer.print(c.getVal() + " ");
                    }
                    printer.print("\n");
                }
        } catch (InterruptedException e) {
            System.out.println("Interruption occurred");
        }
    }

    /**
     * Creates file to store player moves
     * @return Printer for file
     * @throws IOException
     */
    public PrintWriter createPlayerFile() throws IOException {
        File playerFile = new File(this.name + "_output.txt");
        FileWriter writer = new FileWriter(playerFile);
        PrintWriter printer = new PrintWriter(writer);

        return printer;

    }

    /**
     * Checks if any other player has won
     * @param printer The printer for the player file
     * @return boolean stating whether someone has won
     */
    public boolean checkIfWinnerExists(PrintWriter printer) {
        boolean response = false;
        if (CardGame.gameWinner != 0) {
            printer.println("Player " + CardGame.gameWinner + " has informed " + this.name + " that Player " +
                    CardGame.gameWinner + " has won");
            System.out.println(CardGame.gameWinner);
            response = true;
        }
        return response;

    }

    /**
     * Checks if this player has finished and alerts CardGame class
     * @param printer The printer for the player file
     * @return boolean stating whether you've won
     */
    private boolean checkIfFinished(PrintWriter printer) {
        boolean allEqual = true;

        for (Card c : hand) {
            if(c.getVal() != hand.get(0).getVal())
                allEqual = false;
        }

        if (allEqual == true && CardGame.gameWinner == 0) {
            printer.println(this.name + " wins");
            System.out.println(this.name + " wins");
            CardGame.gameWinner = this.playerNo;
        }

        return allEqual;

    }

    /**
     * Plays the game
     * @param printer The printer for the player file
     */
    public void playGame(PrintWriter printer) {
        boolean finished = false;

        while (true) {
            if(checkIfWinnerExists(printer) || checkIfFinished(printer)){
                break;
            }

            takeTurn(printer);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e){
            }
        }

        int maxTurns = turnsTaken;

        for (Player p : CardGame.playersList){
            maxTurns = (p.getTurns() > maxTurns) ? p.getTurns() : maxTurns;
        }

        while (turnsTaken < maxTurns){
            takeTurn(printer);
        }

        System.out.println(turnsTaken);
    }

    /**
     * @return The number of turns player has taken
     */
    public int getTurns(){
        return this.turnsTaken;
    }

    public void run() {
        PrintWriter printer;
        try {
            printer = createPlayerFile();
            printer.print(this.name + "'s initial hand is ");
            for (Card c : hand) {
                printer.print(c.getVal() + " ");
            }
            ;
            printer.print("\n");


            playGame(printer);


            printer.println(this.name + " exits");
            printer.println(this.name + "'s final hand: ");
            for (Card c : hand) {
                printer.print(c.getVal() + " ");
            }

            printer.print("\n");
            printer.close();
        } catch (IOException e) {
            System.out.println("No file found");
        }

    }
}
