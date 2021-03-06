package screen;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author javier
 */
public class ScreenCapture {
    private static final String SHOTS_DIR = "shots";
    private static final int THUMB_WIDTH = 150;
    private static final int THUMB_HEIGHT = 100;
    private final BufferedImage img;
    private ScreenCapture(BufferedImage img){
        this.img = img;
    }
    public String[][] getRgbMap(){
        String [][] map = new String[THUMB_HEIGHT][THUMB_WIDTH];
        StringBuilder builder ;
        for(int row = 0; row < THUMB_HEIGHT;row++)
            for(int col = 0 ; col < THUMB_WIDTH ; col++){
                builder = new StringBuilder();
                int rgb = img.getRGB(col, row);
                int  red = (rgb & 0x00ff0000) >> 16;
                int  green = (rgb & 0x0000ff00) >> 8;
                int  blue = rgb & 0x000000ff;
                builder.append("rgb(").append(red).append(",").append(green).
                        append(",").append(blue).append(")");
                map[row][col] = builder.toString();
            }
        return map;
    }
    public String saveToFile(){
        String path = "sshot.jpg";
        try {
            // retrieve image
            BufferedImage bi = img;
            File outputfile = new File(path);
            ImageIO.write(bi, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            return null;
        }
        return path;
    }
    public static ScreenCapture takeScreenshot(){
        Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        BufferedImage capture = null;
        try {
            capture = new Robot().createScreenCapture(screenRect);
        } catch (AWTException ex) {
            ex.printStackTrace(System.err);
            return null;
        }
        Image thumb = capture.getScaledInstance(THUMB_WIDTH, THUMB_HEIGHT, BufferedImage.SCALE_SMOOTH);
        BufferedImage img = new BufferedImage(THUMB_WIDTH, THUMB_HEIGHT, BufferedImage.TYPE_INT_RGB);
        img.createGraphics().drawImage(thumb,0,0,null);
        return new ScreenCapture(img);
    }
}
