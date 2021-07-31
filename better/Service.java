import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The service is considered as an enum in order to easily enforce it's singleton-ness.
 * A dictionary file must be loaded with the loadDictionary(String path)
 */
public enum Service {
    INSTANCE;
    // Internal database of the anagrams: key = prime hash of words, value = list of words that have the same prime hash
    private static final ConcurrentHashMap<Long, List<String>> data = new ConcurrentHashMap<>();
    private static final List<Integer> primes = generatePrimes();


    private static void internalAddWord(long hash, String word) {
        if (data.containsKey(hash)) {
            data.get(hash).add(word);
        } else {
            List<String> list = new LinkedList<>();
            list.add(word);
            data.put(hash, Collections.synchronizedList(list));
        }
    }

    public static void loadDictionary(String dictFilePath) throws IOException {
        try (InputStream is = new BufferedInputStream(new FileInputStream(dictFilePath))) {
            int c;
            long product = 1;   // this will be the prime key
            StringBuilder currentWord = new StringBuilder();

            // design choice note: using the pain old buffered input stream as opposed to scanner in order to
            // compute the hash, as well as take care of upper and lower case
            while ((c = is.read()) != -1) {
                char ch = (char) c;
                if (ch == '\n') {
                    internalAddWord(product, currentWord.toString());
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
    }

    public static List<String> getAnagrams(String word) {
        return data.get(getPrimeHash(word));
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

}
