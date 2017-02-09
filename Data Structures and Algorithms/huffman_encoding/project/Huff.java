/* 
 * Huff.java
 *
 * A program that compresses a file using Huffman encoding.
 *
 * <Paul Zeng>, <paulmiaozeng@gmail.com>
 * <12/7/2015>
 */
package project;
import java.io.*;
import java.util.*;

public class Huff {

    /* variables */
    private static final int NUM_ASCII = 256;
    private static int[] frequencies;
    private static Code[] codes;

    /* Put any methods that you add here. */

    static void updateFrequencies(int pos) {
        frequencies[pos]++;
    }

    static HuffmanNode buildTree(int[] frequencies) {
        HuffmanNode head = initNodes(frequencies);
        while (head != null && head.next != null) {
            HuffmanNode node1 = head;
            HuffmanNode node2 = head.next;
            head = head.next.next;
            HuffmanNode combinedNode = combineTwoNodes(node1, node2);
            head = insertNodeToLinkedList(combinedNode, head);
        }

        return head;
    }

    // insert node into the linked list; may change the head of the node
    private static HuffmanNode insertNodeToLinkedList(HuffmanNode node, HuffmanNode head) {
        if (head == null) {
            return node;
        }
        HuffmanNode curr = head; // always head at the front of the list
        HuffmanNode prev = null;

        while (true) { // insert the node into sorted positions
            if (node.data > curr.data) {
                if (curr.next == null) { // end of the list
                    curr.next = node;
                    return head;
                } else { // advance
                    prev = curr;
                    curr = curr.next;
                }
            } else if (node.data == curr.data) {
                while (curr != null && (node.key > curr.key)) { // advance
                    prev = curr;
                    curr = curr.next;
                }
                // insert
                node.next = prev.next;
                prev.next = node;
                return head;
            } else { // node.data < curr.data
                if (prev == null) {
                    node.next = head;
                    head = node;
                } else {
                    node.next = curr;
                    prev.next = node;
                }
                return head;
            }
        }
    }


    private static HuffmanNode combineTwoNodes(HuffmanNode node1, HuffmanNode node2) {
        HuffmanNode combinedNode = new HuffmanNode(-1, node1.data + node2.data, node1, node2, null, null); // use -1 to represent blank key
        node1.parent = combinedNode;
        node2.parent = combinedNode;

        return combinedNode;
    }

    // Build a linked list of nodes where the key of each node corresponds to an ASCII character
    private static HuffmanNode initNodes(int[] frequencies) {
        int rootIndex = 0;
        for (; rootIndex < frequencies.length; rootIndex++) {
            if (frequencies[rootIndex] > 0)
                break;
        }
        HuffmanNode head = new HuffmanNode(rootIndex, frequencies[rootIndex]);

        for (int i = rootIndex + 1; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                HuffmanNode node = new HuffmanNode(i, frequencies[i]);
                head = insertNodeToLinkedList(node, head);
            }
        }
        return head;
    }

    // Generate an array of Code objects
    private static Code[] buildCodes(HuffmanNode curr, Code[] codes) {
        if (curr.left == null && curr.right == null) {
            codes[curr.key] = genCode(curr);
        }
        if (curr.left != null)
            buildCodes(curr.left, codes);
        if (curr.right != null)
            buildCodes(curr.right, codes);

        return codes;

    }

    // Generate a Code object for a single node
    private static Code genCode(HuffmanNode leaf) {
        HuffmanNode curr = leaf;
        HuffmanNode parent = null;
        Code code = new Code();
        String encoding = ""; // reading from leaf up to the root will get us reversed Huffman enconding
        while (curr != null && curr.parent != null) {
            parent = curr.parent;
            if (curr == parent.left) {
                encoding = "0" + encoding;
            } else {
                encoding = "1" + encoding;
            }
            curr = parent;
        }

        for (int i = 0; i < encoding.length(); i++) {
            code.addBit(Integer.parseInt(encoding.substring(i, i + 1)));
        }
        return code;
    }

    // Write to the header
    private static void writeHeader(int[] frequencies, ObjectOutputStream out) {

        for (int i = 0; i < frequencies.length; i++) {
            int count = frequencies[i];
            try {
                out.writeInt(count);
            } catch (IOException e) {
                System.out.printf("Cannot write header for position %d \n", i);
            }
        }
    }

    // Write the body of the encoded file
    private static void writeBody(Code[] codes, FileReader in, BitWriter writer) throws IOException {

        // Read the input file
        int ch = 0;
        while ((ch = in.read()) > -1) {
            String code = codes[ch].toString();
            try {
                for (int i = 0; i < code.length(); i++) {
                    int bit = Integer.parseInt(code.substring(i, i + 1));
                    writer.putBit(bit);
                }
            } catch (IOException e) {
                System.out.println("Cannot write to file");
                System.exit(1);
            }
        }
        writer.flushBits();
    }

    /**
     * main method for compression.  Takes command line arguments.
     * To use, type: java Huff input-file-name output-file-name
     * at the command-line prompt.
     */
    public static void main(String[] args) throws IOException {
        frequencies = new int[NUM_ASCII];
        codes = new Code[NUM_ASCII];
        Scanner console = new Scanner(System.in);
        FileReader in = null;               // reads in the original file
        ObjectOutputStream out = null;      // writes out the compressed file

        // Get the file names from the command line (if any) or from the console.
        String infilename, outfilename;
        if (args.length >= 2) {
            infilename = args[0];
            outfilename = args[1];
        } else {
            System.out.print("Enter the name of the original file: ");
            infilename = console.nextLine();
            System.out.print("Enter the name to be used for the compressed file: ");
            outfilename = console.nextLine();
        }

        // Open the input file.
        try {
            in = new FileReader(infilename);
        } catch (FileNotFoundException e) {
            System.out.println("Can't open file " + infilename);
            System.exit(1);
        }

        try {
            out = new ObjectOutputStream(new FileOutputStream(outfilename));
        } catch (FileNotFoundException e) {
            System.out.println("Can't open file " + outfilename);
            System.exit(1);
        }

        /****** Add your code below. ******/
        /*
         * Note: After you read through the input file once, you will need
         * to reopen it in order to read through the file
         * a second time.
         */

        // Read the input file
        int ch = 0;
        while ((ch = in.read()) > -1) {
            updateFrequencies(ch);
        }

        HuffmanNode tree = buildTree(frequencies); // build tree
        buildCodes(tree, codes); // generate array of code objects

        // Open the output file.
        try {
            out = new ObjectOutputStream(new FileOutputStream(outfilename));
        } catch (FileNotFoundException e) {
            System.out.println("Can't open file " + outfilename);
            System.exit(1);
        }
        writeHeader(frequencies, out);

        // Create a BitWriter that is able to write to the compressed file.
        BitWriter writer = new BitWriter(out);

        // Open the input file AGAIN.
        try {
            in = new FileReader(infilename);
        } catch (FileNotFoundException e) {
            System.out.println("Can't open file " + infilename);
            System.exit(1);
        }
        writeBody(codes, in, writer);


        /* Leave these lines at the end of the method. */
        in.close();
        out.close();
    }
}
