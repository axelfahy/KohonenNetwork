package Utilities;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by rudy on 09.06.15.
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

    public static ArrayList<ArrayList<Double>> datStrToArrayList (String filename) {
        String inputStr = read(filename);
        ArrayList<ArrayList<Double>> input = new ArrayList<>();

        String[] separateInput = inputStr.split("\n");
        String[] vectors;
        String line;
        String vector;
        int index = 0;

        for (int j = 0; j < separateInput.length; j++) {
        //for (String line : separateInput) {
            line = separateInput[j];
            if (j > 3) {
                input.add(new ArrayList<Double>());
                vectors = line.split("\t");
                for (int i = 1; i < vectors.length; i++) {
                    vector = vectors[i];
                    input.get(index).add(Double.parseDouble(vector));
                }
                index++;
            }
            else {
                // Manage the header
            }
        }

        return input;
    }

    public static HashMap<String, ArrayList<Double>> datStrToHashMap (String filename) {
        String inputStr = read(filename);
        HashMap<String, ArrayList<Double>> input = new HashMap<>();

        String[] separateInput = inputStr.split("\n");
        String[] vectors;
        String line;
        String vector;
        ArrayList<Double> newVector;

        for (int j = 0; j < separateInput.length; j++) {
            line = separateInput[j];
            if (j > 3) {
                newVector = new ArrayList<>();
                vectors = line.split("\t");
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
