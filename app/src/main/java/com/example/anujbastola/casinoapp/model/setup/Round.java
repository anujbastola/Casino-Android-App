package com.example.anujbastola.casinoapp.model.setup;

import com.example.anujbastola.casinoapp.model.players.Computer;
import com.example.anujbastola.casinoapp.model.players.Human;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Round {

//    ------------------- Public Methods -------------------------

    // Default constructor
    public Round() {
    }


    //------------------------------------------------------------------------------------------------------------------------------------------
    // Sets the name of the player who will play next
    public void setNextPlayer(String next) {
        nextPlayer = next;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getter for name of the next player
    public String getNextPlayer() {
        return nextPlayer;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    /* *********************************************************************
     Function Name: setHands
     Purpose: to add 4 cards to players and table
     Parameters:

     Return Value: none
     Local Variables:
                temp, Deque object to store each table card

     Algorithm:
                 1) Add four cards to Computer hand by getting front card from deck
                 2) Add four cards to Human hand by getting front card from deck
                 3) Add 4 cards to table.
     Assistance Received: none
     ********************************************************************* */
    public void setHands() {

        //Adds four cards to Computer's hand
        for (int i = 0; i < 4; i++) {
            player1.addCard(deck.returnFrontCard());
        }

        //Adds four cards to Human's hand
        for (int i = 0; i < 4; i++) {
            player2.addCard(deck.returnFrontCard());
        }

        // Function gets 4 cards from deck and adds it to Deque of table
        for (int i = 0; i < 4; i++) {
            Deque<Cards> temp = new ArrayDeque<>();
            temp.add(deck.returnFrontCard());
            table.add(temp);
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
     /* *********************************************************************
     Function Name: addCardsPlayer
     Purpose: whenever player's hands are empty and there are some cards in deck, this function adds four cards to each player
     Parameters:

     Return Value: none
     Local Variables:

     Algorithm:
                 1) Add four cards to Computer hand by getting front card from deck
                 2) Add four cards to Human hand by getting front card from deck
     Assistance Received: none
     ********************************************************************* */
    public void addCardsPlayer() {
        for (int i = 0; i < 4; i++) {
            player1.addCard(deck.returnFrontCard());
        }
        for (int i = 0; i < 4; i++) {
            player2.addCard(deck.returnFrontCard());
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getter of table cards
    public Deque<Deque<Cards>> getTable() {
        return table;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getter for computer hand
    public Deque<Cards> getComputerHand() {
        return player1.returnPlayerHand();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getter for human player hand
    public Deque<Cards> getHumanHand() {
        return player2.returnPlayerHand();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getter for computer pile
    public Deque<Cards> getComputerPile() {
        return player1.returnPlayerPile();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getter for human pile
    public Deque<Cards> getHumanPile() {
        return player2.returnPlayerPile();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getter for table cards
    public Deque<Deque<Cards>> returnTable() {
        return table;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getter for human pile
    public Deque<Cards> getDeck() {
        return deck.getDeck();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getters for computer score
    public int getComputerScore() {
        return player1.getScore();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getters for human score
    public int getHumanScore() {
        return player2.getScore();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getter for round number
    public int getRoundNum() {
        return roundNum;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Setter for round number
    public void setRoundNum(int roundnumber) {
        roundNum = roundnumber;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Setter for human score
    public void setHumanScore(int human) {
        player2.setScore(human);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Setter for computer score
    public void setComputerScore(int comp) {
        player1.setScore(comp);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getter for number of spades in human pile
    public int getHumanSpadesNum() {
        return player2.getSpadesNum();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getter for number of spades in computer pile
    public int getComputerSpadesNum() {
        return player1.getSpadesNum();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getter for last capture
    public String getLastCaptureName(){
            return lastCapture;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Adds card to computer pile
    public void addToComputerPile(Cards temp){
        player1.addToPile(temp);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Adds card to human pile
    public void addToHumanPile(Cards temp){
        player2.addToPile(temp);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getter for Computer Move Info
    public String getComputerMoveInfo() {
        return player1.getComputerMoveInfo();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    public void playGame() {
        // Computer plays for move
        player1.play(table);
        // IF the move done by computer involves capturing cards, lastCapture will be computer
        if ( player1.getLastCaptureBool()){
            lastCapture = "Computer";
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    /* *********************************************************************
     Function Name: trailHumanCard
     Purpose: when human clicks on Trail button, this function trails card to table
     Parameters:
                humanCard, String card to trail in human
     Return Value: boolean value, true if trail is possible, else false
     Local Variables:
            toAddInTable, Card object for human card
            toTrailInTable, deque object for table card
     Algorithm:
                 1) Check if human is build owner, if build owner, trail move is not possible, so return false
                 2) Loop through table cards, if face of human card matches face of any loose cards in table, then trail is not possible
                 3) if trail is possible, add human card to table
                 4) then, delete human card from player hand
     Assistance Received: none
     ********************************************************************* */
    public boolean trailHumanCard(String humanCard) {
        Cards toAddInTable = new Cards(humanCard.substring(0, 1), humanCard.substring(1, 2));

        if (!player2.getBuildOwner()) {
            for (Deque<Cards> temp : table) {
                if (temp.peekFirst().getFace().equals(humanCard.substring(1, 2))) {
                    return false;
                }
            }
            Deque<Cards> toTrailInTable = new ArrayDeque<>();
            toTrailInTable.add(toAddInTable);
            table.add(toTrailInTable);
            player2.deleteHandCard(toAddInTable);
            lastCapture = "Human";
            return true;
        } else {
            return false;
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
     /* *********************************************************************
     Function Name: doCaptureMoveForHuman
     Purpose: do capture for human
     Parameters:
                selectedTableCards, String deque for all the selected table cards
                selectedHumanCard, String for selected human card
     Return Value: none
     Local Variables:
            humanCard, Card object for human card
     Algorithm:
                 1) add human card to pile, delete the card from human hand
                 2) Convert the cards that could be captured to Deque of Cards
                 3) Delete the possible capture possible cards from table
                 4) Add possible cards to player's pile
     Assistance Received: none
     ********************************************************************* */
    public void doCaptureMoveForHuman(Deque<String> selectedTableCards, String selectedHandCard) {
        player2.setTable(table);
        Cards humanCard = new Cards(selectedHandCard.substring(0, 1), selectedHandCard.substring(1, 2));
        player2.addToPile(humanCard);
        player2.deleteHandCard(humanCard);

        for (String tableCard : selectedTableCards) {
            Cards temp = new Cards(tableCard.substring(0, 1), tableCard.substring(1, 2));
            Deque<Cards> deq = new ArrayDeque<>();
            deq.add(temp);
            player2.deleteCardFromTable(deq);
            player2.addToPile(temp);
        }
        table = player2.returnTableToRoundClass();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
     /* *********************************************************************
     Function Name: doSetBuildForHuman
     Purpose: to set build for human
     Parameters:
                selectedTableCards, String deque for all the selected table cards
                selectedHumanCard, String for selected human card
     Return Value: none
     Local Variables:
            humanCard, Card object for human card
     Algorithm:
                 1) Delete cards that are part of build form table
                 2) Store all that cards that are part of build in one deque
                 3) add the human card to deque, delete human card from hand
                 4) Add deque to table
     Assistance Received: none
     ********************************************************************* */
    public void doSetBuildForHuman(Deque<String> selectedTableCards, String selectedHandCard) {

        player2.setTable(table);
        Cards humanCard = new Cards(selectedHandCard.substring(0, 1), selectedHandCard.substring(1, 2));
        player2.deleteHandCard(humanCard);
        Deque<Cards> build = new ArrayDeque<>();
        for (String oneSelectedCard : selectedTableCards) {
            Cards temp = new Cards(oneSelectedCard.substring(0, 1), oneSelectedCard.substring(1, 2));
            build.add(temp);
        }
        player2.setBuildInBuildClass(build);
        player2.setBuildOwner(true);
        player2.deleteCardFromTable(build);
        build.add(humanCard);
        table.addFirst(build);
    }

    /* *********************************************************************
     Function Name: doExtendBuild
     Purpose: to set build for human
     Parameters:
                selectedTableCards, String deque for all the selected table cards
                selectedHumanCard, String for selected human card
     Return Value: none
     Local Variables:
            humanCard, Card object for human card
            build, extended build
     Algorithm:
                 1) get the sum of builds cards and human cards
                 2) check if player has card that will be used to capture the extended build
                 3) if player does not have card to capture extended build, then return check = false, else check = true
                 4) if check = true, delete the old build, add new build to table
                 5) delete human card from table
     Assistance Received: none
     ********************************************************************* */
    public void doExtendBuild(Deque<String> selectedTableCards, String selectedHandCard) {
        player2.setTable(table);
        Cards humanCard = new Cards(selectedHandCard.substring(0, 1), selectedHandCard.substring(1, 2));
        Deque<Cards> build = new ArrayDeque<>();
        int sum = 0;
        for (String oneSelectedCard : selectedTableCards) {
            Cards temp = new Cards(oneSelectedCard.substring(0, 1), oneSelectedCard.substring(1, 2));
            sum = sum + player2.getCardNumber(temp);
            build.add(temp);
        }
        sum = sum + player2.getCardNumber(humanCard);

        boolean check = false;
        for (Cards temp : player2.returnPlayerHand()) {
            if (player2.getCardNumber(temp) == sum) {
                check = true;
            }
        }
        if (check) {
            for (Deque<Cards> insideTable : table) {
                if (insideTable.size() > 1) {
                    for (Cards temp : insideTable) {
                        if (temp.toString().equals(build.peekFirst().toString())) {
                            player2.deleteHandCard(humanCard);
                            insideTable.addLast(humanCard);
                            player2.setBuildOwner(true);
                            break;
                        }
                    }
                }
            }
        }

    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    /* *********************************************************************
     Function Name: doCaptureBuildForHuman
     Purpose: to capture build
     Parameters:
                selectedTableCards, String deque for all the selected table cards
                selectedHumanCard, String for selected human card
     Return Value: none
     Local Variables:
            humanCard, Card object for human card
            build,  build to capture
     Algorithm:
                 1) if sum of cards on build is equal to card number of human
                 2) Add build cards to pile
                 3) Add human card to pile
                 4) Delete build cards from table and delete human card from hand
     Assistance Received: none
     ********************************************************************* */
    public void doCaptureBuildForHuman(Deque<String> selectedTableCards, String selectedHandCard) {

        player2.setTable(table);
        Cards humanCard = new Cards(selectedHandCard.substring(0, 1), selectedHandCard.substring(1, 2));
        Deque<Cards> build = new ArrayDeque<>();
        int sum = 0;
        for (String oneSelectedCard : selectedTableCards) {
            Cards temp = new Cards(oneSelectedCard.substring(0, 1), oneSelectedCard.substring(1, 2));
            sum = player2.getCardNumber(temp) + sum;
            build.add(temp);
        }

        int handNumber = player2.getCardNumber(humanCard);
        if (sum == handNumber) {
            deleteBuiltFromTable(build);
            player2.deleteHandCard(humanCard);

            for (Cards a : build) {
                player2.addToPile(a);
            }
            player2.addToPile(humanCard);
        }
        player2.setTable(table);
        // If the player does not have build, then the owner will be set to false
        if (player2.hasOwnBuild(table)) {

        } else {
            player2.emptyPlayersBuilt();
            player2.setBuildOwner(false);
        }
        lastCapture = "Human";

    }

    //------------------------------------------------------------------------------------------------------------------------------------------
        /* *********************************************************************
     Function Name: deleteBuiltFromTable
     Purpose: to set build for human
     Parameters:
                toDelete, stores cards that are part of build
     Return Value: none
     Local Variables:

     Algorithm:
                 1) declare temptable to store all cards that are not part of build
                 2) if card in toDelete matches with build, do not add the buildin temptable
                 3) Add all the card that are no part of build
                 4) original table = tempTable
     Assistance Received: none
     ********************************************************************* */
    public void deleteBuiltFromTable(Deque<Cards> toDelete) {
        Deque<Deque<Cards>> tempTable = new ArrayDeque<>();
        for (Deque<Cards> insideTable : table) {
            boolean check = false;
            for (Cards inDelete : toDelete) {
                if (insideTable.peekFirst().toString().equals(inDelete.toString())) {
                    // if this is the built
                    check = true;
                }
            }
            if (check) {
                // do not add anything
            } else {
                tempTable.add(insideTable);
            }
        }
        table = tempTable;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Getter for help fo human, calls human class and gets the best possible move
    public String getHelp() {
        player2.setTable(table);
        String help = player2.askForHelp(table);
        return help;
    }


    //------------------------------------------------------------------------------------------------------------------------------------------
    // If the user opts to load game, then this function is called from mainactivity clas
    public void loadGame(String filepath) {

        int roundNumber = 99, computerScore = 99, humanScore = 99;
        String buildOwnerName = "";
        Deque<Cards> mydeck = new ArrayDeque<>();
        Deque<Cards> computerHand = new ArrayDeque<>();
        Deque<Cards> computerPile = new ArrayDeque<>();
        Deque<Cards> humanHand = new ArrayDeque<>();
        Deque<Cards> humanPile = new ArrayDeque<>();
        Deque<Cards> buildOwner = new ArrayDeque<>();
        Deque<Deque<Cards>> mytable = new ArrayDeque<>();

        try {
            File file = new File(filepath);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;
            int lineNumber = 1;
            while ((line = br.readLine()) != null) {

                // Checks for the line with round Number and stores the number in roundNum private variable
                if (lineNumber == 1) {
                    roundNumber = Integer.parseInt(line.split(": ")[1]);
                    roundNum = roundNumber;
                }
                // extracting computer score
                if (lineNumber == 4) {
                    computerScore = Integer.parseInt(line.split(": ")[1]);
                }
                // extracting human score
                // Cards from computer's hand
                if (lineNumber == 5) {
                    String[] compHandString = getStringForCards(line);
                    for (int i = 0; i < compHandString.length; i++) {
                        String suit = compHandString[i].substring(0, 1);
                        String face = compHandString[i].substring(1, 2);
                        Cards cards = new Cards(suit, face);
                        computerHand.add(cards);
                    }
                }
                // Cards from computer's pile
                if (lineNumber == 6) {
                    String[] compPileString = getStringForCards(line);
                    for (int i = 0; i < compPileString.length; i++) {
                        String suit = compPileString[i].substring(0, 1);
                        String face = compPileString[i].substring(1, 2);
                        Cards cards = new Cards(suit, face);
                        computerPile.add(cards);
                    }

                }
                System.out.println();

                // Human Score
                if (lineNumber == 8) {
                    humanScore = Integer.parseInt(line.split(": ")[1]);
                }
                // Cards from human's hand
                if (lineNumber == 9) {
                    String[] humanHandString = getStringForCards(line);
                    for (int i = 0; i < humanHandString.length; i++) {
                        String suit = humanHandString[i].substring(0, 1);
                        String face = humanHandString[i].substring(1, 2);
                        Cards cards = new Cards(suit, face);
                        humanHand.add(cards);
                    }
                }
                System.out.println();
                // Cards from human's pile
                if (lineNumber == 10) {
                    String[] humanPileString = getStringForCards(line);
                    System.out.println(humanPileString.length);
                    for (int i = 0; i < humanPileString.length; i++) {
                        String suit = humanPileString[i].substring(0, 1);
                        String face = humanPileString[i].substring(1, 2);
                        Cards cards = new Cards(suit, face);
                        humanPile.add(cards);
                    }
                }
                System.out.println();

                // Getting table cards
                if (lineNumber == 12) {
                    String[] filetable = getStringForCards(line);

                    build.clear();
                    boolean check = false;
                    for (int i = 0; i < filetable.length; i++) {

                        if (filetable[i].equals("[")) {
                            check = true;
                        } else if (filetable[i].equals("]")) {
                            table.addFirst(build);
                            check = false;
                        } else if (check) {
                            Cards temp = new Cards(filetable[i].substring(0, 1), filetable[i].substring(1, 2));
                            build.addLast(temp);
                        } else {
                            Deque<Cards> tableCard = new ArrayDeque<>();
                            Cards temp = new Cards(filetable[i].substring(0, 1), filetable[i].substring(1, 2));
                            tableCard.addLast(temp);
                            table.addLast(tableCard);
                        }
                    }
                }

                // Getting build Owner
                if (lineNumber == 14) {
                    String[] rightSide = getStringForCards(line);
                    boolean check = false;
                    for (int i = 0; i < rightSide.length; i++) {
                        if (rightSide[i].equals("[")) {
                            check = true;
                        } else if (rightSide[i].equals("]")) {
                            check = false;
                        } else if (check) {
                            Cards cards = new Cards(rightSide[i].substring(0, 1), rightSide[i].substring(1, 2));
                            buildOwner.addLast(cards);
                        } else if (!check) {
                            buildOwnerName = rightSide[i];
                        }
                    }
                }

                // Last Capture
                if (lineNumber == 16) {
                    lastCapture = line.split(": ")[1];
                }

                // Getting cards on the deck
                if (lineNumber == 18) {
                    String[] deckString = getStringForCards(line);
                    for (int i = 0; i < deckString.length; i++) {
                        String suit = deckString[i].substring(0, 1);
                        String face = deckString[i].substring(1, 2);
                        Cards cards = new Cards(suit, face);
                        System.out.print(cards.toString() + " ");
                        mydeck.add(cards);
                    }
                }

                // Getting the name of the next player
                if (lineNumber == 20) {
                    nextPlayer = line.split(": ")[1];
                }
                lineNumber++;
            }

            //Setting the score of computer in player class
            player1.setScore(computerScore);

            // Setting the score of human in player class
            player2.setScore(humanScore);

            // Setting computer hand in player class
            player1.setPlayersHand(computerHand);
            // Setting computer pile in player class
            player1.setPlayersPile(computerPile);

            // Setting human hand in player class
            player2.setPlayersHand(humanHand);
            // Setting human pile in player class
            player2.setPlayersPile(humanPile);
            // Setting deck of cards
            deck.setDeck(mydeck);


            if (buildOwner.size() != 0){
                if (buildOwnerName.equals("Human")) {
                    player2.setBuildInBuildClass(buildOwner);
                } else if (buildOwnerName.equals("Computer")) {
                    player1.setBuildInBuildClass(buildOwner);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Clear Table
    public void clearTable(){
        table.clear();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    public void saveGame(String path) {
        String roundLine = "Round: " + roundNum;

    }

    // NOT WORKING IF THE LINE CONTAINS ZERO CARDS
    //------------------------------------------------------------------------------------------------------------------------------------------
    public String[] getStringForCards(String line) {
        String temp = line.split(":")[1];
        if (temp.equals(" ")) {
            // iF THERE IS NOTHING IN LINE, THEN IT RETURNS EMPTY STRING ARRAY
            String[] ret = {};
            return ret;
        } else {

            String separated = line.split(": ")[1];
            String cards[] = {};
            cards = separated.split(" ");
            for (int i = 0; i < cards.length; i++) {
                System.out.print(cards[i] + " ");
            }
            System.out.println();
            if (cards.length == 0) {
                System.out.println("Zero");
            }
            return cards;
        }

    }


    // Calculates the score of players after the round is completed using Player's Pile
    public void calculateScore() {

        // The player with the most cards in the pile gets 3 points. In the event of a tie, neither player gets points.
        int initialComp = player1.getScore();
        int initialHuman = player2.getScore();


        int computerPileSize = player1.getPileSize();
        int humanPileSize = player2.getPileSize();

        if (computerPileSize > humanPileSize) {
            player1.addScore(3);
        } else if (computerPileSize < humanPileSize) {
            player2.addScore(3);
        }

        //The player with the most spades gets 1 point. In the event of a tie, neither player gets points.
        int computerSpadesNum = player1.getSpadesNum();
        int humanSpadesNum = player2.getSpadesNum();

        if (computerSpadesNum > humanSpadesNum) {
            player1.addScore(1);
        } else if (computerSpadesNum < humanSpadesNum) {
            player2.addScore(1);
        }

        //The player with 10 of Diamonds gets 2 points.
        if (player1.hasTenOfDiamonds()) {
            player1.addScore(2);
        } else if (player2.hasTenOfDiamonds()) {
            player2.addScore(2);
        }

        //The player with 2 of Spades gets 1 point.
        if (player1.hasTwoOfSpades()) {
            player1.addScore(1);
        } else if (player2.hasTwoOfSpades()) {
            player2.addScore(1);
        }

        // Each player gets one point per Ace.

        int acePlayer1 = player1.getAceNum();
        int acePlayer2 = player2.getAceNum();

        player1.addScore(acePlayer1);
        player2.addScore(acePlayer2);
    }


    // Stores the round number
    private int roundNum;

    // Deque to store the cards on table
    private Deque<Deque<Cards>> table = new ArrayDeque<>();

    // deck object
    private DeckOfCards deck = new DeckOfCards();

    // Computer object
    private Computer player1 = new Computer();

    // Human object
    private Human player2 = new Human();

    // String variable to store the next player
    private String nextPlayer;

    // Stores the name of the last capture
    private String lastCapture;

    private Deque<Cards> build = new ArrayDeque<>();
}
