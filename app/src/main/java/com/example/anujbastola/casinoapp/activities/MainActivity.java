package com.example.anujbastola.casinoapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.EditText;
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
import static java.lang.System.in;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int layout_width;

    private String handCardSelected;
    private int handCardId = 0;

    private String nextPlayer;
    private String lastCapturer;
    private Deque<String> tableCardSelected = new ArrayDeque<>();

    private Vector<Integer> onClickedBuildId = new Vector<>();
    // Deque to store the cards on table

    private Round round = new Round();

    private int humanScore;
    private int computerScore;
    private int initHumanScore;
    private int initCompScore;
    private int roundNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String gameState = getIntent().getStringExtra("state");
        final String fileName = getIntent().getStringExtra("file");


        // IF user wants to load a game, then this if statement will execute
        if (gameState.equals("load")) {
            System.out.println("User Clicked on Load Game");
            System.out.println("File Name: " + fileName);
            try {
                if (isExternalStorageReadable()) {
                    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/savefiles/" + fileName;
                    System.out.println("Path is " + path);
                    round.loadGame(path);
                    nextPlayer = round.getNextPlayer();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // this if statement is executed when a new round is started
        else if (gameState.equals("nextRound")) {
            nextPlayer = getIntent().getStringExtra("lastcapture");
            final int humanScoreFromLastRound = getIntent().getIntExtra("humanScore", 1);
            final int compScoreFromLastRound = getIntent().getIntExtra("compScore", 1);
            final int roundnumber = getIntent().getIntExtra("roundNumber", 1);

            round.setHumanScore(humanScoreFromLastRound);
            round.setComputerScore(compScoreFromLastRound);
            round.setRoundNum(roundnumber + 1);
            System.out.println("Next player is " + nextPlayer);
            round.setHands();
        }
        // Starting new game
        else {
            roundNum = 1;
            round.setRoundNum(roundNum);
            nextPlayer = "Computer";
            round.setHands();
        }
        // populate Hand card  and table cards
        try {
            populateHand();
            populateTable();

        } catch (Exception e) {
            e.printStackTrace();
        }
        // For the first move, if human is computer, then disable buttons for computer, else disable button of human
        if (nextPlayer.equals("Human")) {
            disableComputerMove("Disable");
        } else {
            disableHumanMove("Disable");
        }

    }

    // Disable Move Buttons(Capture, set build, capture builds, trail etc)
    /* *********************************************************************
    Function Name: disableHumanMove
    Purpose: to to disable move buttons for human

    Parameters:
                state, Disable for disabling buttons or Enable for enabling buttons

    Return Value:
    Local Variables:
               btn, button for capture
               btn1, button for setBuild
               btn2, button for trail
               btn3, button for extendBuild
               btn4, button for captureBuild
    Algorithm:
                1) if state is Enable, enable all the buttons
                2) if state is Disable, disable all the buttons
    Assistance Received: none
    ********************************************************************* */
    public void disableHumanMove(String state) {

        Button btn = findViewById(R.id.capture);
        Button btn1 = findViewById(R.id.setBuild);
        Button btn2 = findViewById(R.id.trail);
        Button btn3 = findViewById(R.id.extendBuild);
        Button btn4 = findViewById(R.id.captureBuild);

        if (state.equals("Disable")) {
            btn.setEnabled(false);
            btn1.setEnabled(false);
            btn2.setEnabled(false);
            btn3.setEnabled(false);
            btn4.setEnabled(false);
        } else if (state.equals("Enable")) {
            btn.setEnabled(true);
            btn1.setEnabled(true);
            btn2.setEnabled(true);
            btn3.setEnabled(true);
            btn4.setEnabled(true);
        }
    }

    // Disable DO MOVE button
        /* *********************************************************************
    Function Name: disableComputerMove
    Purpose: to to disable DO MOVE BUTTON for Computer

    Parameters:
                state, Disable for disabling buttons or Enable for enabling buttons

    Return Value:
    Local Variables:
              btn, DO MOVE button
    Algorithm:
                1) if state is Enable, enable all the buttons
                2) if state is Disable, disable all the buttons
    Assistance Received: none
    ********************************************************************* */
    public void disableComputerMove(String state) {

        Button btn = findViewById(R.id.compMove);

        if (state.equals("Disable")) {
            btn.setEnabled(false);
        } else if (state.equals("Enable")) {
            btn.setEnabled(true);
        }
    }

    //checks if the ExternalStorage is Readable or not
    //returns boolean values
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }


    /* *********************************************************************
    Function Name: populateHand
    Purpose: to populate in human card box
    Parameters:

    Return Value:
    Local Variables:
               humanHand, cards from human hand
               computerHand, cards from computer hand
    Algorithm:
                1) Get humanHand and computerHand from round class(calls player class)
                2) get STring value of cards and store it in handCards List
                3) Get linearlayout of humanbox and passed it to displayCard function with hand cards
                4) Get linearlayout of computerbox and passed it to diaplayCard function with hand cards
    Assistance Received: none
    ********************************************************************* */
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
        displayCard(linearLayout, handCards, "Computer");

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
        displayTableWithBuild(round.getTable());
    }

    @Override
    public void onClick(View view) {

        Drawable highlight = getResources().getDrawable(R.drawable.selected);
        view.setBackground(highlight);

        String tagName = view.getTag().toString();
        // int count = view.getChild
        if (!checkIfHumanCard(tagName)) {
            tableCardSelected.addFirst(tagName);
        } else {
            if (checkForDoubleClick(view.getId(), "card")) {
                ImageView imageView = findViewById(view.getId());
                imageView.setBackgroundResource(android.R.color.transparent);
            }
            handCardSelected = view.getTag().toString();
        }

    }

    // checks if the card is double clicked
        /* *********************************************************************
    Function Name: checkForDoubleClick
    Purpose: to check if the card is double clicked
    Parameters:
               buildId, id of the clicked card
               type, type of card
    Return Value:
    Local Variables:
    Algorithm:
                1) if type is build, check in onClickedBuildId function to see if the card was clicked before
                2) if type is card, then check in handCardId to see if the card is double clicked
                3) If human card is double clicked, then remove the border
    Assistance Received: none
    ********************************************************************* */
    public boolean checkForDoubleClick(int buildId, String type) {

        if (type.equals("build")) {
            if (onClickedBuildId.size() > 0) {
                for (Integer id : onClickedBuildId) {
                    if (id.equals(buildId)) {
                        return true;
                    }
                }
            }
            return false;
        } else if (type.equals("card")) {
            if (handCardId == buildId) {
                handCardId = 0;
                return true;
            } else {
                handCardId = buildId;
            }

        }
        return false;
    }

    /* *********************************************************************
    Function Name: checkIfHumanCard
    Purpose: To check if clicked card is human card
    Parameters:
           cardName, the name of the clicked card
    Return Value:
    Local Variables:
            humanCards: stores human cards
    Algorithm:
            1) Loop through humancards to check if card clicked is human cards
    Assistance Received: none
    ********************************************************************* */
    public boolean checkIfHumanCard(String cardName) {
        Deque<Cards> humanCards = round.getHumanHand();
        for (Cards oneCard : humanCards) {
            if (oneCard.toString().equals(cardName)) {
                return true;
            }
        }
        return false;
    }


    /* *********************************************************************
    Function Name: trailHumanCard
    Purpose: to trail human card
    Parameters:
    Return Value:
    Local Variables:
            status, checks if trail is possible or not

    Algorithm:
            1) call round.trailHumanCarf
            2) if trail is succesful , gets true
            3) if trail is sucessful, then nextPlayer = computer, disable human buttons
            3) if trail is not successful , tries again
    Assistance Received: none
    ********************************************************************* */
    public void trailHumanCard(View view) {
        System.out.println("In Human Trail Card");
        boolean status = true;

        status = round.trailHumanCard(handCardSelected);

        if (status) {
            nextPlayer = "Computer";
            disableComputerMove("Enable");
            disableHumanMove("Disable");
            checkAndPopulate();
        }
        else{

        }
    }

    // When human clicks on CAPTURE button, this function is loaded

    public void humanCaptureMove(View view) {

        round.doCaptureMoveForHuman(tableCardSelected, handCardSelected);
        tableCardSelected.clear();
        disableComputerMove("Enable");
        disableHumanMove("Disable");
        checkAndPopulate();
    }

    public void setBuildHuman(View view) {

        round.doSetBuildForHuman(tableCardSelected, handCardSelected);
        tableCardSelected.clear();
        disableComputerMove("Enable");
        disableHumanMove("Disable");
        checkAndPopulate();
    }

    public void extendBuild(View view) {
        round.doExtendBuild(tableCardSelected, handCardSelected);
        tableCardSelected.clear();
        disableComputerMove("Enable");
        disableHumanMove("Disable");
        checkAndPopulate();
    }

    // Captures the build when human clicks on capture build
    public void doCaptureBuild(View view) {
        round.doCaptureBuildForHuman(tableCardSelected, handCardSelected);
        tableCardSelected.clear();
        disableComputerMove("Enable");
        disableHumanMove("Disable");
        checkAndPopulate();
    }

    // Checks if hands are empty, if hands and deck are empty, then next round is create if score is less than 21, if deck is not empy, 4 cards are dealt to players
    public void checkAndPopulate() {
        checkForEmptyHand();
        populateHand();
        populateTable();
    }

    // Whenever DO MOVE button is clicked, this fuction is triggers which perfrom move from computer
    public void doMoveForComputer(View view) {
        round.playGame();
        // Displays the description of move done by computer
        displayComputerMoveInfo();
        // Checks if human and computer both don't have cards in their hand
        // if both human and computer hands  are empty, then it adds 4 more cards to both player
        checkForEmptyHand();
        disableComputerMove("Disable");
        disableHumanMove("Enable");
        // Populates the updated table cards
        populateTable();

        // populated the update table cards
        populateHand();

    }

    public void displayCard(LinearLayout linearLayout, List<String> handCards, String playerName) {

        for (String onecard : handCards) {
            // Creates a new image view
            ImageView imageView = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(180, 220);
            params.setMargins(40, 10, 35, 10);
            // setting margins for imagview
            imageView.setLayoutParams(params);

            // Getting the card image using the element in the string array
            Context context = linearLayout.getContext();
            int id = context.getResources().getIdentifier(onecard.toLowerCase(), "drawable", context.getPackageName());
            imageView.setImageResource(id);

            // Since all cards are unique, tag name for the imageview is the name of image
            // it is used to get the card value when clicked
            imageView.setTag(onecard);
            imageView.setPadding(5, 5, 5, 5);
            imageView.setId(id);

            // Only human cards are clickable, computer cards can not be clicked
            if (playerName.equals("Human")) {
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

        // Checks if round is finishes
        // if there are no cards in human and computer hand and deck, then round is completed
        if ((round.getDeck().size() == 0) && (round.getComputerHand().size() == 0) && (round.getHumanHand().size() == 0)) {


            // Implement last capture captures all
            // Whoever captured last, the rest of table cards goes to that player's pile
            // -----------------------------------
            lastCapturer = round.getLastCaptureName();

            if ( lastCapturer.equals("Computer")){
                for (Deque<Cards> temp: round.getTable()){
                    round.addToComputerPile(temp.peekFirst());
                }
            }
            else if (lastCapturer.equals("Human")){
                for (Deque<Cards> temp: round.getTable()){
                    round.addToHumanPile(temp.peekFirst());
                }
            }
            // clears table
            round.clearTable();

            // ---------------------------------------

            initCompScore = round.getComputerScore();
            initHumanScore = round.getHumanScore();
            int computerPileSize = round.getComputerPile().size();
            int humanPileSize = round.getHumanPile().size();
            int computerSpadesNum = round.getComputerSpadesNum();
            int humanSpadesNum = round.getHumanSpadesNum();


            round.calculateScore();

            // Diaglog for round end
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("---- ROUND ENDED ----");
            alertDialog.setMessage("Computer(Round): " + (round.getComputerScore() - initCompScore) + "\n" + "Human(Round): " + (round.getHumanScore() - initHumanScore) +
                    "\n" + "Total Human Score: " + round.getHumanScore() + "\n" + "Total Computer Score: " + round.getComputerScore() + "\n" + "# of Spades in Computer Pile: " + computerSpadesNum + "\n" +
                    "# of Spades in Human Pile: " + humanSpadesNum + "\n" + "Human Pile Size: " + round.getHumanPile().size() + "\n" + "Computer Pile Size: " + round.getComputerPile().size());

            // Setting a position button on alertdialog
            // When clicked would check the state of the tournament
            alertDialog.setPositiveButton("Next", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    String winner;
                    computerScore = round.getComputerScore();
                    humanScore = round.getHumanScore();

                    //  if one of the player score is >= 21, then the player wins
                    // in case of draw, the winner would be set to DRAW
                    if (computerScore >= 21 || humanScore >= 21) {

                        if (computerScore > humanScore) {
                            winner = "Computer";
                        } else if (humanScore > computerScore) {
                            winner = "Human";
                        } else {
                            winner = "Draw";
                        }

                        // Calls the EndActivity since the tournament ended
                        Intent intent = new Intent(MainActivity.this, EndActivity.class);
                        intent.putExtra("humScore", humanScore);
                        intent.putExtra("compScore", computerScore);
                        intent.putExtra("win", winner);
                        startActivity(intent);

                        // if the score is less then 21, then next round is started
                        // this calls the mainactivity by intenting all scores, roundNumber, nextplayer
                    } else {
                        // ---------------------------------------------------------------------------------------------------------
                        Dialog dialoginfo = new Dialog(MainActivity.this);
                        dialoginfo.setContentView(R.layout.activity_infos);
                        dialoginfo.setTitle("Info:");
                        dialoginfo.show();

                        Deque<Cards> computerPile = new ArrayDeque<>();
                        computerPile = round.getComputerPile();
                        List<String> compPile = new ArrayList<>();

                        for (Cards compCard : computerPile) {
                            compPile.add(compCard.toString());
                        }

                        LinearLayout linearLayout = dialoginfo.findViewById(R.id.computerPileDisplay);
                        displayInfoCard(linearLayout, compPile);


                        Deque<Cards> humanPile = new ArrayDeque<>();
                        List<String> humPile = new ArrayList<>();
                        humanPile = round.getHumanPile();
                        for (Cards humanCard : humanPile) {
                            humPile.add(humanCard.toString());
                        }

                        LinearLayout linearLayout1 = dialoginfo.findViewById(R.id.humanPileDisplay);
                        displayInfoCard(linearLayout1, humPile);

                        // Stores the string value of deck
                        List<String> deck = new ArrayList<>();

                        Deque<Cards> mydeck = new ArrayDeque<>();
                        mydeck = round.getDeck();

                        for (Cards deckCard : mydeck) {
                            deck.add(deckCard.toString());
                        }

                        LinearLayout linearLayout2 = dialoginfo.findViewById(R.id.deckDisplay);
                        displayInfoCard(linearLayout2, deck);

                        // -----------------------------------------------------------------------------------------------------

                        Button button = dialoginfo.findViewById(R.id.nextInInfo);

                        button.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                                intent.putExtra("state", "nextRound");
                                intent.putExtra("roundNumber", round.getRoundNum());
                                intent.putExtra("humanScore", humanScore);
                                intent.putExtra("compScore", computerScore);
                                lastCapturer = "Human";
                                intent.putExtra("lastcapture", lastCapturer);

                                startActivity(intent);
                            }
                        });
                    }
                }

            });
            alertDialog.show();
        }

        System.out.println("Before deck, player checking ");
        System.out.println("Human Hand: " + round.getHumanHand());
        System.out.println("Computer Hand: " + round.getComputerHand());
        System.out.println("Deck: " + round.getDeck());

        // IF there is still some cards in deck, the 4 cards are dealt to each player for more moves
        if ((round.getHumanHand().size() == 0) && (round.getComputerHand().size() == 0) && (round.getDeck().size() != 0)) {
            System.out.println("THe deck is not empty, so adding cards");
            round.addCardsPlayer();
        } else {
            System.out.println("The players still have cards in their hands");
        }
    }


    // Single cards gets one linear layout
    // all the cards in build are combined in one linear layout
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
            params.setMargins(15, 0, 0, 10);
            linearLayout1.setPadding(5, 5, 5, 5);
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


    // When computer performs moves, the a dialogue box explains the move done by the computer
    public void displayComputerMoveInfo() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setIcon(R.drawable.move);
        alertDialog.setTitle("Computer Move");
        alertDialog.setMessage(round.getComputerMoveInfo());
        alertDialog.show();
    }


    // NOT IMPLEMENTED
    public void saveGame(View view) {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.savedialogue);
        dialog.setTitle("Enter new file name to save.");
        dialog.show();

        Button button = dialog.findViewById(R.id.submitToSave);
        final EditText editText = dialog.findViewById(R.id.fileNameInput);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String filename = editText.getText().toString();
                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/savefiles/" + filename;
                System.out.println("Full path is " + path);
                round.saveGame(path);
            }
        });

    }

}







