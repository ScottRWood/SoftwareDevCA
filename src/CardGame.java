import java.io.*;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Scanner;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;


public class CardGame {

    public static final int NUMBER_OF_CARDS_PER_HAND = 4;

    private static final int MIN_PLAYERS = 1;
    private static final int MAX_PLAYERS = 10000;

    public static ArrayList<Player> playersList = new ArrayList<Player>();
    public static int gameWinner = 0;

    /**
     * Reads a file, checks validity of values and converts the values into a stack of cards
     *
     * @param path                 The path to a file containing the list of values
     * @param players              The number of players in the game
     * @return                     A stack of cards representing the pack
     * @throws java.io.IOException If file not found
     * @throws Exception           Exceptions for invalid values and size
     */
    public static Stack<Card> readPackFile(String path, int players) throws java.io.IOException, Exception {
        Scanner s = new Scanner(new File(path));
        ArrayList<Integer> values = new ArrayList<>();
        Stack<Card> pack = new Stack<>();
        int a;

        while (s.hasNext()) {
            try {
                a = Integer.parseInt(s.next());
                if (a >= 0) {
                    values.add(a);
                } else {
                    throw new Exception("Invalid values");
                }
            } catch (Exception e) {
                throw new Exception("Invalid values");
            }
        }

        s.close();

        if (values.size() == 2 * NUMBER_OF_CARDS_PER_HAND * players) {
            for (Integer i : values) {
                pack.push(new Card(i));
                //System.out.println(i);
            }
        } else {
            throw new Exception("Invalid size");
        }

        return pack;
    }

    /**
     * Creates a deck file for holding the contents of said deck
     *
     * @param  i Represents the deck number
     * @return Printer for deck file
     */
    public static File createDeckFile(int i) {
        return new File("deck_" + i + ".txt");
    }

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        Stack<Card> pack;
        int players;

        while (true) {
            System.out.println("Please enter the number of players: ");
            players = reader.nextInt();
            if (players >= MIN_PLAYERS && players <= MAX_PLAYERS) {
                break;
            } else {
                System.out.println("Please enter valid number of players");
            }
        }

        ArrayList<BlockingDeque<Card>> decks = new ArrayList<>(players);
        ArrayList<ArrayList<Card>> hands = new ArrayList<>(players);

        while (true) {
            System.out.println("Please enter the path of the pack file: ");
            String path = reader.next();

            try {
                pack = readPackFile(path, players);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        for (int i = 0; i < players; i++) {
            BlockingDeque<Card> deck = new LinkedBlockingDeque<>();
            ArrayList<Card> hand = new ArrayList<>();

            for (int j = 0; j < NUMBER_OF_CARDS_PER_HAND; j++) {
                hand.add(pack.pop());
                deck.add(pack.pop());
            }

            decks.add(deck);
            hands.add(hand);
        }


        for (int i = 0; i < players; i++) {
            playersList.add(new Player("Player " + Integer.toString(i + 1), i + 1, decks.get(i), decks.get((i + 1) % players), hands.get(i), players));
        }

        for (Player q : playersList) {
            q.start();
        }

        for (int i = 0; i < playersList.size(); i++) {
            try {
                playersList.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i = 0; i < decks.size(); i++) {
            try {
                FileWriter writer = new FileWriter(createDeckFile(i + 1));
                PrintWriter printer = new PrintWriter(writer);

                printer.print("deck " + (i + 1) + " contents: ");
                for (Card c : decks.get(i)) {
                    printer.print(c.getVal() + " ");
                }
                printer.close();
            } catch (Exception e) {
                System.out.print("Cannot find file");
            }

        }


    }
}
