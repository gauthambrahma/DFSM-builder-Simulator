
/* Author:Gautham Brahma Ponnaganti
 * Short Description:This program takes a file containing transition table
 * ,alphabets and final states as input and indicates weather a input,
 * given as a parameter, is accepted by it or not. We print yes or no 
 * on the screen accordingly */

import java.io.*;
import java.util.*;

public class SimDFSM {
	public static void main(String[] args)
	{
		// initialization
		String[] alphabets;
		String[][] transitionTable;
		String[] finalStates;
		List<String> contentOfFile = new ArrayList<String>();
		List<String> states = new ArrayList<String>();
		String[] temp1;
		String fileName, inputString;
		String temp;
		String[] temporary;
		int currentState = 0;
		String result;

		if (args.length == 2)
		{
			try
			{
				// reading File
				fileName = args[0];
				inputString = args[1];
				try
				{
					File file = new File(fileName);
					Scanner sc = new Scanner(new FileInputStream(file));
					while (sc.hasNextLine())
					{
						contentOfFile.add(sc.nextLine());
					}
					sc.close();
				} catch (FileNotFoundException e)
				{
					System.out.println("File Not Found. Please check the file name.");
					System.exit(0);
				}
				temp = contentOfFile.get(0);
				alphabets = temp.split("\\s+");
				validateAlphabets(alphabets);
				contentOfFile.remove(0);
				temp = contentOfFile.get(contentOfFile.size() - 1);
				finalStates = temp.split("\\s+");
				contentOfFile.remove(contentOfFile.size() - 1);
				validateTransitionTable(contentOfFile);
				for (int i = 0; i < contentOfFile.size(); i++)
				{
					temp1 = contentOfFile.get(i).split("\\s+");
					for (int j = 0; j < temp1.length; j++)
					{
						if (!states.contains(temp1[j]))
						{
							states.add(temp1[j]);
						}
					}
				}
				for (int i = 0; i < finalStates.length; i++)
				{
					if (!states.contains(finalStates[i]))
					{
						System.out.println("The DFA has final states that are not reachable.");
						System.exit(0);
					}
				}
				transitionTable = new String[contentOfFile.size()][contentOfFile.get(0).split("\\s+").length];
				for (int i = 0; i < contentOfFile.size(); i++)
				{
					temp = contentOfFile.get(i);
					temporary = temp.split("\\s+");
					transitionTable[i] = temporary;
				}
				// validating input
				validateInput(alphabets, inputString);
				// checking if the input is accepted by DFA
				result = isLanguageAccepted(inputString, currentState, alphabets, transitionTable, finalStates);
				System.out.println(result);
			} catch (ArrayIndexOutOfBoundsException e)
			{
				System.out
						.println("Problem with the format of transition table. Please enter a valid transition table");
			}
		} else
		{
			System.out.println("Incorrenct number of arguments Please enter the correct arguments");
			System.exit(0);
		}
	}
	
	//validates alphabets for duplicates and validity
	private static void validateAlphabets(String[] alphabets)
	{
		for(int i=0;i<alphabets.length;i++)
		{
			for(int j=i+1;j<alphabets.length;j++)
			{
				if(alphabets[i].length()!=1)
				{
					System.out.println("Alphabets should not be paired together.");
					System.exit(0);
				}
				if(alphabets[i].equals(alphabets[j]))
				{
					System.out.println("Alphabets should not have duplicates.");
					System.exit(0);
				}
			}
		}
	}

	// Validates transition table for bad format
	private static void validateTransitionTable(List<String> contentOfFile)
	{
		if (contentOfFile.size() < 1)
		{
			System.out.println("The transition table is empty. Please enter a valid transition table");
			System.exit(0);
		} else
		{
			int size = contentOfFile.get(0).length();
			for (int i = 0; i < contentOfFile.size(); i++)
			{
				if (contentOfFile.get(i).length() != size)
				{
					System.out.println(
							"Rows and columns of Transition table are invalid. Please enter a valid transition table");
					System.exit(0);
				}
				if (contentOfFile.get(i).equals(""))
				{
					System.out
							.println("Transition table has empty transitions. Please enter a valid transition table.");
					System.exit(0);
				}

			}
		}

	}

	// consumes input to see if the language is accepted
	private static String isLanguageAccepted(String inputString, int currentState, String[] alphabets,
			String[][] transitionTable, String[] finalStates)
	{
		String currentAlphabet = "";
		try
		{
			while (inputString.length() > 0)
			{
				currentAlphabet = inputString.substring(0, 1);
				currentState = Integer
						.parseInt(transitionTable[currentState][Arrays.asList(alphabets).indexOf(currentAlphabet)])-1;
				inputString = inputString.substring(1);
			}
		} catch (NumberFormatException e)
		{
			System.out.println(
					"The transition table must only contain states represented by numbers. Please enter a valid transition table.");
			System.exit(0);
		}
		// if the current pointer is in the accepting state the language is
		// accepted
		if (Arrays.asList(finalStates).indexOf(Integer.toString(currentState+1)) == -1)
		{
			return "NO";
		} else
		{
			return "YES";
		}
	}

	// Input symbols are validated against set of alphabets
	private static void validateInput(String[] alphabets, String inputString)
	{
		for (int i = 0; i < inputString.length(); i++)
		{
			if (!Arrays.asList(alphabets).contains(Character.toString(inputString.charAt(i))))
			{
				System.out.println(
						"Given input has invalid characters. The input string should contain input alphabets only");
				System.exit(0);
			}
		}
	}
}
