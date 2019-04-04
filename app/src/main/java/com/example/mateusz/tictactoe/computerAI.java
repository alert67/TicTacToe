package com.example.mateusz.tictactoe;


import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class computerAI {



    // 0 - krzyzyk /// 1 - kolko

    private int huPlayer=0;

    private int aiPlayer=1;


    public computerAI() {
    }

    public boolean winPosition(int[] gameState, int player){
        if(
                        (gameState[0] == player && gameState[1] == player && gameState[2] == player) ||
                        (gameState[3] == player && gameState[4] == player && gameState[5] == player) ||
                        (gameState[6] == player && gameState[7] == player && gameState[8] == player) ||
                        (gameState[0] == player && gameState[3] == player && gameState[6] == player) ||
                        (gameState[1] == player && gameState[4] == player && gameState[7] == player) ||
                        (gameState[2] == player && gameState[5] == player && gameState[8] == player) ||
                        (gameState[0] == player && gameState[4] == player && gameState[8] == player) ||
                        (gameState[2] == player && gameState[4] == player && gameState[6] == player)
                )
        {
            return true;
        }else{
            return false;
        }
    }

    int isWin(int[] gameState, int depth){
        if(
                        (gameState[0] == aiPlayer && gameState[1] == aiPlayer && gameState[2] == aiPlayer) ||
                        (gameState[3] == aiPlayer && gameState[4] == aiPlayer && gameState[5] == aiPlayer) ||
                        (gameState[6] == aiPlayer && gameState[7] == aiPlayer && gameState[8] == aiPlayer) ||
                        (gameState[0] == aiPlayer && gameState[3] == aiPlayer && gameState[6] == aiPlayer) ||
                        (gameState[1] == aiPlayer && gameState[4] == aiPlayer && gameState[7] == aiPlayer) ||
                        (gameState[2] == aiPlayer && gameState[5] == aiPlayer && gameState[8] == aiPlayer) ||
                        (gameState[0] == aiPlayer && gameState[4] == aiPlayer && gameState[8] == aiPlayer) ||
                        (gameState[2] == aiPlayer && gameState[4] == aiPlayer && gameState[6] == aiPlayer)

                ){return -(10);}
                else if(

                        (gameState[0] == huPlayer && gameState[1] == huPlayer && gameState[2] == huPlayer) ||
                        (gameState[3] == huPlayer && gameState[4] == huPlayer && gameState[5] == huPlayer) ||
                        (gameState[6] == huPlayer && gameState[7] == huPlayer && gameState[8] == huPlayer) ||
                        (gameState[0] == huPlayer && gameState[3] == huPlayer && gameState[6] == huPlayer) ||
                        (gameState[1] == huPlayer && gameState[4] == huPlayer && gameState[7] == huPlayer) ||
                        (gameState[2] == huPlayer && gameState[5] == huPlayer && gameState[8] == huPlayer) ||
                        (gameState[0] == huPlayer && gameState[4] == huPlayer && gameState[8] == huPlayer) ||
                        (gameState[2] == huPlayer && gameState[4] == huPlayer && gameState[6] == huPlayer)


                ) {return 10;}
                {
            return 0;
        }
    }

    public boolean checkMovesLeft(int[] gameState){
        for(int i=0;i<gameState.length;i++){
            if(gameState[i]!=1 && gameState[i]!=0){
                return true;
            }
        }
        return false;
    }



    int minimax(int[] gameState, int depth, int player){
        int score = isWin(gameState,depth);
        if(isWin(gameState,depth)==10) return score-depth;
        else if(isWin(gameState,depth)== -10) return score+depth;

        if(checkMovesLeft(gameState)==false) return 0;

        if(player==aiPlayer){
            int bestValue = 99999999;
            for(int i=0;i<gameState.length; i++){
                if(gameState[i]==2){
                    int before = gameState[i];
                    gameState[i]= aiPlayer;

                    int value = minimax(gameState,depth++,huPlayer);
                    bestValue = Math.min(bestValue,value);

                    gameState[i]=before;

                }
            }
           //Log.i("bestValue","bestValue:"+bestValue);
            return bestValue;
        }else{
            int bestValue = -99999999;
            for(int i =0; i<gameState.length;i++){
                if(gameState[i]==2){
                    int before = gameState[i];
                    gameState[i]=huPlayer;

                    int value = minimax(gameState,depth++,aiPlayer);
                    bestValue = Math.max(bestValue,value);

                    gameState[i]=before;
                }
            }
            //Log.i("bestValue","bestValue:"+bestValue);
            return bestValue;

        }

    }

    public int findBestMoves(int[] gameState){
        int bestMoveValues = 999999999;
        int bestMove = -1;

        for(int i = 0; i<gameState.length;i++){
            if(gameState[i]==2){
                int before = gameState[i];
                gameState[i]= aiPlayer;
                //Log.i("Game state after",""+ gameState[i]);

                int bestValue = minimax(gameState,0,huPlayer);

                gameState[i]=before;

                if(bestValue<bestMoveValues){
                   // Log.i("best values",i+"bestValue "+bestValue+"<"+bestMoveValues+" bestMoveValues");
                    bestMoveValues = bestValue;
                    bestMove = i;
                }
            }
        }
        return bestMove;
    }




}
