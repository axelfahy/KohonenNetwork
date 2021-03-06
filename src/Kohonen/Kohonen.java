package Kohonen;

import Utilities.ParserDat;

import javax.swing.*;
import java.util.ArrayList;

/**
 * Class to implement a Kohonen network - Self-Organizing Maps (SOM).
 *
 * Vectors of weights should already have the same importance. (Pretreatment)
 *
 * @author Axel Fahy, Rudolf Höhn
 * @version 30.05.2015
 */
public class Kohonen {
    // Number of iterations (should be at least 3 times the number of weights (not check))
    private int nbIterations;

    // Learning rate
    private final double learningRate = 1;

    // Size of the network.
    private int width;
    private int height;

    // Network
    private Neuron[][] network;

    // Input of the Kohonen network. Weights will be compared with them.
    ArrayList<ArrayList<Double>> input;

    // JPanel for update of screen.
    JPanel screen;

    /**
     * Constructor
     *
     * @param filename The input file. Datas will be transformed as these vectors.
     * @param w        Width of the network.
     * @param h        Height of the network.
     */
    public Kohonen(String filename, int w, int h, JPanel screen) {
        this.width = w;
        this.height = h;
        this.network = new Neuron[w][h];
        this.input = ParserDat.datStrToArrayList(filename);
        this.nbIterations = 2000;
        this.screen = screen;
    }

    /**
     * Add a neuron into the network.
     *
     * @param weights Array with the weights of the neuron.
     * @param i       i position.
     * @param j       j position.
     */
    public void addNeuron(ArrayList<Double> weights, int i, int j) {
        this.network[i][j] = new Neuron(weights, i, j);
    }

    /**
     * Get a neuron from the network.
     *
     * @param i i position.
     * @param j j position.
     * @return The neuron in position (i, j) of the network.
     */
    public Neuron getNeuron(int i, int j) {
        return this.network[i][j];
    }

    /**
     * Get the width of the network.
     *
     * @return The width.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the network.
     *
     * @return The height.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the input array.
     *
     * @return The input.
     */
    public ArrayList<ArrayList<Double>> getInput() {
        return input;
    }

    /**
     * Calculate the Euclidean distance.
     * $\sigma(vectorA_i - vectorB_i)^2$
     *
     * @param vectorA The first vector.
     * @param vectorB The second vector.
     * @return The Euclidean distance.
     */
    public double euclideDistance(ArrayList<Double> vectorA, ArrayList<Double> vectorB) {
        double distance = 0.0;
        for (int i = 0; i < vectorA.size(); i++) {
            distance += Math.pow(vectorA.get(i) - vectorB.get(i), 2);
        }
        return Math.sqrt(distance);
    }

    /**
     * Return the BMU of the network with a input vector.
     *
     * The BMU (Best Matching Unit) is the neuron that has the smallest distance from the input vector.
     *
     * @param input The input vector.
     * @return The BMU (as a neuron).
     */
    public Neuron getBMU(ArrayList<Double> input) {
        Neuron BMU = null;
        double distanceMin = Double.MAX_VALUE;
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                double distanceTmp = this.euclideDistance(input, this.network[i][j].getWeights());
                if (distanceTmp < distanceMin) {
                    distanceMin = distanceTmp;
                    BMU = this.network[i][j];
                }
            }
        }
        return BMU;
    }

    /**
     * Update the values of the BMU's neighbors.
     *
     * First, calculate the radius of influence.
     * Then, in this radius, values neuron will be modified.
     *
     * @param BMU   The Best Matching Unit (neuron).
     * @param input The input vector.
     * @param t     Value of the current iteration.
     */
    public void updateNeighbors(Neuron BMU, ArrayList<Double> input, int t) {
        double radiusMap = this.width / 2;
        double lambda = this.nbIterations / radiusMap;

        // Calculation of the radius of influence.
        double radius = radiusMap * Math.exp(-t / lambda);

        int iMin = (BMU.getY() - radius) < 0 ? 0 : (int) (BMU.getY() - radius);
        int iMax = (BMU.getY() + radius) > this.width ? this.width : (int) (BMU.getY() + radius);
        int jMin = (BMU.getX() - radius) < 0 ? 0 : (int) (BMU.getX() - radius);
        int jMax = (BMU.getX() + radius) > this.width ? this.width : (int) (BMU.getX() + radius);

        // Set the new weights of neurons in proximity
        for (int i = iMin; i < iMax; i++) {
            for (int j = jMin; j < jMax; j++) {
                // Run over weights
                Neuron n = this.network[i][j];
                double Lt = this.learningRate * Math.exp(-t / lambda);

                double sumDist = this.euclideDistance(BMU.getWeights(), n.getWeights());

                double delta = sumDist / (2 * radius * radius);
                //double theta = Math.exp(-1 * (sumDist / (2 * Math.pow(radius, 2))));
                double theta = Math.exp(-delta);

                for (int k = 0; k < BMU.getSizeWeights(); k++) {

                    double value = n.getWeightI(k) + theta * Lt * (input.get(k) - n.getWeightI(k));

                    n.setWeightI(k, value);
                    if (value > 1.0)
                        System.out.println("error");
                }
            }
        }
    }

    /**
     * SOM (Self-Organizing Map) algorithm.
     *
     * Weights of network has to already be randomize.
     *
     * Until 'nbIterations' is reached :
     *
     * - Pick an input vector randomly.
     * - Calculate the BMU of this input.
     * - Update the BMU's neighbors.
     */
    public void SOM() {
        // Pick an input vector randomly.
        // A input vector can't be picked twice in a row.
        ArrayList<Double> oldVector = new ArrayList<>();
        ArrayList<Double> vector;

        for (int t = 0; t < this.nbIterations; t++) {

            // Choose another entry vector than the previous one.
            do {
                int randomInput = (int) (Math.random() * this.input.size());
                vector = this.input.get(randomInput);
            } while (oldVector.equals(vector));

            // The old vector is replaced by the new one.
            oldVector = new ArrayList<>(vector);

            // Calculation of BMU (Best Matching Unit).
            Neuron BMU = this.getBMU(vector);

            // Update the BMU's neighbors.
            this.updateNeighbors(BMU, vector, t);

            // Update the screen.
            this.screen.repaint();
            // Sleep for the animation.
            try {
                Thread.sleep(10);
            }
            catch (InterruptedException e) {
                System.out.println("Erreur sleep : " + e);
            }
        }
    }
}
