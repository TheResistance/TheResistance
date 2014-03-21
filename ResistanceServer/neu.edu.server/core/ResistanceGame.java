package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
Created by: Britton Horn
Group: The Resistance

Date: March 18, 2014
*/

public class ResistanceGame
{
    int player_turn = -1;
    
    public void beginGame()
    {
        List<Integer> order = new ArrayList<Integer>();
        order.add(1);
        order.add(2);
        order.add(3);
        order.add(4);
        order.add(5);
        System.out.println("order: " + order.toString());
        Collections.shuffle(order);
        System.out.println("order: " + order.toString());
    }
    
    public void message(Object message)
    {
        System.out.println("Game received message: " + message);
    }
}
