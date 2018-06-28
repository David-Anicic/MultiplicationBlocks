import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import javafx.scene.layout.Border;

public class GUI extends JFrame
{
	EngineMultBlocks engine;
	MyButton[][] buttons;
	
	JPanel middle;
	JPanel header;
	JPanel footer;
	JPanel side;
	
	JLabel levelLabel;
	JLabel scoreLabel;
	JLabel timeLabel;
	JButton[] sideButtons;
	
	MyButton clickedButton1;
	MyButton clickedButton2;
	
	public GUI()
	{
		super("Multiplication blocks");
		
		engine = new EngineMultBlocks();

		setHeader();
		setMiddle();
		setSide();
		
		refreshGUI();
		
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pack();
	}
	
	private void refreshGUI()
	{
		scoreLabel.setText("Score: " + engine.getScore());
		levelLabel.setText("Level: " + engine.getLevel());
		
		int a;
		for (int i = 0; i < buttons.length; i++)
		{
			for (int j = 0; j < buttons.length; j++)
			{	
				a = engine.getNumber(i, j);
				buttons[i][j].setText(a != -1 ? a+"" : "" );
			}
		}
		
		for (int i = 0; i < sideButtons.length; i++)
		{
			a = engine.getUnsolvedNumber(i);
			if (a != -1)
				sideButtons[i].setBackground(Color.BLACK);
		}
		sideButtons[0].setText(engine.getCurrentProduct()+"");
	}

	private void setSide()
	{
		side = new JPanel(new GridLayout(9, 1, 1, 1));
		
		sideButtons = new JButton[9];
		for (int i = 0; i < sideButtons.length; i++)
		{
			sideButtons[i] = new JButton();
			//sideButtons[i].setPreferredSize(new Dimension(50, 50));
			
			side.add(sideButtons[i]);
		}
		
		getContentPane().add(side, BorderLayout.EAST);
	}

	private void setHeader()
	{
		header = new JPanel();
		
		levelLabel = new JLabel("Level: 1");
		timeLabel = new JLabel("Time: 2:00");
		scoreLabel = new JLabel("Score: 0");

		header.add(timeLabel, BorderLayout.EAST);
		header.add(scoreLabel, BorderLayout.CENTER);
		header.add(levelLabel,BorderLayout.WEST);
		
		getContentPane().add(header, BorderLayout.NORTH);
	}

	private void setMiddle()
	{
		middle = new JPanel(new GridLayout(6, 6, 1, 1));
		buttons = new MyButton[6][6];
		
		for (int i = 0; i < buttons.length; i++)
		{
			for (int j = 0; j < buttons.length; j++)
			{	
				buttons[i][j] = new MyButton(i, j);
				buttons[i][j].setPreferredSize(new Dimension(50, 50));
				buttons[i][j].setForeground(Color.BLACK);
				
				buttons[i][j].addActionListener(new ActionListener()
				{	
					@Override
					public void actionPerformed(ActionEvent e)
					{
						MyButton mb = (MyButton)e.getSource();
						
						if (clickedButton1 == null)
							clickedButton1 = mb;
						else
							clickedButton2 = mb;
						
						if (clickedButton1 != null && clickedButton2 != null)
						{
							if (engine.move(clickedButton1.getI(), clickedButton1.getJ(), clickedButton2.getI(), clickedButton2.getJ()))
								refreshGUI();
							
							clickedButton1 = null;
							clickedButton2 = null;
						}
					}
				});
				
				middle.add(buttons[i][j]);
			}
		}
		
		getContentPane().add(middle, BorderLayout.CENTER);
	}
}
