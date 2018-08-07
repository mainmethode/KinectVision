package de.rwth.i5.kinectvision.robot;

import de.rwth.i5.kinectvision.machinevision.model.Face;
import de.rwth.i5.kinectvision.machinevision.model.Marker3d;
import de.rwth.i5.kinectvision.machinevision.model.PolygonMesh;
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
    public static RobotPart parseModelFile(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("Group");

        //If there is no group element
        if (nList.getLength() == 0) {
            log.error("No element \"Group\" found.");
            return null;
        }

        RobotPart arm = handleGroup(nList.item(0));
        if (arm == null) {
            log.error("Arm could not be generated");
            return null;
        }
        NodeList transformList = doc.getElementsByTagName("Transform");

        for (int i = 0; i < transformList.getLength(); i++) {
            handleAxisDummy(arm, transformList.item(i));
        }

        return arm;
    }

    /**
     * This method parses the 3d file for the robot base containing the
     * base bounding box, two dummies for the rotation and multiple dummies for markers
     *
     * @param file The file to be parsed
     * @return A robot model containing the base bounding box, the first rotation axis and the markers
     */
    public static RobotModel parseBaseFile(File file) throws IOException, SAXException, ParserConfigurationException {
        log.info("Parse base file");
        RobotModel res = new RobotModel();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("Group");

        //If there is no group element
        if (nList.getLength() == 0) {
            log.error("No element \"Group\" found.");
            return null;
        }

        RobotPart base = handleGroup(nList.item(0));
        if (base == null) {
            log.error("Base could not be generated");
            return null;
        }
        res.addRobotPart(base);
        NodeList transformList = doc.getElementsByTagName("Transform");

        for (int i = 0; i < transformList.getLength(); i++) {
            handleAxisDummy(base, transformList.item(i));
            handleMarkerDummy(res, transformList.item(i));
        }

        return res;
    }

    /**
     * Adds a base position to the robot model if there is a marker dummy in the node
     *
     * @param res  The robot model to change
     * @param node The node containing the information
     */

    private static void handleMarkerDummy(RobotModel res, Node node) {
        log.info("Handle marker dummy");
        //Get the object's name
        String name = node.getAttributes().getNamedItem("DEF").getNodeValue();
        String translation = node.getAttributes().getNamedItem("translation").getNodeValue();

        if (!name.startsWith("marker_")) {
            return;
        }

        String[] splitted = name.split("_");
        if (splitted.length < 2) {
            return;
        }
        int markerID;
        try {
            markerID = Integer.parseInt(splitted[1]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        res.addBasePoint(new Marker3d(markerID, parseTranslationString(translation)));
        log.info("Base point created: " + res.getBasePoints().get(res.getBasePoints().size() - 1));
    }

    /**
     * Handles the transformation containing empties.
     *
     * @param arm  The robot arm to set the axis for
     * @param node Node containing the transformation
     */
    private static void handleAxisDummy(RobotPart arm, Node node) {
        //Get the object's name
        String name = node.getAttributes().getNamedItem("DEF").getNodeValue();
        String translation = node.getAttributes().getNamedItem("translation").getNodeValue();

        switch (name) {
            case "link_start_1_TRANSFORM":
                arm.setAxis1Start(parseTranslationString(translation));
                break;
            case "link_start_2_TRANSFORM":
                arm.setAxis2Start(parseTranslationString(translation));
                break;
            case "link_end_1_TRANSFORM":
                arm.setAxis1End(parseTranslationString(translation));
                break;
            case "link_end_2_TRANSFORM":
                arm.setAxis2End(parseTranslationString(translation));
                break;
        }
    }

    /**
     * Parses a string containing coordinates and returns a new Vector
     *
     * @param translation The string to parse
     * @return The vector containing the coordinates
     */
    private static Vector3d parseTranslationString(String translation) {
        List<Double> values = Arrays.stream(translation.split(" ")).map(Double::parseDouble).collect(Collectors.toList());
        if (values.size() < 3) {
            log.error("Translation is invalid. Size < 3");
            return null;
        }
        Vector3d res = new Vector3d(values.get(0), values.get(1), values.get(2));
        return res;
    }

    /**
     * Method for parsing the groups containing the cubes
     *
     * @param node
     */
    private static RobotPart handleGroup(Node node) {
        log.debug("Handle group");

        //Get the cube name
        String name = node.getAttributes().getNamedItem("DEF").getNodeValue();

        Node shape = findItem("Shape", node.getChildNodes());
        if (shape == null) {
            log.error("No shape found in file.");
            return null;
        }

        Node indexSet = findItem("IndexedFaceSet", shape.getChildNodes());
        if (indexSet == null) {
            log.error("No IndexFaceSet found in file.");
            return null;
        }

        Node coordinate = findItem("Coordinate", indexSet.getChildNodes());
        if (coordinate == null) {
            log.error("No coordinate found in file.");
            return null;
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

        RobotPart arm = new RobotPart();
        PolygonMesh box = new PolygonMesh();
        box.setFaces(faces);
        arm.setBoundingBox(box);
        arm.setName(name);
        log.debug(arm.toString());
        return arm;
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