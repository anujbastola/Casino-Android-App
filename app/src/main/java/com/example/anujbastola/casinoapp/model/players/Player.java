package com.example.anujbastola.casinoapp.model.players;

import com.example.anujbastola.casinoapp.model.setup.Cards;

import java.util.ArrayDeque;
import java.util.Deque;

public class Player {

    // Holds the cards that are currently on the table
    protected Deque<Deque<Cards>> table;

    // Every time a move is made, this variable sets if the move is capture;
    protected boolean isLastCapture;

    private int score;

    private boolean isBuildOwner = false;

    private Deque<Cards> playersHand = new ArrayDeque<>();

    private Deque<Cards> playersPile = new ArrayDeque<>();

    private Deque<Deque<Cards>> subset;

    private Deque<Deque<Cards>> possibleBuilds;

    private Deque<Deque<Cards>> built;

    private Deque<Deque<Cards>> tempTable;

    private Deque<Deque<Cards>> matches;




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
    public void addCardTable(Cards fromPlayer){
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
        subset.clear();
        possibleBuilds.clear();
        built.clear();
        tempTable.clear();
        matches.clear();
    }

    // Set the score of the player, used in deserialization
    public void setScore(int num) {
        score = num;
    }

    // get score of the player
    final public int getScore()  {
        return score;
    }

    // Adds score in every calculation
    public void addScore(int num) {
        score = score + num;
    }

    // Gets card from deck every time the player's hand is empty and the card is added to the hand
    public void addCard(Cards hand) {
        System.out.println("Hand is " + hand.toString());
        playersHand.addLast(hand);
    }

    // Get card from the playersHand
    public Cards getCard() {
        return playersHand.pollFirst();
    }

    // Receives captured card and the card is added to player's pile
    public void addToPile(Cards temp) {
        playersPile.addLast(temp);
    }

    // Returns th size of pile. This function is used when calculating the score
    public int getPileSize() {
        return playersPile.size();
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
    public int getAceNum()  {
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

    public boolean hasTenOfDiamonds () {
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

    public void printDoubleDeque(Deque<Deque<Cards>> doubleDeque){
        for (Deque<Cards> insideDeque: doubleDeque){
            for ( Cards oneCard: insideDeque ){
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
        }
        else if (face.equals("J")) {
            value = 11;
        }
        else if (face.equals("Q")) {
            value = 12;
        }
        else if (face.equals("K")) {
            value = 13;
        }
        else if (face.equals("A")) {
            value = 1;
        }
        else {
            value = Integer.parseInt(face);
        }
        return value;
    }


    // Returns all the cards in player's hand
    public Deque<Cards> returnPlayerHand(){
        return playersHand;
    }

    // Returns all the cards in player's pile
    public Deque<Cards> returnPlayerPile(){
        return playersPile;
    }


}
