package com.example.anujbastola.casinoapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
import com.example.anujbastola.casinoapp.model.players.Build;
import com.example.anujbastola.casinoapp.model.setup.Cards;
import com.example.anujbastola.casinoapp.model.setup.Round;
import com.example.anujbastola.casinoapp.model.setup.Tournament;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Vector;

import static java.lang.System.exit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int layout_width;

    private String handCardSelected;
    private int handCardId = 0;

    private  String nextPlayer;
    private Deque<String> tableCardSelected = new ArrayDeque<>();

    private Vector<Integer> onClickedBuildId = new Vector<>();
    // Deque to store the cards on table

    private Round round = new Round();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String gameState = getIntent().getStringExtra("state");
        final String fileName = getIntent().getStringExtra("file");


        nextPlayer = "Human";
        if ( nextPlayer.equals("Human")){
            disableComputerMove("Disable");
        }
        else {
            disableHumanMove("Disable");
        }

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
        } else {
            System.out.println("User Clicked on New Game");
            round.setHands();
        }

        try {
            //System.out.println("Infront of populate");
            round.printDetails();
            populateHand();
            populateTable();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Disable Move Buttons(Capture, set build, capture builds, trail etc)
    public void disableHumanMove(String state){

        Button btn = findViewById(R.id.capture);
        Button btn1 = findViewById(R.id.setBuild);
        Button btn2 = findViewById(R.id.trail);
        Button btn3 = findViewById(R.id.extendBuild);
        Button btn4 = findViewById(R.id.captureBuild);

        if ( state.equals("Disable")){
            btn.setEnabled(false);
            btn1.setEnabled(false);
            btn2.setEnabled(false);
            btn3.setEnabled(false);
            btn4.setEnabled(false);
        }
        else if(state.equals("Enable")){
            btn.setEnabled(true);
            btn1.setEnabled(true);
            btn2.setEnabled(true);
            btn3.setEnabled(true);
            btn4.setEnabled(true);
        }
    }

    // Disable DO MOVE button
    public void disableComputerMove(String state){

        Button btn = findViewById(R.id.compMove);

        if ( state.equals("Disable")){
            btn.setEnabled(false);
        }
        else if ( state.equals("Enable")){
            btn.setEnabled(true);
        }
    }

    //checks if the ExternalStorage is Readable or not
    //returns boolean values
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
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
        displayCard(linearLayout, handCards , "Computer");

        handCards.clear();
        // Displaying Human cards
        for (Cards oneHumanCard : humanHand) {
            handCards.add(oneHumanCard.toString());
        }

        LinearLayout linearLayout1 = findViewById(R.id.humanCardBox);
        linearLayout1.removeAllViews();
        displayCard(linearLayout1, handCards, "Human");
    }


    public void populateTable() {
        Deque<Deque<Cards>> table = round.getTable();

        System.out.println("Table in populate Table: " + table);

        displayTableWithBuild(table);

    }

    public void displayTableCard(LinearLayout linearLayout, List<String> tableCards) {

        for (String tableCard : tableCards) {
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
    public void onClick(View view) {

        Drawable highlight = getResources().getDrawable(R.drawable.selected);
        view.setBackground(highlight);

        String tagName = view.getTag().toString();
       // int count = view.getChild
        if (!checkIfHumanCard(tagName)){

                System.out.println("Table card clicked is " + tagName);
                tableCardSelected.addFirst(tagName);
        }
        else {
            if(checkForDoubleClick(view.getId(), "card")){
                ImageView imageView = findViewById(view.getId());

                imageView.setBackgroundResource(android.R.color.transparent);
                System.out.println("DOUBLE CLICKED");

            }
            handCardSelected = view.getTag().toString();
            System.out.println("Hand Card Clicked is " + handCardSelected);
            System.out.println("On CLICKEDDDD: " + view.getTag().toString());
        }



//        String tagName = view.getTag().toString();
//        System.out.println("Tag Name is " + tagName);
//
//        if (checkIfHumanCard(tagName)) {
//            System.out.println("Human Card Clicked");
//            handCardSelected = tagName;
//        } else {
//            tableCardSelected.add(tagName);
//        }

    }

    // checks if the card is double clicked
    public boolean checkForDoubleClick(int buildId, String type){

        if ( type.equals("build")){
            if (onClickedBuildId.size() > 0 ){
                for ( Integer id: onClickedBuildId){
                    if (id.equals(buildId)){
                        return true;
                    }
                }
            }
            return false;
        }
        else if ( type.equals("card")){
            if ( handCardId == buildId){
                System.out.println("Double ClickedDDDD");
                handCardId = 0;
                return  true;
            }
            else{
                System.out.println("New Card Clicked:");
                handCardId = buildId;
            }

        }
        return false;
    }


    public boolean checkIfHumanCard(String cardName) {
        Deque<Cards> humanCards = round.getHumanHand();
        for (Cards oneCard : humanCards) {
            if (oneCard.toString().equals(cardName)) {
                return true;
            }
        }
        return false;
    }

    public void trailHumanCard(View view) {
        System.out.println("In Human Trail Card");
        round.trailHumanCard(handCardSelected);

        System.out.println("After Trail: Table: " + round.getTable());
        System.out.println("After Trail: Hand: " + round.getHumanHand());

        nextPlayer = "Computer";
        disableComputerMove("Enable");
        disableHumanMove("Disable");
        populateHand();
        populateTable();
    }

    // When human clicks on CAPTURE button, this function is loaded
    public void humanCaptureMove(View view) {

        System.out.println("Table Cards Clicked: " + tableCardSelected);
        System.out.println("Hand Card Clicked: " + handCardSelected);

        round.doCaptureMoveForHuman(tableCardSelected, handCardSelected);
        tableCardSelected.clear();
        disableComputerMove("Enable");
        disableHumanMove("Disable");
        populateHand();
        populateTable();
    }

    public void setBuildHuman(View view){

        System.out.println("Human clicked on set build button");
        System.out.println();
        System.out.println("Hand Card Clicked: " + handCardSelected);
        System.out.println("Table Cards Clicked: " + tableCardSelected);

        round.doSetBuildForHuman(tableCardSelected, handCardSelected);
        tableCardSelected.clear();
        disableComputerMove("Enable");
        disableHumanMove("Disable");
        populateHand();
        populateTable();
    }

    public void doCaptureBuild(View view){
        System.out.println("Human clicked on capture build");
        System.out.println("Hand Card Clicked: " + handCardSelected);
        System.out.println("Table Cards Clicked: " + tableCardSelected);

        round.doCaptureBuildForHuman(tableCardSelected, handCardSelected);
        tableCardSelected.clear();
        disableComputerMove("Enable");
        disableHumanMove("Disable");
        populateHand();
        populateTable();
    }


    public void doMoveForComputer(View view) {
        round.playGame();
        // Displays the description of move done by computer
        displayComputerMoveInfo();

        // Checks if human and computer both don't have cards in their hand
        // if both human and computer hands  are empty, then it adds 4 more cards to both player
        checkForEmptyHand();

        System.out.println("table before calling populate hand: " + round.getTable());

        disableComputerMove("Disable");
        disableHumanMove("Enable");
        // Populates the updated table cards
        populateTable();

        // populated the update table cards
        populateHand();

    }

    public void displayCard(LinearLayout linearLayout, List<String> handCards, String playerName) {

        for (String onecard : handCards) {

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

            if (playerName.equals("Human")){
                imageView.setClickable(true);
                imageView.setOnClickListener(this);
            }

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

    // Display player's pile and deck in a seperate dialogue box
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


    public void checkForEmptyHand() {

        if((round.getDeck().size() == 0) && (round.getComputerHand().size() == 0) && (round.getHumanHand().size() == 0)){


            // NEED TO DO, jasle last ma capture garxa, tesle sab card laanxa
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("---- ROUND ENDED ----");
            round.calculateScore();
            alertDialog.setMessage("Computer: " + round.getComputerScore() + "\n\n" + "Human: " + round.getHumanScore() + "\n\n");
            alertDialog.show();

            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.putExtra("state", "newGame");
            startActivity(intent);
        }

        if ((round.getHumanHand().size() == 0) && (round.getComputerHand().size() == 0) && (round.getDeck().size() != 0)) {
            round.addCardsPlayer();
        } else {
            System.out.println("The players still have cards in their hands");
        }

        String winner;
        int computerScore = round.getComputerScore();
        int humanScore = round.getHumanScore();
        if (computerScore >= 21 || humanScore >= 21) {

            if (computerScore > humanScore) {
                winner = "Computer";
            }
            else if (humanScore > computerScore) {
                winner = "Human";
            }
            else {
               winner = "Draw";
            }

            Intent intent = new Intent(MainActivity.this, EndActivity.class);
            intent.putExtra("humScore", humanScore);
            intent.putExtra("compScore", computerScore);
            intent.putExtra("win", winner);
            startActivity(intent);

        }
    }


    public void displayTableWithBuild(Deque<Deque<Cards>> tableCards) {

        System.out.println("Table Received in displayTableWithBuild: " + tableCards);
       // tableCards.pollLast();
        LinearLayout linearLayout = findViewById(R.id.tableCardBox);
        linearLayout.removeAllViews();
        for (Deque<Cards> insideTable : tableCards) {
            LinearLayout linearLayout1 = new LinearLayout(this);

            for (Cards singleCard : insideTable) {
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(180, 250);
                params.setMargins(15, 10, 15, 10);
                imageView.setLayoutParams(params);

                Context context = linearLayout.getContext();
                int id = context.getResources().getIdentifier(singleCard.toString().toLowerCase(), "drawable", context.getPackageName());
                imageView.setImageResource(id);

                imageView.setTag(singleCard.toString());

                imageView.setPadding(5, 5, 5, 5);
                imageView.setId(id);
                imageView.setClickable(true);
                imageView.setOnClickListener(this);

                linearLayout1.addView(imageView);
            }
            linearLayout1.setClickable(false);
            linearLayout1.setOnClickListener(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(15,0,0,10);
            linearLayout1.setPadding(5,5,5,5);
            linearLayout1.setLayoutParams(params);
            linearLayout1.setBackground(getResources().getDrawable(R.drawable.onborder));
            linearLayout1.setTag("table");

            linearLayout.addView(linearLayout1);
        }
    }



    //    This function is called when HUMAN player wants to  get help from computer
// This function is called when the player clicks on Score button
    public void displayGetHelp(View view) {

        String helpMove = round.getHelp();

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.help);
        alertDialog.setTitle("Here is the best move!");
        alertDialog.setMessage(helpMove);
        alertDialog.show();
    }

    public void displayComputerMoveInfo() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.move);
        alertDialog.setTitle("Computer Move");
        alertDialog.setMessage(round.getComputerMoveInfo());
        alertDialog.show();
    }
}







