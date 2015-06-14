package Countries;

import javax.swing.*;

/**
 * Created by rudy on 14.06.15.
 */
public class CountryApplication {

    public static void main(String[] args) {

        final int h = 1000;
        final int w = 1000;

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
