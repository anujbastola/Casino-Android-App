package com.example.anujbastola.casinoapp.model.players;

import com.example.anujbastola.casinoapp.model.setup.Cards;

import java.util.ArrayDeque;
import java.util.Deque;

public class Human extends Player {

        private  String help;

        public  String askForHelp(Deque<Deque<Cards>> mytable){
            table = mytable;

            System.out.println("Table in Human Class: " + table);

            lookforSingleBuild();
            findBestSingleBuild();
            Deque<Deque<Cards>> builtsFromTable = new ArrayDeque<>();
            builtsFromTable.clear();

            builtsFromTable = getBuiltFromTable();

            System.out.println("Builts from table in Human class " + builtsFromTable);
            System.out.println("Find Single best build in Human Class " + built);

            if ( hasOwnBuild(builtsFromTable)){

            }
            else{
                build.emptyBuilt();
                build.setOwner(false);
            }

            if ( DXCaptureIsPossible()){
                help = "DX Capture is possible. It will give you 2 points.";
            }
            else if ( aceCaptureIsPossible()){
                help = "Capture Ace Card(s). It will give you 1 point for each Ace capture";
            }
            else  if (helpModeCheckExtend()){
                help = "Extend Opponent's build";
            }
            else if(isBuildCapturePossible()){
                help = "Capture Build from table.";
            }
            else  if (possibleBuilds.size() > 0){
                help = "The possible build that you could you use to set are : " + possibleBuilds;
            }
            else if ( isThereCapture()){
                Deque<Deque<Cards>> capturePossible = possibleCaptures();
                help = "Here are possible capture(s): " + capturePossible;
            }
            else {
                help = "Your option is to Trail Card. Trail any card from your hand." ;
            }

            clearDequeValues();
            return help;
        }



    public boolean helpModeCheckExtend() {

        Deque<Deque<Cards>> opponentBuilds = getOpponentBuilds();
        Deque<Cards> temp;
        for (Deque<Cards> eachBuild : opponentBuilds) {
            int sum = 0;
            for (Cards eachCard : eachBuild) {
                sum = sum + getCardNumber(eachCard);
            }

            for (Cards playerCard : returnPlayerHand()) {
                for (Cards tempPlayerCard : returnPlayerHand()) {
                    if ((getCardNumber(playerCard) + sum) == getCardNumber(tempPlayerCard)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}



