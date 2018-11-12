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
    public void newGame(View view){
        AlertDialog.Builder prompt = new AlertDialog.Builder(this);
        prompt.setTitle("Coin Toss");
        String [] items = {"Head", "Tail"};
        prompt.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ListView select = ((AlertDialog)dialog).getListView();
                String boardSize = (String) select.getAdapter().getItem(which);
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("state", 1);
                intent.putExtra("boardSize", boardSize);
                //finishAffinity();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        prompt.show();
    }


    public void loadGame(View view){

        AlertDialog.Builder prompt = new AlertDialog.Builder(this);
        prompt.setTitle("Load Game");
        //System.out.println("I am inside load game");
        String fileDir = Environment.getExternalStorageDirectory() +"/savefiles";
        //System.out.println("I am here " + fileDir);
        final File[] files = new File(fileDir).listFiles();
        List<String> nameList = new ArrayList<>();
        for (File file : files ){
            String name = file.getName();
            System.out.println("File Name: " + name);

            if (name.endsWith(".txt")){
                nameList.add(name);
                System.out.println("File Name: " + name);
            }
        }

        String [] items = new String[nameList.size()];
        items = nameList.toArray(items);

        prompt.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int these) {
              ListView select = ((AlertDialog)dialog).getListView();
              // Gets the name of the file selected by user from list of files
               String fileName = (String) select.getAdapter().getItem(these);
                //Intent intent = new Intent(StartActivity.this, MainActivity.class);
                System.out.println("The file selected is " + fileName);
                //intent.putExtra("filename", fileName);

                readFile(fileName);

                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                //startActivity(intent);
            }
        });
        prompt.show();
    }

    public void readFile(String fileName){




    }

}
