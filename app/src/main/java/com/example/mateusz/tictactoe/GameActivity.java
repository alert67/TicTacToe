package com.example.mateusz.tictactoe;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;

public class GameActivity extends AppCompatActivity {


    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private LocationManager locationManager;
    private LocationListener locationListener;

    private View root;

    private String data;

    private TextView textTimer;

    private DBHelper dbHelper;

    long secondsPassed;
    long minutesPassed;

    private CountDownTimer timer;

    private String winner ="";
    private String address;
    private String gameTime;
    // Player  0 krzyzyk 1 kolko
    private int player=0;
    private boolean computer;
    AlertDialog.Builder builder;


    private computerAI computerAI = new computerAI();


    // 2 - puste  0 - krzyzyk 1 - kolko

    private boolean gameActive = true;

    private int[] gameState={2,2,2,2,2,2,2,2,2};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        root = findViewById(android.R.id.content);


        computer = getIntent().getExtras().getBoolean("versus_computer");

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        data = dateFormat.format(date);

        Log.i("data_log", data);
        builder = new AlertDialog.Builder(this);



        dbHelper = DBHelper.getInstance(this);

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {

                if (count == 2) {
                    playAgain();
                }


            }
        });

        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                updateLocationInfo(location);

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (Build.VERSION.SDK_INT < 23) {

            startListening();

        } else {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            } else {

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                if (location != null) {

                    updateLocationInfo(location);

                }

            }

        }

        startTimer();




    }

    public void startTimer(){

        final long EndTime=3600;
        textTimer = findViewById(R.id.textTimer);
        minutesPassed=0;
        secondsPassed=0;


        timer = new CountDownTimer(EndTime*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                long secondUntilFinished = (long) (millisUntilFinished/1000);
                secondsPassed = (EndTime - secondUntilFinished);
                minutesPassed = (long) (secondsPassed/60);
                secondsPassed = secondsPassed%60;
                // So now at this point your time will be: minutesPassed:secondsPassed
                gameTime=String.format("%02d", minutesPassed) + ":" + String.format("%02d", secondsPassed);
                textTimer.setText(gameTime);
            }

            public void onFinish() {
            }

        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }


    public void draw(View view) {
        ImageView counter = (ImageView) view;

        ImageView imageMove = (ImageView) findViewById(R.id.imageMove);


        Log.i("Counter", counter.getTag().toString());

        int tappedCounter = Integer.parseInt(counter.getTag().toString());

        if (gameState[tappedCounter] == 2 && gameActive) {

            gameState[tappedCounter] = player;

            counter.setTranslationY(-1500);

            if (player == 0) {

                counter.setImageResource(R.drawable.cross);
            } else {
                counter.setImageResource(R.drawable.circle);
            }

            counter.animate().translationYBy(1500).setDuration(300);


            if (computerAI.winPosition(gameState, player)) {

                gameActive = false;
                timer.cancel();

                if (player == 1) {
                    winner = getString(R.string.circle);
                } else {
                    winner = getString(R.string.cross);
                }

                Toast.makeText(this, winner + " " + getString(R.string.won) + "!", Toast.LENGTH_SHORT).show();
                zapis();

            }else if(computerAI.checkMovesLeft(gameState)== false){
                gameActive = false;
                timer.cancel();

                winner = getString(R.string.draw);
                Toast.makeText(this, getString(R.string.draw), Toast.LENGTH_SHORT).show();
                zapis();
            }



            if (player == 0) {
                player = 1;
                imageMove.setImageResource(R.drawable.circle);
            } else {
                player = 0;
                imageMove.setImageResource(R.drawable.cross);
            }

            if (computer && player == 1) {
                ImageView best;
                int bestMove = computerAI.findBestMoves(gameState);

                switch (bestMove) {
                    case 0:
                        best = findViewById(R.id.imageView1);
                        best.performClick();
                        break;
                    case 1:
                        best = findViewById(R.id.imageView2);
                        best.performClick();
                        break;
                    case 2:
                        best = findViewById(R.id.imageView3);
                        best.performClick();
                        break;
                    case 3:
                        best = findViewById(R.id.imageView4);
                        best.performClick();
                        break;
                    case 4:
                        best = findViewById(R.id.imageView5);
                        best.performClick();
                        break;
                    case 5:
                        best = findViewById(R.id.imageView6);
                        best.performClick();
                        break;
                    case 6:
                        best = findViewById(R.id.imageView7);
                        best.performClick();
                        break;
                    case 7:
                        best = findViewById(R.id.imageView8);
                        best.performClick();
                        break;
                    case 8:
                        best = findViewById(R.id.imageView9);
                        best.performClick();
                        break;


                }

            }

        }
    }

    public void playAgain(){

        Toast.makeText(this, getString(R.string.reset),Toast.LENGTH_LONG).show();

        GridLayout gridLayout = (GridLayout) findViewById(R.id.grid);

        ImageView imageMove = (ImageView) findViewById(R.id.imageMove);

        timer.cancel();

        startTimer();


        for(int i=0; i<gridLayout.getChildCount(); i++){
            ImageView counter = (ImageView) gridLayout.getChildAt(i);

            counter.setImageDrawable(null);
        }

        for (int i=0; i<gameState.length; i++){
            gameState[i]=2;
        }

        player=0;

        imageMove.setImageResource(R.drawable.cross);

        gameActive = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            startListening();

        }

    }

    public void startListening() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        }

    }

    public void updateLocationInfo(Location location) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {

            address =getString(R.string.place_not_found)+" ";

            List<Address> listAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if (listAddresses != null && listAddresses.size() > 0 ) {

                Log.i("PlaceInfo", listAddresses.get(0).toString());

                address ="";

                if (listAddresses.get(0).getSubThoroughfare() != null) {

                    address += listAddresses.get(0).getSubThoroughfare() + " ";

                }

                if (listAddresses.get(0).getThoroughfare() != null) {

                    address += listAddresses.get(0).getThoroughfare() + "\n";

                }

                if (listAddresses.get(0).getLocality() != null) {

                    address += listAddresses.get(0).getLocality() + " ";

                }

                if (listAddresses.get(0).getPostalCode() != null) {

                    address += listAddresses.get(0).getPostalCode() + "\n";

                }

                if (listAddresses.get(0).getCountryName() != null) {

                    address += listAddresses.get(0).getCountryName() + "";

                }

            }

            TextView addressTextView = (TextView) findViewById(R.id.textPlace);

            addressTextView.setText(getString(R.string.place)+address);


        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    public void zapis(){

        builder.setTitle(getString(R.string.save));
        builder.setMessage(getString(R.string.save_question));

        builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // Do nothing but close the dialog
                int intComputer;
                if(computer){
                    intComputer=1;
                }else{
                    intComputer=0;
                }
                Bitmap croppedBitmap = Bitmap.createBitmap(Screenshot.takescreenshotOfRootView(root), 0, 0, Screenshot.takescreenshotOfRootView(root).getWidth() -0, Screenshot.takescreenshotOfRootView(root).getHeight() - 300);
                croppedBitmap = Bitmap.createBitmap(croppedBitmap,0,420,croppedBitmap.getWidth(),croppedBitmap.getHeight() -420);
                Save save = new Save(data,croppedBitmap,winner,address,intComputer,gameTime);
                dbHelper.addSave(save);

                dialog.dismiss();
            }
        });

        builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });



        AlertDialog alert = builder.create();
        alert.show();

    }

}
