import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AnagramDictionaryServiceImpl implements AnagramDictionaryService {
    private static final ConcurrentHashMap<Long, List<String>> data = new ConcurrentHashMap<>();
    private static final List<Integer> primes = generatePrimes();

    private final MetricService metricService;

    public AnagramDictionaryServiceImpl() {
        this.metricService = new MetricServiceImpl();
    }

    public AnagramDictionary createDictionary(String filePath, Properties dict) {
        metricService.logStartTime();
        try {
            loadDictionary(filePath);
        } catch (IOException e) {
            e.printStackTrace(); //TODO
        }
        AnagramDictionary anagramDictionary = new AnagramDictionary();
        String id = UUID.randomUUID().toString();
        anagramDictionary.setId(id);
        anagramDictionary.setName(dict.getOrDefault("dictionary.name", "dictionary-" + id).toString());
        metricService.logEndTime();
        metricService.printDuration("Dictionary loaded in");
        return anagramDictionary;
    }

    public void addToDictionary(String word) {
        long hash = getPrimeHash(word);
        internalAddWord(hash, word);
    }

    public List<String> getAnagrams(String word) {
        return getAnagrams(getPrimeHash(word));
    }

    public List<String> getAnagrams(long hash) {
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

    private synchronized void internalAddWord(long hash, String word) {
        if (data.containsKey(hash)) {
            data.get(hash).add(word);
        } else {
            List<String> list = new LinkedList<>();
            list.add(word);
            data.put(hash, Collections.synchronizedList(list));
        }
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
