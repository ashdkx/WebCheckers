package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.model.Position;
import com.webcheckers.model.Row;
import com.webcheckers.util.Message;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;

/**
 * @author Nicholas Curl
 */

public class PostSubmitTurnRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());
    private Gson gson = new Gson();



    public PostSubmitTurnRoute(){
        LOG.config("PostSubmitTurnRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response) {


        LOG.finer("PostSubmitTurnRoute is invoked.");

        final Session httpSession = request.session();

        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        Player player2;
        String json;
        GameBoard board = player.getGame();
        List<Row> playerBoard = board.getPlayerBoard(player);
        if(player.isPlayer1()){
            player2 = board.getPlayer2();
        }
        else{
            player2 = board.getPlayer1();
        }

        Position moveEnd = board.getActiveEnd();
        Position moveStart = board.getActiveStart();
        int[] position = new int[]{moveStart.getRow(), moveStart.getCell()};

        checkRequiredMoves(board, player, playerBoard);
        if(board.getRequiredMovePieces().isEmpty()){
            json = move(board,player, player2, playerBoard,moveStart, moveEnd);
        }
        else {
            if(board.isRequiredMovePiece(position)&&hasJumped(board,position)) {
                json = move(board,player,player2,playerBoard,moveStart,moveEnd);
            }
            else {
                json = gson.toJson(Message.error("There are pieces that have to jump."));
                board.clearRequiredMovePieces();
            }
        }

        return json;
    }


    private boolean hasJumped(GameBoard board, int[] position){
        boolean jumped = false;
        for (int[] jumpPositions : board.getRequiredMoveJumps(position)){
            for (int[] jumps : board.getPieceRemove()) {
                if (jumps[0] == jumpPositions[0] && jumps[1] == jumpPositions[1]) {
                    jumped = true;
                    break;
                }
            }
        }
        return jumped;
    }

    private void checkRequiredMoves(GameBoard board, Player player, List<Row> playerBoard){
        for(int i = 7; i>-1;i--){
            for(int j = 0; j<8;j++){
                Piece piece = board.getPiece(playerBoard,i,j);
                if(player.isNotActiveColor(piece)||piece==null) {
                    continue;
                }
                if (canJump(board,player,playerBoard,i,j,piece)){
                    int[] position = new int [] {i,j};
                    List<int[]> jumpPositions = new ArrayList<>(board.getJumpPositions());
                    board.addRequiredMovePieces(position,jumpPositions);
                    board.clearJumpPositions();
                }
            }
        }
    }

    private boolean canJump(GameBoard board, Player player, List<Row> playerBoard, int row, int col, Piece piece){
        boolean validPos1 = false;
        boolean validPos2 = false;
        boolean validPos3 = false;
        boolean validPos4 = false;
        if (board.getPiece(playerBoard, row - 1, col + 1) != null) {
            Piece pieceJump = board.getPiece(playerBoard, row - 1, col + 1);
            boolean isCorrectColor = player.isNotActiveColor(pieceJump);
            boolean isValidSpace = board.isValid(playerBoard,row-2,col+2);
            validPos1 = isValidSpace&&isCorrectColor;
            if(validPos1){
                board.addJumpPosition(new int[]{row-1,col+1});
            }
        }
        if (board.getPiece(playerBoard, row - 1, col - 1) != null) {
            Piece pieceJump = board.getPiece(playerBoard, row - 1, col - 1);
            boolean isCorrectColor = player.isNotActiveColor(pieceJump);
            boolean isValidSpace = board.isValid(playerBoard,row-2,col-2);
            validPos2 = isCorrectColor&&isValidSpace;

            if (validPos2){
                board.addJumpPosition(new int[]{row-1,col-1});
            }

        }
        return validPos1||validPos2||validPos3||validPos4;
    }

    private String submit(GameBoard board, List<Row> playerBoard, Player player, Position moveStart, Position moveEnd){
        board.setPiece(playerBoard, moveStart.getRow(), moveStart.getCell(), null);
        board.setPiece(playerBoard, moveEnd.getRow(), moveEnd.getCell(), board.getActivePiece());
        board.setActivePiece(null);
        board.setActivePieceMoves(0);

        if(player.isPlayer1()){
            board.updatePlayer2();
            player.setMyTurn(false);
            board.getPlayer2().setMyTurn(true);
        }
        else{
            board.updatePlayer1();
            player.setMyTurn(false);
            board.getPlayer1().setMyTurn(true);
        }
        player.setSingleMove(false);
        board.clearRequiredMovePieces();
        return gson.toJson(Message.info("Valid Move."));
    }


    private String move(GameBoard board, Player player, Player player2, List<Row> playerBoard, Position moveStart, Position moveEnd){
        String json;
        if (player.isSingleMove()) {
            json = submit(board, playerBoard, player, moveStart, moveEnd);
        } else {
            if (canJump(board, player, playerBoard, moveEnd.getRow(), moveEnd.getCell(), board.getActivePiece())) {
                json = gson.toJson(Message.error("Piece is still able to jump."));
            } else {
                while (!board.getPieceRemove().isEmpty()) {
                    int[] positionRemove = board.removePieceRemove();
                    if (board.getPiece(playerBoard, positionRemove[0], positionRemove[1]).getType() == Piece.type.KING) {
                        player2.removeTotalPieces(2);
                    } else {
                        player2.removeTotalPieces(1);
                    }
                    playerBoard.get(positionRemove[0]).getSpace(positionRemove[1]).setPiece(null);
                }
                json = submit(board, playerBoard, player, moveStart, moveEnd);
            }
        }
        return json;
    }
}
