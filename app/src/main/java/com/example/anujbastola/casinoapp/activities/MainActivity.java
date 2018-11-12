package com.example.anujbastola.casinoapp.activities;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View;

import com.example.anujbastola.casinoapp.R;
import com.example.anujbastola.casinoapp.model.setup.Cards;
import com.example.anujbastola.casinoapp.model.setup.Round;
import com.example.anujbastola.casinoapp.model.setup.Tournament;

import java.util.ArrayDeque;
import java.util.Deque;

public class MainActivity extends AppCompatActivity {

    private int layout_width;

    // Deque to store the cards on table

    private Round round = new Round();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        round.setHands();

        populateHand();
        populateTable();

    }


    public void populateHand() {

//        System.out.println("Human Hands in populate hand function");
//        Deque<Cards> humanHand;
//        humanHand = round.getHumanHand();
//
//        for ( Cards oneHumanCard: humanHand){
//            System.out.print(oneHumanCard.toString() + "  ");
//        }
//        System.out.println();
//        System.out.println("Computer Hands in populate hand function");
//
//        Deque<Cards> computerHand;
//        computerHand = round.getComputerHand();
//
//        for ( Cards oneHumanCard: computerHand){
//            System.out.print(oneHumanCard.toString() + "  ");
//        }
//        System.out.println();

        Deque<Cards> humanHand = new ArrayDeque<>();
        humanHand = round.getHumanHand();
        Deque<Cards> computerHand = new ArrayDeque<>();
        computerHand = round.getComputerHand();

        String[] handCards = new String[4];


        // Displaying computer cards
        int count = 0;
        for (Cards oneHumanCard : computerHand) {
            handCards[count] = oneHumanCard.toString();
            count++;
        }

        LinearLayout linearLayout = findViewById(R.id.computerCardBox);
        displayCard(linearLayout, handCards);

//        Displaying human Cards
        count = 0;
        for (Cards oneHumanCard : humanHand) {
            handCards[count] = oneHumanCard.toString();
            count++;
        }

        LinearLayout linearLayout1 = findViewById(R.id.humanCardBox);
        displayCard(linearLayout1, handCards);
    }


    public void populateTable() {
        Deque<Deque<Cards>> table = new ArrayDeque<>();
        table = round.getTable();

        String[] tableCards = {"HA", "S9", "CK", "HQ", "CA"};
        LinearLayout linearLayout = findViewById(R.id.tableCardBox);
        displayCard(linearLayout, tableCards);
    }

    public void displayCard(LinearLayout linearLayout, String[] handCards) {
//        int device_width =  Resources.getSystem().getDisplayMetrics().widthPixels;
//        int eachImageWidth = (device_width / handCards.length);

        for (int i = 0; i < handCards.length; i++) {
            Button li = new Button(this);
            Context context = linearLayout.getContext();
            int id = context.getResources().getIdentifier(handCards[i].toLowerCase(), "drawable", context.getPackageName());
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), id, null);

            li.setBackground(drawable);

            li.setLayoutParams(new LinearLayout.LayoutParams(200, 300));
            li.setId(i);
            li.setClickable(true);
            linearLayout.addView(li);
        }
    }

    // This function is called when the player clicks on Score button
    public void displayScoreBox(View view) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Score");
        int compScore = 0;
        int humanScore = 0;
        alertDialog.setMessage("Computer: " + compScore + "     " + "Human: " + humanScore);
        alertDialog.show();
    }

    public void displayInfoBox(View view) {
        setContentView(R.layout.activity_infos);

        String[] piles = {"HA", "D5", "CK", "H2", "H3", "H5", "D6", "D9"};
        LinearLayout linearLayout = findViewById(R.id.computerPileDisplay);
        displayInfoCard(linearLayout, piles);

        LinearLayout linearLayout1 = findViewById(R.id.humanPileDisplay);
        displayInfoCard(linearLayout1, piles);


        // Stores the string value of deck
        String[] deck = new String[52];
        Deque<Cards> mydeck = new ArrayDeque<>();
        mydeck = round.getDeck();

        int count = 0;
        for (Cards deckCard : mydeck) {
            deck[count] = deckCard.toString();
            count++;
        }

        LinearLayout linearLayout2 = findViewById(R.id.deckDisplay);
        displayInfoCard(linearLayout2, deck);
    }

    public void displayInfoCard(LinearLayout linearLayout, String[] handCards) {

        for (int i = 0; i < handCards.length; i++) {

            System.out.println("Adding --->");
            Button li = new Button(this);
            Context context = linearLayout.getContext();
            int id = context.getResources().getIdentifier(handCards[i].toLowerCase(), "drawable", context.getPackageName());
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), id, null);
            li.setBackground(drawable);

            li.setLayoutParams(new LinearLayout.LayoutParams(265, 420));
            li.setId(i);
            li.setClickable(true);
            linearLayout.addView(li);
        }
    }


    //    This function is called when HUMAN player wants to  get help from computer
// This function is called when the player clicks on Score button
    public void displayGetHelp(View view) {
        String move = "You can capture HX H1 with your DX My name is Anuj Bastola. I go to Ramapo College of New Jersey";
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Here is a best move for you!");
        alertDialog.setMessage(move);
        alertDialog.show();
    }



}

