package com.example.anujbastola.casinoapp.model.setup;

import com.example.anujbastola.casinoapp.model.players.Computer;
import com.example.anujbastola.casinoapp.model.players.Human;

import java.util.ArrayDeque;
import java.util.Deque;

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
    private  String lastCapture;


//    ------------------- Public Methods -------------------------

    // Default constructor
    public  Round(){}

    // This constructor is called when user wants to load from a file
    public Round(int round, int compScore, int huScore){
        roundNum = round;
        // Sets score of computer
        player1.setScore(compScore);
        // Sets score of human
        player2.setScore(huScore);
    }

    // This constructor is called when the user wants to start a new game
    public  Round(String next, int roundNumber, int compScore, int huScore) {
        roundNum = roundNumber;
        setHands();
        nextPlayer = next;
        player1.setScore(compScore);
        player2.setScore(huScore);
    }


    // Sets the name of the player who will play next
    public  void setNextPlayer(String next){
        nextPlayer = next;
    }

    public String getNextPlayer(){
        return nextPlayer;
    }

    // Deals 4 cards to Table, player 1 and player2
    public void setHands() {

        for ( int i = 0; i<4; i++){
            Deque<Cards> temp = new ArrayDeque<>();
            temp.add(deck.returnFrontCard());
            table.add(temp);
        }

        //Adds four cards to Computer's hand
        for (int i = 0; i < 4; i++) {
            player1.addCard(deck.returnFrontCard());
        }

        //Adds four cards to Human's hand
        for (int i = 0; i < 4; i++) {
            player2.addCard(deck.returnFrontCard());
        }
    }

    public void setTable(){
        System.out.println("Adding Cards to Table");
        for ( int i = 0; i<5; i++){
            Deque<Cards> temp = new ArrayDeque<>();
            temp.add(deck.returnFrontCard());
            table.add(temp);
            System.out.println(temp.peek());
        }
    }



    public  void printTable(){
        System.out.println("Printing Cards of Table");
        for ( Deque<Cards> insideTable : table){
            for ( Cards oneCard: insideTable){
                System.out.print(oneCard.toString());
            }
            System.out.println();
        }
    }

    public void addCardsPlayer(){
        System.out.println("I am inside Add");
        Cards temp;
        temp = deck.returnFrontCard();
        System.out.println("Printing the added card: " + temp);
        player1.addCard(temp);
    }

    public Deque<Deque<Cards>> getTable() {
        return table;
    }

    // Getter for computer hand
    public Deque<Cards> getComputerHand(){
        return player1.returnPlayerHand();
    }

    // Getter for human player hand
    public Deque<Cards> getHumanHand(){
        return player2.returnPlayerHand();
    }

    // Getter for computer pile
    public Deque<Cards> getComputerPile(){
        return player1.returnPlayerPile();
    }

    // Getter for human pile
    public Deque<Cards> getHumanPile(){
        return player2.returnPlayerPile();
    }

    // Getter for table cards
    public Deque<Deque<Cards>> returnTable(){
        return table;
    }

    // Getter for human pile
    public Deque<Cards> getDeck(){
        return deck.getDeck();
    }

    public static void main(String[] args){
//        Round round = new Round();
//        System.out.println("Inside Round Main");
//        for ( int i = 0; i<4; i++){
//            round.addCardsPlayer();
//        }
//
//        round.setTable();
//        round.printTable();
    }

}
