package com.example.anujbastola.casinoapp.model.players;

import com.example.anujbastola.casinoapp.model.setup.Cards;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.ArrayList;
import java.util.List;

public class Player {

    // Holds the cards that are currently on the table
    private Deque<Deque<Cards>> table = new ArrayDeque<>();

    // Stores all the builts from table
    private Deque<Deque<Cards>> builtFromTable = new ArrayDeque<>();

    // Every time a move is made, this variable sets if the move is capture;
    private boolean isLastCapture;

    private int score;

    private boolean isBuildOwner = false;

    private Deque<Cards> playersHand = new ArrayDeque<>();

    private Deque<Cards> playersPile = new ArrayDeque<>();

    private Deque<Deque<Cards>> subsets = new ArrayDeque<>();

    protected Deque<Deque<Cards>> possibleBuilds = new ArrayDeque<>();

    protected Deque<Cards> built = new ArrayDeque<>();

    private Deque<Deque<Cards>> tempTable = new ArrayDeque<>();

    private Deque<Deque<Cards>> matches = new ArrayDeque<>();

    protected Build build = new Build();

    private  Cards handUsed = new Cards();

    private String computerMoveInfo;

    // Get the builts that are in table
    public Deque<Deque<Cards>> getBuiltFromTable() {
        return builtFromTable;
    }

    public void play(Deque<Deque<Cards>> tablefromRound){

        System.out.println("In Play in Player class");
        System.out.println("Player Hand: " + playersHand);
        System.out.println("Table received from round class: " + tablefromRound);

        System.out.println("Table At top is " + table);
        table = tablefromRound;
        System.out.println("Table Copied: " + table);


//        lookforSingleBuild();
//        System.out.println("Possible Builds are: " + possibleBuilds);
//        findBestSingleBuild();
//        System.out.println("Builts are: " + built);
//
//        Deque<Deque<Cards>> builtsFromTable = getBuiltsFromTable();
//
//        // if statement to check if built still exists on table
//        // One player's built can be captured by another player, without changing the owner status
//        if ( hasOwnBuild(builtsFromTable)){
//            System.out.println("Has Own Build");
//            System.out.println("Own Build in table: " + build.getBuilt());
//        }
//        else {
//            build.emptyBuilt();
//            build.setOwner(false);
//        }
//        int buildPosition = 0;


//        else if(built.size() > 0){
//            System.out.println("Setting Build Is Possible.");
//            computerMoveInfo = "Computer chose to set build on table. Build Done: " + built + " Hand Card Used: " + built.peekLast() + ".";
//            addBuiltToTable(built);
//            deleteCardFromTable(built);
//            System.out.println("Table After returning: " + table);
//            deleteHandCard(built.peekLast());
//            isBuildOwner = true;
//            build.setBuilts(built);
//            isLastCapture = false;
//        }

        // This is used to track hand card to be used to capture build
        int buildPosition = 0;
        if ( DXCaptureIsPossible()){
            System.out.println("Inside DX capture if");
            captureCards();
            isLastCapture = true;
        }
        else if ( aceCaptureIsPossible()){
            System.out.println("Inside ACE capture if");
            captureCards();
            isLastCapture = true;
        }
        else if(isBuildCapturePossible(buildPosition)){
            System.out.println("Inside isBuildCapturePossible");
            System.out.println("Hand card used to capture is " + handUsed.toString());
            System.out.println("Position of build that can be captured: " + buildPosition);
            for (Deque<Cards> insideTable: table ){
                int sum = 0;
                if (insideTable.size() > 1 ){
                    for ( Cards temp: insideTable){
                        sum = sum + getCardNumber(temp);
                    }
                    if ( sum == getCardNumber(handUsed)){
                        computerMoveInfo = "Build " + insideTable + " is Captured by using computer "+ handUsed.toString()  + " card.";
                        deleteBuiltFromTable(insideTable);
                        for ( Cards toAdd : insideTable){
                            addToPile(toAdd);
                        }
                        addToPile(handUsed);
                        deleteHandCard(handUsed);
                    }
                }
            }
        }
        else if (isThereCapture()){
            System.out.println("Inside IS there Capture");
            System.out.println("Table: " + table);
            System.out.println("Matches: " + matches );

            System.out.println("Inside Normal Capture");
            captureCards();
        }
        else {
            // If there is no any other move, then trail a card
            System.out.println("Inside Trail If statement");
            Cards temp = playersHand.poll();
            computerMoveInfo = "Computer chose to trail " + temp.toString();
            addCardTable(temp);
            isLastCapture = false;
        }


        tablefromRound = table;
        System.out.println("Table From Round at End: " + tablefromRound);
        System.out.println("Table: " + table);
        System.out.println("Player Hand at End: " + playersHand);
        clearDequeValues();
    }


    public Deque<Deque<Cards>> getTable(){
        return table;
    }


    // -------------------------------------------------------------------------------------------------------------
    // Checks if the player has its own build on the table
    public boolean hasOwnBuild(Deque<Deque<Cards>> builtsFromTable){

        Deque<Deque<Cards>> myBuilt = build.getBuilt();

        if ( myBuilt.size() !=0){
            for ( Deque<Cards> BuiltTable: builtsFromTable){
                for ( Deque<Cards> myOneBuilt: myBuilt){
                    if ( myOneBuilt.peek().toString().equals(BuiltTable.peek().toString())){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // -------------------------------------------------------------------------------------------------------------
    // Checks if capturing build that are in the table possible
    public boolean isBuildCapturePossible(int position){
        for ( Deque<Cards> insideTable: table){
            int sum = 0;
            if(insideTable.size() > 1){
                for ( Cards temp: insideTable){
                    sum = sum + getCardNumber(temp);
                }
                for ( Cards handC: playersHand){
                    if ( sum == getCardNumber(handC)){
                        handUsed = handC;
                        System.out.println("Hand is in 123: " + handUsed.toString());
                        return true;
                    }
                }
            }
            position++;
        }
        return  false;

    }

    // -------------------------------------------------------------------------------------------------------------
    // Captures individual cards, set of cards, or combination of individual and set of cards
    public  void captureCards(){


        // Returns the possible capture
        Deque<Deque<Cards>> capturePossible = possibleCaptures();


        System.out.println("Capture Possible in captureCards: " + capturePossible);
        System.out.println("hand Used in captureCards: " + handUsed.toString());

        // Stores the best capture possible
        Deque<Cards> largeCapture = getBestCapture(capturePossible);

        System.out.println("Large Capture in captureCards: " + largeCapture);
        String playerHandCardUsed = handUsed.toString();
        System.out.println("Player Hand Card Used: " + playerHandCardUsed);
        computerMoveInfo = "Computer chose to capture card(s) " + largeCapture + " by using it's " + playerHandCardUsed + " card";

        for ( Cards capt: largeCapture){
            addToPile(capt);
            Deque<Cards> temp = new ArrayDeque<>();
            temp.add(capt);
            deleteCardFromTable(temp);
        }

        System.out.println("Table After delete: " + table);
        deleteHandCard(handUsed);
        System.out.println("Hand After delete: " + playersHand);

        addToPile(handUsed);
    }



    // -------------------------------------------------------------------------------------------------------------
    public void deleteCardFromTable(Deque<Cards> temp){

        Deque<Deque<Cards>> deletedCardTable = new ArrayDeque<>();

        for ( Deque<Cards> insideTable : table){
            if ( insideTable.size() == 1){
                boolean toDelete = true;

                for ( Cards insideTemp : temp){
                    if ( insideTable.peekFirst().toString().equals(insideTemp.toString())){
                        toDelete = false;
                    }
                }

                if (toDelete){
                    // do not add to temp table
                    deletedCardTable.add(insideTable);
                }
                else {

                }
            }
            else {
                deletedCardTable.add(insideTable);
            }
        }

        table.clear();

        for ( Deque<Cards> insideTempTable: deletedCardTable){
            table.add(insideTempTable);
        }
    }


    // -------------------------------------------------------------------------------------------------------------
    public  void getMatchesForCapture(){

        for ( Deque<Cards> insideTable : table){
            int sum = 0;
            for ( Cards a : insideTable){
                sum = sum + getCardNumber(a);
            }

            for (Cards b: playersHand){
                if ( getCardNumber(b) == sum){
                    Deque<Cards> toAddInMatch = new ArrayDeque<>();
                    for ( Cards toadd: insideTable){
                        toAddInMatch.add(toadd);
                    }
                    toAddInMatch.add(b);
                    System.out.println("Matches --> " + toAddInMatch);
                    matches.add(toAddInMatch);
                }
            }
        }

       // System.out.println("Table getMatches before return: " + table);
    }
    // -------------------------------------------------------------------------------------------------------------
    public  boolean isThereCapture(){
        System.out.println("Table IN is there capture(first): " + table);
        //getMatchesForCapture();

        Deque<Deque<Cards>> possibleCap = possibleCaptures();
        System.out.println("Possible Captures: " + possibleCap);
        if ( possibleCap.size() > 0){
            System.out.println("Table IN is there capture(after): " + table);
            return true;
        }
        else {
            System.out.println("Table IN is there capture(after): " + table);
            return false;

        }
    }

    // -------------------------------------------------------------------------------------------------------------
    // Delete Cards from table that are part of build
    public void deleteCardTable(Deque<Cards> deleteFrom){

        boolean received;
        int position = 0;

        for ( Deque<Cards>  inTable: table){
            if ( inTable.size() == 1){
                received = checkCard(inTable.peekFirst(), deleteFrom);
                if ( received){
                    tempTable.add(inTable);
                }
            }
            else {
                tempTable.add(inTable);
            }
        }
        table.clear();
        table = tempTable;

    }

    // -------------------------------------------------------------------------------------------------------------

    public  boolean checkCard(Cards temp, Deque<Cards> deleteFrom){
        boolean ans = true;
        for ( Cards a : deleteFrom){
            if ( a.toString().equals(temp.toString())){
                ans = false;
                break;
            }
            else {

            }
        }
        return ans;
    }

    //  -------------------------------------------------------------------------------------------------------------
    /* *********************************************************************
    Function Name: addBuiltToTable
    Purpose: To add built to the table
    Parameters:
                add, a deuqe of built
    Return Value: none
    Local Variables:
                none
    Algorithm:
                1) Push add to built
    Assistance Received: none
    ********************************************************************* */
    public  void addBuiltToTable(Deque<Cards> add){
        System.out.println("Built to be Added in table: " + add);
        table.addFirst(add);
        System.out.println("Table after adding build: " + table);
    }

    //  -------------------------------------------------------------------------------------------------------------
    public boolean checkIfTableHadBuild(){
        for ( Deque<Cards> temp : table){
            if ( temp.size() > 1){
                return true;
            }
        }
        return false;
    }

    //  -------------------------------------------------------------------------------------------------------------
    // Finds the build with largest number of cards
     public void findBestSingleBuild() {
        //deque<Cards> built;
        int size = 0;
        Deque<Cards> temp = new ArrayDeque<>();
        for (Deque<Cards> a : possibleBuilds) {
            if (a.size() > size) {
                temp = a;
            }
            size = a.size();
        }
        for (Cards toBuilt : temp) {
            built.add(toBuilt);
        }
    }

    //  -------------------------------------------------------------------------------------------------------------
    public void lookforSingleBuild() {
        int number;
        subsets.clear();
        subsets = getAllSubsets();
        System.out.println("Subsets are: " + subsets);
        Deque<Cards> temp = new ArrayDeque<>();
        for (Cards playerCard : playersHand) {

            number = getCardNumber(playerCard);

            for (Deque<Cards> oneSubset : subsets) {
                int sum = 0;
                for (Cards b : oneSubset) {
                    sum = sum + getCardNumber(b);
                }
                int totalSum = number + sum;

                for (Cards playerCard2 : playersHand) {
                    int playerNumber = getCardNumber(playerCard2);
                    //System.out.println("Player Number: " + playerNumber + " Subset sum: " + sum);
                    if ((totalSum == playerNumber) && !(playerCard2.toString().equals(playerCard.toString()))) {
                        System.out.println("Can Build: "+ oneSubset+ " Hand: " + playerCard.toString());
                        addToPossibleBuild(oneSubset, playerCard);
                    }
                }
            }
        }
    }

    //  -------------------------------------------------------------------------------------------------------------
    public void addToPossibleBuild(Deque<Cards>fromTable, Cards fromPlayer) {

        Deque<Cards> temp = new ArrayDeque<>();
        for (Cards a : fromTable) {
            temp.add(a);
        }
        temp.add(fromPlayer);
        possibleBuilds.add(temp);
    }


    //  -------------------------------------------------------------------------------------------------------------
    // Capture the build that is on table
    public void captureBuild(Deque<Cards> buildInTable) {
        Cards h = buildInTable.peekLast();
        int sum = 0;
        for (Cards temp : buildInTable) {
            sum = sum + getCardNumber(temp);
            addToPile(temp);
        }
        for (Cards a : playersHand) {
            if (getCardNumber(a) == sum) {
                deleteHandCard(a);
                addToPile(a);
                return;
            }
        }
    }


    //  -------------------------------------------------------------------------------------------------------------
    public void deleteHandCard(Cards a){
        Deque<Cards> deletedPlayerHand = new ArrayDeque<>();

        for ( Cards oneCard : playersHand){
            if ( oneCard.toString().equals(a.toString())){

            }
            else {
                deletedPlayerHand.add(oneCard);
            }
        }

        playersHand.clear();
        for ( Cards one: deletedPlayerHand){
            playersHand.add(one);
        }
    }


    //  -------------------------------------------------------------------------------------------------------------

    //  -------------------------------------------------------------------------------------------------------------

    //  -------------------------------------------------------------------------------------------------------------



    //  -------------------------------------------------------------------------------------------------------------


    //  -------------------------------------------------------------------------------------------------------------




    //  -------------------------------------------------------------------------------------------------------------



    /* *********************************************************************
    Function Name: addCardTable
    Purpose: To add card to the table
    Parameters:
                fromPlayer, card that passes card played by the players
    Return Value: returns none(void function)
    Local Variables:
                temp, deque to store the passed card
    Algorithm:
                1) Push card to the temp deque
                2) Push temp to the table
    Assistance Received: none
    ********************************************************************* */
    public void addCardTable(Cards fromPlayer) {
        Deque<Cards> temp = new ArrayDeque<>();
        temp.addLast(fromPlayer);
        table.addLast(temp);
    }

    /* *********************************************************************
    Function Name: clearDequeValue
    Purpose: To clear the values of deque
    Parameters:
                none
    Return Value: none
    Local Variables:
                none
    Algorithm:
                1) Clear deque values
    Assistance Received: none
    ********************************************************************* */

    public void clearDequeValues() {
        subsets.clear();
        possibleBuilds.clear();
        built.clear();
        tempTable.clear();
        matches.clear();
    }

    public String getComputerMoveInfo() {
        return computerMoveInfo;
    }
    // -------------------------------------------------------------------------------------------------------------
    // Set the score of the player, used in deserialization
    public void setScore(int num) {
        score = num;
    }

    // -------------------------------------------------------------------------------------------------------------
    // get score of the player
    final public int getScore() {
        return score;
    }

    // -------------------------------------------------------------------------------------------------------------
    // Adds score in every calculation
    public void addScore(int num) {
        score = score + num;
    }

    // -------------------------------------------------------------------------------------------------------------
    // Gets card from deck every time the player's hand is empty and the card is added to the hand
    public void addCard(Cards hand) {
        playersHand.addLast(hand);
    }

    // -------------------------------------------------------------------------------------------------------------
    // Get card from the playersHand
    public Cards getCard() {
        return playersHand.pollFirst();
    }

    // -------------------------------------------------------------------------------------------------------------
    // Receives captured card and the card is added to player's pile
    public void addToPile(Cards temp) {
        playersPile.addLast(temp);
    }

    // -------------------------------------------------------------------------------------------------------------
    // Returns th size of pile. This function is used when calculating the score
    public int getPileSize() {
        return playersPile.size();
    }

    // -------------------------------------------------------------------------------------------------------------
    // Setter for Player Hand
    public void setPlayersHand(Deque<Cards> hand) {
        playersHand = hand;
    }

    // -------------------------------------------------------------------------------------------------------------
    // Setter for Player Pile
    public void setPlayersPile(Deque<Cards> pile) {
        playersPile = pile;
    }

    // -------------------------------------------------------------------------------------------------------------
    public void setBuildInBuildClass(Deque<Cards> buildToSet){
        isBuildOwner = true;
        build.setBuilts(buildToSet);
    }

    // -------------------------------------------------------------------------------------------------------------
    // Getter for build from build class
    public Deque<Deque<Cards>> getBuilt(){
        return  build.getBuilt();
    }

    // -------------------------------------------------------------------------------------------------------------
    // Clears builts from build class
    public void emptyPlayersBuilt(){
        build.emptyBuilt();
    }

    // -------------------------------------------------------------------------------------------------------------
    public void setTable(Deque<Deque<Cards>> currTable){
        table = currTable;
    }

    // -------------------------------------------------------------------------------------------------------------
    public boolean checkIfBuildOwner() {
        return isBuildOwner;
    }

    // -------------------------------------------------------------------------------------------------------------
    public  Deque<Deque<Cards>> returnTableToRoundClass(){
        System.out.println("Table to return is " + table);
        return table;
    }

    // -------------------------------------------------------------------------------------------------------------


    /* *********************************************************************
    Function Name: DXCaptureIsPossible
    Purpose: To check if dx capture is possible, capturing DX will get 2 points
    Parameters:

    Return Value: handUsed: hand card from pkayer used to do capture
                  capturePossible, all the possible captures from the table
                  largeCapture, the largest number of capture possible
    Local Variables:
                numSpades, number of spades in the player's pile
    Algorithm:
                1) Get Possible capture by calling possibleCapture(handUSed) function
                2) Get largest Capture by calling getBestCapture(POSSIBLE CAPTURES)
                3) Check if handUSed is "DX" or "DX" is present in largecapture
                4) If Yes, return true, else return false
    Assistance Received: none
    ********************************************************************* */
    public boolean DXCaptureIsPossible() {
        // Hand used to capture

        // Returns the possible Capture
        Deque<Deque<Cards>> capturePossible = possibleCaptures();

        // Stores the best capture possible
        Deque<Cards> largeCapture = getBestCapture(capturePossible);

        for (Cards cardInCapture : largeCapture) {
            if (cardInCapture.toString().equals("DX") || handUsed.toString().equals("DX")) {
                return true;
            }
        }
        //System.out.println("Table in DX Capture before return: " + table);
        return false;
    }

    // -------------------------------------------------------------------------------------------------------------
   /* *********************************************************************
    Function Name: aceCaptureIsPossible
    Purpose: To check if ace capture is possible
    Parameters:
                none
    Return Value: returns true if ace capture is possible, else returns false
    Local Variables:
                position, keeps track of the elements in table, used to delete built in that position
    Algorithm:
                1) loop through table
                2) check the front card of the built in table with the first card of the parameter
                3) if both card equal, then delete that built from table
    Assistance Received: none
    ********************************************************************* */

    public boolean aceCaptureIsPossible() {


        // Returns the possible capture
        Deque<Deque<Cards>> capturePossible = possibleCaptures();

        // Stores the best capture possible
        Deque<Cards> largeCapture = getBestCapture(capturePossible);

        for (Cards cardInCapture : largeCapture) {
            if (cardInCapture.getFace().equals("A") || handUsed.getFace().equals("A")) {
                System.out.println("Capture: ");
                printDeque(largeCapture);
                System.out.println("Hand Card to Capture: " + handUsed.toString());
                return true;
            }
        }
       // System.out.println("Table in Ace Capture before return: " + table);
        return false;
    }
    // -------------------------------------------------------------------------------------------------------------


    public Deque<Deque<Cards>> possibleCaptures() {
        Deque<Deque<Cards>> capturePossible = new ArrayDeque<>();
        int size = 0;

        // Gets the maximum number of cards that can be captured using cards from playersHand
        for (Cards card : playersHand) {

            Deque<Deque<Cards>> captureTemp = new ArrayDeque<>();
            // Get Possible function take a card and
            // returns all the possible captured possible by using that card

            captureTemp = getPossible(card);
            int num = getNumberOfCards(captureTemp);
            if (num > size) {
                handUsed = card;
                capturePossible.clear();
                capturePossible = captureTemp;
                size = num;
            }
        }
        return capturePossible;
    }

    // -------------------------------------------------------------------------------------------------------------
    public Deque<Deque<Cards>> getPossible(Cards handCheck){
        Deque<Deque<Cards>> forCheck = new ArrayDeque<>();

        Deque<Deque<Cards>> subsets = getAllSubsets();
        System.out.println("Subsets in getPossible: " + subsets);

        for ( Deque<Cards> a : subsets){
            int sum = 0;
            for (Cards b : a){
                sum = sum + getCardNumber(b);
            }
            if ( sum == getCardNumber(handCheck)){
                forCheck.add(a);
            }
        }
        return forCheck;
    }

    // -------------------------------------------------------------------------------------------------------------
    // Returns the best possible captures
    public Deque<Cards> getBestCapture(Deque<Deque<Cards>> capturePossible) {

        // Removes the repeated cards in the capture possiblle
        Deque<Deque<Cards>> tempMatch = new ArrayDeque<>();
        boolean check = false;
        for (Deque<Cards> a : capturePossible) {
            for (Cards b : a) {
                if (checkForRepeat(b, tempMatch)) {
                    check = true;
                    continue;
                }
            }
            if (!check) {
                tempMatch.add(a);
            }
        }

        Deque<Cards> largeCapture = new ArrayDeque<>();        // to store all the cards from best capture is a 1-D array
        for (Deque<Cards> a : tempMatch) {
            for (Cards b : a) {
                largeCapture.add(b);
            }
        }
        return largeCapture;
    }

    // -------------------------------------------------------------------------------------------------------------
    // Only stores the unique value for capture
    public boolean checkForRepeat(Cards a, Deque<Deque<Cards>> tempMatch) {
        for (Deque<Cards> temp : tempMatch) {
            for (Cards insideTemp : temp) {
                if (insideTemp.toString().equals(a.toString())) {
                    return true;
                }
            }
        }
        return false;
    }

    // -------------------------------------------------------------------------------------------------------------
    public int getSumOfMyBuild() {
        int sum = 0;
//        Deque<Cards> myBuild = getBuilt();
//        int sum = 0;
//        for (Cards temp : myBuild) {
//            sum = sum + getCardNumber(temp);
//        }
        return sum;
    }

    // -------------------------------------------------------------------------------------------------------------
    private Deque<Deque<Cards>> getBuiltsFromTable() {
        Deque<Deque<Cards>> toReturn = new ArrayDeque<>();
        for (Deque<Cards> insideTable : table) {
            if (insideTable.size() > 1) {
                toReturn.add(insideTable);
            }
        }
        return toReturn;
    }

    // -------------------------------------------------------------------------------------------------------------
    /* *********************************************************************
     Function Name: checkForTrail
     Purpose: To check if trail is posible or not
     Parameters:

     Return Value: true if trail is possible
                   false if trail is not possible
     Local Variables:

     Algorithm:
                 1) Check if the player is build owner
                 2) if not build owner, loop through player's hand, if the player is build owner return false
                 3) Loop through table cards
                 4) Check only loose cards, (size == 1)
                 5) Again loop through table cards
                 6) Check if face of handcard is equal to face to table card
                 7) if Yes return false, if no return true
     Assistance Received: none
     ********************************************************************* */
    public boolean checkForTrail() {
        if (!checkIfBuildOwner()) {
            for (Cards handCard : playersHand) {
                for (Deque<Cards> insideTable : table) {
                    if (insideTable.size() == 1) {
                        for (Cards tableCard : insideTable) {
                            if (handCard.getFace().equals(tableCard.getFace())) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }

    // -------------------------------------------------------------------------------------------------------------
    /* *********************************************************************
    Function Name: deleteBuiltFromTable
    Purpose: To delete built from a table
    Parameters:
    toDelete, a deque that stores built to delete from table
    Return Value: none
    Local Variables:
                    none
    Algorithm:
            1) delete Built
    Assistance Received: none
    ********************************************************************* */
    public void deleteBuiltFromTable(Deque<Cards> toDelete) {
        Deque<Deque<Cards>> tempTable = new ArrayDeque<>();
        for ( Deque<Cards> insideTable: table ){
            if ( insideTable.peekFirst().toString().equals(toDelete.peekFirst().toString())){
                System.out.println("This will not be added to temptable " + insideTable );
            }
            else {
                tempTable.add(insideTable);
            }
        }
        System.out.println("Temp Table after deleting built is " + tempTable);
        table = tempTable;
    }

    // -------------------------------------------------------------------------------------------------------------
   /* *********************************************************************
    Function Name: getNumberOfCards
    Purpose: to count number of cards in the given double deque
    Parameters:
             findMyNum, a double Deque passed to count its number of cards

    Return Value: num, number of counts in the given double deque
    Local Variables:
                num, to count number of cards
    Algorithm:
                1) Loop through double deque
                2) Update num by 1 every time there is a card
                3) return num after the loop is done
    Assistance Received: none
    ********************************************************************* */

    // -------------------------------------------------------------------------------------------------------------
    public int getNumberOfCards(Deque<Deque<Cards>> findMyNum) {
        int num = 0;
        for (Deque<Cards> singleDeque : findMyNum ) {
            for (Cards temp : singleDeque) {
                num++;
            }
        }
        return num;
    }

    /* *********************************************************************
    Function Name: getSpadesNum
    Purpose: Calculate the number of spades present in the players pile
    Parameters:

    Return Value: numSpades, returns number of spades present in the player's pile
    Local Variables:
                numSpades, number of spades in the player's pile
    Algorithm:
                1) Loop through player's pile
                2) check the suit of card to "S"
                3) if "S" increment numSpades
                4) return numSpades at the end
    Assistance Received: none
    ********************************************************************* */
    public int getSpadesNum() {
        int numSpades = 0;
        for (Cards pile : playersPile) {
            if (pile.getSuit().equals("S")) {
                numSpades++;
            }
        }
        return numSpades;
    }

    /* *********************************************************************
    Function Name: getAceNum
    Purpose: Calculate the number of Ace present in the players pile
    Parameters:
    Return Value: numAce, returns number of Ace present in the player's pile
    Local Variables:
                numSpades, number of Ace in the player's pile
    Algorithm:
                1) Loop through player's pile
                2) check the face of card to "A"
                3) if "A" increment numSpades
                4) return numAce at the end
    Assistance Received: none
    ********************************************************************* */
    public int getAceNum() {
        int numAce = 0;
        for (Cards pile : playersPile) {
            if (pile.getFace().equals("A")) {
                numAce++;
            }
        }
        return numAce;
    }


    /* *********************************************************************
    Function Name: hasTenOfDiamonds
    Purpose: checks if the player's pile has DX
    Parameters:
    Return Value: true if pile has DX , else false
    Local Variables:
                none
    Algorithm:
                1) Loop through player's pile
                2) check if card is DX
                3) if "DX" return true
                4) return false if DX is not found
    Assistance Received: none
    ********************************************************************* */

    public boolean hasTenOfDiamonds() {
        for (Cards pile : playersPile) {
            if (pile.toString().equals("DX")) {
                return true;
            }
        }
        return false;
    }


    /* *********************************************************************
    Function Name: hasTwoOfSpades
    Purpose: checks if the player's pile has S2
    Parameters:
    Return Value: true if pile has S2 , else false
    Local Variables:
                none
    Algorithm:
                1) Loop through player's pile
                2) check if card is S2
                3) if "S2" return true
                4) return false if S2 is not found
    Assistance Received: none
    ********************************************************************* */
    public boolean hasTwoOfSpades() {
        for (Cards pile : playersPile) {
            if (pile.toString().equals("S2")) {
                return true;
            }
        }
        return false;
    }


    /* *********************************************************************
    Function Name: printDetail
    Purpose: to print table, hand and pile
    Parameters:
                none
    Return Value: none
    Local Variables:
                none
    Algorithm:
                1) Call printDoubleDeque by passing table
                2) Call printDeque by passing hand and tail
    Assistance Received: none
    ********************************************************************* */

    public void printDetail() {

        System.out.println("Table");
        printDoubleDeque(table);

        System.out.println("Hand");
        printDeque(playersHand);

        System.out.println("Pile");
        printDeque(playersPile);
    }

    /* *********************************************************************
    Function Name: printDeque
    Purpose: to print all the cards in the given deque
    Parameters:
                singleDeque, 1 dimensional deque passed in the function
    Return Value: none
    Local Variables:
                none
    Algorithm:
                1) Loop through deque
                2) print the card on the deque by calling .toString() function from Cards class
    Assistance Received: none
    ********************************************************************* */
    public void printDeque(Deque<Cards> singleDeque) {

        for (Cards temp : singleDeque) {
            System.out.print(temp.toString() + " ");
        }
        System.out.println();
    }

    /* *********************************************************************
    Function Name: printDoubleDeque
    Purpose: to print all the cards in the given 2 dimensional deque
    Parameters:
                doubleDeque, 2 dimensional deque passed in the function
    Return Value: none
    Local Variables:
                none
    Algorithm:
                1) Loop through 2 dimensional deque
                2) print the card on the doubleDeque by calling .toString() function from Cards class
    Assistance Received: none
    ********************************************************************* */

    public void printDoubleDeque(Deque<Deque<Cards>> doubleDeque) {
        for (Deque<Cards> insideDeque : doubleDeque) {
            for (Cards oneCard : insideDeque) {
                System.out.print(oneCard.toString() + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /* *********************************************************************
    Function Name: getCardNumber
    Purpose: To get the number value of face of a card
    Parameters:
                findNum, card to find its number
                face, string to store the face of card
    Return Value: returns value, number value of the card
    Local Variables:
                value, integer to store the number value of the card
    Algorithm:
                1) Get the face of card and store it in String face
                2) Compare face with X J K A and store the respective value
                3) else parse the string of face to integer and store it in value
    Assistance Received: none
    ********************************************************************* */
    public int getCardNumber(Cards findNum) {
        int value;
        String face = findNum.getFace();
        if (face.equals("X")) {
            value = 10;
        } else if (face.equals("J")) {
            value = 11;
        } else if (face.equals("Q")) {
            value = 12;
        } else if (face.equals("K")) {
            value = 13;
        } else if (face.equals("A")) {
            value = 1;
        } else {
            value = Integer.parseInt(face);
        }
        return value;
    }


    // Returns all the cards in player's hand
    public Deque<Cards> returnPlayerHand() {
        return playersHand;
    }

    // Returns all the cards in player's pile
    public Deque<Cards> returnPlayerPile() {
        return playersPile;
    }


    public Deque<Deque<Cards>> getAllSubsets() {

        Deque<Deque<Cards>> tempTable = new ArrayDeque<>();
        tempTable = copyTable();

        // Temp Array to store all the string value of loose cards on table
        // This string deque is used to find subset
        // This string deque is then converted to Deque of Cards
        ArrayList<String> tempArray = new ArrayList<>();
        for (Deque<Cards> insideTable : tempTable) {
            if (insideTable.size() == 1) {
                for (Cards a : insideTable) {
                    tempArray.add(a.toString());
                }
            }
        }

        ArrayList<ArrayList<String>> lists = getSubsets(tempArray);

        // This calls convert
        Deque<Deque<Cards>> subset = covertToCards(lists);
        subset.pollFirst();
        return subset;
    }


    public Deque<Deque<Cards>> covertToCards(ArrayList<ArrayList<String>> list){
        Deque<Deque<Cards>> convertedDeque = new ArrayDeque<>();
        for (ArrayList<String> insideList: list){
            Deque<Cards> temp = new ArrayDeque<>();
            for ( String singleCard: insideList){
                Cards a = new Cards( singleCard.substring(0,1), singleCard.substring(1,2) );
                temp.add(a);
            }
            convertedDeque.add(temp);
        }
        return  convertedDeque;
    }

    public ArrayList<ArrayList<String>> getSubsets(ArrayList<String> SubList) {
        if (SubList.size() > 0) {
            ArrayList<ArrayList<String>> list = addToList(SubList.remove(0), SubList);
            return list;
        } else {
            ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
            list.add(SubList);
            return list;
        }
    }

    public ArrayList<ArrayList<String>> addToList(
            String firstElement, ArrayList<String> SubList) {
        ArrayList<ArrayList<String>> listOfLists = getSubsets(SubList);
        ArrayList<ArrayList<String>> superList = new ArrayList<ArrayList<String>>();
        for (ArrayList<String> iList : listOfLists) {
            superList.add(new ArrayList<String>(iList));
            iList.add(firstElement);
            superList.add(new ArrayList<String>(iList));
        }
        return superList;
    }

    public  Deque<Deque<Cards>> copyTable(){
        Deque<Deque<Cards>> temp = new ArrayDeque<>();

        for ( Deque<Cards> insideTable: table){
            temp.add(insideTable);
        }
        return temp;
    }


    public  void printTable(Deque<Deque<Cards>> mytable){
        System.out.println("Table in Player: " + mytable);
    }
}
