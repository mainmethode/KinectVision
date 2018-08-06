package robot;

import de.rwth.i5.kinectvision.robot.ModelFileParser;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class ModelFileParserTest {
    @Test
    public void testFileParse() {
        try {
            ModelFileParser.parseModelFile(new File("C:\\Users\\Justin\\Desktop\\sonnebrudi.x3d"));
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
//        String coordIndex = "1 2 3 4 5 6 ";
//        List<Integer> coordinateIndexes = Arrays.stream(coordIndex.split(" ")).map(Integer::parseInt)
//                .collect(Collectors.toList());
    }
}
