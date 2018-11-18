package com.example.anujbastola.casinoapp.model.setup;



import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;

public class DeckOfCards{

    private Deque<Cards> deck = new ArrayDeque<>();

    public DeckOfCards(){
        System.out.println("Enter 1 to randomly generate deck of cards, 2 to get deck from file");
        generateRandomly();
    }


    /* *********************************************************************
    Function Name: generateRandomly
    Purpose: to generate the deck of cards randomly
    Parameters:
                none
    Return Value: returns none
    Local Variables:
                faces[], string array to store face of cards
                suit[], string array to store suit of cards
    Algorithm:
                1) Iterate over faces and suits
                2) Declare a card object using new Cards();
                3) add the card object to deck of cards
    Assistance Received: none
    ********************************************************************* */

    public void generateRandomly(){
        // Stores all the faces available in a deck of cards
        // A for Ace, X for 10, J for Jack, Q for Queen and K for King
        String faces[] = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "X", "J", "Q", "K"};

        // Stores all four suits
        String suits[] = { "H", "C", "S", "D"};

        // All cards are stored in this ArrayList to shuffle by using Collection.shuffle function

        ArrayList<String> toShuffleDeck = new ArrayList<>();
        // Goes through all the faces and int and stores all unique cards in a deck
        for ( int i =0; i<13; i++){
            for ( int j =0; j<4; j++){
                Cards temp = new Cards(suits[j], faces[i]);
                toShuffleDeck.add(temp.toString());
                deck.add(temp);
            }
        }
        Collections.shuffle(toShuffleDeck);
        deck.clear();
        for ( String card: toShuffleDeck){
            Cards temp = new Cards(card.substring(0,1), card.substring(1,2));
            deck.add(temp);
        }

        System.out.println("Shuffled Decks: " + deck);

    }

    /* *********************************************************************
    Function Name: printDeck
    Purpose: To print the deck
    Parameters:
                none
    Local Variables:
                none
    Algorithm:
                1) Loop through deck deque
                2) print the string value of each card
    Assistance Received: none
    ********************************************************************* */

    public void printDeck(){
        System.out.println("Deck:");
        for ( Cards oneCard : deck){
            System.out.print("Card:: " + oneCard.toString() + " ");
        }
        System.out.println();
    }

    /* *********************************************************************
    Function Name: returnFrontCard
    Purpose: To get the front card of deck deque, used to deal cards to table and player's hand
    Parameters:
                none
    Return Value: returns the front card of deck deque
    Local Variables:
                temp, stores front card
    Algorithm:
                1) get front card from deck deque
                2) Delete front card from deck deque
                3) return front card
    Assistance Received: none
    ********************************************************************* */
    public Cards returnFrontCard(){
        Cards temp;
        temp = deck.pollFirst();
        return temp;
    }

    /* *********************************************************************
    Function Name: dealCards
    Purpose: To deal four cards to table
    Parameters:
                table, 2 dimensional array passed by reference, stors the cards on table

    Return Value: none
    Local Variables:
                topushinTable, deque that stores card received from deck
    Algorithm:
                1) loop four times to get four cards from deck
                2) get front card from deck deque
                3) delete front card from deck
                4) push front card to topushinTable
                5) push topushinTable to table
    Assistance Received: none
    ********************************************************************* */


    // Getter for deque of deck
    Deque<Cards> getDeck(){
        return  deck;
    }

    // Whenever a game is loaded from a file, this function is called to set the deck
    void setDeck(Deque<Cards> mydeck){
        deck = mydeck;
    }



    public static void main(String[] args){
        String line = "Hand: S7 D5 HQ";
        String sepated = line.split(": ")[1];
        String cards[] = {};
        cards = sepated.split(" ");
        for ( int i = 0; i<cards.length; i++){
            System.out.println("Card: " + cards[i]);
            String suit = cards[i].substring(0,1);
            String face = cards[i].substring(1,2);
            System.out.println("Face is " + face);
            System.out.println("Suit is " + suit);
        }

    }

}
