import javax.swing.JButton;

public class MyButton extends JButton
{
	private int i;
	private int j;
	
	public MyButton(int i, int j)
	{
		this.i = i;
		this.j = j;
	}

	public int getI()
	{
		return i;
	}

	public int getJ()
	{
		return j;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "i= " + i + ", j = " + j;
	}
}
