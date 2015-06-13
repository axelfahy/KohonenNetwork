package Colors;

import Kohonen.*;
import Utilities.ParserDat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * Class to create the windows with the colors.
 *
 * TODO : Print two windows ? Before and after ? or is it possible to show the evolution ?
 *
 * @author Axel Fahy
 *
 * @version 27.05.2015
 */
public class ColorScreen extends JPanel {

    private int width;
    private int height;
    private int squareSize;
    private Kohonen network;
    private ArrayList<ArrayList<Double>> eightColors;

    /**
     * Constructor of the class.
     *
     * The window is initialized and create a Kohonen network with random colors.
     * Then, the SOM algorithm is applied and the window is shown.
     *
     * @param w     Window's width.
     * @param h     Window's height.
     * @param size  Window's size.
     */
    public ColorScreen(int w, int h, int size) {
        this.height = h;
        this.width = w;
        this.squareSize = size;
        /*
        // Set the colors (entries of the SOM)
        eightColors = new ArrayList<ArrayList<Double>>();
        eightColors.add(new ArrayList<Double>(Arrays.asList(0.0, 0.0, 0.0)));        // Black
        eightColors.add(new ArrayList<Double>(Arrays.asList(1.0, 0.0, 0.0)));      // Red
        eightColors.add(new ArrayList<Double>(Arrays.asList(0.0, 1.0, 0.0)));      // Green
        eightColors.add(new ArrayList<Double>(Arrays.asList(0.0, 0.0, 1.0)));      // Blue
        eightColors.add(new ArrayList<Double>(Arrays.asList(1.0, 1.0, 0.0)));    // Yellow
        eightColors.add(new ArrayList<Double>(Arrays.asList(0.0, 1.0, 1.0)));    // Light Blue
        eightColors.add(new ArrayList<Double>(Arrays.asList(1.0, 0.0, 1.0)));    // Pink
        eightColors.add(new ArrayList<Double>(Arrays.asList(1.0, 1.0, 1.0)));  // White
        */
        this.network = new Kohonen("colors.dat", w / size, h / size);
        this.init();
        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setVisible(true);
        this.network.SOM();
    }

    /**
     * Initialize the neurons network with random values.
     */
    public void init() {
        for (int i = 0; i < this.height / this.squareSize; i++) {
            for (int j = 0; j < this.width / this.squareSize; j++) {
                // Set random colors
                double red = Math.random();
                double green = Math.random();
                double blue = Math.random();
                // Create the neuron and add it to the network.
                this.network.addNeuron(new ArrayList<Double>(Arrays.asList(red, green, blue)), j, i);
            }
        }
    }

    /**
     * Draw the rectangles on the window.
     *
     * @param g Graphics element.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //for (int i = 0; i < height; i++) {
        //    for (int j = 0; j < width; j++) {
        //        // Set random colors
        //        int red = (int)(Math.random() * 255);
        //        int green = (int)(Math.random() * 255);
        //        int blue = (int)(Math.random() * 255);
        //        // Create the neuron and add it to the network.
        //        this.network.addNeuron(new ArrayList<Integer>(Arrays.asList(red, green, blue)), i, j);
        //        g.setColor(new Color(red, green, blue));
        //        // Fill a rectangle with the color
        //        g.fillRect(j * this.squareSize, i * this.squareSize, this.squareSize, this.squareSize);
        //    }
        //}

        for (int i = 0; i < this.height / this.squareSize; i++) {
            for (int j = 0; j < this.width / this.squareSize; j++) {
                Neuron n = this.network.getNeuron(i, j);
                ArrayList<Double> colors = n.getWeights();
                //g.setColor(new Color(n.getWeightI(0), n.getWeightI(1), n.getWeightI(2)));
                g.setColor(new Color((int)(colors.get(0)*255), (int)(colors.get(1)*255), (int)(colors.get(2)*255)));
                // Fill a rectangle with the color
                g.fillRect(j * this.squareSize, i * this.squareSize, this.squareSize, this.squareSize);
            }
        }

    }
}
