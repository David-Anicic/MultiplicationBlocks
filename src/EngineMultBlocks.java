import java.awt.Image;
import java.util.Random;

import javax.swing.ImageIcon;

import sun.util.resources.cldr.ur.CurrencyNames_ur;

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
		// povecavamo brojac <<brojacRandom>> i ako dostigne odredjeni broj
		// biramo rucno proizvod za nalazenje
		// (bira se neki od brojeva iz prvog reda odozdo)
		int brojacRandom = 0; 
		while(true)
		{
			if (brojacRandom == 2)
			{
				i = 5;
				j = 0;// ovo mogu da izmenim da prvi red trazi random
			}
			else
			{
				i = r.nextInt(5);
				j = r.nextInt(5);
			}
			
			if (table[i][j] != -1)
			{
				System.out.println("table[i][j] = " + table[i][j]);
				while(true)
				{
					if (brojacRandom == 2)
					{
						br = 1;
						if (table[i-1][j]!=-1)
							br = 0;
						if (table[i][j+1]!=-1)
							br = 1;
						//br = r.nextInt(1);
					}
					else
						br = r.nextInt(3);
					System.out.println("Proveravam komsije br = " + br);
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
			else
				brojacRandom++;
		}
	}
	
	boolean move(int firstI, int firstJ, int secondI, int secondJ)
	{
		System.out.println("Usao u engine-move");
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
					if (!(Math.abs(firstI - secondI) == 1 && Math.abs(firstJ -secondJ) == 1)) // ne smeju da budu izabrane kockice ukuso - kao sto ide kralj sahovska figura
					{
						if (table[firstI][firstJ] * table[secondI][secondJ] == currentProduct)
						{
							score += 25;
							
							// table[firstI][firstJ] = -1;
							// table[secondI][secondJ] = -1;
							int ii = -1; // gde treba da postavim prvi element
							int jj = -1; // index poslednjeg elementa kojeg treba da pomerim
							
							// ovo je ako su izabrane kockice u istoj koloni
							if (firstJ == secondJ)
							{
								if (firstI > secondI) // prva kockica se nalazi ISPOD druge
								{
									ii = firstI;
									jj = 0;
									// trazimo poslednji index koji treba da pomerimo
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
									while (secondI >= jj)
									{	
										if (ii >= 0 && secondI >= 0)
											table[ii][firstJ] = table[secondI][firstJ];
										ii -= 1;
										secondI -= 1;
									}
									
									if (jj != -1)
									{
										table[jj][firstJ] = -1;
										table[jj+1][firstJ] = -1;
									}
								}
								else if (secondI >= firstI)
								{
									ii = secondI;
									jj = 0;
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
									while (firstI >= 0 && firstI >= jj)
									{
										/*
										if (firstI < 0 && firstI < jj)
										{
											break;
										}
										*/
										
										if (ii >= 0 && firstI >= 0)
											table[ii][secondJ] = table[firstI][secondJ];
										ii -= 1;
										firstI -= 1;
									}
									
									//System.out.println("jj = " + jj + " secondJ = " + secondJ);
									if (jj != -1)
									{
										table[jj][secondJ] = -1;
										table[jj+1][secondJ] = -1;
									}
								}
							}
							else // kockice nisu u istoj koloni
							{
								// this is for the first column firstJ
								ii = firstI;
								jj = 0;
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
								while (firstI >= jj)
								{
									/*
									if (firstI < jj)
									{
										break;
									}
									*/
									
									if (ii >= 0 && firstI >= 0)
										table[ii][firstJ] = table[firstI][firstJ];
									ii -= 1;
									firstI -= 1;
								}
								
								if (jj != -1)
									table[jj][firstJ] = -1;
								
								// this is for the second column secondJ
								ii = secondI;
								jj = 0;
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
								while (secondI >= jj)
								{
									/*
									if (secondI < jj)
									{
										break; // ili return
									}
									*/
									
									if (ii >= 0 && secondI >= 0)
										table[ii][secondJ] = table[secondI][secondJ];
									ii -= 1;
									secondI -= 1;
								}
								
								if (jj != -1)
									table[jj][secondJ] = -1;
							}
							
							// if column is empty - do shifting
							System.out.println("Siftovanje svih kolona zapoceto!");
							int pom;	
							for (int j = table.length-2; j >= 0; j--) // go through the columns from behind (column 5)
							{
								pom = 1;
								
								if (table[table.length-1][j] == -1) // if there is not number <> -1 of the end of column - there is no numbers <> -1 in column
									pom = 0;
								
								// do shifting columns if pom is 0
								if (pom == 0)
								{
									// here we do shifting
									for (int k = 0; k < table.length; k++)
									{
										table[k][j] = table[k][j+1];
										table[k][j+1] = -1;
									}
									
									// now shift other columns to the left
									//if (table[table.length-1][j+2] != -1) // nisam siguran da ovo moze da se pita
									for (int k = j+1; k <= table.length-2; k++)
									{
										for (int i = 0; i < table.length; i++)
										{
											table[i][k] = table[i][k+1];
											table[i][k+1] = -1;
										}
									}
								}
							}
							System.out.println("Siftovanje svih kolona zavrseno!");
							
							checkStateOfTheMatrix();
							
							if (gameFlow == TypesOfTheGameFlow.NEXT_LVL)
							{
								nextLevelInitialization();
							}
							else if(gameFlow != TypesOfTheGameFlow.WIN)
							{
								// find new product to find solution
								System.out.println("Ulazim u giveRandomProduct");
								currentProduct = giveRandomProduct();
								System.out.println("Izasao iz giveRandomProduct");
							}
			
							System.out.println("Izasao iz Engine-move\n");
							return true;
						}
						else
						{
							appendUnsolvedNumber(); // ovo stoji samo privremeno za testiranje
							
							if (level == 3) // ovo vazi samo za lvl 3
							{
								score -= 10; // ukoliko izabrane dve kockice ne daju trazeni proizvod
											 // score se umanjuje za 10
								if (score < 0)
								{
									gameFlow = TypesOfTheGameFlow.GAME_OVER;
								}
							}
						}
					}
				}
			}
		}
		
		System.out.println("Izasao iz Engine-move\n");
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
		
		// matrix solved go to the next level or y won
		if (level == 3)
		{
			gameFlow = TypesOfTheGameFlow.WIN;
		}
		else
		{
			gameFlow = TypesOfTheGameFlow.NEXT_LVL;
		}
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
				int pom = r.nextInt(7);
				if (level > 1 && pom == 0)
					table[i][j] = pom+1; // na levelu 2 nemamo 0 kao broj - najmanji broj je 1
				else
					table[i][j] = pom;
			}
		}
		
		// gameFlow = TypesOfTheGameFlow.IN_PROGRESS;
		currentProduct = giveRandomProduct();
	}
	
	// method that appends currentProduct at the first unused place from the end to the beginning of unsolvedNumbers array
	void appendUnsolvedNumber()
	{
		for (int i = 8; i >= 1; i--)
		{
			if (unsolvedNumbers[i] == -1)
			{
				unsolvedNumbers[i] = currentProduct;
				currentProduct = giveRandomProduct();
				return;
			}
		}
		
		// max of unsolved numbers reached
		unsolvedNumbers[0] = currentProduct;
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
	
	ImageIcon getImage(int i, int j)
	{
		ImageIcon ii = new ImageIcon("images/"+table[i][j]+".png");
		ii.setImage(ii.getImage().getScaledInstance(50, 50, Image.SCALE_AREA_AVERAGING));
		
		return ii;
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

	public boolean goToTheNextLvl()
	{
		if (this.gameFlow == TypesOfTheGameFlow.NEXT_LVL)
		{
			gameFlow = TypesOfTheGameFlow.IN_PROGRESS;
			return true;
		}
		
		return false;
	}

	public boolean win()
	{
		if (gameFlow == TypesOfTheGameFlow.WIN)
		{
			return true;
		}
		
		return false;
	}
}
