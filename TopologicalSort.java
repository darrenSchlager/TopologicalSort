/*
	Author: Darren Schlager
*/

import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;

public class TopologicalSort 
{	
	private int[][] connections;	// row 0 and column 0 are not used since node numbering starts at 1
	private int numRowsColumns;
	private int[] columnSums;
	private int[] nodesByOrderNumber;
	private int[] orderNumbersByNode;
	
	public TopologicalSort(int[][] connections)
	{
		this.connections = connections;
		numRowsColumns = connections.length;
		columnSums = new int[numRowsColumns];
		nodesByOrderNumber = new int[numRowsColumns];
		orderNumbersByNode = new int[numRowsColumns];
	}
	
	// perform the topological sort
	public int[] perform()
	{	
		sumColumns();
		
		// try to order all nodes
		for(int nextOrderNumber = 1; nextOrderNumber<numRowsColumns; nextOrderNumber++)
		{
			// print the connections matrix
			printConnections();
			System.out.println("   --------------- sum columns");
			printColumnSums();
			
			// find the next node with no incoming connections
			int nextNodeToOrder = findFirstUnorderedNodeWithZeroIncomingConnections();
			if(nextNodeToOrder==0) return null;	// there is a cycle
			
			// order the node
			System.out.println("\n   assign order number "+ nextOrderNumber +" to node " + nextNodeToOrder);
			orderNumbersByNode[nextNodeToOrder] = nextOrderNumber;
			nodesByOrderNumber[nextOrderNumber] = nextNodeToOrder;
			
			// update incoming connections
			subtractRowFromColumnSums(nextNodeToOrder);
			
			// print order numbers
			printOrderNumbers();
			if(nextOrderNumber<numRowsColumns-1) System.out.println("\n\n");
		}
		
		return nodesByOrderNumber;
	}
	
	private void sumColumns()
	{
		for(int col=1; col<numRowsColumns; col++)
		{
			int sum = 0;
			for(int row=1; row<numRowsColumns; row++)
			{
				sum += connections[row][col];
			}
			columnSums[col] = sum;
		}
	}
	
	private int findFirstUnorderedNodeWithZeroIncomingConnections()
	{
		for(int i=1; i<numRowsColumns; i++)
		{
			if(orderNumbersByNode[i]==0 && columnSums[i]==0)
			{
				return i;
			}
		}
		return 0;
	}
	
	private void subtractRowFromColumnSums(int row) 
	{
		for(int col=1; col<numRowsColumns; col++)
		{
			columnSums[col] -= connections[row][col];
		}
	}
	
	private void printConnections()
	{
		// print the column numbers
		System.out.print("   ");
		for(int col=1; col<numRowsColumns; col++)
		{
			System.out.print(col+" ");	
		}
		System.out.println("\n + ");
		
		// print each row that is unordered
		for(int row=1; row<numRowsColumns; row++)
		{
			if(orderNumbersByNode[row]==0)
			{
				System.out.print(row+"  ");
				for(int col=1; col<numRowsColumns; col++)
				{
					System.out.print(connections[row][col]+" ");
					if(col==connections.length-1) System.out.println();
				}
			}
		}
	}
	
	private void printColumnSums()
	{
		System.out.print("   ");
		for(int i=1; i<numRowsColumns; i++)
		{
			if(orderNumbersByNode[i]!=0)
			{
				System.out.print("- ");
			}
			else 
			{
				System.out.print(columnSums[i]+" ");
			}
		}
		System.out.println();
	}
	
	private void printOrderNumbers()
	{
		System.out.print("   ");
		for(int i=1; i<numRowsColumns; i++)
		{
			if(orderNumbersByNode[i]==0)
			{
				System.out.print("  ");
			}
			else 
			{
				System.out.print(orderNumbersByNode[i]+" ");
			}
		}
		System.out.println();
	}
	
	public static void main(String[] args) 
	{
		// print description of expeteted file contents
		System.out.println("TopologicalSort\n");
		System.out.println("Format your file as follows:");
		System.out.println("===================================================================");
		System.out.println(" <number of nodes>");
		System.out.println(" <0-0> <0-1> ... <0-n> use a 1 to indicate a connection");
		System.out.println(" <1-0> <1-1> ... <1-n> use a 0 to indicate there isn't a connection");
		System.out.println(" <n-0> <n-1> ... <n-n>");
		System.out.println("===================================================================");
		
		int[][] connections = getConnectionsFromFile();
		if(connections != null)
		{
			TopologicalSort ts = new TopologicalSort(connections);
			int[] result = ts.perform();
			
			// print the result
			if(result!=null)
			{
				System.out.print("\n   Sorted Nodes:\n   ");
				for(int i=1; i<result.length; i++)
				{
					System.out.print(result[i]+ " ");
				}
				System.out.println();
			}
			// error
			else 
			{
				System.out.println("\nThis graph has a cycle and cannot be sorted.");
			}
		}
		else 
		{
			System.out.println("A valid file was not received.\nEXIT");
		}
		
	}
	
	public static int[][] getConnectionsFromFile()
	{
		
		int numAttempts = 5;	// stop after this number of unsuccessful attempts
		int[][] connections;	// row 0 and column 0 are not used since node numbering starts at 1
		
		// used to retrieve keyboard input from the user
		Scanner keyboard = new Scanner(System.in);
		
		Scanner file;
		boolean dataInputSuccessfully = false;
		
		do 
		{
			numAttempts--;
			try 
			{
				// open the file specified by the user
				System.out.print("file path: ");
				file = new Scanner(new FileReader(keyboard.nextLine()));
				
				try 
				{
					// get the number of nodes
					int numNodes = file.nextInt();
					
					//prepare to read and store the the connections
					file.nextLine();
					connections = new int[numNodes+1][numNodes+1];
					
					// read and store the connections
					for(int row=1; row<numNodes+1; row++)
					{
						for(int col=1; col<numNodes+1; col++)
						{
							connections[row][col] = Integer.parseInt(file.next("0|1"));
						}
					}
					System.out.println();
					return connections;
				}
				catch (Exception e)
				{
					System.out.println("That file is not formatted correctly.");
				}
				
				// close the file
				file.close();
				
				dataInputSuccessfully = true;
			} 
			catch (IOException e)
			{
				// invalid file
				System.out.println("That file does not exist.");
			}
		} while (numAttempts>0);
		
		System.out.println();
		return null;
	}
}