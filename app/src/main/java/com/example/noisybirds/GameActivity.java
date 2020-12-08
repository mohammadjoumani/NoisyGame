package com.example.noisybirds;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Toast;

import io.paperdb.Paper;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    private  MediaPlayer mediaPlayer;

    private long backPressTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

        mediaPlayer= MediaPlayer.create(this,R.raw.background);
        if(!Paper.book().read( "isMute",false )){
            mediaPlayer.start();
        }
        else{
            mediaPlayer.pause();
        }

        Point point = new Point();
        getWindowManager().getDefaultDisplay().getSize( point );

        gameView = new GameView( this, point.x, point.y  );

        setContentView( gameView );
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        gameView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
        this.mediaPlayer.pause();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public void onBackPressed() { // function for show message when click button to back

        if (backPressTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
            this.getMediaPlayer().pause();
            return;
        } else {
            backToast = Toast.makeText( getBaseContext(), "Press back again to exite", Toast.LENGTH_SHORT );
            backToast.show();
        }
        backPressTime = System.currentTimeMillis();
    }
}