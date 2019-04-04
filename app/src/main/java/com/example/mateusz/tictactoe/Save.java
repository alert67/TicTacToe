package com.example.mateusz.tictactoe;

import android.graphics.Bitmap;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class Save {

    private String data;
    private Bitmap screenshoot;
    private String winner;
    private String address;
    int computer;
    String gameTime;

    public Save(String data, Bitmap screenshoot, String winner, String address, int computer, String gameTime) {
        this.data = data;
        this.screenshoot = screenshoot;
        this.winner = winner;
        this.address = address;
        this.computer = computer;
        this.gameTime = gameTime;
    }

    public String getGameTime() {
        return gameTime;
    }

    public void setGameTime(String gameTime) {
        this.gameTime = gameTime;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Bitmap getScreenshoot() {
        return screenshoot;
    }

    public void setScreenshoot(Bitmap screenshoot) {
        this.screenshoot = screenshoot;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getComputer() {
        return computer;
    }

    public void setComputer(int computer) {
        this.computer = computer;
    }
}
