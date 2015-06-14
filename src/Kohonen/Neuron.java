package Kohonen;

import java.util.ArrayList;

/**
 * Class to represent a Neuron.
 *
 * A neuron has weights (vector) and a position (x, y).
 *
 * @author Axel Fahy, Rudolf Höhn
 * @version 30.05.2015
 */
public class Neuron {
    // Array with the weights.
    private ArrayList<Double> weights;
    private int x;
    private int y;

    /**
     * Construtor with a list of weights.
     *
     * @param w ArrayList containing the weights.
     */
    public Neuron(ArrayList<Double> w) {
        this.weights = w;
    }

    /**
     * Constructor with a list of weights and position.
     *
     * @param w ArrayList containing the weights.
     * @param x x position.
     * @param y y position.
     */
    public Neuron(ArrayList<Double> w, int x, int y) {
        this.weights = w;
        this.x = x;
        this.y = y;
    }

    public ArrayList<Double> getWeights() {
        return weights;
    }

    /**
     * Get the weight of position i in the list of weights.
     *
     * @param i Position of the weight to get.
     * @return  The weight.
     */
    public Double getWeightI(int i) {
        return this.weights.get(i);
    }

    /**
     * Get x position of neuron.
     *
     * @return The x position.
     */
    public int getX() {
        return x;
    }

    /**
     * Get y position of neuron.
     *
     * @return The y position.
     */
    public int getY() {
        return y;
    }

    /**
     * Set all the weights of the neuron with the given array.
     *
     * @param weights   The new weights in a ArrayList.
     */
    public void setWeights(ArrayList<Double> weights) {
        this.weights = weights;
    }

    /**
     * Set a weight at position i of neuron's weights.
     *
     * @param i     The position in the weights array.
     * @param value The new value.
     */
    public void setWeightI(int i, double value) {
        this.weights.set(i, value);
    }

    /**
     * Set x position of neuron.
     *
     * @param x The new x position.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Set y position of neuron.
     *
     * @param y The new y position.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Get the number of weight.
     *
     * @return The ArrayList size.
     */
    public int getSizeWeights() {
        return this.weights.size();
    }

}
