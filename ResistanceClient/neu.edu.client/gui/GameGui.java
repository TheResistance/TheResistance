package gui;
/*
Created by: Britton Horn
Group: The Resistance

Date: March 22, 2014
*/

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class GameGui extends JFrame 
{
	private JPanel panel;
	private JButton acceptButton;
	private JButton rejectButton;
	
	private JLabel selfLabel;
	private JLabel turnLabel;
	private JLabel factionLabel;
	private JLabel turnRoleLabel;
	private JLabel otherSpyLabel;
	private JLabel selectedTeamLabel;
	
	private JTextField selfText;
	private JTextField turnText;
	private JTextField factionText;
	private JTextField turnRoleText;
	private JTextField otherSpyText;
	private JTextField selectedTeamText;
	
	private String turn = "Player 5";
	private List<String> selectedTeam = new ArrayList<String>();

    public GameGui(int player, String role, String otherSpy) 
    {
		panel = new JPanel();
		getContentPane().add(panel);
		
		panel.setLayout(null);
		
		selfLabel = new JLabel("Player " + player);
		selfLabel.setBounds(50, 10, 200, 40);
		Font myFont = new Font("Times New Roman", Font.BOLD, 36);
		selfLabel.setFont(myFont);
		
		factionLabel = new JLabel("Faction:");
		factionLabel.setBounds(255, 10, 200, 40);
		myFont = new Font("Times New Roman", Font.BOLD, 36);
		factionLabel.setFont(myFont);
		
		factionText = new JTextField(role);
		factionText.setFont(myFont);
		factionText.setBounds(400, 10, 200, 40);
		factionText.setEditable(false);
		factionText.setBorder(null);
		
		myFont = new Font("Sans Serif", Font.PLAIN, 18);
		otherSpyLabel = new JLabel("Other Spy:");
		otherSpyLabel.setBounds(320, 50, 200, 20);
		
		otherSpyText = new JTextField(otherSpy);
		otherSpyText.setFont(myFont);
		otherSpyText.setBounds(400, 50, 200, 20);
		otherSpyText.setEditable(false);
		otherSpyText.setBorder(null);
		
		turnRoleLabel = new JLabel("Role this turn: ");
		turnRoleLabel.setBounds(50, 100, 100, 20);
		
		turnRoleText = new JTextField("Voting on mission participants");
		turnRoleText.setFont(myFont);
		turnRoleText.setBounds(150, 95, 400, 30);
		turnRoleText.setEditable(false);
		turnRoleText.setBorder(null);
		
		turnLabel = new JLabel("Current turn: ");
		turnLabel.setBounds(50, 150, 200, 30);
		
		turnText = new JTextField(turn);
		turnText.setFont(myFont);
		turnText.setBounds(150, 150, 400, 30);
		turnText.setEditable(false);
		turnText.setBorder(null);
		
		selectedTeamLabel = new JLabel("Selected team: ");
		selectedTeamLabel.setBounds(50, 200, 200, 30);
		
		selectedTeam.add("Player 1");
		selectedTeam.add("Player 4");
		selectedTeam.add("Player 5");
		
		String tempTeam = "";
		for (String member : selectedTeam)
		{
			tempTeam += ", " + member;
		}
		tempTeam = tempTeam.substring(1);
		
		selectedTeamText = new JTextField(tempTeam);
		selectedTeamText.setFont(myFont);
		selectedTeamText.setBounds(150, 200, 400, 30);
		selectedTeamText.setEditable(false);
		selectedTeamText.setBorder(null);
		
		acceptButton = new JButton("Accept");
		acceptButton.setBounds(100, 500, 150, 50);
		
		acceptButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				createTeamSelectionGui();
			}
		});
		
		rejectButton = new JButton("Reject");
		rejectButton.setBounds(350, 500, 150, 50);
		
		acceptButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
			
			}
		});
		
		panel.add(selfLabel);
		panel.add(factionLabel);
		panel.add(factionText);
		panel.add(otherSpyLabel);
		panel.add(otherSpyText);
		panel.add(turnRoleLabel);
		panel.add(turnRoleText);
		panel.add(turnLabel);
		panel.add(turnText);
		panel.add(selectedTeamLabel);
		panel.add(selectedTeamText);
		panel.add(acceptButton);
		panel.add(rejectButton);

		setTitle("The Resistance");
		setSize(600, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);        
    }
    
    private void createTeamSelectionGui()
    {
    	TeamSelectionGui selGui = new TeamSelectionGui();
    	selGui.setVisible(true);
    }
    
}
