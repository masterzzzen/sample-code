The goal of the project was to implement the canonical Huffman algorithm, with all of its intricacies. The Huffman was devised by MIT computer scientist David Huffman.

It is a compression algorithm based on the idea that we can encode characters according to the frequencies with which they appear in a document. More specifically, characters that appear more frequently will have not have longer encodings than characters that appear less frequently. This algorithm yields significant space savings.

In order to run this code, first download the huffman_encoding package.   
Then load the package into Intellij by selecting File -> New -> Project from Existing Sources.  
 Then select this directory "huffman_encoding" and click OK. 
  Then click through all of the default options. 
  
After the project has been created, run Huff.java. This will encode the input file. There is an existing testing file named "romeo.txt". When Huff.java runs, the console will ask you for an input file name. You type type in romeo.txt, without quotes. If you wish to create your own input file, make sure that place it outside the /project directory. 
  
After you've encoded a file, run Puff.java to decode it. You will see that the decoded file will be the same as your original file.   