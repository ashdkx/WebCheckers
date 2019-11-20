package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.GameBoard;
import com.webcheckers.appl.GameCenter;
import com.webcheckers.util.Message;
import spark.*;

import java.util.logging.Logger;


/**
 * @author Nicholas Curl
 */
public class PostBackupMoveRoute implements Route {


    private static final Logger LOG = Logger.getLogger(PostBackupMoveRoute.class.getName());
    private GameCenter gameCenter;
    private Gson gson;
    public PostBackupMoveRoute(GameCenter gameCenter, Gson gson){
        LOG.config("PostBackupMoveRoute is initialized.");
        this.gameCenter = gameCenter;
        this.gson = gson;
    }

    @Override
    public Object handle(Request request, Response response) {

        LOG.finer("PostBackupMoveRoute is invoked.");
        String json;
        GameBoard board = gameCenter.getGame(request.queryParams(GetGameRoute.GAMEID_PARAM));
        board.decrementActivePieceMoves();
        if(!board.getPieceRemove().isEmpty()){
            board.removePieceRemove();
        }
        if (board.isSingleMove()){ //Checks to see if the piece has moved one space
            board.setSingleMove(false);
        }
        if(board.getActivePieceMoves()==0){ //Checks to see if there are no pending moves
            board.setActivePiece(null);
            board.getActivePieceEnd(); //pops the activePieceEnd
            json = gson.toJson(Message.info("Backup Successful."));
        }
        else if (board.getActivePieceMoves() < 0||board.getActivePiece()==null) { //Error checking if player attempts to undo move
            board.setActivePieceMoves(0);
            board.clearActivePieceEnd();
            json = gson.toJson(Message.error("Cannot Backup."));
        }
        else {
            board.getActivePieceEnd();
            json = gson.toJson(Message.info("Backup Successful."));
        }
        return json;
    }


}
