import java.util.Random;

public class EngineMultBlocks
{
	// STATES
	
	int[][] table; // table for playing
	int score = 0; // sum of the points
	int level = 1;
	TypesOfTheGameFlow gameFlow = TypesOfTheGameFlow.IN_PROGRESS; // current state of the game
	int currentProduct;
	int[] unsolvedNumbers; // numbers that user failed to solve in properly time
	
	public EngineMultBlocks()
	{
		table = new int[6][6];
		unsolvedNumbers = new int[9];
		
		initialInitialization();
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
		if (firstI >= 0 && firstI <= 5 && firstJ >= 0 && firstJ <= 5 &&
				secondI >= 0 && secondI <= 5 && secondJ >= 0 && secondJ <= 5)
		{
			// we can't select numbers that are gone
			if (table[firstI][firstJ] != -1 && table[secondI][secondJ] != -1)
			{
				if ((Math.abs(firstI - secondI) == 1 || Math.abs(firstI - secondI) == 0) &&
						(Math.abs(firstJ - secondJ) == 1 || Math.abs(firstJ - secondJ) == 0))
				{
					if (table[firstI][firstJ] * table[secondI][secondJ] == currentProduct)
					{
						score += 25;
						
						// treba jos dodati da se kockice spustaju jedna do druge
						// table[firstI][firstJ] = -1;
						// table[secondI][secondJ] = -1;
						int ii = -1; // gde treba da postavim prvi element
						int jj = -1; // index poslednjeg elementa kojeg treba da pomerim
						
						// ovo je ako su izabrane kockice u istoj koloni
						if (firstJ == secondJ)
						{
							if (firstI > secondI)
							{
								ii = firstI;
								for (int i = secondI-1; i >= 0 ; i--)
								{
									if (table[i][firstJ] != -1)
									{
										jj = i;
									}
									else
									{
										break;
									}
								}
								
								secondI -= 1; // da ne obuhvatimo kockicu koja je kliknuta
								while (true)
								{
									if (secondI < jj)
									{
										break; // ili return
									}
									
									if (ii >= 0 && secondI >= 0)
										table[ii][firstJ] = table[secondI][firstJ];
									ii -= 1;
									secondI -= 1;
								}
								
								table[jj][firstJ] = -1;
								table[jj+1][firstJ] = -1;
							}
							else if (secondI >= firstI)
							{
								ii = secondI;
								for (int i = firstI-1; i >= 0 ; i--)
								{
									if (table[i][secondJ] != -1)
									{
										jj = i;
									}
									else
									{
										break;
									}
								}
								
								firstI -= 1; // da ne obuhvatimo kockicu koja je kliknuta
								while (true)
								{
									if (firstI < 0 && firstI < jj)
									{
										break; // ili return
									}
									
									if (ii >= 0 && firstI >= 0)
										table[ii][secondJ] = table[firstI][secondJ];
									ii -= 1;
									firstI -= 1;
								}
								
								//System.out.println("jj = " + jj + " secondJ = " + secondJ);
								table[jj][secondJ] = -1;
								table[jj+1][secondJ] = -1;
							}
						}
						else
						{
							// this is for the first column firstJ
							ii = firstI;
							for (int i = firstI-1; i >= 0 ; i--)
							{
								if (table[i][firstJ] != -1)
								{
									jj = i;
								}
								else
								{
									break;
								}
							}
							
							firstI -= 1;
							while (true)
							{
								if (firstI < jj)
								{
									break; // ili return
								}
								
								if (ii >= 0 && firstI >= 0)
									table[ii][firstJ] = table[firstI][firstJ];
								ii -= 1;
								firstI -= 1;
							}
							
							table[jj][firstJ] = -1;
							
							// this is for the second column secondJ
							ii = secondI;
							for (int i = secondI-1; i >= 0 ; i--)
							{
								if (table[i][secondJ] != -1)
								{
									jj = i;
								}
								else
								{
									break;
								}
							}
							
							secondI -= 1; // da ne obuhvatimo kockicu koja je kliknuta
							while (true)
							{
								if (secondI < jj)
								{
									break; // ili return
								}
								
								if (ii >= 0 && secondI >= 0)
									table[ii][secondJ] = table[secondI][secondJ];
								ii -= 1;
								secondI -= 1;
							}
							
							table[jj][secondJ] = -1;
						}
						
						// find new product to find solution
						currentProduct = giveRandomProduct();
						
						checkStateOfTheMatrix();
						
						if (gameFlow == TypesOfTheGameFlow.NEXT_LVL)
						{
							nextLevelInitialization();
						}
		
						return true;
					}
					else
						appendUnsolvedNumber(); // ovo stoji samo privremeno za testiranje
				}
			}
		}
		
		return false;
	}
	
	private void checkStateOfTheMatrix()
	{
		for (int i = 0; i < table.length; i++)
		{
			for (int j = 0; j < table.length; j++)
			{
				if (table[i][j] != -1)
				{
					return;
				}
			}
		}
		
		// matrix solved go to the next level
		gameFlow = TypesOfTheGameFlow.NEXT_LVL;
	}

	boolean isItEnd()
	{
		if (gameFlow == TypesOfTheGameFlow.GAME_OVER)
		{
			return true;
		}
		
		return false;
	}
	
	void initialInitialization()
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
		gameFlow = TypesOfTheGameFlow.IN_PROGRESS; // zameniti za status igre
		
		currentProduct = giveRandomProduct();
		
		for (int i = 0; i < unsolvedNumbers.length; i++)
		{
			unsolvedNumbers[i] = -1;
		}
	}
	
	private void nextLevelInitialization()
	{
		level += 1;
		
		Random r = new Random();
		for (int i = 0; i < 6; i++)
		{
			for (int j = 0; j < 6; j++)
			{
				table[i][j] = r.nextInt(7);
			}
		}
		
		gameFlow = TypesOfTheGameFlow.IN_PROGRESS;
		currentProduct = giveRandomProduct();
	}
	
	// method that appends currentProduct at the first unused place from the end to the beginning of unsolvedNumbers array
	void appendUnsolvedNumber()
	{
		for (int i = 8; i >= 0; i--)
		{
			if (unsolvedNumbers[i] == -1)
			{
				unsolvedNumbers[i] = currentProduct;
				currentProduct = giveRandomProduct();
				return;
			}
		}
		
		// max of unsolved numbers reached
		gameFlow = TypesOfTheGameFlow.GAME_OVER;
	}
	
	int getNumber(int i, int j)
	{
		return table[i][j];
	}
	
	int getUnsolvedNumber(int i)
	{
		return unsolvedNumbers[i];
	}
	
	@Override
	public String toString()
	{
		// TODO Auto-generated method stub
		String poruka = "";
		
		poruka += "Level: " + level + "\n";
		poruka += "Number: " + currentProduct + "\n";
		poruka += "Score: " + score + "\n";
		poruka += "UnsolvedNumbers: ";
		for (int i = 0; i < unsolvedNumbers.length; i++)
		{
			poruka += unsolvedNumbers[i] + " ";
		}
		poruka += "\n\n";
		
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

	public int getScore()
	{
		return score;
	}

	public int getLevel()
	{
		return level;
	}

	public int getCurrentProduct()
	{
		return currentProduct;
	}
}
