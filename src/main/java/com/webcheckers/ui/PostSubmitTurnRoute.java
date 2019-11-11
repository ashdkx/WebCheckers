package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
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
    private Gson gson;
    private GameCenter gameCenter;
    private List<int[]> jumpPositions = new ArrayList<>();

    public PostSubmitTurnRoute(GameCenter gameCenter, Gson gson){
        LOG.config("PostSubmitTurnRoute is initialized.");
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    @Override
    public Object handle(Request request, Response response) {


        LOG.finer("PostSubmitTurnRoute is invoked.");

        final Session httpSession = request.session();

        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);
        Player player2;
        String json;
        GameBoard board = gameCenter.getGame(request.queryParams(GetGameRoute.GAMEID_PARAM));
        List<Row> playerBoard = board.getPlayerBoard(player);

        Position moveEnd = board.getActivePieceEnd();
        Position moveStart = board.getActivePieceStart();
        int[] position = new int[]{moveStart.getRow(), moveStart.getCell()};

        checkRequiredMoves(board, player, playerBoard);
        if(board.getRequiredMovePieces().isEmpty()){
            json = move(board,player, playerBoard,moveStart, moveEnd);
        }
        else {
            if(board.isRequiredMovePiece(position)&&hasJumped(board,position)) {
                json = move(board,player,playerBoard,moveStart,moveEnd);
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
                if(board.isNotPlayerColor(piece,player)||piece==null) {
                    continue;
                }
                if (canJump(board,player,playerBoard,i,j,piece)){
                    int[] position = new int [] {i,j};
                    List<int[]> jumpPositionsAdd = new ArrayList<>(jumpPositions);
                    board.addRequiredMovePieces(position,jumpPositionsAdd);
                    jumpPositions.clear();
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
            boolean isCorrectColor = board.isNotPlayerColor(pieceJump,player);
            boolean isValidSpace = board.isValidSpace(playerBoard,row-2,col+2);
            validPos1 = isValidSpace&&isCorrectColor;
            if(validPos1){
                jumpPositions.add(new int[]{row-1,col+1});
            }
        }
        if (board.getPiece(playerBoard, row - 1, col - 1) != null) {
            Piece pieceJump = board.getPiece(playerBoard, row - 1, col - 1);
            boolean isCorrectColor = board.isNotPlayerColor(pieceJump,player);
            boolean isValidSpace = board.isValidSpace(playerBoard,row-2,col-2);
            validPos2 = isCorrectColor&&isValidSpace;

            if (validPos2){
                jumpPositions.add(new int[]{row-1,col-1});
            }

        }
        
        if(piece.getType() == Piece.type.KING){
            if (board.getPiece(playerBoard, row+1, col+1) != null){
                Piece pieceJump = board.getPiece(playerBoard, row + 1, col + 1);
                boolean isCorrectColor = board.isNotPlayerColor(pieceJump,player);
                boolean isValidSpace = board.isValidSpace(playerBoard,row+2,col+2);
                validPos3 = isValidSpace&&isCorrectColor;
                if (validPos3){
                    jumpPositions.add(new int[]{row+1,col+1});
                }
            }
            if (board.getPiece(playerBoard, row+1, col-1) != null){
                Piece pieceJump = board.getPiece(playerBoard, row + 1, col - 1);
                boolean isCorrectColor = board.isNotPlayerColor(pieceJump,player);
                boolean isValidSpace = board.isValidSpace(playerBoard,row+2,col-2);
                validPos4 = isValidSpace&&isCorrectColor;
                if (validPos4){
                    jumpPositions.add(new int[]{row+1,col-1});
                }
            }
        }
        return validPos1||validPos2||validPos3||validPos4;
    }

    private String submit(GameBoard board, List<Row> playerBoard, Player player, Position moveStart, Position moveEnd){
        board.setPiece(playerBoard, moveStart.getRow(), moveStart.getCell(), null);

        if(moveEnd.getRow()==0){
            board.setPieceKing(player,playerBoard,moveEnd.getRow(),moveEnd.getCell());
            board.addPlayerTotalPieces(player);
        }
        else{
            board.setPiece(playerBoard, moveEnd.getRow(), moveEnd.getCell(), board.getActivePiece());
        }

        if(board.isRedPlayer(player)){
            board.updateWhitePlayer();
            board.setPlayerTurn(board.getWhitePlayer());

        }
        else{
            board.updateRedPlayer();
            board.setPlayerTurn(board.getRedPlayer());
        }


        board.setActivePiece(null);
        board.setActivePieceMoves(0);
        board.setSingleMove(false);
        board.clearRequiredMovePieces();
        board.clearActivePieceEnd();
        jumpPositions.clear();
        return gson.toJson(Message.info("Valid Move."));
    }


    private String move(GameBoard board, Player player, List<Row> playerBoard, Position moveStart, Position moveEnd){
        String json;
        if (board.isSingleMove()) {
            json = submit(board, playerBoard, player, moveStart, moveEnd);
        } else {
            if (canJump(board, player, playerBoard, moveEnd.getRow(), moveEnd.getCell(), board.getActivePiece())) {
                json = gson.toJson(Message.error("Piece is still able to jump."));
            } else {
                while (!board.getPieceRemove().isEmpty()) {
                    int[] positionRemove = board.removePieceRemove();
                    if (board.getPiece(playerBoard, positionRemove[0], positionRemove[1]).getType() == Piece.type.KING) {
                        board.removeOpponentTotalPieces(player,2);

                    } else {
                        board.removeOpponentTotalPieces(player,1);
                    }
                    board.setPiece(playerBoard,positionRemove[0],positionRemove[1],null);
                }
                json = submit(board, playerBoard, player, moveStart, moveEnd);
            }
        }
        return json;
    }
}
