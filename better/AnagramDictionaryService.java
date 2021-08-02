import java.util.Properties;
import java.util.Set;

public interface AnagramDictionaryService {
    AnagramDictionary createDictionary(String filePath, Properties dict) throws AnagramDictionaryException;

    void addToDictionary(String word);

    Set<String> getAnagrams(String word);

    Set<String> getAnagrams(long hash);
}
