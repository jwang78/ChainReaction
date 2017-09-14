package ChainReaction.server;

import java.util.List;

import ChainReaction.model.Move;
import ChainReaction.model.CRBoard;
import ChainReaction.view.BoardView;
import javafx.scene.Node;
import javafx.scene.layout.Pane;

/**
 * This class does nothing.  It voids any input it is asked to display, making it suitable for a server.
 * */
public class UselessBoardView implements BoardView {

    @Override
    public void update(CRBoard _board, Move last, List<Move> legalMoves, Move... highlights) {
        return;
    }

    @Override
    public Pane getNode() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
