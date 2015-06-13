package Kohonen;

import Utilities.ParserDat;

import java.util.ArrayList;

/**
 * Class to implement a Kohonen network - Self-Organizing Maps (SOM).
 * <p/>
 * Vectors of weights should already have the same importance. (Pretreatment)
 * <p/>
 * TODO : Check formulas.
 *
 * @author Axel Fahy
 * @version 30.05.2015
 */
public class Kohonen {
    // Number of iterations (should be at least 3 times the number of weights (not check))
    private int nbIterations;

    // Learning rate
    private final double learningRate = 1;    // TODO : Quelle valeur ? A partir de 1, erreur

    // Size of the network.
    private int width;
    private int height;

    // Network
    private Neuron[][] network;

    // Input of the Kohonen network. Weights will be compared with them.
    ArrayList<ArrayList<Double>> input;


    /**
     * Constructor
     *
     * @param filename The input file. Datas will be transformed as these vectors.
     * @param w        Width of the network.
     * @param h        Height of the network.
     */
    public Kohonen(String filename, int w, int h) {
        this.width = w;
        this.height = h;
        this.network = new Neuron[w][h];
        this.input = ParserDat.DatStrToArrayList(filename);
        //this.nbIterations = input.size() * 3;
        this.nbIterations = 2000;
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
     * <p/>
     * The BMU (Best Matching Unit) is the neuron that has the smallest distance from the input vector.
     * There could be more than one BMU, so each BMU is put into a list and then
     * one is pick randomly.
     *
     * @param input The input vector.
     * @return The BMU (as a neuron).
     */
    public Neuron getBMU(ArrayList<Double> input) {
        ArrayList<Neuron> list = new ArrayList<Neuron>();
        Neuron BMU = null;
        double distanceMin = Double.MAX_VALUE;
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                double distanceTmp = this.euclideDistance(input, this.network[i][j].getWeights());
                if (distanceTmp < distanceMin) {
                    distanceMin = distanceTmp;
                    BMU = this.network[i][j];
                    //list.add(BMU);

                }
            }
        }
        //return list.get((int) (Math.random() * list.size()));
        return BMU;
    }

    /**
     * Update the values of the BMU's neighbors.
     * <p/>
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
     * <p/>
     * Weights of network has to already be randomize.
     * <p/>
     * Until 'nbIterations' is reached :
     * <p/>
     * - Pick an input vector randomly.
     * - Calculate the BMU of this input.
     * - Update the BMU's neighbors.
     */
    public void SOM() {
        // Pick an input vector randomly.
        // A input vector can't be picked twice in a row.
        ArrayList<Double> oldVector = new ArrayList<Double>();
        ArrayList<Double> vector;

        for (int t = 0; t < this.nbIterations; t++) {

            // Choose another entry vector than the previous one.
            do {
                int randomInput = (int) (Math.random() * this.input.size());
                vector = this.input.get(randomInput);
            } while (oldVector.equals(vector));

            //vector = this.input.get(t % 100);
            //System.out.println(vector.toString());

            // The old vector is replaced by the new one.
            //oldVector = new ArrayList<>(vector);

            // Calculation of BMU (Best Matching Unit)
            Neuron BMU = this.getBMU(vector);

            // Update the BMU's neighbors
            this.updateNeighbors(BMU, vector, t);
        }
        System.out.println("done");

    }
}
