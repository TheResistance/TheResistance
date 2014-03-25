package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*
Created by: Britton Horn
Group: The Resistance

Date: March 23, 2014
*/

public class TeamSelectionGui extends JFrame
{
	JPanel panel;
	JButton acceptButton;
	JButton rejectButton;
	
	TeamSelectionGui(int choiceCount)
	{
		panel = new JPanel();
		getContentPane().add(panel);
		
		panel.setLayout(null);
//		panel.setLayout(new GridLayout(5, 1));
//	    panel.setAlignmentY(JComponent.LEFT_ALIGNMENT);
		
		JLabel choices = new JLabel("Please choose " + choiceCount + " players.");
		choices.setBounds(125, 25, 200, 20);
		panel.add(choices);
		
		CheckBoxList cbList = new CheckBoxList();
	    JCheckBox player1 = new JCheckBox("Player 1");
	    JCheckBox player2 = new JCheckBox("Player 2");
	    JCheckBox player3 = new JCheckBox("Player 3");
	    JCheckBox player4 = new JCheckBox("Player 4");
	    JCheckBox player5 = new JCheckBox("Player 5");
	    JCheckBox[] myList = { player1, player2, player3, player4, player5};
	    cbList.setListData(myList);
	    cbList.setBounds(100, 50, 200, 150);
	    panel.add(cbList);
	    
		acceptButton = new JButton("Accept");
		acceptButton.setBounds(25, 300, 150, 50);
		
		acceptButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				
			}
		});
		
		rejectButton = new JButton("Reject");
		rejectButton.setBounds(200, 300, 150, 50);
		
		acceptButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
			
			}
		});
		
		panel.add(acceptButton);
		panel.add(rejectButton);
		
		setTitle("Team Selection");
		setSize(400, 400);
		setLocationRelativeTo(null);  
	}
}
