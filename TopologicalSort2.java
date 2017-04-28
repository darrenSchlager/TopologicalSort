/*
	Author: Darren Schlager
*/

import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;

public class TopologicalSort2 
{	
	private int[][] connections;
	private int[] nodesByOrderNumber;
	private int[] orderNumbersByNode;
	private int[] columnSums;
	private int numNodes;
	
	public TopologicalSort2(int[][] connections)
	{
		this.connections = connections;
		numNodes = connections.length;
		nodesByOrderNumber = new int[numNodes];
		columnSums = new int[numNodes];
		for(int i=0; i<numNodes; i++) nodesByOrderNumber[i] = -1;
		orderNumbersByNode = new int[numNodes];
		for(int i=0; i<numNodes; i++) orderNumbersByNode[i] = -1;
	}
	
	public int[] perform()
	{	
		for(int nextOrderNumber = 0; nextOrderNumber<numNodes; nextOrderNumber++)
		{
			printConnections();
			System.out.println("   ––––––––––––––– sum columns");
			
			int nextNodeToOrder = findFirstUnorderedNodeWithZeroIncomingConnections();
			printColumnSums();
			if(nextNodeToOrder==-1) return null;
			System.out.println("\n   assign order number "+ nextOrderNumber +" to node " + nextNodeToOrder);
			
			nodesByOrderNumber[nextOrderNumber] = nextNodeToOrder;
			orderNumbersByNode[nextNodeToOrder] = nextOrderNumber;
			printOrderNumbers();
			if(nextOrderNumber<numNodes-1) System.out.println("\n\n");
		}
		
		return nodesByOrderNumber;
	}
	
	private int findFirstUnorderedNodeWithZeroIncomingConnections()
	{
		int firstZeroSum = -1;
		
		for(int col=0; col<numNodes; col++)
		{
			int sum = 0;
			for(int row=0; row<numNodes; row++)
			{
				if(orderNumbersByNode[row]==-1)
				{
					sum += connections[row][col];
				}
			}
			columnSums[col] = sum;
			if(firstZeroSum==-1 && orderNumbersByNode[col]==-1 && sum==0) firstZeroSum = col;
		}
		return firstZeroSum;
	}
	
	private void printConnections()
	{
		System.out.print("   ");
		for(int col=0; col<numNodes; col++)
		{
			System.out.print(col+" ");	
		}
		System.out.println("\n + ");
		
		for(int row=0; row<numNodes; row++)
		{
			if(orderNumbersByNode[row]==-1)
			{
				System.out.print(row+"  ");
				for(int col=0; col<numNodes; col++)
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
		for(int i=0; i<numNodes; i++)
		{
			if(orderNumbersByNode[i]!=-1)
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
		for(int i=0; i<numNodes; i++)
		{
			if(orderNumbersByNode[i]==-1)
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
			TopologicalSort2 ts = new TopologicalSort2(connections);
			int[] result = ts.perform();
			if(result!=null)
			{
				System.out.print("\n   Sorted Nodes:\n   ");
				for(int i=0; i<result.length; i++)
				{
					System.out.print(result[i]+ " ");
				}
				System.out.println();
			}
			else 
			{
				System.out.println("This graph has a cycle and cannot be sorted.");
			}
		}
		else 
		{
			System.out.println("A valid file was not received.\nEXIT");
		}
		
	}
	
	public static int[][] getConnectionsFromFile()
	{
		
		int numAttempts = 5;
		int[][] connections;
		
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
					connections = new int[numNodes][numNodes];
					
					// read and store the connections
					for(int row=0; row<numNodes; row++)
					{
						for(int col=0; col<numNodes; col++)
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

/*

import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;

public class TopologicalSort 
{	
	private int[][] connections;
	private int[] nodesByOrderNumber;
	private int[] orderNumbersByNode;
	private int[] columnSums;
	private int numNodes;
	
	public TopologicalSort(int[][] connections)
	{
		this.connections = connections;
		numNodes = connections.length;
		columnSums = new int[numNodes];
		nodesByOrderNumber = new int[numNodes];
		for(int i=0; i<numNodes; i++) nodesByOrderNumber[i] = -1;
		orderNumbersByNode = new int[numNodes];
		for(int i=0; i<numNodes; i++) orderNumbersByNode[i] = -1;
	}
	
	public int[] perform()
	{	
		sumColumns();
		
		for(int nextOrderNumber = 0; nextOrderNumber<numNodes; nextOrderNumber++)
		{
			printConnections();
			System.out.println("   ––––––––––––––– sum columns");
			printColumnSums();
			
			int nextNodeToOrder = findFirstUnorderedNodeWithZeroIncomingConnections();
			if(nextNodeToOrder==-1) return null;
			orderNumbersByNode[nextNodeToOrder] = nextOrderNumber;
			nodesByOrderNumber[nextOrderNumber] = nextNodeToOrder;
			subtractRowFromColumnSums(nextNodeToOrder);
			
			System.out.println("\n   assign order number "+ nextOrderNumber +" to node " + nextNodeToOrder);
			printOrderNumbers();
			if(nextOrderNumber<numNodes-1) System.out.println("\n\n");
		}
		
		return nodesByOrderNumber;
	}
	
	private void sumColumns()
	{
		for(int col=0; col<numNodes; col++)
		{
			int sum = 0;
			for(int row=0; row<numNodes; row++)
			{
				sum += connections[row][col];
			}
			columnSums[col] = sum;
		}
	}
	
	private int findFirstUnorderedNodeWithZeroIncomingConnections()
	{
		for(int i=0; i<numNodes; i++)
		{
			if(orderNumbersByNode[i]==-1 && columnSums[i]==0)
			{
				return i;
			}
		}
		return -1;
	}
	
	private void subtractRowFromColumnSums(int row) 
	{
		for(int col=0; col<numNodes; col++)
		{
			columnSums[col] -= connections[row][col];
		}
	}
	
	private void printConnections()
	{
		System.out.print("   ");
		for(int col=0; col<numNodes; col++)
		{
			System.out.print(col+" ");	
		}
		System.out.println("\n + ");
		
		for(int row=0; row<numNodes; row++)
		{
			if(orderNumbersByNode[row]==-1)
			{
				System.out.print(row+"  ");
				for(int col=0; col<numNodes; col++)
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
		for(int i=0; i<numNodes; i++)
		{
			if(orderNumbersByNode[i]!=-1)
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
		for(int i=0; i<numNodes; i++)
		{
			if(orderNumbersByNode[i]==-1)
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
			if(result!=null)
			{
				System.out.print("\n   Sorted Nodes:\n   ");
				for(int i=0; i<result.length; i++)
				{
					System.out.print(result[i]+ " ");
				}
				System.out.println();
			}
			else 
			{
				System.out.println("This graph has a cycle and cannot be sorted.");
			}
		}
		else 
		{
			System.out.println("A valid file was not received.\nEXIT");
		}
		
	}
	
	public static int[][] getConnectionsFromFile()
	{
		
		int numAttempts = 5;
		int[][] connections;
		
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
					connections = new int[numNodes][numNodes];
					
					// read and store the connections
					for(int row=0; row<numNodes; row++)
					{
						for(int col=0; col<numNodes; col++)
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

*/