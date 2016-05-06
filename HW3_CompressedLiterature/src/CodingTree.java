import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
/**
 * 
 * @author Brandon Chambers and Brendan Crawford
 *
 */

//HuffmanTree is now in its own class file:


/*
 * To implement your CodingTree all other design choices are left to you. It is
 * strongly encouraged that you use additional classes and methods and try to
 * use the proper built in data structures whenever possible. For example, in my
 * sample solution I make use of a private class to count the frequency of each
 * character, a private node class to implement my tree, a recursive function to
 * read the codes out of the finished tree, and a priority queue to handle
 * selecting the minimum weight tree.
 */

public class CodingTree
{
	HuffmanTree tree;
	// a public data member that is the message encoded using the Huffman codes
	// can also be: String bits
	public String bits = new String();// for testing
	// a public data member that is a map of characters in the message to binary
	// codes--
	// Strings of 1s and 0s -- created by your tree
	public static Map<Character, String> mapCharCodes = new HashMap<Character, String>();
	// for storing the char and its frequency
	public static Map<Character, Integer> mapCharFreq = new HashMap<Character, Integer>();

	// getters
	public Map<Character, String> getMapCharCodes()
	{
		return mapCharCodes;
	}

	public Map<Character, Integer> getMapCharFreq()
	{
		return mapCharFreq;
	}

	public String getBits()
	{
		return bits;
	}
	//setters

	public void setBits(StringBuffer sbuff)
    {
		this.bits=sbuff.toString();
    }

	
	
	/*
	 * constructor that takes the text of a message to be compressed. The
	 * constructor is responsible for calling all private methods that carry out
	 * the Huffman coding algorithm
	 */
	public CodingTree(String message)
	{
		char[] charFreq = message.toCharArray();
		countCharFreq(charFreq);
		// build the tree
		tree = buildTree(CodingTree.mapCharFreq);
		findCodes(tree, new StringBuffer());
		
	}

	// input is an array of frequencies, indexed by character code
	private HuffmanTree buildTree(Map<Character, Integer> map)
	{
		PriorityQueue<HuffmanTree> trees = new PriorityQueue<HuffmanTree>();

		for (Map.Entry<Character, Integer> entry : map.entrySet())
		{
			trees.offer(new HuffmanLeaf(entry.getValue(), entry.getKey()));
		}

		//testing
		assert trees.size() > 0;
		
		// loop until there is only one tree left
		while (trees.size() > 1)
		{
			// two trees with least frequency
			HuffmanTree a = trees.poll();
			HuffmanTree b = trees.poll();

			// put into new node and re-insert into queue
			trees.offer(new HuffmanNode(a, b));
		}
		return trees.poll();
	}

	private void findCodes(HuffmanTree tree, StringBuffer prefix)
	{
		//testing
		assert tree != null;
		if (tree instanceof HuffmanLeaf)
		{
			HuffmanLeaf leaf = (HuffmanLeaf) tree;
			String s = prefix.toString();
			// enter each char (Key) with its respective binary code (Value)
			// into a new HashMap
			mapCharCodes.put(leaf.value, s);
			// System.out.println(leaf.value + "\t" + leaf.frequency + "\t\t"
			// + prefix);

		}
		else if (tree instanceof HuffmanNode)
		{
			HuffmanNode node = (HuffmanNode) tree;

			// traverse left
			prefix.append('0');
			findCodes(node.left, prefix);
			prefix.deleteCharAt(prefix.length() - 1);

			// traverse right
			prefix.append('1');
			findCodes(node.right, prefix);
			prefix.deleteCharAt(prefix.length() - 1);
		}
	}

	// (OPTIONAL) -- will take the output of Huffman's encoding
	// and produce the original text.
	public String decode(String bits, HuffmanTree tree)// throws FileNotFoundException, IOException
	{
		StringBuffer sb=new StringBuffer();
		HuffmanTree current = tree;
		for (char c: bits.toCharArray())
		{
			if(current instanceof HuffmanLeaf)
			{
				sb.append(((HuffmanLeaf) current).value);
				current=tree;
			}
			else
			{
				current=(HuffmanNode) current;
				if(c == '0')
					current=((HuffmanNode) current).left;
				else 
					current=((HuffmanNode) current).right;
				if(current instanceof HuffmanLeaf)
				{
					sb.append(((HuffmanLeaf) current).value);
					current=tree;
				}
			}
		}
		return sb.toString();
	}

	public void countCharFreq(char[] chars)
	{
		for (char c : chars)
		{
			if (mapCharFreq.containsKey(c))
			{
				mapCharFreq.put(c, mapCharFreq.get(c) + 1);
			}
			else
				mapCharFreq.put(c, 1);
		}
	}

}
