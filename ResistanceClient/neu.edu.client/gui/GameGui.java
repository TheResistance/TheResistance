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

import networking.Client;

public class GameGui extends JFrame 
{
	private Client client;
	
	private JPanel panel;
	private JButton acceptButton;
	private JButton rejectButton;
	
	private JLabel selfLabel;
	private JLabel turnLabel;
	private JLabel factionLabel;
	private JLabel turnRoleLabel;
	private JLabel otherSpyLabel;
	private JLabel selectedTeamLabel;
	private JLabel gameMessagesLabel;
	
	private JTextField selfText;
	private JTextField turnText;
	private JTextField factionText;
	private JTextField turnRoleText;
	private JTextField otherSpyText;
	private JTextField selectedTeamText;
	private JTextField gameMessages;
	
	private String turn = "";
	private String currentRole = "";
	private String playerFaction;
	private int playerNumber;
	private boolean isLeader = false;
	private boolean isMissionParticipant = false;
	private List<String> selectedTeam = new ArrayList<String>();

    public GameGui(Client c, int player, String faction, String otherSpy) 
    {
    	this.client = c;
    	playerFaction = faction;
    	
		panel = new JPanel();
		getContentPane().add(panel);
		
		panel.setLayout(null);
		
		/*
		 * Player number
		 */
		selfLabel = (player == 0) ? new JLabel() : new JLabel("Player " + player);
		selfLabel.setBounds(50, 10, 200, 40);
		Font myFont = new Font("Times New Roman", Font.BOLD, 36);
		selfLabel.setFont(myFont);
		
		/*
		 * Faction or the player.
		 * Faction text shows actual faction.
		 */
		factionLabel = new JLabel("Faction:");
		factionLabel.setBounds(255, 10, 200, 40);
		myFont = new Font("Times New Roman", Font.BOLD, 36);
		factionLabel.setFont(myFont);
		
		factionText = new JTextField(playerFaction);
		factionText.setFont(myFont);
		factionText.setBounds(400, 10, 200, 40);
		factionText.setEditable(false);
		factionText.setBorder(null);
		
		myFont = new Font("Sans Serif", Font.PLAIN, 18);
		/*
		 * Says who the other spy is if the player is a spy
		 */
		otherSpyLabel = new JLabel("Other Spy:");
		otherSpyLabel.setBounds(320, 50, 200, 20);
		
		otherSpyText = new JTextField(otherSpy);
		otherSpyText.setFont(myFont);
		otherSpyText.setBounds(400, 50, 200, 20);
		otherSpyText.setEditable(false);
		otherSpyText.setBorder(null);
		
		/*
		 * States what the player is doing this turn.
		 */
		turnRoleLabel = new JLabel("Role this turn: ");
		turnRoleLabel.setBounds(50, 100, 100, 20);
		
		turnRoleText = new JTextField(currentRole);
		turnRoleText.setFont(myFont);
		turnRoleText.setBounds(150, 95, 400, 30);
		turnRoleText.setEditable(false);
		turnRoleText.setBorder(null);
		
		turnLabel = new JLabel("Current turn: ");
		turnLabel.setBounds(50, 150, 200, 30);
		
		/*
		 * States who's turn it currently is.
		 */
		turnText = new JTextField(turn);
		turnText.setFont(myFont);
		turnText.setBounds(150, 150, 400, 30);
		turnText.setEditable(false);
		turnText.setBorder(null);
		
		/*
		 * Currently selected team for a mission.
		 */
		selectedTeamLabel = new JLabel("Selected team: ");
		selectedTeamLabel.setBounds(50, 200, 200, 30);
		
//		selectedTeam.add("Player 1");
//		selectedTeam.add("Player 4");
//		selectedTeam.add("Player 5");
		
		String tempTeam = "   ";
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
		
		/*
		 * This field has all the game communication.
		 */
		gameMessagesLabel = new JLabel("Messages");
		gameMessagesLabel.setBounds(25, 250, 525, 20);
		
		myFont = new Font("Sans Serif", Font.PLAIN, 10);
		gameMessages = new JTextField();
		gameMessages.setFont(myFont);
		gameMessages.setBounds(25, 270, 525, 225);
		gameMessages.setEditable(false);
		
		
		/*
		 * Accept and Reject buttons.
		 * Run validation on current information before sending to server.
		 */
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
		
		/*
		 * Add everything to the panel.
		 */
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
		panel.add(gameMessagesLabel);
		panel.add(gameMessages);
		panel.add(acceptButton);
		panel.add(rejectButton);

		setTitle("The Resistance");
		setSize(600, 600);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
		
		if (playerFaction != "Spy") hideSpyInformation();
    }
    
    private void createTeamSelectionGui()
    {
    	TeamSelectionGui selGui = new TeamSelectionGui(3);
    	selGui.setVisible(true);
    }
    
    public void hideSpyInformation()
    {
    	otherSpyLabel.setVisible(false);
    	otherSpyText.setVisible(false);
    	panel.updateUI();
    }
    
    public void showSpyInformation()
    {
    	otherSpyLabel.setVisible(true);
    	otherSpyText.setVisible(true);
    }
    
    public void setTurnRole(String role)
    {
    	currentRole = role;
    	turnRoleText.setText(currentRole);
		panel.updateUI();
    }
    
    public void setCurrentPlayer(String curPlayer)
    {
    	turn = curPlayer;
    	turnText.setText(turn);
    	panel.updateUI();
    }
    
    public void setCurrentlySelectedTeam(List<String> team)
    {
    	selectedTeam = new ArrayList<String>();
    	String tempTeam = new String();
    	for (String member : team)
    	{
    		selectedTeam.add(member);
			tempTeam += ", " + member;
		}
		tempTeam = tempTeam.substring(1);
    	selectedTeamText.setText(tempTeam);
    	panel.updateUI();
    }
    
    public void sendClientMessage(String message)
    {
    	gameMessages.setText(gameMessages.getText() + "\n" + message);
    	panel.updateUI();
    }
    
    public void updatePlayerNumber(int num)
    {
    	playerNumber = num;
    	selfLabel.setText("Player " + num);
    	panel.updateUI();
    }
}
