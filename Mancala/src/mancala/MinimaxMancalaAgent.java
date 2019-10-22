package mancala;

/**
 *
 * @author Hera
 */
public class MinimaxMancalaAgent extends Agent {
    
    public static final int MAX_DEPTH = 15;
    public static final int INFINITY = 999999;
    
    public MinimaxMancalaAgent(String name) {
        super(name);
    }
    
    @Override
    public void makeMove(Game game) {
        
        Mancala mancalaGame = (Mancala) game;
        ActionUtilityTuple bestMove = this.max(mancalaGame, -INFINITY, INFINITY, MAX_DEPTH);
        if(bestMove.choice != -1) {
            mancalaGame.gui.highlightBotMove(bestMove.choice);
            this.makeMoveByMax(mancalaGame, bestMove.choice);
        }
        
    }
    
    private boolean makeMoveByMax(Mancala mancalaGame, int choice) {
        
        int length = mancalaGame.boardForA.length * 2 + 1;
        int[] tempArray = new int[length];
        
        int j = 0;
        for(int i = mancalaGame.boardForB.length - 1; i >= 0; i--) {
            tempArray[j++] = mancalaGame.boardForB[i];
        }
        tempArray[j++] = mancalaGame.countForB;
        for(int i = 0; i < mancalaGame.boardForA.length; i++) {
            tempArray[j++] = mancalaGame.boardForA[i];
        }
        
        // start from the index choice-1, and distribute
        
        j = Mancala.boardLength - choice;
        int lastIndex = -1;
        int count = tempArray[Mancala.boardLength - choice - 1];
        tempArray[Mancala.boardLength - choice - 1] = 0;
        
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
        for(int i = mancalaGame.boardForB.length - 1; i >= 0; i--) {
            mancalaGame.boardForB[i] = tempArray[j++];
        }
        mancalaGame.countForB = tempArray[j++];
        for(int i = 0; i < mancalaGame.boardForA.length; i++) {
            mancalaGame.boardForA[i] = tempArray[j++];
        }
        
        // determine capture
        int index = Mancala.boardLength - 1 - lastIndex;
        if(lastIndex < mancalaGame.boardForB.length && lastIndex >= 0 && tempArray[lastIndex] == 1 && mancalaGame.boardForA[index] != 0) {
            mancalaGame.countForB += mancalaGame.boardForA[index];
            mancalaGame.countForB++;
            mancalaGame.boardForA[index] = 0;
            mancalaGame.boardForB[index] = 0;
        }
        
        // determine free turn
        
        if(lastIndex == Mancala.boardLength) {
            mancalaGame.turn = (mancalaGame.turn + 1) % 2;
            return true;
        } else {
            return false;
        }
    }
    
    
    private boolean makeMoveByMin(Mancala mancalaGame, int choice) {
        
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
        
        if(lastIndex < mancalaGame.boardForA.length && lastIndex >= 0 && tempArray[lastIndex] == 1 && mancalaGame.boardForA[lastIndex] != 0) {
            mancalaGame.countForA += mancalaGame.boardForB[lastIndex];
            mancalaGame.countForA++;
            mancalaGame.boardForB[lastIndex] = 0;
            mancalaGame.boardForA[lastIndex] = 0;
        }
        
        // determine free turn
        
        if(lastIndex == Mancala.boardLength) {
            mancalaGame.turn = (mancalaGame.turn + 1) % 2;
            return true;
        } else {
            return false;
        }
        
    }
    
    
    private int heuristic(Mancala game) {
        int totalB = game.countForB;
        for(int i = 0; i < Mancala.boardLength; i++)
            totalB += game.boardForB[i];
        boolean[] captByA = new boolean[Mancala.boardLength];
        boolean[] captByB = new boolean[Mancala.boardLength];
        for(int i = 0;i < Mancala.boardLength; i++)
            captByA[i] = captByB[i] = false;
        for(int i = 0; i < Mancala.boardLength; i++) {
            int index = i + game.boardForA[i];
            if(index < 6 && game.boardForA[index] == 0 && game.boardForB[index] > 0) {
                captByA[index] = true;
            } else if(index >= 13 && index <= 18 && game.boardForA[index - 13] == 0 && game.boardForB[index - 13] > 0) {
                captByA[index - 13] = true;
            } else if(index >= 26 && index <= 31 && game.boardForA[index - 26] == 0 && game.boardForB[index - 26] > 0) {
                captByA[index - 26] = true;
            }
        }
        for(int i = 0; i < Mancala.boardLength; i++) {
            int index = i - game.boardForB[i];
            if(index >= 0 && game.boardForB[index] == 0 && game.boardForA[index] > 0) {
                captByB[index] = true;
            } else if(index >= -13 && index <= -8 && game.boardForB[index + 13] == 0 && game.boardForA[index + 13] > 0 ) {
                captByB[(index) + 13] = true;
            } else if(index >= -26 && index <= -21 && game.boardForB[index + 26] == 0 && game.boardForA[index + 26] > 0) {
                captByB[index + 26] = true;
            }
        }
        for(int i = 0; i < Mancala.boardLength; i++) {
            if(captByA[i] == true) {
                totalB -= 3 * game.boardForB[i];
            }
            if(captByB[i] == true) {
                totalB += 2 * game.boardForA[i];
                //if(totalB >= 20) totalB += 2*game.boardForA[i];
            }
        }
        return totalB;
        //if(totalB == 24) return 0;
        //else if(totalB > 24) return 1;
        //else return -1;
    }
    
    
    private ActionUtilityTuple max(Mancala game, int alpha, int beta, int depth) {
        
        ActionUtilityTuple aut = new ActionUtilityTuple();
        if(game.isFinished()) {
            aut.utility = game.countForB;
            return aut;
        }
        
        if(game.countForA > 24) {
            aut.utility = -48;
            return aut;
        }
        
        if(game.countForB > 24 && depth <= 2) {
            aut.utility = game.countForB;
            return aut;
        }
        
        if(depth == 0) {
            aut.utility = heuristic(game);
            return aut;
        }
        
        aut.utility = -INFINITY;
        
        for(int i = 0; i < Mancala.boardLength; i++) {
            if(game.boardForB[i] == 0) {
                continue;
            }
            Mancala game_ = game.makeCopy();
            boolean bool = this.makeMoveByMax(game_, i);
            ActionUtilityTuple aut_;
            if(bool == false)
                aut_ = min(game_, alpha, beta, depth - 1);
            else
                aut_ = max(game_, alpha, beta, depth - 1);
            if(aut.utility < aut_.utility) {
                aut.utility = aut_.utility;
                aut.choice = i;
            }
            if(aut.utility >= beta) {
                return aut;
            }
            alpha = Math.max(alpha, aut.utility);
        }
        
        return aut;
        
    }
    
    private ActionUtilityTuple min(Mancala game, int alpha, int beta, int depth) {
        
        ActionUtilityTuple aut = new ActionUtilityTuple();
        if(game.isFinished()) {
            aut.utility = game.countForB;
            return aut;
        }
        
        if(game.countForA > 24) {
            aut.utility = -48;
            return aut;
        }
        
        if(game.countForB > 24 && depth <= 2) {
            aut.utility = game.countForB;
            return aut;
        }
        
        if(depth == 0) {
            aut.utility = heuristic(game);
            return aut;
        }
        
        aut.utility = INFINITY;
        
        for(int i = 1; i <= Mancala.boardLength; i++) {
            if(game.boardForA[i - 1] == 0) {
                continue;
            }
            Mancala game_ = game.makeCopy();
            boolean bool = this.makeMoveByMin(game_, i);
            ActionUtilityTuple aut_;
            if(bool == false)
                aut_ = max(game_, alpha, beta, depth - 1);
            else
                aut_ = min(game_, alpha, beta, depth - 1);
            if(aut.utility > aut_.utility) {
                aut.utility = aut_.utility;
                aut.choice = i;
            }
            if(aut.utility <= alpha) {
                return aut;
            }
            beta = Math.min(beta, aut.utility);
        }
        
        return aut;
        
    }
    
    class ActionUtilityTuple {
        
        int choice;  // this is the action, this choice is within 0~(MAX-1)
        int utility; // self explanatory
        
        public ActionUtilityTuple() {
            choice = -1;
            utility = -1;
        }
        
    }
    
}
