package com.example.anujbastola.casinoapp.model.setup;

public class Cards {

    public void setFace(String face) {
        this.face = face;
    }

    public void setSuit(String suit) {
        this.suit = suit;
    }

    // This variable holds the face name of a card
    private String face;

    // This variable holds the suit name of a card
    private  String suit;

    // Default constructor for Cards class
    public Cards(){
        this.face = "0";
        this.suit = "0";
    }

    public Cards(String suit, String face){
        this.face = face;
        this.suit = suit;
    }

    // Returns the string value of a card object
    public String toString(){
        return suit + face;
    }

    // Returns the face of a card
    public String getFace(){
        return face;
    }

    // Getter for suit of a card
    public  String getSuit(){
        return suit;
    }

    public static void main(String args[]){
        Cards cards = new Cards();

    }
}
