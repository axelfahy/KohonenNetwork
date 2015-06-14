package Utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *  Parser for the input file.
 *  Files contain input vectors which will be added to the network.
 *
 * @author Axel Fahy, Rudolf Höhn
 * @version 14.06.2015
 */
public class ParserDat {

    /**
     * Read a file and put it in a StringBuffer.
     * Since this is a static method, it can be used without a class instance.
     *
     * @param filename  The filename to load.
     * @return          The String.
     */
    public static String read(String filename) {
        // Load the file into a String.
        String text = "";
        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String line = br.readLine();

            while (line != null) {
                text += line;
                text += "\n";
                line = br.readLine();
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }

    /**
     * Parse a .dat file into a list that contains the vectors and their values
     *
     * @param filename  The filename to load.
     * @return          The list of vectors.
     */
    public static ArrayList<ArrayList<Double>> datStrToArrayList (String filename) {
        String inputStr = read(filename);
        ArrayList<ArrayList<Double>> input = new ArrayList<>();

        String[] separateInput = inputStr.split("\n");
        String[] vectors;
        String line;
        String vector;
        int index = 0;

        // Going through vectors
        for (int j = 0; j < separateInput.length; j++) {
            line = separateInput[j];

            // Except the header
            if (j > 3) {
                input.add(new ArrayList<Double>());
                vectors = line.split("\t");

                // Going through the values of a vector
                for (int i = 1; i < vectors.length; i++) {
                    vector = vectors[i];
                    input.get(index).add(Double.parseDouble(vector));
                }
                index++;
            }
        }

        return input;
    }

    /**
     * Parse a .dat file into a Hash table that associates the key of a vector to its values.
     *
     * @param filename  The filename to load.
     * @return          The list of vectors associated with their name (key).
     */
    public static HashMap<String, ArrayList<Double>> datStrToHashMap (String filename) {
        String inputStr = read(filename);
        HashMap<String, ArrayList<Double>> input = new HashMap<>();

        String[] separateInput = inputStr.split("\n");
        String[] vectors;
        String line;
        String vector;
        ArrayList<Double> newVector;

        // Going through the vectors
        for (int j = 0; j < separateInput.length; j++) {
            line = separateInput[j];

            // Except the header
            if (j > 3) {
                newVector = new ArrayList<>();
                vectors = line.split("\t");

                // Going through the values of a vector
                for (int i = 1; i < vectors.length; i++) {
                    vector = vectors[i];
                    newVector.add(Double.parseDouble(vector));
                }
                input.put(vectors[0], newVector);
            }
        }

        return input;
    }

}
