package Countries;

import Kohonen.*;
import Utilities.ParserDat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to create the windows with the countries.
 *
 * @author Axel Fahy, Rudolf Höhn
 * @version 14.06.2015
 */
public class CountryScreen extends JPanel {

    private int width;
    private int height;
    private int squareSize;
    private Kohonen network;
    private String filename = "countries.dat";

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
    public CountryScreen(int w, int h, int size) {
        this.height = h;
        this.width = w;
        this.squareSize = size;

        this.network = new Kohonen(this.filename, (w / size) * 3, (h / size) * 3, this);
        this.init();
        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setVisible(true);
    }

    /**
     * Getter for the Kohonen network.
     *
     * @return The Kohonen network.
     */
    public Kohonen getNetwork() {
        return this.network;
    }

    /**
     * Initialize the neurons network with random values.
     */
    public void init() {
        for (int i = 0; i < (this.height / this.squareSize) * 3; i++) {
            for (int j = 0; j < (this.width / this.squareSize) * 3; j++) {

                ArrayList<Double> newNeuron = new ArrayList<>();

                // Create the neuron and add it to the network
                for (int k = 0; k < this.network.getInput().get(k).size(); k++) {
                    newNeuron.add(Math.random());
                }
                this.network.addNeuron(newNeuron, j, i);
            }
        }
    }

    /**
     * Create an HashMap containing a neuron and each countries associate with it.
     *
     * @return The HashMap.
     */
    public HashMap<Neuron, ArrayList<String>> getNeuronSet() {
        // Get HashMap containing CountryName / Vector with values.
        HashMap<String, ArrayList<Double>> countrySet = ParserDat.datStrToHashMap(this.filename);
        // Create an HashMap with Neuron and countries.
        HashMap<Neuron, ArrayList<String>> neuronSet = new HashMap<>();

        for (Map.Entry<String, ArrayList<Double>> entry : countrySet.entrySet()) {
            // Get the best neuron.
            Neuron BMU = this.network.getBMU(entry.getValue());
            // Add the neuron if doesn't exist.
            if (!neuronSet.containsKey(BMU)) {
                neuronSet.put(BMU, new ArrayList<String>());
            }
            // Add the country.
            neuronSet.get(BMU).add(entry.getKey());
        }
        return neuronSet;
    }

    /**
     * Draw the rectangles on the window.
     *
     * @param g Graphics element.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        HashMap<Neuron, ArrayList<String>> neuronSet = getNeuronSet();
        // Run over neuron to print their country on the map.
        for (Map.Entry<Neuron, ArrayList<String>> entry : neuronSet.entrySet()) {
            for (int i = 0; i < entry.getValue().size(); i++) {
                int x = (entry.getKey().getX() * this.width) / this.network.getWidth();
                int y = (entry.getKey().getY() * this.height) / this.network.getHeight();
                g.drawString(entry.getValue().get(i), x, y - (i * 10));
            }
        }

        //for (int i = 0; i < this.height / this.squareSize; i++) {
        //    for (int j = 0; j < this.width / this.squareSize; j++) {
        //        Neuron n = this.network.getNeuron(i, j);
        //        ArrayList<Double> colors = n.getWeights();
        //        //g.setColor(new Color(n.getWeightI(0), n.getWeightI(1), n.getWeightI(2)));
        //        g.setColor(new Color((int) (colors.get(0) * 255), (int) (colors.get(1) * 255), (int) (colors.get(2) * 255)));
        //        // Fill a rectangle with the color
        //        g.fillRect(j * this.squareSize, i * this.squareSize, this.squareSize, this.squareSize);
        //    }
        //}

    }

}
