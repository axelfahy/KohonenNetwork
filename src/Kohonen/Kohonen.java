package Kohonen;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class to implement a Kohonen network - Self-Organizing Maps (SOM).
 *
 * Vectors of weights should already have the same importance. (Pretreatment)
 *
 * TODO : Check formulas.
 *
 * @author Axel Fahy
 *
 * @version 30.05.2015
 */
public class Kohonen {
    // Number of iterations (should be at least 3 times the number of weights (not check))
    private int nbIterations;

    // Learning rate
    private final double learningRate = 0.4;    // TODO : Quelle valeur ?

    // Max : constante of waning.
    private final double max = 1; // TODO : Quelle valeur ?

    // Size of the network.
    private int width;
    private int height;

    // Network
    private Neuron[][] network;

    // Input of the Kohonen network. Weights will be compared with them.
    ArrayList<ArrayList<Integer>> input;    // TODO : Normaliser les vecteurs ? Le sont-ils déjà ?

    /**
     * Constructor
     *
     * @param input         The input vector. Datas will be transformed as these vectors.
     * @param nbIterations  Number of iterations.
     * @param w             Width of the network.
     * @param h             Height of the network.
     */
    public Kohonen(ArrayList<ArrayList<Integer>> input, int nbIterations, int w, int h) {
        this.nbIterations = nbIterations;
        this.width = w;
        this.height = h;
        this.network = new Neuron[w][h];
        this.input = input;
    }

    /**
     * Add a neuron into the network.
     *
     * @param weights   Array with the weights of the neuron.
     * @param i         i position.
     * @param j         j position.
     */
    public void addNeuron(ArrayList<Integer> weights, int i, int j) {
        this.network[i][j] = new Neuron(weights, i, j);
    }

    /**
     * Get a neuron from the network.
     *
     * @param i i position.
     * @param j j position.
     * @return  The neuron in position (i, j) of the network.
     */
    public Neuron getNeuron(int i, int j) {
        return this.network[i][j];
    }

    // Randomize the weights of neurons.
    public void randomizeNeurons() {

    }

    /**
     * Calculate the Euclidean distance.
     * $\sigma(vectorA_i - vectorB_i)^2$
     *
     * @param vectorA   The first vector.
     * @param vectorB   The second vector.
     * @return          The Euclidean distance.
     */
    public double euclideDistance(ArrayList<Integer> vectorA, ArrayList<Integer> vectorB) {
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
     * There could be more than one BMU, so each BMU is put into a list and then
     * one is pick randomly.
     *
     * @param input The input vector.
     * @return      The BMU (as a neuron).
     */
    public Neuron getBMU(ArrayList<Integer> input) {
        ArrayList<Neuron> list = new ArrayList<Neuron>();
        Neuron BMU = this.network[0][0];
        double distanceMin = this.euclideDistance(input, BMU.getWeights());
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                for (int k = 0; k < input.size(); k++) {
                    double distanceTmp = this.euclideDistance(input, this.network[i][j].getWeights());
                    if (distanceTmp < distanceMin) {
                        distanceMin = distanceTmp;
                        BMU = this.network[i][j];
                        list.add(BMU);
                    }
                }
            }
        }
        return list.get((int)(Math.random() * list.size()));
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
    public void updateNeighbors(Neuron BMU, ArrayList<Integer> input, int t) {
        // Calculation of the radius of influence.
        double radius = (this.width / 2) * Math.exp(-t / ((this.width / 2) / this.max));
        radius *= 0.6;  // TODO : Radius trop grand ?
        int iMin = (BMU.getY() - radius) < 0 ? 0 : (int)(BMU.getY() - radius);
        int iMax = (BMU.getY() + radius) > this.width ? this.width : (int)(BMU.getY() + radius);
        int jMin = (BMU.getX() - radius) < 0 ? 0 : (int)(BMU.getX() - radius);
        int jMax = (BMU.getX() + radius) > this.width ? this.width : (int)(BMU.getX() + radius);
        // Set the new weights of neurons in proximity
        for (int i = iMin; i < iMax; i++) {
            for (int j = jMin; j < jMax; j++) {
                // Run over weights
                for (int k = 0; k < BMU.getSizeWeights(); k++) {
                    Neuron n = this.network[i][j];
                    double Lt = this.learningRate * Math.exp(-t / (this.width / 2));
                    double dist = Math.exp(-(Math.pow(BMU.getWeightI(k) - n.getWeightI(k), 2) / (2 * Math.pow(radius, 2))));
                    double sumDist = 0.0;
                    for (int l = 0; l < BMU.getSizeWeights(); l++) {
                        sumDist += Math.pow(BMU.getWeightI(l) - n.getWeightI(l), 2);
                        //sumDist += BMU.getWeightI(l) - n.getWeightI(l);
                    }
                    //sumDist = Math.pow(sumDist, 2);
                    //double dist = Math.exp(-(sumDist / (2 * Math.pow(radius, 2))));
                    int value = (int)(n.getWeightI(k) + dist * Lt * (input.get(k) - n.getWeightI(k)));
                    //int value = (int) (n.getWeightI(k) + Math.exp(-(BMU.getWeightI(k) - n.getWeightI(k) / 2 * Math.pow(radius, 2))));
                    n.setWeightI(k, value);
                    if (value > 254)
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
     *  - Pick an input vector randomly.
     *  - Calculate the BMU of this input.
     *  - Update the BMU's neighbors.
     *
     */
    public void SOM() {

        for (int t = 0; t < this.nbIterations; t++) {
            // Pick an input vector randomly.
            // A input vector can't be picked twice in a row.
            ArrayList<Integer> oldVector = new ArrayList<Integer>(Arrays.asList(1, 1, 1));
            ArrayList<Integer> vector;
            do {
                int randomInput = (int)(Math.random() * this.input.size());
                vector = this.input.get(randomInput);
            } while (oldVector.equals(vector));

            // Calculation of BMU (Best Matching Unit)
            Neuron BMU = this.getBMU(vector);

            // Update the BMU's neighbors
            this.updateNeighbors(BMU, vector, t);
        }

    }
}
