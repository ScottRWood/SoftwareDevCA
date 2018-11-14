import java.io.File;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.PrintWriter;

public class CardGame {

    public static final int NUMBER_OF_CARDS_PER_HAND = 4;

    public static ArrayList<Player> playersList;

    /**
     * Reads a file, checks validity of values and converts the values into a stack of cards
     * @param path The path to a file containing the list of values
     * @param players The number of players in the game
     * @return A stack of cards representing the pack
     * @throws java.io.IOException If file not found
     * @throws Exception Exceptions for invalid values and size
     */
    private static Stack<Card> readPackFile(String path, int players) throws java.io.IOException, Exception {
        Scanner s = new Scanner(new File(path));
        ArrayList<Integer> values = new ArrayList<Integer>();
        Stack<Card> pack = new Stack<Card>();
        int a;
        String next;

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
        
        if (values.size()  == 2 * NUMBER_OF_CARDS_PER_HAND * players) {
            for (Integer i: values) {
                pack.push(new Card(i));
                System.out.println(i);
            }
        } else {
            throw new Exception("Invalid size");
        }

        return pack;
    }

    public static File createDeckFile(int i) {
        File deckFile = new File("deck_" + i + ".txt");

        return deckFile;

    }

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        

        Stack<Card> pack;

        System.out.println("Please enter the number of player: ");
        int players = reader.nextInt();

        ArrayList<Deque<Card>> decks = new ArrayList<>(players);
        ArrayList<ArrayList<Card>> hands = new ArrayList<>(players);

        while (true) {
            System.out.println("Please enter the path of the pack file: ");
            String path = reader.next();

            try {
                pack = readPackFile("/Users/Prince/SoftwareDevCA/src/pack.txt", players);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        for (int i = 0; i < players; i++) {
            Deque<Card> deck = new LinkedList<>();
            ArrayList<Card> hand = new ArrayList<>();

            for (int j = 0; j < NUMBER_OF_CARDS_PER_HAND; j++) {
                hand.add(pack.pop());
                deck.push(pack.pop());
            }

            decks.add(deck);
            hands.add(hand);
        }

        Player p;

        for (int i = 0; i < players; i++) {
            p = new Player("Player " + Integer.toString(i+1), i, decks.get(i), decks.get((i+1)%players), hands.get(i), players);
            playersList.add(p);
        }

        for (Player q : playersList) {
            q.start();
        }

        for (int i = 0, i < decks.size(); i++) {
            FileWriter writer = new FileWriter(createDeckFile(i));
            PrintWriter printer = new PrintWriter(writer);
            
            printer.print(deck.get(i));
            printer.close();
            
        }


    }
}
