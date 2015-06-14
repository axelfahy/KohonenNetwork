package Countries;

import javax.swing.*;

/**
 * Main file for the Country application.
 * This application creates a window with the name of each country from the input file (ISO3).
 * Then, Countries are regrouping with the Kohonen network.
 *
 * Each country is spread with his PIB and its evolution over the past 6 years (2008-2013).
 *
 * @author Axel Fahy, Rudolf Höhn
 * @version 14.06.2015
 */
public class CountryApplication {

    public static void main(String[] args) {

        final int h = 800;
        final int w = 800;

        CountryScreen screen = new CountryScreen(w, h, 20);
        JFrame f = new JFrame();
        f.setResizable(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(screen, "Center");
        f.setTitle("Country classification");
        f.setSize(w, h);
        f.setVisible(true);

        // Execution of SOM algorithm.
        screen.getNetwork().SOM();
    }


}
