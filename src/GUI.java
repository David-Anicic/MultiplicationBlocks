import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;

public class GUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	
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
	Timer t;
	int sekunde = 0;
	int minuti = 2;
	Timer t2;
	
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
	
	void refreshGUI()
	{
		scoreLabel.setText("Score: " + engine.getScore());
		levelLabel.setText("Level: " + engine.getLevel());
		
		int a;
		for (int i = 0; i < buttons.length; i++)
		{
			for (int j = 0; j < buttons.length; j++)
			{	
				//a = engine.getNumber(i, j);
				//buttons[i][j].setText(a != -1 ? a+"" : "" );
				
				buttons[i][j].setIcon(engine.getImage(i, j));
			}
		}
		
		// ovo mi nece trebati kada namestim sa tajmerom
		for (int i = 0; i < sideButtons.length; i++)
		{
			a = engine.getUnsolvedNumber(i);
			System.out.println(a);
			if (a != -1) {
				sideButtons[i].setBackground(Color.GRAY); System.out.println("ulazi");}
		}
		sideButtons[0].setText(engine.getCurrentProduct()+"");
		
		pack();
	}

	private void setSide()
	{
		side = new JPanel(new GridLayout(9, 1, 1, 1));
		side.setBackground(Color.WHITE);
		
		sideButtons = new JButton[9];
		for (int i = 0; i < sideButtons.length; i++)
		{
			sideButtons[i] = new JButton();
			sideButtons[i].setBackground(Color.WHITE);
			sideButtons[i].setBorder(new LineBorder(Color.WHITE));
			sideButtons[i].setPreferredSize(new Dimension(20, 30));
			
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
		
		t = new Timer(1000, new ActionListener()
		{	
			
			
			public void actionPerformed(ActionEvent e)
			{
				sekunde -= 1;
				
				if (sekunde < 0)
				{
					sekunde = 59;
					minuti -= 1;
				}
				
				if (minuti < 0)
				{
					t.stop();
					
					int op = JOptionPane.showConfirmDialog(null, "Game over!\nYour score is: " + engine.getScore() + "\n\nDo you want new game?", "Game over.", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (op == JOptionPane.YES_OPTION)
					{
						engine.initialInitialization();
						refreshGUI();
						
						minuti = 2;
						sekunde = 0;
						t.start();
					}
					else if (op == JOptionPane.NO_OPTION)
					{
						System.exit(0);
					}
				}
				else
					timeLabel.setText("Time: " + minuti + ":" + sekunde);
			}
		});
		
		t.start();
		
		getContentPane().add(header, BorderLayout.NORTH);
	}

	private void setMiddle()
	{
		middle = new JPanel(new GridLayout(6, 6, 1, 1));
		middle.setBackground(Color.WHITE);
		
		buttons = new MyButton[6][6];
		
		for (int i = 0; i < buttons.length; i++)
		{
			for (int j = 0; j < buttons.length; j++)
			{	
				buttons[i][j] = new MyButton(i, j);
				buttons[i][j].setPreferredSize(new Dimension(50, 50));
				buttons[i][j].setForeground(Color.BLACK);
				buttons[i][j].setBackground(Color.WHITE);
				buttons[i][j].setBorder(null);
				
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
							
							refreshGUI();
							
							if (engine.isItEnd())
							{
								t.stop();
								
								int op = JOptionPane.showConfirmDialog(null, "Game over!\nYour score is: " + engine.getScore() + "\n\nDo you want new game?", "Game over.", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
								if (op == JOptionPane.YES_OPTION)
								{
									sekunde = 0;
									minuti = 2;
									t.start();
									
									engine.initialInitialization();
									refreshGUI();
								}
								else if (op == JOptionPane.NO_OPTION)
								{
									System.exit(0);
								}
							}
							
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
