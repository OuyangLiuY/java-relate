package tank.util.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class ImageManager {
    public static BufferedImage tankL,tankR,tankU,tankD;
    public static BufferedImage bulletL,bulletR,bulletU,bulletD;
    static {
        try {
            tankL = ImageIO.read(Objects.requireNonNull(ImageManager.class.getClassLoader().getResource("images/tankL.gif")));
            tankR = ImageIO.read(Objects.requireNonNull(ImageManager.class.getClassLoader().getResource("images/tankR.gif")));
            tankU = ImageIO.read(Objects.requireNonNull(ImageManager.class.getClassLoader().getResource("images/tankU.gif")));
            tankD = ImageIO.read(Objects.requireNonNull(ImageManager.class.getClassLoader().getResource("images/tankD.gif")));

            bulletL = ImageIO.read(Objects.requireNonNull(ImageManager.class.getClassLoader().getResource("images/bulletL.gif")));
            bulletR = ImageIO.read(Objects.requireNonNull(ImageManager.class.getClassLoader().getResource("images/bulletR.gif")));
            bulletU = ImageIO.read(Objects.requireNonNull(ImageManager.class.getClassLoader().getResource("images/bulletU.gif")));
            bulletD = ImageIO.read(Objects.requireNonNull(ImageManager.class.getClassLoader().getResource("images/bulletD.gif")));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
