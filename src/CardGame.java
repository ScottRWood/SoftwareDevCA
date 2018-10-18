import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CardGame {

    private static ArrayList<Card> readPackFile(String path, int players) throws java.io.IOException, Exception {
        Scanner s = new Scanner(new File(path));
        ArrayList<Integer> values = new ArrayList<Integer>();
        ArrayList<Card> pack = new ArrayList<Card>();
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
        
        if (values.size() % 8 == 0) {
            for (Integer i: values) {
                pack.add(new Card(i));
                System.out.println(i);
            }
        } else {
            throw new Exception("Invalid size");
        }

        return pack;
    }

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);
        ArrayList<Card> pack = new ArrayList<Card>();

        System.out.println("Please enter the number of player: ");
        int players = reader.nextInt();

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

        System.out.println(pack.size());
    }
}
