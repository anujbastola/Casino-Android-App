package com.example.anujbastola.casinoapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.anujbastola.casinoapp.R;

public class EndActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        int humScore = getIntent().getIntExtra("humScore", 1);
        int compScore = getIntent().getIntExtra("compScore", 1);
        String win = getIntent().getStringExtra("win");

        TextView human = findViewById(R.id.humanScore);
        String humanText = "Human Score: " + humScore;
        human.setText(humanText);



        TextView computer = findViewById(R.id.computerScore);
        String compText = "Computer Score: " + compScore;
        computer.setText(compText);

        TextView winner = findViewById(R.id.winner);
        String winnerText = "Winner: " + win;
        winner.setText(winnerText);


    }
}
