package com.example.anujbastola.casinoapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.example.anujbastola.casinoapp.R;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
    }

    //changes intent to Main Activity
    //puts integer 1 as extra
    /* *********************************************************************
    Function Name: newGame
    Purpose: to start a new game

    Parameters:

    Return Value:
    Local Variables:

    Algorithm:
                1) Create alert dialog for new game
                2) Add to item, "Head" "Tail" to the dialog box
    Assistance Received: none
    ********************************************************************* */
    public void newGame(View view){
        AlertDialog.Builder prompt = new AlertDialog.Builder(this);
        prompt.setTitle("Coin Toss");
        String [] items = {"Head", "Tail"};
        prompt.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView select = ((AlertDialog)dialog).getListView();

                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("state", "new");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        prompt.show();
    }

    /* *********************************************************************
    Function Name: loadGame
    Purpose: to provide the list of file names to load game from the file

    Parameters:

    Return Value:
    Local Variables:

    Algorithm:
                1) Create alert dialog for new game
                2) Set the items by getting all items from "/savefile" folder in the mobile app
    Assistance Received: none
    ********************************************************************* */
    public void loadGame(View view){

        System.out.println("I am in Load Game");
        AlertDialog.Builder prompt = new AlertDialog.Builder(this);
        prompt.setTitle("Load Game");

        String fileDir = Environment.getExternalStorageDirectory().getAbsolutePath()+"/savefiles";
        System.out.println("File Directory: " + fileDir);
        final File[] files = new File(fileDir).listFiles();
        List<String> nameList = new ArrayList<>();
        for (File file : files ){
            String name = file.getName();
            if (name.endsWith(".txt")){
                nameList.add(name);
            }
        }

        String [] items = new String[nameList.size()];
        items = nameList.toArray(items);

        prompt.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int these) {
                ListView select = ((AlertDialog)dialog).getListView();
                String fileName = (String) select.getAdapter().getItem(these);
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("state", "load");
                intent.putExtra("file", fileName);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        prompt.show();
    }
}
