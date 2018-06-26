import java.util.Random;

public class EngineMultBlocks
{
	// STATES
	
	int[][] table; // table for playing
	int score = 0; // sum of the points
	int level = 1;
	TypesOfTheGameFlow gameFlow = TypesOfTheGameFlow.IN_PROGRESS; // current state of the game
	int currentProduct;
	
	public EngineMultBlocks()
	{
		table = new int[6][6];
		
		inicijalizuj();
	}
	
	int giveRandomProduct()
	{
		// dodati brojac ako mu mnogo treba da odredi random proizvod
		Random r = new Random();
		int i;
		int j;
		int br;
		while(true)
		{
			i = r.nextInt(5);
			j = r.nextInt(5);
			
			if (table[i][j] != -1)
			{
				while(true)
				{
					br = r.nextInt(3);
					if (br == 0 && i-1 >= 0 && table[i-1][j] != -1) // we choose UP neighbor
						return table[i][j] * table[i-1][j];
					else if (br == 1 && j+1 <= 6 && table[i][j+1] != -1) // we choose RIGHT neighbor
						return table[i][j] * table[i][j+1];
					else if (br == 2 && i+1 <= 6 && table[i+1][j] != -1) // we choose DOWN neighbor
						return table[i][j] * table[i+1][j];
					else if (br == 3 && j-1 >= 0 && table[i][j-1] != -1) // we choose BOTTOM neighbor
						return table[i][j] * table[i][j-1];
						
				}
			}
		}
	}
	
	boolean move(int firstI, int firstJ, int secondI, int secondJ)
	{
		// checking if the coordinates are correct
		if ((Math.abs(firstI - secondI) == 1 || Math.abs(firstI - secondI) == 0) &&
				(Math.abs(firstJ - secondJ) == 1 || Math.abs(firstJ - secondJ) == 0))
		{
			if (table[firstI][firstJ] * table[secondI][secondJ] == currentProduct)
			{
				score += 25;
				
				// treba jos dodati da se kockice spustaju jedna do druge
				table[firstI][firstJ] = -1;
				table[secondI][secondJ] = -1;
				
				// find new product to find solution
				currentProduct = giveRandomProduct();

				return true;
			}
		}
		
		return false;
	}
	
	boolean isItEnd()
	{
		for (int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				if (table[i][j] != -1)
				{
					return false;
				}
			}
		}
		
		return true;
	}
	
	void inicijalizuj()
	{
		Random r = new Random();
		for (int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				table[i][j] = r.nextInt(7);
			}
		}
		
		score = 0;
		level = 1;
		gameFlow = TypesOfTheGameFlow.IN_PROGRESS;
		
		currentProduct = giveRandomProduct();
	}
	
	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		String poruka = "";
		
		poruka += "Level: " + level + "\n";
		poruka += "Number: " + currentProduct + "\n";
		poruka += "Score: " + score + "\n\n";
		
		for (int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				if (table[i][j] == -1)
					poruka += "  ";
				else
					poruka += table[i][j] + " ";
			}
			
			poruka += "\n";
		}
		
		return poruka;
	}
}
