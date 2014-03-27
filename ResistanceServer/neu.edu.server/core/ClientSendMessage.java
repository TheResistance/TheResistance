package core;

import java.io.Serializable;
/*
Created by: Britton Horn
Group: The Resistance

Date: March 27, 2014
*/

public class ClientSendMessage implements Serializable
{
	public int playerId;
	public String messageType;
	public String message;
}
