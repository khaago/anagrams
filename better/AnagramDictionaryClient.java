import java.util.List;
import java.util.Properties;

public class AnagramDictionaryClient {
    private final static String EXIT_WORD = "exit";

    public static void main(String[] args) {
        AnagramDictionaryService service = new AnagramDictionaryServiceImpl();
        if (args == null || args.length < 1) {
            throw new IllegalArgumentException("Dictionary.java file name missing! " +
                    "Provide absolute path of the dictionary file as the only argument.");
        }
        Properties properties = new Properties();
        properties.put("name", args[0]);
        service.createDictionary(args[0], properties);
        MetricService metricService = new MetricServiceImpl();
        while (true) {
            System.out.print("Anagrams>");
            String word = System.console().readLine();
            if (EXIT_WORD.equalsIgnoreCase(word)) return;
            metricService.logStartTime();
            List<String> anagramList = service.getAnagrams(word);
            metricService.logEndTime();
            if (anagramList != null && anagramList.size() > 1) {
                metricService.printDuration(anagramList.size() + " Anagrams found for " + word + " in ");
                System.out.println(String.join(",", service.getAnagrams(word)));
            } else {
                metricService.printDuration("No Anagrams found for " + word + " in ");
            }
        }

    }
}
