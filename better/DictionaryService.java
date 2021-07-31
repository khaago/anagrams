import java.io.FileOutputStream;
import java.util.Properties;

/**
 * Create or up
 */
public interface DictionaryService {
    boolean createDictionary(FileOutputStream stream, Properties dict);

    void addToDictionary(String word);

}
