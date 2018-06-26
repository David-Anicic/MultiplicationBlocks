import java.util.Random;

public class EngineMultBlocks
{
	// STATES
	
	int[][] table; // tabla za igranje
	int score = 0; // ukupan broj postignutih poena
	int level = 1;
	TypesOfTheGameFlow gameFlow = TypesOfTheGameFlow.IN_PROGRESS; // koje je trenutno stanje igre
	int currentProduct;
	
	public EngineMultBlocks()
	{
		table = new int[6][6];
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
			i = r.nextInt(6);
			j = r.nextInt(6);
			
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
		if (Math.abs(firstI - secondI) == 1 && Math.abs(secondJ - secondJ) == 1)
		{
			if (table[firstI][firstJ] * table[secondI][secondJ] == currentProduct)
			{
				score += 25;
				currentProduct = giveRandomProduct();
				// treba jos dodati da se kockice spustaju jedan do druge

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
		// inic tablu za nivo 1
		Random r = new Random();
		for (int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				table[i][j] = r.nextInt(7);
			}
		}
		
		score = 0;
		// promeni cilj brojeve
		level = 1;
		gameFlow = TypesOfTheGameFlow.IN_PROGRESS;
		
		currentProduct = giveRandomProduct();
	}
}
