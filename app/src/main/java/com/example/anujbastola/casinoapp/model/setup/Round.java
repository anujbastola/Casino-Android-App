package com.example.anujbastola.casinoapp.model.setup;

import com.example.anujbastola.casinoapp.model.players.Computer;
import com.example.anujbastola.casinoapp.model.players.Human;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

import static java.lang.Integer.parseInt;

public class Round {

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

    private Cards globalCard = new Cards();


//    ------------------- Public Methods -------------------------

    // Default constructor
    public Round() {
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // This constructor is called when user wants to load from a file
    public Round(int round, int compScore, int huScore) {
        roundNum = round;
        // Sets score of computer
        player1.setScore(compScore);
        // Sets score of human
        player2.setScore(huScore);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // This constructor is called when the user wants to start a new game
    public Round(String next, int roundNumber, int compScore, int huScore) {
        roundNum = roundNumber;
        setHands();
        nextPlayer = next;
        player1.setScore(compScore);
        player2.setScore(huScore);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Sets the name of the player who will play next
    public void setNextPlayer(String next) {
        nextPlayer = next;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    public String getNextPlayer() {
        return nextPlayer;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    // Deals 4 cards to Table, player 1 and player2
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
        printTable();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    public void setTable() {
        System.out.println("Adding Cards to Table");
        for (int i = 0; i < 5; i++) {
            Deque<Cards> temp = new ArrayDeque<>();
            temp.add(deck.returnFrontCard());
            table.add(temp);
            System.out.println(temp.peek());
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    public void printTable() {
        System.out.println("Printing Cards of Table");
        for (Deque<Cards> insideTable : table) {
            for (Cards oneCard : insideTable) {
                System.out.print(oneCard.toString());
            }
            System.out.println();
        }
        System.out.println("Ending Cards of Table");
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    public void addCardsPlayer() {
        System.out.println("I am inside Add");
        ;
        for (int i = 0; i < 4; i++) {
            player1.addCard(deck.returnFrontCard());
        }
        for (int i = 0; i < 4; i++) {
            player2.addCard(deck.returnFrontCard());
        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
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
    public void printTableInPlayer() {
        player1.printTable(table);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    public void setTableInPlayer() {
        player1.setTable(table);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    public Deque<Deque<Cards>> getSubsetsFromPlayer() {
        return player1.getAllSubsets();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------

    public void playGame() {
        System.out.println("In Play Game Round class");
        player1.play(table);

        Deque<Deque<Cards>> tableGet = player1.getTable();
        table = tableGet;
        System.out.println("Original Table in round class: " + table);
        System.out.println("Table From PlayerClass " + tableGet);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    public String getComputerMoveInfo() {
        return player1.getComputerMoveInfo();
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    public void trailHumanCard(String humanCard) {
        Cards toAddInTable = new Cards(humanCard.substring(0, 1), humanCard.substring(1, 2));
        System.out.println("Card object for human card selected: " + toAddInTable.toString());

        Deque<Cards> toTrailInTable = new ArrayDeque<>();

        toTrailInTable.add(toAddInTable);

        table.add(toTrailInTable);

        player2.deleteHandCard(toAddInTable);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    public void doCaptureMoveForHuman(Deque<String> selectedTableCards, String selectedHandCard) {
        player2.setTable(table);
        Cards humanCard = new Cards(selectedHandCard.substring(0, 1), selectedHandCard.substring(1, 2));
        player2.addToPile(humanCard);
        player2.deleteHandCard(humanCard);

        for (String tableCard : selectedTableCards) {
            Cards temp = new Cards(tableCard.substring(0, 1), tableCard.substring(1, 2));
            Deque<Cards> deq = new ArrayDeque<>();
            deq.add(temp);
            System.out.println("Deq to delete: " + deq);
            player2.deleteCardFromTable(deq);
            player2.addToPile(temp);
        }
        System.out.println("Table before capture return ------ " + table);
        table = player2.returnTableToRoundClass();
        System.out.println("Table After capture: " + table);
        System.out.println("Hand after capture: " + player2.returnPlayerHand());
    }

//------------------------------------------------------------------------------------------------------------------------------------------

    public void doSetBuildForHuman(Deque<String> selectedTableCards, String selectedHandCard) {

        player2.setTable(table);
        Cards humanCard = new Cards(selectedHandCard.substring(0, 1), selectedHandCard.substring(1, 2));
        player2.deleteHandCard(humanCard);
        Deque<Cards> build = new ArrayDeque<>();
        for (String oneSelectedCard : selectedTableCards) {
            Cards temp = new Cards(oneSelectedCard.substring(0, 1), oneSelectedCard.substring(1, 2));
            build.add(temp);
        }
        player2.deleteCardFromTable(build);
        System.out.println("Table Is before adding build and after deleting table cards: " + table);
        build.add(humanCard);
        table.addFirst(build);
        System.out.println("Table after adding build: " + table);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    public void doCaptureBuildForHuman(Deque<String> selectedTableCards, String selectedHandCard) {

        player2.setTable(table);
        System.out.println("Table in do capture build in round class " + selectedTableCards);
        Cards humanCard = new Cards(selectedHandCard.substring(0, 1), selectedHandCard.substring(1, 2));
        Deque<Cards> build = new ArrayDeque<>();
        int sum = 0;
        for (String oneSelectedCard : selectedTableCards) {
            Cards temp = new Cards(oneSelectedCard.substring(0, 1), oneSelectedCard.substring(1, 2));
            System.out.println("Card number(Built): " + player2.getCardNumber(temp));
            System.out.println("Sum(loop): " + sum);
            sum = player2.getCardNumber(temp) + sum;
            build.add(temp);
        }
        System.out.println("Sum of build: " + sum);

        int handNumber = player2.getCardNumber(humanCard);
        System.out.println("Card number of hand card: " + handNumber);
        System.out.println("Build: " + build);
        if (sum == handNumber) {
            deleteBuiltFromTable(build);
            player2.deleteHandCard(humanCard);

            for (Cards a : build) {
                player2.addToPile(a);
            }
            player2.addToPile(humanCard);
        }
        System.out.println("Table After capturing build: " + table);
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    public void deleteBuiltFromTable(Deque<Cards> toDelete) {
        System.out.println("Table before deleting: " + toDelete);
        Deque<Deque<Cards>> tempTable = new ArrayDeque<>();
        for (Deque<Cards> insideTable : table) {
            boolean check = false;
            for ( Cards inDelete : toDelete){
                if ( insideTable.peekFirst().toString().equals(inDelete.toString())){
                    // if this is the built
                    check = true;
                }
            }
            if ( check){
                // do not add anything
            }
            else {
                tempTable.add(insideTable);
            }
//            if (insideTable.peekFirst().toString().equals(toDelete.peekFirst().toString())) {
//                System.out.println("This will not be added to temptable " + insideTable);
//            } else {
//                tempTable.add(insideTable);
//            }
        }
        System.out.println("Temp Table after deleting built is " + tempTable);
        table = tempTable;
    }

    //------------------------------------------------------------------------------------------------------------------------------------------
    public String getHelp() {
        player2.setTable(table);
        String help = player2.askForHelp(table);

        return help;
    }


    //------------------------------------------------------------------------------------------------------------------------------------------
    public void loadGame(String filepath) {

        int roundNumber = 99, computerScore = 99, humanScore = 99;
        String buildOwnerName;
        String nextPlayer;
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

                // Checks for the line with round Number and stores the number in roundNum private varibable
                if (lineNumber == 1) {
                    roundNumber = Integer.parseInt(line.split(": ")[1]);
                    roundNum = roundNumber;
                    // System.out.println("Round NUmber is " + roundNumber);
                }
                if (lineNumber == 4) {
                    computerScore = Integer.parseInt(line.split(": ")[1]);
                    //System.out.println("Computer Score: " + computerScore);
                }
                // Cards from computer's hand
                if (lineNumber == 5) {
                    String[] compHandString = getStringForCards(line);
                    System.out.println("Computer Hand From File");
                    for (int i = 0; i < compHandString.length; i++) {
                        String suit = compHandString[i].substring(0, 1);
                        String face = compHandString[i].substring(1, 2);
                        Cards cards = new Cards(suit, face);
                        System.out.print(cards.toString() + " ");
                        computerHand.add(cards);
                    }
                }
                System.out.println();
                // Cards from computer's pile
                if (lineNumber == 6) {
                    String[] compPileString = getStringForCards(line);
                    System.out.println("Computer Pile From File");
                    for (int i = 0; i < compPileString.length; i++) {
                        String suit = compPileString[i].substring(0, 1);
                        String face = compPileString[i].substring(1, 2);
                        Cards cards = new Cards(suit, face);
                        System.out.print(cards.toString() + " ");
                        computerPile.add(cards);
                    }

                }
                System.out.println();

                // Human Score
                if (lineNumber == 8) {
                    humanScore = Integer.parseInt(line.split(": ")[1]);
                    //System.out.println("Human Score: " + humanScore);
                }
                // Cards from human's hand
                if (lineNumber == 9) {
                    String[] humanHandString = getStringForCards(line);
                    System.out.println("Human Hand From File");
                    for (int i = 0; i < humanHandString.length; i++) {
                        String suit = humanHandString[i].substring(0, 1);
                        String face = humanHandString[i].substring(1, 2);
                        Cards cards = new Cards(suit, face);
                        System.out.print(cards.toString() + " ");
                        humanHand.add(cards);
                    }
                }
                System.out.println();
                // Cards from human's pile
                if (lineNumber == 10) {
                    String[] humanPileString = getStringForCards(line);
                    System.out.println("Human Pile From File");
                    System.out.println(humanPileString.length);
                    for (int i = 0; i < humanPileString.length; i++) {
                        String suit = humanPileString[i].substring(0, 1);
                        String face = humanPileString[i].substring(1, 2);
                        Cards cards = new Cards(suit, face);
                        System.out.print(cards.toString() + " ");
                        humanPile.add(cards);
                    }
                }
                System.out.println();

                // Getting table cards
                if (lineNumber == 12) {

                    System.out.println("Table Line is " + line);
                    String[] filetable = getStringForCards(line);

                    System.out.println("Table Cards From File" + filetable);
                    System.out.println(filetable.length);

                    for (int i = 0; i < filetable.length; i++) {
                        String suit = filetable[i].substring(0, 1);
                        String face = filetable[i].substring(1, 2);
                        Cards cards = new Cards(suit, face);
                        System.out.print(cards.toString() + " ");
                        Deque<Cards> temp = new ArrayDeque<>();
                        temp.add(cards);
                        mytable.add(temp);
                    }

                }

                // Getting build Owner
                if (lineNumber == 14) {

                }

                // Last Capture
                if (lineNumber == 16) {

                }

                // Getting cards on the deck
                if (lineNumber == 18) {
                    String[] deckString = getStringForCards(line);
                    System.out.println("Deck From File");
                    for (int i = 0; i < deckString.length; i++) {
                        String suit = deckString[i].substring(0, 1);
                        String face = deckString[i].substring(1, 2);
                        Cards cards = new Cards(suit, face);
                        System.out.print(cards.toString() + " ");
                        mydeck.add(cards);
                    }
                    System.out.println("End deck From File");
                }

                // Getting the name of the next player
                if (lineNumber == 20) {

                }
                lineNumber++;
            }
            System.out.println("Computer: " + computerScore);
            System.out.println("Human: " + humanScore);

            System.out.println("Printing Computer Hand");
            for (Cards a : computerHand) {
                System.out.println("Handy: " + a.toString());
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

            table = mytable;
            System.out.println("At last the table is " + table);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // NOT WORKING IF THE LINE CONTAINS ZERO CARDS

    public String[] getStringForCards(String line) {
        System.out.println("Line-->" + line + "?");
        String temp = line.split(":")[1];
        System.out.println("Line is:" + line + "!");
        if (temp.equals(" ")) {
            // iF THERE IS NOTHING IN LINE, THEN IT RETURNS EMPTY STRING ARRAY
            System.out.println("Line is empty");
            String[] ret = {};
            return ret;
        } else {
            System.out.println("Line is not empty");

            String separated = line.split(": ")[1];
            String cards[] = {};
            cards = separated.split(" ");
            System.out.println("Cards in getString for cards: ");
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

    public void printDetails() {
        System.out.println("----- Printing All Details -----");
        System.out.println("Computer Card:");
        for (Cards card : player1.returnPlayerHand()) {
            System.out.print(card.toString() + " ");
        }
        System.out.println();

        System.out.println("Human Card:");
        for (Cards card : player2.returnPlayerHand()) {
            System.out.print(card.toString() + " ");
        }
        System.out.println();

        System.out.println("Computer Pile:");
        for (Cards card : player1.returnPlayerPile()) {
            System.out.print(card.toString() + " ");
        }
        System.out.println();

        System.out.println("Human Pile:");
        for (Cards card : player2.returnPlayerPile()) {
            System.out.print(card.toString() + " ");
        }
        System.out.println();

        System.out.println("Deck Card");
        for (Cards card : deck.getDeck()) {
            System.out.print(card.toString() + " ");
        }
        System.out.println();

    }

    // Calculates the score of players after the round is completed using Player's Pile
    public void calculateScore() {

        // The player with the most cards in the pile gets 3 points. In the event of a tie, neither player gets points.
        int initialComp = player1.getScore();
        int initialHuman = player2.getScore();

        System.out.println("Computer Score Initial: " + initialComp);
        System.out.println("Human Score Initial: " + initialHuman);

        int computerPileSize = player1.getPileSize();
        int humanPileSize = player2.getPileSize();
        System.out.println("Computer Pile Size: " + computerPileSize);
        System.out.println("Human Pile Size: " + humanPileSize);

        if (computerPileSize > humanPileSize) {
            player1.addScore(3);
        } else if (computerPileSize < humanPileSize) {
            player2.addScore(3);
        }

        //The player with the most spades gets 1 point. In the event of a tie, neither player gets points.
        int computerSpadesNum = player1.getSpadesNum();
        int humanSpadesNum = player2.getSpadesNum();
        System.out.println("Computer Spades Number: " + computerSpadesNum);
        System.out.println("Human Spades Number: " + humanSpadesNum);

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

        System.out.println("Computer Score in that round is " + (player1.getScore() - initialComp));
        System.out.println("Human Score in that round is " + (player2.getScore() - initialHuman));
    }


}
