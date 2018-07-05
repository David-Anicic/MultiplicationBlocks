import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
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
	JPanel scorePanel;
	JPanel timePanel;
	JPanel levelPanel;
	
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
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		pack();
	}
	
	void refreshGUI()
	{
		scoreLabel.setText("Score: " + engine.getScore());
		
		levelLabel.setText("Level: " + engine.getLevel());
		
		int a;
		int b;
		for (int i = 0; i < buttons.length; i++)
		{
			for (int j = 0; j < buttons.length; j++)
			{	
				//a = engine.getNumber(i, j);
				//buttons[i][j].setText(a != -1 ? a+"" : "" );
				
				buttons[i][j].setIcon(engine.getImage(i, j));
			}
		}
		
		for (int i = 1; i < sideButtons.length; i++)
		{
			a = engine.getUnsolvedNumber(i);
			b = engine.getUnsolvedNumber(i-1);
			/*
			if (a != -1)
				if (i == 0)
					sideButtons[i].setBackground(Color.RED);
				else
					sideButtons[i].setBackground(Color.BLUE);
			*/
			if (a != -1 && b == -1)
			{
				ImageIcon ii = new ImageIcon("images/eraser.jpg");
				ii.setImage(ii.getImage().getScaledInstance(40, 30, Image.SCALE_AREA_AVERAGING));
				sideButtons[i].setIcon(ii);
			}
			else
			{
				sideButtons[i].setIcon(null);
			}
		}
		
		if (engine.getUnsolvedNumber(0) != -1)
		{
			sideButtons[0].setText("");
			
			ImageIcon ii = new ImageIcon("images/eraser.jpg");
			ii.setImage(ii.getImage().getScaledInstance(40, 30, Image.SCALE_AREA_AVERAGING));
			sideButtons[0].setIcon(ii);
		}
		else
		{
			sideButtons[0].setText(engine.getCurrentProduct()+"");
			sideButtons[0].setIcon(null);
		}
		
		pack();
	}

	private void setSide()
	{
		side = new JPanel(new GridLayout(9, 1, 1, 1));
		side.setBackground(Color.WHITE);
		side.setBorder(new LineBorder(Color.BLUE, 1, true));
		
		sideButtons = new JButton[9];
		for (int i = 0; i < sideButtons.length; i++)
		{
			sideButtons[i] = new JButton();
			if (i == 0)
			{
				sideButtons[i].setBackground(Color.LIGHT_GRAY);
				sideButtons[i].setFont(new Font("Arial", 1, 30));
			}
			else
				sideButtons[i].setBackground(Color.WHITE);
			sideButtons[i].setBorder(new LineBorder(Color.WHITE));
			sideButtons[i].setPreferredSize(new Dimension(40, 30));
			
			side.add(sideButtons[i]);
		}
		
		getContentPane().add(side, BorderLayout.EAST);
	}

	private void setHeader()
	{
		header = new JPanel();
		header.setBorder(new LineBorder(Color.BLUE, 1, true));
		header.setBackground(Color.WHITE);
		
		levelPanel = new JPanel();
		levelPanel.setPreferredSize(new Dimension(113, 30));
		levelLabel = new JLabel("Level: 1");
		levelLabel.setForeground(Color.ORANGE);
		levelPanel.add(levelLabel, BorderLayout.EAST);
		
		timePanel = new JPanel();
		timePanel.setPreferredSize(new Dimension(113, 30));
		timeLabel = new JLabel("Time: 2:00");
		timeLabel.setForeground(Color.magenta);
		timePanel.add(timeLabel, BorderLayout.WEST);
		
		scorePanel = new JPanel();
		scorePanel.setPreferredSize(new Dimension(113,30));
		scoreLabel = new JLabel("Score: 0");
		scoreLabel.setForeground(Color.green);
		scorePanel.add(scoreLabel, BorderLayout.CENTER);

		header.add(timePanel, BorderLayout.WEST);
		header.add(scorePanel, BorderLayout.CENTER);
		header.add(levelPanel,BorderLayout.EAST);
		
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
						for (int i = 0; i < sideButtons.length; i++)
						{
							sideButtons[i].setBackground(Color.WHITE);
						}
						
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
						System.out.println("Dugme1: " + clickedButton1 + " \nDugme2: " + clickedButton2);
						if (clickedButton1 != null && clickedButton2 != null)
						{
							engine.move(clickedButton1.getI(), clickedButton1.getJ(), clickedButton2.getI(), clickedButton2.getJ());
							
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
									for (int i = 0; i < sideButtons.length; i++)
									{
										sideButtons[i].setBackground(Color.WHITE);
									}
								}
								else if (op == JOptionPane.NO_OPTION)
								{
									System.exit(0);
								}
							}
							else if (engine.goToTheNextLvl())
							{
								// risetujemo tajmer
								if (engine.getLevel() == 3)
								{
									sekunde = 30;
									minuti = 1;
								}
								else
								{
									sekunde = 0;
									minuti = 2;
								}
							}
							else if (engine.win())
							{
								t.stop();
								
								int op = JOptionPane.showConfirmDialog(null, "You WON!\nYour score is: " + engine.getScore() + "\n\nDo you want new game?", "End of the game.", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
								if (op == JOptionPane.YES_OPTION)
								{
									sekunde = 0;
									minuti = 2;
									t.start();
									
									engine.initialInitialization();
									
									refreshGUI();
									for (int i = 0; i < sideButtons.length; i++)
									{
										sideButtons[i].setBackground(Color.WHITE);
									}
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
