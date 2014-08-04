import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class SubsetTest {
    public static void main(String[] args) {
        try {
            Path filePath = new File(args[0]).toPath();
            Charset charset = Charset.defaultCharset();
            List<String> stringList = Files.readAllLines(filePath, charset);
            List<SubsetTestExecutionInput> executions = new ArrayList<SubsetTestExecutionInput>();

            for (String s : stringList) { executions.add(new SubsetTestExecutionInput(s.split(">"))); }

            for (SubsetTestExecutionInput ex : executions) {
                System.setIn( new ByteArrayInputStream(ex.inputData.getBytes()));
                Subset.main(new String[] { String.valueOf((int)ex.subsetDesired) });
            }
        } catch (IOException e) {
            assert false;
        }
    }
}