package com.example.mateusz.tictactoe;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        TextView textData = findViewById(R.id.textDataDetails);
        ImageView image = findViewById(R.id.image);
        TextView textGameTime = findViewById(R.id.textTimeDetails);
        TextView textAddress = findViewById(R.id.textLocationDetails);
        TextView textComputer = findViewById(R.id.textVersusDetails);
        TextView textWinner = findViewById(R.id.textWinnerDetails);


        Bitmap bmp;

        byte[] byteArray = getIntent().getByteArrayExtra("Image");
        bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);


        image.setImageBitmap(bmp);
        textData.setText(getString(R.string.date)+getIntent().getStringExtra("Data"));
        textAddress.setText(getString(R.string.place)+getIntent().getStringExtra("Address"));
        textGameTime.setText(getString(R.string.game_time)+getIntent().getStringExtra("GameTime"));

        if(getIntent().getStringExtra("Winner").equals(getString(R.string.circle))){
            textWinner.setText(getString(R.string.winner)+getString(R.string.circle));
        }else if(getIntent().getStringExtra("Winner").equals(getString(R.string.cross))){
            textWinner.setText(getString(R.string.winner)+getString(R.string.cross));
        }else{
            textWinner.setText(getString(R.string.winner)+getString(R.string.draw));
        }

        if(getIntent().getIntExtra("Computer",0)==1){
            textComputer.setText(getString(R.string.versus_computer));
        }else{
            textComputer.setText(getString(R.string.versus_player));
        }


    }
}
