import java.io.InputStream;
import java.util.Scanner;

public class Test
{
	public static void main(String[] args)
	{
		EngineMultBlocks engine = new EngineMultBlocks();
		
		System.out.println(engine);
		
		Scanner s = new Scanner(System.in);
		int fi, fj, si, sj;
		while (!engine.isItEnd())
		{
			fi = s.nextInt();
			fj = s.nextInt();
			si = s.nextInt();
			sj = s.nextInt();
			
			engine.move(fi, fj, si, sj);
			
			System.out.println(engine);
		}
		
		s.close();
	}
}
