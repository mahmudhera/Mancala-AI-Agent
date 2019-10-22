package mancala;

import java.util.Scanner;

public class Solver {
	
	/*
	 * The starting point of the game.
	 * Instantiates two agents (human/ minimax/ alpha beta pruning/ or other) and pass them to a game object.
	 * Here a TickTackToe game is implemented as an example. You need to extend the abstract class Game to create your own game.
	 * */
    
        public static Scanner in = new Scanner(System.in);
	
	public static void main(String[] args) 
	{
		
		Agent human = new HumanMancalaAgent("Human");
		//Agent human = new MinimaxTTTAgent("007");
		Agent machine = new MinimaxMancalaAgent("Computer");
                
                MancalaGUI gui = new MancalaGUI();
                gui.setName("Mancala");
                gui.setVisible(true);
                gui.setAlwaysOnTop(true);

		//System.out.println(human.name+" vs. "+machine.name);
		
		Game game = new Mancala(human,machine, gui);
		game.play();
		
	}

}
