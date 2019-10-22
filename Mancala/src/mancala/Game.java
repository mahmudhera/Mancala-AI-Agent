package mancala;

import java.util.Random;
import java.util.Scanner;
import javax.swing.JOptionPane;

/**
 * Extend this abstract class for your game implementation
 * @author Azad
 * @modifiedBy Hera
 *
 */
public abstract class Game 
{
	Agent agent[];                  // Array containing all the agents, here we only consider two player games.
	String name = "A Generic Game"; // A name for the Game object, it will be changed by the actual game class
                                        // extending it
	
	Agent winner = null;            // The winning agent will be saved here after the game compeltes,
                                        // if null the game is drawn
        
        int turn = 0;                   // just an initial value, will be changed later. The subclass will determine
                                        // who starts the game and fix this turn accordingly. 0-human, 1-ai agent
        
        Scanner in = Solver.in;
        
        MancalaGUI gui;
        
	public Game(Agent a, Agent b) 
	{
            agent = new Agent[2];
            agent[0] = a;
            agent[1] = b;
	}
        
        public Game(Game game) {
            agent = new Agent[2];
            agent[0] = game.agent[0];
            agent[1] = game.agent[1];
        }
	
	/**
	 * The actual game loop, each player takes turn.
	 * The first player is selected randomly -- this was Sir's implementation.
         * Here...Agent at index 0 will start the game
	 */
	public void play()
	{
            
            updateMessage("Starting " + name + " between "+ agent[0].name+ " and "+ agent[1].name+".");
            initialize(false);
            
            /*
            // learning who will start...later from GUI, from console for now
            while(true) {
                System.out.println("Who starts?\n0: " + agent[0].name + "\n1: " + agent[1].name);
                try {
                    int choice = in.nextInt();
                    if(choice != 0 && choice != 1) {
                        choice = Integer.parseInt("y");
                    }
                    turn = choice;
                    break;
                } catch (Exception e) {
                    System.out.println("Invalid input!\n");
                }
            }
            */
            
            int choice = JOptionPane.showConfirmDialog(null, "Do you want to start?", "Pick", JOptionPane.YES_NO_OPTION);
            if(choice == JOptionPane.YES_OPTION)
                turn = 0;
            else
                turn = 1;
            
            System.out.println("The game starts now...");
            showGameState();
            
            
            // now the game begins...
            while(!isFinished())
            {
                updateMessage(agent[turn].name+ "'s turn. ");
                agent[turn].makeMove(this);
                showGameState();

                turn = (turn + 1) % 2;
            }

            if (winner != null)
                if(winner == agent[1])
                    updateMessage(winner.name+ " wins!!!");
                else
                    updateMessage("You win!!!!");
            else	
                updateMessage("Game drawn!!");
            
            System.out.println("The final board: ");
            showGameState();

	}
	
	@Override
	public String toString() {
		String str = "";
		return str;
	}
	
	/**
	 * @return Returns true if the game has finished. 
         * Also must update the winner member variable.
	 */
	abstract boolean isFinished();
	
	/**
	 * Initializes the game as needed. If the game starts with different initial configurations,
         * it can be read from file.
	 * @param fromFile if true loads the initial state from file. 
	 */
	abstract void initialize(boolean fromFile);
	
	/**
	 * Prints the game state in console, or show it in the GUI
	 */
	abstract void showGameState();
	
	/**
	 * Shows game messages in console, or in the GUI
	 */
	abstract void updateMessage(String msg);
}
