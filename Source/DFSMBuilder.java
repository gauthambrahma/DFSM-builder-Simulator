/* Name:Gautham Brahma Ponnaganti
 * Course Number:CS5413
 * Assignment Numeber:1
 * Short Description:Given a string we generate a transition table for DFA that 
 * accepts the string. Here the algorithm used is KMP which describes how to back 
 * track in each state for failure cases. */

import java.io.*;
import java.util.*;

public class DFSMBuilder {
	public static void main(String[] args)
	{
		// initialization
		String inputString, fileName;
		int[][] transitionTable;
		int currentStatePointer = 0;
		int currentSymbolIndex= 0;
		int currentSymbolPointer = 0;
		String currentSymbol = "", currentSubstring = "";
		int stateToReplicate;
		if (args.length == 2)
		{
			// reading File
			fileName = args[0];
			inputString = args[1];
			try
			{
				File file = new File(fileName);
				file.createNewFile();
				FileWriter fw = new FileWriter(file);

				// using arrayList over arrays because we do not know # of alphabets
				List<String> alphabets = new ArrayList<String>();
				String alphabet;

				// picking up unique elements form input string
				for (int i = 0; i < inputString.length(); i++)
				{
					alphabet = Character.toString(inputString.charAt(i));
					if (!alphabets.contains(alphabet))
					{
						alphabets.add(alphabet);
						 fw.write(alphabet+" ");
					}
				}
				fw.write("\n");
				transitionTable = new int[inputString.length() + 1][alphabets.size()];

				//first two rows of transition table
				currentSymbol = inputString.substring(currentSymbolPointer, currentSymbolPointer+1);
				currentSymbolIndex = alphabets.indexOf(currentSymbol);
				
				//row1
				for(int i=0;i<transitionTable[i].length;i++)
				{
					if(i==currentSymbolIndex)
					{
						transitionTable[0][i]=2;
					}
					else
					{
						transitionTable[0][i]=1;
					}
				}
				currentSymbolPointer++;
				currentStatePointer++;
				
				//row2
				currentSymbol = inputString.substring(currentSymbolPointer, currentSymbolPointer+1);
				currentSymbolIndex = alphabets.indexOf(currentSymbol);
				System.arraycopy(transitionTable[0], 0, transitionTable[1], 0, transitionTable[0].length);
				transitionTable[1][currentSymbolIndex]=3;
				currentSymbolPointer++;
				currentStatePointer++;
				
				//next all rows
				for(int i=2;i<inputString.length();i++)
				{
					currentSymbol=inputString.substring(currentSymbolPointer, currentSymbolPointer+1);
					currentSymbolIndex=alphabets.indexOf(currentSymbol);				
					
					//failure case(backTracking)
					currentSubstring=inputString.substring(1,currentStatePointer);
					stateToReplicate=modifyTransitionTable(currentSubstring,transitionTable,alphabets,currentSymbolIndex);
					for(int j=0;j<transitionTable[i].length;j++)
					{
						System.arraycopy(transitionTable[stateToReplicate], 0, transitionTable[currentStatePointer], 0, transitionTable[currentStatePointer].length);
					}
					
					//success case
					transitionTable[currentStatePointer][currentSymbolIndex]=currentStatePointer+2;
					currentStatePointer++;
					currentSymbolPointer++;
				}
				//last row
				for(int i=0;i<transitionTable[inputString.length()].length;i++)
				{
					transitionTable[inputString.length()][i]=currentStatePointer+1;					
				}
				
				//writing transition table to file
				for(int i=0;i<inputString.length()+1;i++)
				{
					for(int j=0;j<alphabets.size();j++)
					{
						fw.write(transitionTable[i][j]+" ");
					}
					fw.write("\n");
				}
				//writing final states
				fw.write(Integer.toString(currentStatePointer+1));
				fw.close();
			} catch (IOException e)
			{
				System.out.println("unable to create file");
			}
		} else
		{
			System.out.println("Incorrenct number of arguments. Please enter the correct arguments");
		}

	}
	
	//modifies transition table according to KMP algorithm
	private static int modifyTransitionTable(String currentSubstring, int[][] transitionTable, List<String> alphabets, int currentSymbolIndex)
	{
		String currentAlphabet="";
		int currentState=0;
		while(currentSubstring.length()>0)
		{
			currentAlphabet=currentSubstring.substring(0, 1);
			currentState=transitionTable[currentState][alphabets.indexOf(currentAlphabet)]-1;
			currentSubstring = currentSubstring.substring(1);
		}
		return currentState;
	}
}
