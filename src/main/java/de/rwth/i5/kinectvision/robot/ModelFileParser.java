package de.rwth.i5.kinectvision.robot;

import de.rwth.i5.kinectvision.machinevision.model.Face;
import lombok.extern.slf4j.Slf4j;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.vecmath.Vector3d;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class for parsing a file containing the 3d bounding box model for the robot
 */
@Slf4j
public class ModelFileParser {
    /**
     * Parses the given model file and creates a robot model containing the bounding boxes
     *
     * @param file The file to be parsed
     * @return The generated model file
     */
    public static RobotModel parseModelFile(File file) throws ParserConfigurationException, IOException, SAXException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("Group");
        for (int i = 0; i < nList.getLength(); i++) {
            System.out.println(nList.item(i));
//            handleCoordinates(nList.item(i));
            handleGroup(nList.item(i));
        }
        System.out.println("----------------------------");
        return null;
    }

    /**
     * Method for parsing the groups containing the cubes
     *
     * @param node
     */
    private static void handleGroup(Node node) {
        log.debug("Handle group");

        //Get the cube name
        String name = node.getAttributes().getNamedItem("DEF").getNodeValue();

        Node shape = findItem("Shape", node.getChildNodes());
        if (shape == null) {
            log.error("No shape found in file.");
            return;
        }

        Node indexSet = findItem("IndexedFaceSet", shape.getChildNodes());
        if (indexSet == null) {
            log.error("No IndexFaceSet found in file.");
            return;
        }

        Node coordinate = findItem("Coordinate", indexSet.getChildNodes());
        if (coordinate == null) {
            log.error("No coordinate found in file.");
            return;
        }
        //Get the coordinate indices for the faces
        String coordIndex = indexSet.getAttributes().getNamedItem("coordIndex").getNodeValue();
        List<Integer> coordinateIndexes = Arrays.stream(coordIndex.split(" "))
                .map(Integer::parseInt).filter(integer -> integer != -1).collect(Collectors.toList());
        //Get the coordinates
        String coords = coordinate.getAttributes().getNamedItem("point").getNodeValue();
        List<Double> coordinates = Arrays.stream(coords.split(" "))
                .map(Double::parseDouble).collect(Collectors.toList());

        //Create vectors
        List<Vector3d> vector3ds = generateVectors(coordinates);
        //Create faces
        ArrayList<Face> faces = generateFaces(vector3ds, coordinateIndexes);

        RobotArm arm = new RobotArm();
        arm.getBoundingBox().setFaces(faces);
        arm.setName(name);
        System.out.println(faces);
    }


    private static Node findItem(String name, NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            if (nodeList.item(i).getNodeName().equals(name)) {
                return nodeList.item(i);
            }
        }
        return null;
    }

    private static ArrayList<Face> generateFaces(List<Vector3d> points, List<Integer> indices) {
        if (indices.size() % 4 != 0) {
            log.error("Size of list for face generation wrong");
            return null;
        }
        ArrayList<Face> res = new ArrayList<>();
        for (int i = 0; i < indices.size(); i += 4) {
            Face face = new Face(new Vector3d(points.get(indices.get(i))), new Vector3d(points.get(indices.get(i + 1))), new Vector3d(points.get(indices.get(i + 2))), new Vector3d(points.get(indices.get(i + 3))));
            res.add(face);
        }
        return res;
    }

    private static ArrayList<Vector3d> generateVectors(List<Double> points) {
        if (points.size() % 3 != 0) {
            log.error("Size of point list is wrong");
            return null;
        }
        ArrayList<Vector3d> res = new ArrayList<>();
        for (int i = 0; i < points.size(); i += 3) {
            res.add(new Vector3d(points.get(i), points.get(i + 1), points.get(i + 2)));
        }
        return res;
    }

    private static void parseTransform(Node nodeList) {
        //TODO Parse emptys here

    }
}