import java.io.File;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Stack;
import java.util.Scanner;

public class CardGame {

    private static Stack<Card> readPackFile(String path, int players) throws java.io.IOException, Exception {
        Scanner s = new Scanner(new File(path));
        ArrayList<Integer> values = new ArrayList<Integer>();
        Stack<Card> pack = new Stack<Card>();
        int a;

        while (s.hasNext()) {
            if (s.hasNextInt()) {
                a = s.nextInt();
                if (a >= 0) {
                    values.add(a);
                } else {
                    throw new Exception("Invalid values");
                }
            } else {
                throw new Exception("Invalid values");
            }
        }
        
        s.close();
        
        if (values.size()  == 8 * players) {
            for (Integer i: values) {
                pack.push(new Card(i));
                System.out.println(i);
            }
        } else {
            throw new Exception("Invalid size");
        }

        return pack;
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
                pack = readPackFile("/Users/Scott/Desktop/SoftwareDevCA/src/pack.txt", players);
                break;
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        for (int i = 0; i < players; i++) {
            Deque<Card> deck = new LinkedList<>();
            ArrayList<Card> hand = new ArrayList<>();

            for (int j = 0; j < 4; j++) {
                hand.add(pack.pop());
                deck.push(pack.pop());
            }

            decks.add(deck);
            hands.add(hand);
        }
        System.out.println(pack.size());
    }
}
