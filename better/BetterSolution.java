import java.io.IOException;
import java.util.List;

public class BetterSolution {
    private static final String EXIT_WORD = "exit";

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        if (args == null || args.length < 1) {
            throw new IllegalArgumentException("Dictionary.java file name missing! " +
                    "Provide absolute path of the dictionary file as the only argument.");
        }
        Service.loadDictionary(args[0]);
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Dictionary.java loaded in " + duration + "ms.");
        while (true) {
            System.out.print("Anagrams>");
            String word = System.console().readLine();
            if (EXIT_WORD.equalsIgnoreCase(word)) return;
            startTime = System.currentTimeMillis();
            List<String> anagramList = Service.getAnagrams(word);
            duration = System.currentTimeMillis() - startTime;
            if (anagramList != null && anagramList.size() > 1) {
                System.out.println(anagramList.size() + " Anagrams found for " + word + " in " + duration + "ms");
                System.out.println(String.join(",", Service.getAnagrams(word)));
            } else {
                System.out.println("No Anagrams found for " + word + " in " + duration + "ms");
            }
        }
    }
}
