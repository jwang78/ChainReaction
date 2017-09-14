package ChainReaction.ai;

/**
 * Represents the board weights; printed from the CS support code to make board
 * 8x8 and remove dependency on cs015.jar.
 * 
 * @author CS15 TAs
 */
public class BoardWeights {
    private int[][] _weights = new int[][] { { 200, -70, 30, 25, 25, 30, -70, 200 },
            { -70, -100, -10, -10, -10, -10, -100, -70 }, { 30, -10, 2, 2, 2, 2, -10, 30 },
            { 25, -10, 2, 2, 2, 2, -10, 25 }, { 25, -10, 2, 2, 2, 2, -10, 25 }, { 30, -10, 2, 2, 2, 2, -10, 30 },
            { -70, -100, -10, -10, -10, -10, -100, -70 }, { 200, -70, 30, 25, 25, 30, -70, 200 } };
    
    public int getWeight(int column, int row) {
        return _weights[column][row];
    }
}
