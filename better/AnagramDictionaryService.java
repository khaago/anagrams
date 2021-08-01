import java.util.List;
import java.util.Properties;

public interface AnagramDictionaryService {
    AnagramDictionary createDictionary(String filePath, Properties dict);

    void addToDictionary(String word);

    List<String> getAnagrams(String word);
    
    List<String> getAnagrams(long hash);
}
