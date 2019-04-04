package com.example.mateusz.tictactoe;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





    }

    public void versus_player(View view){
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("versus_computer",false);
        startActivity(intent);
    }

    public void versus_computer(View view){
        Intent intent = new Intent(this,GameActivity.class);
        intent.putExtra("versus_computer",true);
        startActivity(intent);
    }

    public void stats(View view){
        Intent intent = new Intent(this,StatsActivity.class);
        startActivity(intent);

    }
}
