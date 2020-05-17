import image.ImageManager;
import org.junit.Assert;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Objects;


public class ImageTest {

    @Test
    public void testImage() throws IOException {
        InputStream inputStream1 = new FileInputStream(new File("D:\\workspace\\Spring_framework\\design-mode\\src\\main\\java/images/tankD.gif"));
        URL resource = getClass().getClassLoader().getResource("images/tankD.gif");
        //InputStream inputStream = Objects.requireNonNull(ImageManager.class.getClassLoader().getResourceAsStream("classpath:images/tankD.gif"));
       // BufferedImage read = ImageIO.read();
        Assert.assertNotNull(resource);
    }
}
