import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class AnagramDictionaryServiceImpl implements AnagramDictionaryService {
    private final Map<Long, Set<String>> data;
    private static final List<Integer> primes = generatePrimes();

    private final MetricService metricService;

    public AnagramDictionaryServiceImpl() {
        this.data = new HashMap<>();
        this.metricService = new MetricServiceImpl();
    }

    public AnagramDictionary createDictionary(String filePath, Properties props) throws AnagramDictionaryException {
        metricService.logStartTime();
        if (props == null) props = new Properties();
        try {
            loadDictionary(filePath);
        } catch (IOException e) {
            System.err.println("Error! Could not load Dictionary. Exiting...");
            throw new AnagramDictionaryException("could not load dictionary", e);
        }
        AnagramDictionary anagramDictionary = new AnagramDictionary();
        String id = UUID.randomUUID().toString();
        anagramDictionary.setId(id);
        anagramDictionary.setName(props.getOrDefault("dictionary.name", "dictionary-" + id).toString());
        metricService.logEndTime();
        metricService.printDuration("Dictionary loaded in");
        return anagramDictionary;
    }

    public void addToDictionary(String word) {
        long hash = getPrimeHash(word);
        internalAddWord(hash, word);
    }

    public Set<String> getAnagrams(String word) {
        return getAnagrams(getPrimeHash(word));
    }

    public Set<String> getAnagrams(long hash) {
        return data.get(hash);
    }

    private void loadDictionary(String dictFilePath) throws IOException {
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

    // adds the word to the set keyed with the hash.
    private void internalAddWord(long hash, String word) {
        if (data.containsKey(hash)) {
            data.get(hash).add(word);
        } else {
            Set<String> anagramSet = new HashSet<>();
            anagramSet.add(word);
            data.put(hash, anagramSet);
        }
    }

    // hashes the input word as a product of its prime mappings
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

    // generates first 26 primes
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
