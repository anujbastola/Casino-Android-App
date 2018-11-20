package com.example.anujbastola.casinoapp.model.players;

import com.example.anujbastola.casinoapp.model.setup.Cards;

import java.util.ArrayDeque;
import java.util.Deque;

public class Human extends Player {

        private  String help;

        public  String askForHelp(Deque<Deque<Cards>> table){


            System.out.println("Table in Human Class: " + table);

            lookforSingleBuild();
            findBestSingleBuild();
            Deque<Deque<Cards>> builtsFromTable = new ArrayDeque<>();
            builtsFromTable.clear();

            builtsFromTable = getBuiltFromTable();

            if ( hasOwnBuild(builtsFromTable)){

            }
            else{
                build.emptyBuilt();
                build.setOwner(false);
            }

            int buildPosition = 0;
            Cards handC = new Cards();
            if ( DXCaptureIsPossible()){
                help = "DX Capture is possible. It will give you 2 points.";
            }
            else if ( aceCaptureIsPossible()){
                help = "Capture Ace Card(s). It will give you 1 point for each Ace capture";
            }
//            else  if (helpModeCheckExtend()){
//                help = "Extend Opponent's build";
//            }
            else if(isBuildCapturePossible(buildPosition)){
                help = "Capture Build from table.";
            }
            else  if (built.size() > 0){
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



//    public boolean helpModeCheckExtend() {
//
//        Deque<Deque<Cards>> opponentBuilds = getOpponentBuilds();
//        Deque<Cards> temp;
//        for (Deque<Cards> eachBuild : opponentBuilds) {
//            int sum = 0;
//            for (Cards eachCard : eachBuild) {
//                sum = sum + getCardNumber(eachCard);
//            }
//
//            for (Cards playerCard : returnPlayerHand()) {
//                for (Cards tempPlayerCard : returnPlayerHand()) {
//                    if ((getCardNumber(playerCard) + sum) == getCardNumber(tempPlayerCard)) {
//                        printDeque(eachBuild);
//                        return true;
//                    }
//                }
//            }
//        }
//        return false;
//    }

}



