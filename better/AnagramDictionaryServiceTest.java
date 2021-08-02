import java.io.IOException;
import java.util.Arrays;
import java.util.Set;

public class AnagramDictionaryServiceTest {

    public static void main(String[] args) throws IOException {
        AnagramDictionaryService service = new AnagramDictionaryServiceImpl();
        service.createDictionary(args[0], null);
        Set<String> anagrams = service.getAnagrams("cab");
        assert anagrams.size() == 2;
        assert anagrams.containsAll(Arrays.asList("cab", "bac"));
    }
}
