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

                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra("state", "new");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        prompt.show();
    }

//    String fileDir = Environment.getExternalStorageDirectory() +"/savefiles";

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
