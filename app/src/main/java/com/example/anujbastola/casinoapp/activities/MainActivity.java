package com.example.anujbastola.casinoapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.View;
import android.os.Environment;

import com.example.anujbastola.casinoapp.R;
import com.example.anujbastola.casinoapp.model.setup.Cards;
import com.example.anujbastola.casinoapp.model.setup.Round;
import com.example.anujbastola.casinoapp.model.setup.Tournament;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int layout_width;

    private String handCardSelected;

    private Deque<String> tableCardSelected = new ArrayDeque<>();

    // Deque to store the cards on table

    private Round round = new Round();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String gameState = getIntent().getStringExtra("state");
        final String fileName = getIntent().getStringExtra("file");

        if (gameState.equals("load")) {
            System.out.println("User Clicked on Load Game");
            System.out.println("File Name: " + fileName);
            try {
                if (isExternalStorageReadable()) {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/savefiles/" + fileName;
                    System.out.println("Path is " + path);
                    round.loadGame(path);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (gameState.equals("new")) {
            System.out.println("User Clicked on New Game");
            round.setHands();
        }

        try{
            //System.out.println("Infront of populate");
            round.printDetails();
            populateHand();
            populateTable();

        } catch (Exception e){
            e.printStackTrace();
        }

//        for ( int i = 0; i<2; i++){
//            round.playGame();
//            displayComputerMoveInfo();
//        }
//
        round.playGame();
        displayComputerMoveInfo();

    }


    //checks if the ExternalStorage is Readable or not
    //returns boolean values
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    // Whenever I access card from player class, the data types is Cards
    // Displaying cards takes String data type
    public List<String> convertCardsToString(Deque<Cards> singleDeque) {

        List<String> temp = new ArrayList<>();
        return temp;
    }


    public void populateHand() {
        Deque<Cards> humanHand = round.getHumanHand();

        Deque<Cards> computerHand = round.getComputerHand();

        List<String> handCards = new ArrayList<>();
        handCards.clear();
        // Displaying computer cards
        for (Cards oneCompCard : computerHand) {
            handCards.add(oneCompCard.toString());
        }
        LinearLayout linearLayout = findViewById(R.id.computerCardBox);
        linearLayout.removeAllViews();
        displayCard(linearLayout, handCards);

        handCards.clear();
        // Displaying Human cards
        for (Cards oneHumanCard : humanHand) {
            handCards.add(oneHumanCard.toString());
        }

        LinearLayout linearLayout1 = findViewById(R.id.humanCardBox);
        linearLayout1.removeAllViews();
        displayCard(linearLayout1, handCards);
    }


    public void populateTable() {
        Deque<Deque<Cards>> table = round.getTable();

        List<String> tableCards = new ArrayList<>();
        tableCards.clear();

        for (Deque<Cards> insideTable : table) {
            for (Cards oneCard : insideTable) {
                tableCards.add(oneCard.toString());
            }
        }

        LinearLayout linearLayout = findViewById(R.id.tableCardBox);
        linearLayout.removeAllViews();

        displayTableCard(linearLayout, tableCards);
    }

    public void displayTableCard(LinearLayout linearLayout, List<String> tableCards) {

        for ( String tableCard: tableCards) {
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(180, 250);
            params.setMargins(15, 10, 15, 10);
            imageView.setLayoutParams(params);

            Context context = linearLayout.getContext();
            int id = context.getResources().getIdentifier(tableCard.toLowerCase(), "drawable", context.getPackageName());

            imageView.setImageResource(id);
            imageView.setTag(tableCard);

            imageView.setPadding(5, 5, 5, 5);
            imageView.setId(id);
            imageView.setClickable(true);

            imageView.setOnClickListener(this);

            linearLayout.addView(imageView);
        }
    }

    @Override
    public  void onClick(View view){

        Drawable highlight = getResources().getDrawable( R.drawable.onborder);
        view.setBackground(highlight);

        String tagName = view.getTag().toString();
        System.out.println("Tag Name is " + tagName);

        if (checkIfHumanCard(tagName)){
            System.out.println("Human Card Clicked");
            handCardSelected = tagName;
        }
        else {
            tableCardSelected.add(tagName);
        }

    }

    public boolean checkIfHumanCard(String cardName){
        Deque<Cards> humanCards = round.getHumanHand();
        for ( Cards oneCard: humanCards){
            if ( oneCard.toString().equals(cardName)){
                return true;
            }
        }
        return false;
    }

    public void trailHumanCard(View view){
        System.out.println("In Human Trail Card");
        round.trailHumanCard(handCardSelected);

        System.out.println("After Trail: Table: " + round.getTable());
        System.out.println("After Trail: Hand: " + round.getHumanHand());

        populateHand();
        populateTable();
    }

    // When human clicks on CAPTURE button, this function is loaded
    public void humanCaptureMove(View view){

        System.out.println("Table Cards Clicked: " + tableCardSelected);
        System.out.println("Hand Card Clicked: " + handCardSelected);

        round.doCaptureMoveForHuman(tableCardSelected, handCardSelected);

        populateHand();
        populateTable();
    }

    public void displayCard(LinearLayout linearLayout, List<String> handCards) {

        for (String onecard: handCards) {

            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(180, 220);
            params.setMargins(40, 10, 35, 10);
            imageView.setLayoutParams(params);

            // Getting the card image using the element in the string array
            Context context = linearLayout.getContext();
            int id = context.getResources().getIdentifier(onecard.toLowerCase(), "drawable", context.getPackageName());
            imageView.setImageResource(id);

            imageView.setTag(onecard);
            imageView.setPadding(5, 5, 5, 5);
            imageView.setId(id);
            imageView.setClickable(true);

            imageView.setOnClickListener(this);
            linearLayout.addView(imageView);
        }
    }

    // This function is called when the player clicks on Score button
    public void displayScoreBox(View view) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Score");
        alertDialog.setIcon(R.drawable.score);
        alertDialog.setMessage("Computer: " + round.getComputerScore() + "\n\n" + "Human: " + round.getHumanScore() + "\n\n" + "Round Number: " + round.getRoundNum());
        alertDialog.show();
    }


    public void displayInfoBox(View view) {


        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.activity_infos);
        dialog.setTitle("Info:");
        dialog.show();

        Deque<Cards> computerPile = new ArrayDeque<>();
        computerPile = round.getComputerPile();
        List<String> compPile = new ArrayList<>();

        for (Cards compCard : computerPile) {
            compPile.add(compCard.toString());
        }

        LinearLayout linearLayout = dialog.findViewById(R.id.computerPileDisplay);
        displayInfoCard(linearLayout, compPile);


        // ---------------------------------------------------------------------------------------------------------
        Deque<Cards> humanPile = new ArrayDeque<>();
        List<String> humPile = new ArrayList<>();
        humanPile = round.getHumanPile();
        for (Cards humanCard : humanPile) {
            humPile.add(humanCard.toString());
        }

        LinearLayout linearLayout1 = dialog.findViewById(R.id.humanPileDisplay);
        displayInfoCard(linearLayout1, humPile);


        // Stores the string value of deck
        List<String> deck = new ArrayList<>();

        Deque<Cards> mydeck = new ArrayDeque<>();
        mydeck = round.getDeck();

        for (Cards deckCard : mydeck) {
            deck.add(deckCard.toString());
        }

        LinearLayout linearLayout2 = dialog.findViewById(R.id.deckDisplay);
        displayInfoCard(linearLayout2, deck);
    }

    // displayInfoBox function calls this function with pile or deck to dispay in the info dialog
    public void displayInfoCard(LinearLayout linearLayout, List<String> handCards) {

        for (String card : handCards) {
            ImageView imageView = new ImageView(this);
            Context context = linearLayout.getContext();
            int id = context.getResources().getIdentifier(card.toLowerCase(), "drawable", context.getPackageName());
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), id, null);
            imageView.setBackground(drawable);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(180, 250);
            params.setMargins(15, 10, 15, 10);
            imageView.setLayoutParams(params);

            imageView.setId(id);
            imageView.setClickable(true);
            linearLayout.addView(imageView);
        }
    }


    //    This function is called when HUMAN player wants to  get help from computer
// This function is called when the player clicks on Score button
    public void displayGetHelp(View view) {
        String move = "You can capture HX H1 with your DX My name is Anuj Bastola. I go to Ramapo College of New Jersey";
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.help);
        alertDialog.setTitle("Here is the best move!");
        alertDialog.setMessage(move);
        alertDialog.show();
    }

    public void displayComputerMoveInfo(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.move);
        alertDialog.setTitle("Computer Move");
        alertDialog.setMessage(round.getComputerMoveInfo());
        alertDialog.show();

        populateHand();
        populateTable();
    }

}

