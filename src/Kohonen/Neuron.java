package Kohonen;

import java.util.ArrayList;

/**
 * Class to represent a Neuron.
 *
 * A neuron has weights (vector) and a position (x, y).
 *
 * @author Axel Fahy
 *
 * @version 30.05.2015
 */
public class Neuron {
    // Array with the weights.
    private ArrayList<Integer> weights;
    private int x;
    private int y;

    /**
     * Construtor with a list of weights.
     *
     * @param w ArrayList containing the weights.
     */
    public Neuron(ArrayList<Integer> w) {
        this.weights = w;
    }

    /**
     * Constructor with a list of weights and position.
     *
     * @param w ArrayList containing the weights.
     * @param x x position.
     * @param y y position.
     */
    public Neuron(ArrayList<Integer> w, int x, int y) {
        this.weights = w;
        this.x = x;
        this.y = y;
    }

    public ArrayList<Integer> getWeights() {
        return weights;
    }

    /**
     * Get the weight of position i in the list of weights.
     *
     * @param i Position of the weight to get.
     * @return  The weight.
     */
    public Integer getWeightI(int i) {
        return this.weights.get(i);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setWeights(ArrayList<Integer> weights) {
        this.weights = weights;
    }

    public void setWeightI(int i, int value) {
        this.weights.set(i, value);
    }

    public void setX(int x) {
        this.x = x;
    }

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
