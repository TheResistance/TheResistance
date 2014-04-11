package gui;
/*
Created by: Britton Horn
Group: The Resistance

Date: March 22, 2014
*/

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import core.ClientSendMessage;
import networking.Client;

public class GameGui extends JFrame 
{
	public Client client;
	
	public JPanel panel;
	public JButton acceptButton;
	public JButton rejectButton;
	
	public JLabel selfLabel;
	public JLabel turnLabel;
	public JLabel factionLabel;
	public JLabel turnRoleLabel;
	public JLabel otherSpyLabel;
	public JLabel selectedTeamLabel;
	public JLabel gameMessagesLabel;
	
	public JTextField selfText;
	public JTextField turnText;
	public JTextField factionText;
	public JTextField turnRoleText;
	public JTextField otherSpyText;
	public JTextField selectedTeamText;
	public JTextArea gameMessages;
	
	public JRadioButton accuse = new JRadioButton("Accuse");
	public JRadioButton suggest = new JRadioButton("Group Suggest");
	public JButton submitCommunication;
	
	private JCheckBox player1 = new JCheckBox("Player 1");
    private JCheckBox player2 = new JCheckBox("Player 2");
    private JCheckBox player3 = new JCheckBox("Player 3");
    private JCheckBox player4 = new JCheckBox("Player 4");
    private JCheckBox player5 = new JCheckBox("Player 5");
	
	public String turn = "";
	public String currentRole = "";
	public String playerFaction;
	public int playerNumber;
	public boolean isLeader = false;
	public boolean isMissionParticipant = false;
	public List<Integer> selectedTeam = new ArrayList<Integer>();
	public String phase;
	
	TeamSelectionGui selectionGui;
	
	public GameGui(Client c, int player, String faction, String otherSpy) 
    {
		this(player, faction, otherSpy);
		this.client = c;
    }

    public GameGui(int player, String faction, String otherSpy) 
    {
    	playerFaction = faction;
    	playerNumber = player;
    	
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
		turnRoleText.setFont(new Font("Sans Serif", Font.PLAIN, 12));
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
		for (Integer member : selectedTeam)
		{
			tempTeam += ", Player " + member;
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
		gameMessagesLabel.setBounds(25, 225, 525, 20);
		
		myFont = new Font("Sans Serif", Font.PLAIN, 10);
		gameMessages = new JTextArea();
		gameMessages.setFont(myFont);
		gameMessages.setBounds(25, 245, 525, 125);
		gameMessages.setEditable(false);
		
		/*
		 * This is for communication methods.
		 */
		accuse.setBounds(115, 395, 100, 25);
		suggest.setBounds(115,420, 125, 25);
		panel.add(accuse);
		panel.add(suggest);
		ButtonGroup commGroup = new ButtonGroup();
		commGroup.add(accuse);
		commGroup.add(suggest);
		
		CheckBoxList cbList = new CheckBoxList();

	    JCheckBox[] myList = { player1, player2, player3, player4, player5};
	    cbList.setListData(myList);
	    cbList.setBounds(250, 375, 75, 100);
	    panel.add(cbList);
	    
		submitCommunication = new JButton("Submit");
		submitCommunication.setBounds(350, 400, 150, 50);
		
		submitCommunication.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				List<Integer> selections = new ArrayList<Integer>();
				String commType = new String();
				if (player1.isSelected())
					selections.add(1);
				if (player2.isSelected())
					selections.add(2);
				if (player3.isSelected())
					selections.add(3);
				if (player4.isSelected())
					selections.add(4);
				if (player5.isSelected())
					selections.add(5);
				if (accuse.isSelected())
					commType = "accuses";
				if (suggest.isSelected())
					commType = "suggests";
				if (selections.size() > 0 && commType != null && !"".equals(commType))
					sendCommunication(commType, selections);
			}
		});
		panel.add(submitCommunication);
		
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
				client.sendServerVote(phase, "accept");
			}
		});
		
		rejectButton = new JButton("Reject");
		rejectButton.setBounds(350, 500, 150, 50);
		
		rejectButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent event) 
			{
				client.sendServerVote(phase, "reject");
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
    
    public void createTeamSelectionGui(int choiceCount)
    {
    	selectionGui = new TeamSelectionGui(this, choiceCount);
    	selectionGui.setVisible(true);
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
    	panel.updateUI();
    }
    
    public void setTurnRole(String role)
    {
    	currentRole = role;
    	turnRoleText.setText(currentRole);
    	turnRoleText.setVisible(true);
		panel.updateUI();
    }
    
    public void setCurrentPlayer(String curPlayer)
    {
    	turn = curPlayer;
    	turnText.setText(turn);
    	turnText.setVisible(true);
    	panel.updateUI();
    }
    
    public void setCurrentlySelectedTeam(List<Integer> team)
    {
    	if (team.size() > 0)
    	{
	    	selectedTeam = new ArrayList<Integer>();
	    	String tempTeam = new String();
	    	for (Integer member : team)
	    	{
	    		selectedTeam.add(member);
				tempTeam += ", Player " + member;
			}
			tempTeam = tempTeam.substring(1);
	    	selectedTeamText.setText(tempTeam);
	    	panel.updateUI();
    	}
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
    	selfLabel.setVisible(true);
    	panel.updateUI();
    }
    
    public void updateFactionInformation()
    {
    	factionText.setText(playerFaction);
    	factionText.setVisible(true);
    	panel.updateUI();
    }
    
    public void sendGroupSelection(List<Integer> groupMembers)
    {
    	ClientSendMessage message = new ClientSendMessage();
    	message.playerId = playerNumber;
    	message.messageType = "groupSelection";
    	for (Integer choice : groupMembers)
    	{
    		System.out.println("group choice: " + choice);
    	}
    	message.groupSelection = groupMembers;
    	selectionGui.setVisible(false);
    	selectionGui.dispose();
    	try
    	{
			client.out.writeObject(message);
		}
    	catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void sendCommunication(String commType, List<Integer> selections)
    {
    	ClientSendMessage message = new ClientSendMessage();
    	message.playerId = playerNumber;
    	message.messageType = "communication";
    	message.message = commType;
    	message.groupSelection = selections;
    	try
    	{
			client.out.writeObject(message);
		} 
    	catch (IOException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
