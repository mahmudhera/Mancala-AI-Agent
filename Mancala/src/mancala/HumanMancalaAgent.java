package mancala;

import java.util.Scanner;

/**
 *
 * @author Hera
 */
public class HumanMancalaAgent extends Agent {
    
    static Scanner in = Solver.in;
    
    public HumanMancalaAgent(String name) {
        super(name);
    }
    
    @Override
    public void makeMove(Game game) {
        
        Mancala mancalaGame = (Mancala) game;
        
        // prompt to enter the choice
        // then change the game accordingly
        /*int choice;
        System.out.println(super.name + "'s choice: choose a valid pot between 1~6");
        while(true) {
            try {
                choice = in.nextInt();
                if(choice < 1 || choice > 6) {
                    Integer.parseInt("U");
                }

                // check if that move is allowed, if not then raise exception
                int index = choice - 1;
                if(mancalaGame.boardForA[index] == 0) {
                    Integer.parseInt("GFY");
                }

                break;
            } catch(Exception e) {
                System.out.println("Invalid input!");
            }
        }*/
        
        int choice = getHumanChoice(mancalaGame);
        this.makeMove(mancalaGame, choice);
        
    }
    
    private void makeMove(Mancala mancalaGame, int choice) {
        
        // by now, hum agent has entered a valid input
        // now we have to change the game accordingly...
        // human is with role 0. Therefore, A[0->5]:countA:B[5->0]
        
        // copy the integers in this order in a new array
        
        int length = mancalaGame.boardForA.length * 2 + 1;
        int[] tempArray = new int[length];
        
        int j = 0;
        for(int i = 0; i < mancalaGame.boardForA.length; i++) {
            tempArray[j++] = mancalaGame.boardForA[i];
        }
        tempArray[j++] = mancalaGame.countForA;
        for(int i = mancalaGame.boardForB.length - 1; i >= 0; i--) {
            tempArray[j++] = mancalaGame.boardForB[i];
        }
        
        // start from the index choice-1, and distribute
        
        j = choice;
        int lastIndex = -1;
        int count = tempArray[choice - 1];
        tempArray[choice - 1] = 0;
        
        while(count-- != 0) {
            lastIndex = j;
            tempArray[j]++;
            j++;
            if(j == length) {
                j = 0;
            }
        }
        
        // copy back everything into the game
        
        j = 0;
        for(int i = 0; i < mancalaGame.boardForA.length; i++) {
            mancalaGame.boardForA[i] = tempArray[j++];
        }
        mancalaGame.countForA = tempArray[j++];
        for(int i = mancalaGame.boardForB.length - 1; i >= 0; i--) {
            mancalaGame.boardForB[i] = tempArray[j++];
        }
        
        // determine capture
        
        if(lastIndex < mancalaGame.boardForA.length && lastIndex >= 0 && tempArray[lastIndex] == 1 && mancalaGame.boardForB[lastIndex] != 0 ) {
            mancalaGame.countForA += mancalaGame.boardForB[lastIndex];
            mancalaGame.countForA++;
            mancalaGame.boardForB[lastIndex] = 0;
            mancalaGame.boardForA[lastIndex] = 0;
        }
        
        // determine free turn
        
        if(lastIndex == Mancala.boardLength) {
            mancalaGame.turn = (mancalaGame.turn + 1) % 2;
        }
        
    }
    
    public int getHumanChoice(Mancala game) {
        MancalaGUI gui = game.gui;
        gui.choice = -1;
        gui.guiBusy = true;
        gui.updateMessage("Your turn");
        while(gui.choice == -1) {
            try {
                Thread.sleep(100);
            } catch(Exception e) {
                ;
            }
        }
        gui.guiBusy = false;
        return gui.choice;
    }
    
}
