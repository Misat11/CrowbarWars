/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misat11.core.server.reasons;

/**
 *
 * @author misat11
 */
public enum ClientStates {
        //Client doing nothing
        NOTHING,
        //Check, if connection is valid
        VALIDATE_CONNECTION,
        //Check, if player name is not used
        VALIDATE_PLAYER_SETTINGS,
        //Sending all info of server map
        SYNC_CLIENT_WITH_SERVER,
        //Spawning and playing
        JOIN_TO_GAME,
        //Check, if client has all info of server map
        POST_CHECK_SYNC,
        //All is done
        DONE;
}
