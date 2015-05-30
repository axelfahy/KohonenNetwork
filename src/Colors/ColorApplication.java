package Colors;

import javax.swing.*;

/**
 * Main file for the Color application.
 * This application create an window with squares of random colors.
 * Then, colors are changed by a Kohonen network.
 *
 * @author Axel Fahy
 *
 * @version 27.05.2015
 */
public class ColorApplication {

    public static void main(String[] args) {
        ColorScreen screen = new ColorScreen(400, 400, 5);
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(screen, "Center");
        f.setTitle("Color classification");
        f.setSize(400,400);
        f.setVisible(true);
    }
}
