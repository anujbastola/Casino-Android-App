package com.example.anujbastola.casinoapp.model.players;

import com.example.anujbastola.casinoapp.model.setup.Cards;

import java.util.Deque;

public class Computer extends Player {

    public void play(Deque<Deque<Cards>> tablefromRound) {
        System.out.println("In computer Class --------------------------------------------------------------");

        System.out.println("In Play in Player class");
        System.out.println("Player Hand: " + returnPlayerHand());
        System.out.println("Table received from round class: " + tablefromRound);

        System.out.println("Table At top is " + table);
        table = tablefromRound;
        System.out.println("Table Copied: " + table);



        System.out.println("Builts are: " + built);

        Deque<Deque<Cards>> builtsFromTable = getBuiltsFromTable();

        // if statement to check if built still exists on table
        // One player's built can be captured by another player, without changing the owner status
        if ( hasOwnBuild(builtsFromTable)){
            System.out.println("Has Own Build");
            System.out.println("Own Build in table: " + build.getBuilt());
        }
        else {
            built.clear();
            lookforSingleBuild();
            System.out.println("Possible Builds are: " + possibleBuilds);
            findBestSingleBuild();
            build.emptyBuilt();
            build.setOwner(false);
        }

        // This is used to track hand card to be used to capture build
        if (DXCaptureIsPossible()) {
            System.out.println("Inside DX capture if");
            captureCards();
            isLastCapture = true;
        } else if (aceCaptureIsPossible()) {
            System.out.println("Inside ACE capture if");
            captureCards();
            isLastCapture = true;
        } else if (isBuildCapturePossible()) {
            System.out.println("Inside isBuildCapturePossible");
            System.out.println("Hand card used to capture is " + handUsed.toString());
            for (Deque<Cards> insideTable : table) {
                int sum = 0;
                if (insideTable.size() > 1) {
                    for (Cards temp : insideTable) {
                        sum = sum + getCardNumber(temp);
                    }
                    if (sum == getCardNumber(handUsed)) {
                         setComputerMoveInfo("Build " + insideTable + " is Captured by using computer " + handUsed.toString() + " card.");
                        deleteBuiltFromTable(insideTable);
                        for (Cards toAdd : insideTable) {
                            addToPile(toAdd);
                        }
                        addToPile(handUsed);
                        deleteHandCard(handUsed);
                    }
                }
            }
            isLastCapture = true;
            builtsFromTable = getBuiltsFromTable();
            if ( !hasOwnBuild(builtsFromTable)){
                build.emptyBuilt();
                built.clear();
            }
        }
        else if(built.size() > 0){
            System.out.println("Setting Build Is Possible.");
            setComputerMoveInfo("Computer chose to set build on table. Build Done: " + built + " Hand Card Used: " + built.peekLast());
            addBuiltToTable(built);
            deleteCardFromTable(built);
            System.out.println("Table After returning: " + table);
            deleteHandCard(built.peekLast());
            isBuildOwner = true;
            build.setBuilts(built);
            isLastCapture = false;
        }

        else if (isThereCapture()) {
            System.out.println("Inside IS there Capture");
            System.out.println("Table: " + table);
            System.out.println("Matches: " + getMatches());

            System.out.println("Inside Normal Capture");
            captureCards();
            isLastCapture = true;
        } else {
            // If there is no any other move, then trail a card
            System.out.println("Inside Trail If statement");
            Cards temp = returnPlayerHand().poll();
            setComputerMoveInfo("Computer chose to trail " + temp.toString());
            addCardTable(temp);
            isLastCapture = false;
        }


        tablefromRound = table;
        System.out.println("Table From Round at End: " + tablefromRound);
        System.out.println("Table: " + table);
        System.out.println("Player Hand at End: " + returnPlayerHand());
        clearDequeValues();
    }
}
