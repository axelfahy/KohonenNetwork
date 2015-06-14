package Colors;

import javax.swing.*;

/**
 * Main file for the Color application.
 * This application creates a window with squares of random colors.
 * Then, colors are changed by a Kohonen network.
 *
 * @author Axel Fahy, Rudolf Höhn
 * @version 27.05.2015
 */
public class ColorApplication {

    public static void main(String[] args) {
        final int h = 800;
        final int w = 800;

        ColorScreen screen = new ColorScreen(w, h, 40);
        JFrame f = new JFrame();
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(screen, "Center");
        f.setTitle("Color classification");
        f.setSize(w, h);
        f.setVisible(true);
        // Execution of SOM algorithm.
        screen.getNetwork().SOM();
    }
}
