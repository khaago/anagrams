import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;

public class ServiceTest {

    public static final String PATHNAME = "/Users/khaago/workspace/realtor-coding/dictionary.txt";

    public static void main(String[] args) throws IOException {
        ServiceTest serviceTest = new ServiceTest();
//        serviceTest.loadDictionary();
        List<String> anagrams = Service.getAnagrams("cab");
        assert anagrams.size() == 2;
        assert anagrams.containsAll(Arrays.asList("cab", "bac"));
    }

    void loadDictionary() throws IOException {
        File file = new File(PATHNAME);
        try (
                InputStream is = new BufferedInputStream(new FileInputStream(PATHNAME));
        ) {
            Files.copy(is, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            Service.loadDictionary(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;   // fail test
        }

    }

    void getAnagrams() {
    }

    void values() {
    }

    void valueOf() {
    }
}
