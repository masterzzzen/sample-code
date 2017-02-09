/* 
 * Puff.java
 *
 * A program that decompresses a file that was compressed using 
 * Huffman encoding.
 *
 * <Paul Zeng>, <paulmiaozeng@gmail.com>
 * <current date>
 */
package project; //TODO: remove package declaration befor submission

import java.io.*;
import java.util.*;

public class Puff {

    /* Put any methods that you add here. */
    private static final int NUM_ASCII = 256;
    private static int[] frequencies;

    // Build a frequency array from the compressed file's header
    private static int[] buildFrequencies(int[] frequencies, ObjectInputStream in) {
        int count = 0;
        for (int i = 0; i < frequencies.length; i++) {
            try {
                count = in.readInt();
                frequencies[i] = count;
            } catch (IOException e) {
                System.out.printf("Cannot read header for position %d \n", i);
            }
        }

        return frequencies;
    }

    // main work horse for decompression
    private static void decompress(HuffmanNode tree, BitReader reader, FileWriter out) {
        // Read the input file
        int bit = 0;
        HuffmanNode trav = tree;
        try {
            while ((bit = reader.getBit()) > -1) {
                if (bit == 1) {
                    trav = trav.right;
                } else {
                    trav = trav.left;
                }

                if (trav.left == null && trav.right == null) { // should be if
                    out.write((char) trav.key);
                    trav = tree; // reset
                }

            }
        } catch (IOException e) {
            System.out.println("Cannot write to file");
            System.exit(1);
        }
    }

    /**
     * main method for decompression.  Takes command line arguments.
     * To use, type: java Puff input-file-name output-file-name
     * at the command-line prompt.
     */

    public static void main(String[] args) throws IOException {
        frequencies = new int[NUM_ASCII];

        Scanner console = new Scanner(System.in);
        ObjectInputStream in = null;      // reads in the compressed file
        FileWriter out = null;            // writes out the decompressed file

        // Get the file names from the command line (if any) or from the console.
        String infilename, outfilename;
        if (args.length >= 2) {
            infilename = args[0];
            outfilename = args[1];
        } else {
            System.out.print("Enter the name of the compressed file: ");
            infilename = console.nextLine();
            System.out.print("Enter the name to be used for the decompressed file: ");
            outfilename = console.nextLine();
        }

        // Open the input file.
        try {
            in = new ObjectInputStream(new FileInputStream(infilename));
        } catch (FileNotFoundException e) {
            System.out.println("Can't open file " + infilename);
            System.exit(1);
        }

        frequencies = buildFrequencies(frequencies, in); // reconstruct the frequency table

        // Open the output file.
        try {
            out = new FileWriter(outfilename);
        } catch (FileNotFoundException e) {
            System.out.println("Can't open file " + outfilename);
            System.exit(1);
        }

        // Create a BitReader that is able to read the compressed file.
        BitReader reader = new BitReader(in);

        /****** Add your code here. ******/
        HuffmanNode tree = Huff.buildTree(frequencies);
        decompress(tree, reader, out);


        /* Leave these lines at the end of the method. */
        in.close();
        out.close();
    }
}
