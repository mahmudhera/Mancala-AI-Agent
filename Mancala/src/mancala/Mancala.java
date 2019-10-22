package mancala;

import java.util.Scanner;

/**
 *
 * Extends Game class
 * 
 * @author Hera
 */
public class Mancala extends Game {

    /*
      The game will be stored as two arrays of integers with size = 6.
        Initially both of these will have all elements: 4
        Also there will the two counts: countForA, countForB
        When A chooses to play, the moves will be [0]->[5]->countForA, this is role 0, by default for human agent
        When B chooses to play, the moves will be [5]->[0]->countForB, this is role 1, by default for ai agent
    
        Have to tell the MancalaAgent what role he is playing.
        So, MancalaAgent should have this thing there.
    */
    
    public static final int boardLength = 6;
    public static final int initialPotSize = 4;
    
    public int[] boardForA;
    public int[] boardForB;
    public int countForA;
    public int countForB;
    
    public static Scanner in = Solver.in;

    /*
        Assummed that a: human, b: ai.
    */
    public Mancala(Agent a, Agent b, MancalaGUI gui) {
        
        super(a, b);
        this.gui = gui;
        // assumming that Agent a is human, Agent b is ai
        super.agent[0].role = 0;
        super.agent[1].role = 1;
        
        name = "Mancala";
        
        boardForA = new int[boardLength];
        boardForB = new int[boardLength];
    
    }
    
    
    public Mancala makeCopy() {
        
        Mancala game = new Mancala(this.agent[0], this.agent[1], this.gui);
        for(int i = 0; i < Mancala.boardLength; i++) {
            game.boardForA[i] = this.boardForA[i];
            game.boardForB[i] = this.boardForB[i];
        }
        game.countForA = this.countForA;
        game.countForB = this.countForB;
        return game;
        
    }
    
    
    @Override
    void initialize(boolean fromFile) {
        for(int i = 0; i < boardLength; i++) {
            boardForA[i] = boardForB[i] = initialPotSize;
        }
        this.countForA = 0;
        this.countForB = 0;
    }
    
    
    /**
     * Prints the current board (may be replaced/appended with by GUI)
     */
    @Override
    void showGameState() {
        System.out.print("===");
        for(int i = 0; i < boardLength; i++)
            System.out.print("==");
        System.out.print("==\n  |");
        for(int i = 0; i < boardLength; i++)
            System.out.print(this.boardForB[i] + "|");
        System.out.print("  \n---");
        for(int i = 0; i < boardLength; i++)
            System.out.print("--");
        System.out.printf("--\n%d|", this.countForB);
        for(int i = 0; i < 2*boardLength - 1; i++)
            System.out.print(" ");
        System.out.printf("|%d\n---", this.countForA);
        for(int i = 0; i < boardLength; i++)
            System.out.print("--");
        System.out.print("--\n  |");
        for(int i = 0; i < boardLength; i++)
            System.out.print(this.boardForA[i] + "|");
        System.out.print("  \n===");
        for(int i = 0; i < boardLength; i++)
            System.out.print("==");
        System.out.println("==");
        System.out.println("\n\n\n\n\n\n");
        
        try {
            gui.updateBoard(this);
            gui.resetBotMove();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    
    
    /**
     * @return Returns true if the game has finished. 
     * Also must update the winner member variable.
     */
    boolean isFinished() {
        int totalA = 0, totalB = 0;
        for(int i = 0; i < boardLength; i++) {
            totalA += boardForA[i];
            totalB += boardForB[i];
        }
        if(totalA == 0 || totalB == 0) {
            countForA = countForA + totalA;
            countForB = countForB + totalB;
            for(int i = 0; i < boardLength; i++) {
                boardForA[i] = boardForB[i] = 0;
            }
            if( countForA > countForB ) {
                super.winner = super.agent[0];
            } else if( countForA < countForB) {
                super.winner = super.agent[1];
            } else {
                super.winner = null;
            }
            return true;
        } else {
            return false;
        }
        
    }
    
    
    @Override
    void updateMessage(String msg) {
        System.out.println(msg);
        gui.updateMessage(msg);
    }
    
    public static void main(String[] args) {
        
        // testing codes here
        
    }
    
    
}
