package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.model.Move;
import com.webcheckers.model.Piece;
import com.webcheckers.model.Player;
import com.webcheckers.model.Row;
import com.webcheckers.util.Message;
import spark.*;

import java.util.List;
import java.util.logging.Logger;


/**
 * @author Nicholas Curl
 */
public class PostValidateMoveRoute implements Route {

    private static final Logger LOG = Logger.getLogger(PostValidateMoveRoute.class.getName());

    private GameCenter gameCenter;
    private Gson gson = new Gson();

    public PostValidateMoveRoute(){
        LOG.config("PostValidateMoveRoute is initialized.");
    }

    @Override
    public Object handle(Request request, Response response){


        LOG.finer("PostValidateMoveRoute is invoked.");

        final Session httpSession = request.session();


        Player player = httpSession.attribute(GetHomeRoute.CURRENT_PLAYER);

        GameBoard board = player.getGame();
        List<Row> playerBoard = board.getPlayerBoard(player);
        String json = request.queryParams("actionData");
        Move move = gson.fromJson(json,Move.class);

        int moveStartRow = move.getStart().getRow();
        int moveStartCell = move.getStart().getCell();

        if (board.getActivePiece() == null){
            board.setActivePiece(board.getPiece(playerBoard,moveStartRow,moveStartCell));
            board.setActivePieceStart(move.getStart());
        }

        return move(board,player,playerBoard,move);
    }


    private String move(GameBoard board, Player player, List<Row> playerBoard, Move move){
        int moveStartRow = move.getStart().getRow();
        int moveStartCell = move.getStart().getCell();
        int moveEndRow = move.getEnd().getRow();
        int moveEndCell = move.getEnd().getCell();
        String json;
        if(board.isValid(playerBoard,moveEndRow,moveEndCell)) {
            if (board.getActivePiece().getType() == Piece.type.SINGLE && moveEndRow > moveStartRow) {
                json = gson.toJson(Message.error("Can't move backwards."));
            } else {
                switch (moveEndCell - moveStartCell) {
                    case -1:
                    case 1:
                        if (player.isSingleMove()) {
                            json = gson.toJson(Message.error("Can only move diagonally once."));
                        } else if (board.getActivePieceMoves() > 1) {
                            json = gson.toJson(Message.error("Cannot move after jump."));
                        } else {
                            board.setActivePieceEnd(move.getEnd());
                            board.incrementActivePieceMoves();
                            player.setSingleMove(true);
                            json = gson.toJson(Message.info("Valid move."));
                        }
                        break;
                    case -2:
                        if (board.getActivePiece().getType() == Piece.type.SINGLE) {
                            json = jump(board, player, playerBoard, moveEndRow + 1, moveEndCell + 1, move);
                        }
                        else {json = gson.toJson(Message.error("Invalid move."));}
                            /*else {
                            switch (moveEndRow - moveStartRow) {
                                case 2:
                                    json = jump(board, player, playerBoard, moveEndRow-1,moveEndCell+1,move);
                                    break;
                                case -2:
                                    json = jump(board, player, playerBoard, moveEndRow+1, moveEndCell+1,move);
                                    break;
                                default:
                                    json = gson.toJson(Message.error("Invalid move."));
                            }
                        }*/
                        break;
                    case 2:
                        if (board.getActivePiece().getType() == Piece.type.SINGLE) {
                            json = jump(board, player, playerBoard, moveEndRow + 1, moveEndCell - 1, move);
                        }else{json = gson.toJson(Message.error("Invalid move."));} /*else {
                            switch (moveEndRow - moveStartRow) {
                                case 2:
                                    json = jump(board, player, playerBoard, moveEndRow-1,moveEndCell-1,move);
                                    break;
                                case -2:
                                    json = jump(board, player, playerBoard, moveEndRow+1, moveEndCell-1,move);
                                    break;
                                default:
                                    json = gson.toJson(Message.error("Invalid move."));
                            }
                        }*/
                        break;
                    default:
                        json = gson.toJson(Message.error("Invalid move."));

                }
            }
        }
        else {
            json = gson.toJson(Message.error("Invalid move."));
        }
        return json;
    }


    private String jump(GameBoard board, Player player, List<Row> playerBoard, int pieceJumpedRow, int pieceJumpedCol, Move move){
        int[] position = new int[2];
        if(!player.isSingleMove()) {
            if (board.getPiece(playerBoard, pieceJumpedRow, pieceJumpedCol) != null) {
                Piece piece = board.getPiece(playerBoard, pieceJumpedRow, pieceJumpedCol);

                if (player.isNotActiveColor(piece)){
                    position[0] = pieceJumpedRow;
                    position[1] = pieceJumpedCol;
                    board.addPieceRemove(position);
                    board.incrementActivePieceMoves();
                    board.setActivePieceEnd(move.getEnd());
                    return gson.toJson(Message.info("Valid Move."));

                }
                else {
                    return gson.toJson(Message.error("Can't jump over own piece."));
                }
            } else {
                return gson.toJson(Message.error("No piece to Jump."));
            }
        }
        else{
            return gson.toJson(Message.error("Cannot jump after single move."));
        }
    }

}
