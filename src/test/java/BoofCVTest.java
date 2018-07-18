import boofcv.io.image.UtilImageIO;
import org.junit.Test;

import java.awt.image.BufferedImage;

/**
 * Class for testing BoofCV related stuff. It is intended to be a playground.
 */
public class BoofCVTest {
    @Test
    public void boofcvInitTest() {
        // load the lens distortion parameters and the input image
        BufferedImage input = UtilImageIO.loadImage(getClass().getResource("infrared_1_fiducial_chess.png").getFile());
//        FiducialFinder.findFiducials(input);

    }
}