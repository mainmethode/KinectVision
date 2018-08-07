package robot;

import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import de.rwth.i5.kinectvision.robot.ModelFileParser;
import de.rwth.i5.kinectvision.robot.RobotModel;
import de.rwth.i5.kinectvision.robot.RobotPart;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.vecmath.Vector3d;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ModelFileParserTest {
    @Test
    public void testFileParse() {
        try {
            RobotPart arm = ModelFileParser.parseModelFile(new File("C:\\Users\\Justin\\Desktop\\link.x3d"));
            System.out.println(arm.toString());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }

//        String coordIndex = "1 2 3 4 5 6 ";
//        List<Integer> coordinateIndexes = Arrays.stream(coordIndex.split(" ")).map(Integer::parseInt)
//                .collect(Collectors.toList());
    }

    @Test
    public void testBaseCreation() throws ParserConfigurationException, SAXException, IOException {
        RobotModel robotModel = ModelFileParser.parseBaseFile(new File(("C:\\Users\\Justin\\Desktop\\base.x3d")));
        assertNotNull(robotModel);
        assertNotNull(robotModel.getRobotParts());
        assertEquals(1, robotModel.getRobotParts().size());

        assertEquals(3, robotModel.getBasePoints().size());

        assertEquals(new Marker3d(0, new Vector3d(-1, -1, -1)), robotModel.getBasePoints().get(2));
        assertEquals(new Marker3d(1, new Vector3d(1, -1, -1)), robotModel.getBasePoints().get(1));
        assertEquals(new Marker3d(2, new Vector3d(-1, -1, 1)), robotModel.getBasePoints().get(0));
    }
}
