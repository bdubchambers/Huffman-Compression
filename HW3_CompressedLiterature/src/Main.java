import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.BitSet;
import java.util.Scanner;

/*
 * capable of compressing a number of files and includes methods used to 
 * test components of your program
 */

/**
 * 
 * @author Brandon Chambers and Brendan Crawford
 *
 */
public class Main
{
	static CodingTree ct;
	static FileInputStream file = null;
	static Scanner sc = null;
	private static String strText;
	final static String FILE = "WarAndPeace.txt";// change to use different file

	/*
	 * will carry out compression of a file using CodingTree class - Read in
	 * from a textfile. You may hardcode the filename into your program but make
	 * sure TO TEST MORE THAN ONE FILE. - Output to two files. Again feel free
	 * to hardcode the names of these files into your program. These are the
	 * codes text file and the compressed binary file. - Display statistics. You
	 * must output the original size in bits, the compressed size in bits, the
	 * compression ratio as percentage and the elapsed time for compression.
	 */
	public static void main(String[] args) throws IOException
	{
		System.out.println("Size of our text file before Huffman compression: "
		        + NumberFormat.getInstance().format(getFileSize(FILE))
		        + " bytes");
		// start the stopwatch for running time
		long startTime = System.nanoTime();

		ct = new CodingTree(parseFile(FILE));
		
		byte[] dataToWrite = strText.getBytes();
		//List<String> myStrings = new ArrayList<String>();
		StringBuffer sbuff = new StringBuffer();
		
		for (int i = 0; i < dataToWrite.length; i++)
		{
			//myStrings.add(ct.getMapCharCodes().get(strText.charAt(i)));
			sbuff.append(ct.getMapCharCodes().get(strText.charAt(i)));
		}

		//use our createFromString method to convert the StringBuffer into
		//bits and output to compressed.txt
		FileOutputStream out = new FileOutputStream("compressed.txt");
		out.write(createFromString(sbuff).toByteArray());
		out.close();

		ct.setBits(sbuff);
		
		System.out.println("\ncompressed.txt file size: "
		        + NumberFormat.getInstance().format(
		                getFileSize("compressed.txt")) + " Bytes");

		System.out.println("\n************Refresh your project folder in Eclipse, then open the text file entitled 'compressed.txt' to view the results.");
		
		FileWriter fw = new FileWriter("codes.txt");
		fw.write(ct.getMapCharCodes().toString());
		fw.close();

		// stop the running time stopwatch and display it at the end of output
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println("\nRunning Time for Compression: " + estimatedTime / 1000000
		        + " milliseconds");

		System.out.printf("\nCompression Ratio: %.2f",
		        (getFileSize("compressed.txt") * 100.0 / getFileSize(FILE)));
		System.out.print("%\n");
		
		
		// start the stopwatch for running time
		startTime = System.nanoTime();
		
		fw= new FileWriter("decompressed.txt");
		fw.write(ct.decode(ct.getBits(), ct.tree));
		fw.close();
		
		estimatedTime = System.nanoTime() - startTime;
		System.out.println("\nRunning Time for Decompression: " + estimatedTime / 1000000
		        + " milliseconds");
		
		System.out.println("\ndecompressed.txt file size: "
		        + NumberFormat.getInstance().format(
		                getFileSize("decompressed.txt")) + " Bytes");
		
		System.out.println("\n************Refresh your project folder in Eclipse, then open the text file entitled 'decompressed.txt' to view the results.");
	}

	public static String parseFile(String fileName) throws FileNotFoundException, IOException
	{
		StringBuffer sb = new StringBuffer();
		try (
		// Open input stream for reading the text file MyTextFile.txt
		InputStream is = new FileInputStream(fileName);

		        // create new input stream reader
		        InputStreamReader instrm = new InputStreamReader(is);

		        // Create the object of BufferedReader object
		        BufferedReader br = new BufferedReader(instrm);

		)
		{
			String strLine;
			// Read one line at a time
			while ((strLine = br.readLine()) != null)
			{
				sb.append(strLine);
				sb.append("\n");
				//adding return carriages was wrong--check the decompressed.txt output against
				//the original txt file--it is correct w/o this line:
				//sb.append("\r");
			}
		}
		strText = sb.toString();
		return strText;
	}
	
	/*
	 * TEST FILES -- include atleast one test file that is a piece of literature
	 * of considerable size. CHECK OUT PROJECT GUTENBERG, an online database of
	 * literature in .txt format
	 */

	/*
	 * (OPTIONAL) Implement your own MyPriorityQueue<T> class using the array
	 * implementation mentioned in lecture. Use it in place of the Java
	 * PriorityQueue in your CodingTree class.
	 * 
	 * Analysis (Optional) In addition to this programming assignment you may
	 * also complete a high level worstcase runtime analysis of Huffman’s
	 * algorithm. You will assume for your analysis that n is the length of the
	 * message to be encoded, and that m is the number of unique characters in
	 * the message. Provide a bigoh analysis of each step of Huffman’s
	 * algorithm:Counting the frequency of characters in a message.Tree
	 * initialization and construction using a priority queue.Reading codes from
	 * the tree.Encoding the message. Combine these estimates to express the
	 * runtime of the algorithm as a whole.
	 */

	// This finds our text file size in bytes
	public static long getFileSize(String filename)
	{
		File file = new File(filename);
		if (!file.exists() || !file.isFile())
		{
			System.out.println("File does not exist!");
			return -1;
		}
		return file.length();
	}

	// from a stackoverflow user: Aditya Gaykar: on Septemper 20th 2013
	public static BitSet createFromString(StringBuffer s)
	{
		BitSet bs = new BitSet(s.length());
		int lastBitIndex = s.length() - 1;
		for (int i = lastBitIndex; i >= 0; i--)
		{
			if (s.charAt(i) == '1')
			{
				bs.set(lastBitIndex - i);
			}
		}

		return bs;
	}

}
