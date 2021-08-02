import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class QuickSolution {
    private static final List<Integer> primes = generatePrimes();
    private static final String EXIT_WORD = "exit";

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        if (args == null || args.length < 1) {
            throw new IllegalArgumentException("Dictionary.java file name missing! " +
                    "Provide absolute path of the dictionary file as the only argument.");
        }
        Map<Long, Set<String>> anagrams = new HashMap<>();
        try (InputStream fis = new BufferedInputStream(new FileInputStream(args[0]))) {
            int c;
            long product = 1;   // this will be the prime key
            StringBuilder currentWord = new StringBuilder();
            while ((c = fis.read()) != -1) {
                char ch = (char) c;
                if (ch == '\n') {
                    if (anagrams.containsKey(product)) {
                        anagrams.get(product).add(currentWord.toString());
                    } else {
                        Set<String> anagramSet = new HashSet<>();
                        anagramSet.add(currentWord.toString());
                        anagrams.put(product, anagramSet);
                    }
                    currentWord.setLength(0);
                    product = 1;
                    continue;
                }
                if (ch >= 'A' && ch <= 'Z') {
                    ch += 32;   // A = 65, a = 97
                }
                if (ch >= 'a' && ch <= 'z') {
                    currentWord.append(ch);
                    product *= primes.get(ch - 'a');
                }
            }
        }
        long duration = System.currentTimeMillis() - startTime;
        System.out.println("Dictionary loaded in " + duration + "ms.");
        while (true) {
            System.out.print("Anagrams>");
            String word = System.console().readLine();
            if (EXIT_WORD.equalsIgnoreCase(word)) return;
            startTime = System.currentTimeMillis();
            long hash = getPrimeHash(word);
            Set<String> anagramSet = anagrams.get(hash);
            duration = System.currentTimeMillis() - startTime;
            if (anagramSet != null && anagramSet.size() > 1) {
                System.out.println(anagramSet.size() + " Anagrams found for " + word + " in " + duration + "ms");
                System.out.println(String.join(",", anagrams.get(hash)));
            } else {
                System.out.println("No Anagrams found for " + word + " in " + duration + "ms");
            }
        }
    }

    private static List<Integer> generatePrimes() {
        boolean[] primes = new boolean[102];

        for (int i = 0; i < 102; i++)
            primes[i] = true;

        for (int p = 2; p * p < 102; p++) {
            if (primes[p]) {
                for (int i = p * p; i < 102; i += p)
                    primes[i] = false;
            }
        }
        List<Integer> primeNumbers = new ArrayList<>();
        for (int p = 2; p < 102; p++)
            if (primes[p]) {
                primeNumbers.add(p);
            }
        return primeNumbers;
    }

    private static long getPrimeHash(String input) {
        long hash = 1L;
        for (char ch : input.toCharArray()) {
            if (ch >= 'A' && ch <= 'Z') {
                ch += 32;
            }
            hash *= primes.get(ch - 'a');
        }
        return hash;
    }
}
