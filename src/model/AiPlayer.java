package model;

import model.Move;
import model.PentagoBoard;
import model.PentagoGame;
import model.Player;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class AiPlayer extends Player {

    private int maxDepth;
    private boolean alphaBeta;

    // Change depth for difficultly levels.
    public AiPlayer(String name, char piece, int depth, boolean alphaBeta) {
        super(name, piece);
        maxDepth = depth;
        this.alphaBeta = alphaBeta;
    }

    /**
     * @param currentGameState
     * @return int[] of size 2 with a row and col for the ai's move
     */
    public Move getMove(PentagoGame currentGameState) {
        // always start as maximizer
        Move result;
        if(alphaBeta) {
            result = getAlphaBetaMove(currentGameState, true, maxDepth, Integer.MIN_VALUE, Integer.MAX_VALUE).getLastMove();
        } else {
            result = getMinMaxMove(currentGameState, true, maxDepth).getLastMove();
        }

        return result;
    }

    private PentagoGame getMinMaxMove(PentagoGame root, boolean maximizer, int depth) {

        if (depth == 0) {
            // gets the best move and returns it.
            // Move move = root.getLastMove();
            // System.err.println(move);
            // move.setUtility(root.getUtility(root.getPentagoBoard()));
            return root;
        }

        ArrayList<PentagoGame> children = root.nextPossibleMoves();

        int bestUtility;
        PentagoGame bestMove = null;

        if (maximizer) {
            bestUtility = Integer.MIN_VALUE;
            for(PentagoGame c: children) {
                PentagoGame b =  getMinMaxMove(c,false,  depth -1);
                 //bestUtility = Math.max(bestUtility, b.getUtility());
                if(b.getUtility(b.getPentagoBoard()) > bestUtility) {
                    bestUtility = b.getUtility(b.getPentagoBoard());
                    bestMove = c;
                }
            }
        } else  {
            bestUtility = Integer.MAX_VALUE;
            for(PentagoGame c: children) {
                PentagoGame b =  getMinMaxMove(c,false,  depth -1);
                //  bestUtility = Math.min(bestUtility, b.getUtility());
                if(b.getUtility(b.getPentagoBoard()) < bestUtility) {
                    bestUtility = b.getUtility(b.getPentagoBoard());
                    bestMove = c;
                }
            }
        }
        return bestMove;
    }


    private PentagoGame getAlphaBetaMove(PentagoGame root, boolean maximizer, int depth, int alpha, int beta) {
        if (depth == 0) {
            // gets the best move and returns it.
            // Move move = root.getLastMove();
            // System.err.println(move);
            // move.setUtility(root.getUtility(root.getPentagoBoard()));
            return root;
        }

        ArrayList<PentagoGame> children = root.nextPossibleMoves();

        int bestUtility;
        PentagoGame bestMove = null;

        if (maximizer) {
            bestUtility = Integer.MIN_VALUE;
            for(PentagoGame c: children) {
                PentagoGame b =  getAlphaBetaMove(c,false,depth -1, alpha, beta);
                //bestUtility = Math.max(bestUtility, b.getUtility());
                if(b.getUtility(b.getPentagoBoard()) > bestUtility) {
                    bestUtility = b.getUtility(b.getPentagoBoard());
                    bestMove = c;
                }
                alpha = Math.max(alpha, b.getUtility(b.getPentagoBoard()));
                if(beta <= alpha) {
                    break; // cut-off beta
                }

            }

            return bestMove;
        } else  {
            bestUtility = Integer.MAX_VALUE;
            for(PentagoGame c: children) {
                PentagoGame b =  getAlphaBetaMove(c,false,depth -1, alpha, beta);
                //  bestUtility = Math.min(bestUtility, b.getUtility());
                if(b.getUtility(b.getPentagoBoard()) < bestUtility) {
                    bestUtility = b.getUtility(b.getPentagoBoard());
                    bestMove = c;
                }
                alpha = Math.max(alpha, b.getUtility(b.getPentagoBoard()));
                if(beta <= alpha) {
                    break; // cut-off alpha
                }

            }

            return bestMove;
        }
    }
}
