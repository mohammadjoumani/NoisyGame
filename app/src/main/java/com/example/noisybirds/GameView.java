package com.example.noisybirds;


import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.paperdb.Paper;

public class GameView extends SurfaceView implements Runnable {

    private Thread thread;

    private int score = 0;

    private List<Integer>scores;

    private boolean isPlaying, isGameOver = false;

    private Background background1, background2;

    private int screenX, screenY;

    public static float screenRatioX, screenRatioY;

    private Paint paint;

    private Bird[] birds;

    //private SharedPreferences sharedPreferences;

    private Random random;

    private SoundPool soundPool;

    private int sound;

    private Flight flight;

    private List<Bullet> bullets;

    private GameActivity gameActivity;

    public GameView(GameActivity gameActivity, int screenX, int screenY) {
        super( gameActivity );

        this.gameActivity = gameActivity;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setContentType( AudioAttributes.CONTENT_TYPE_MUSIC )
                    .setUsage( AudioAttributes.USAGE_GAME )
                    .build();
            soundPool = new SoundPool.Builder()
                    .setAudioAttributes( audioAttributes )
                    .build();
        } else
            soundPool = new SoundPool( 1, AudioManager.STREAM_MUSIC, 0 );

        sound = soundPool.load( gameActivity, R.raw.shoot, 1 );

        this.screenX = screenX;
        this.screenY = screenY;
//        Log.d( "screenX",screenX+"" );
//        Log.d( "screenY",screenY+"" );
        screenRatioX = 2261f / screenX;
        screenRatioY = 1080f / screenY;

//        Log.d( "screenRatioXfff",screenRatioX+"" );
//        Log.d( "screenRatioYfff",screenRatioY+"" );
        background1 = new Background( screenX, screenY, getResources() );
        background2 = new Background( screenX, screenY, getResources() );

        flight = new Flight( this, screenY, getResources() );


        bullets = new ArrayList<>();

        background2.x = screenX;

        paint = new Paint();
        paint.setTextSize( 128 );
        paint.setColor( Color.WHITE );

        birds = new Bird[4];

        random = new Random();

        for (int i = 0; i < 4; i++) {
            Bird bird = new Bird( getResources() );
            birds[i] = bird;
        }

        scores=new ArrayList<>();
    }

    @Override
    public void run() {
        while (isPlaying) {
            update();
            draw();
            sleep();

        }

    }

    private void update() {
        background1.x -= 10 * screenRatioX;
        background2.x -= 10 * screenRatioX;


        if (background1.x + background1.background.getWidth() < 0) {
            background1.x = screenX;
        }

        if (background2.x + background2.background.getWidth() < 0) {
            background2.x = screenX;
        }
        if (flight.isGoinUp)
            flight.y = (int) (flight.yFF);
//        else
//            flight.y += 30 * screenRatioY;

        if (flight.y < 0)
            flight.y = 0;

        if (flight.y > screenY - flight.height)
            flight.y = screenY - flight.height;

        List<Bullet> trash = new ArrayList<>();

        for (Bullet bullet : bullets) {
            if (bullet.x > screenX)
                trash.add( bullet );

            bullet.x += 50 * screenRatioX;

            for (Bird bird : birds) {

                if (Rect.intersects( bird.getCollisionShape(), bullet.getCollisionShape() )) {

                    score++;
                    bird.x = -500;
                    bullet.x = screenX + 500;
                    bird.wasShot = true;
                }
            }
        }
        for (Bullet bullet : trash) {
            bullets.remove( bullet );

        }

        for (Bird bird : birds) {
            bird.x -= bird.speed;

            if (bird.x + bird.width < 0) {

                if (!bird.wasShot) {
                    isGameOver = true;
                    return;
                }
                int bound = (int) (30 * screenRatioX);
                bird.speed = random.nextInt( bound );

                if (bird.speed < 10 * screenRatioX)
                    bird.speed = (int) (10 * screenRatioX);
                bird.x = screenX;
                bird.y = random.nextInt( screenY - bird.height );

                bird.wasShot = false;
            }

            if (Rect.intersects( bird.getCollisionShape(), flight.getCollisionShape() )) {
                isGameOver = true;
                return;
            }
        }

    }

    private void draw() {
        if (getHolder().getSurface().isValid()) {
            Canvas canvas = getHolder().lockCanvas();
            canvas.drawBitmap( background1.background, background1.x, background1.y, paint );
            canvas.drawBitmap( background2.background, background2.x, background1.y, paint );

            for (Bird bird : birds)
                canvas.drawBitmap( bird.getBird(), bird.x, bird.y, paint );

            canvas.drawText( score + "", screenX / 2f, 164, paint );

            if (isGameOver) {
                isPlaying = false;
                gameActivity.getMediaPlayer().pause();
                canvas.drawBitmap( flight.getDead(), flight.x, flight.y, paint );
                getHolder().unlockCanvasAndPost( canvas );
                saveHighScore();
                waitBeforExiting();
                return;
            }

            canvas.drawBitmap( flight.getFlight(), flight.x, flight.y, paint );

            for (Bullet bullet : bullets) {
                canvas.drawBitmap( bullet.bullet, bullet.x, bullet.y, paint );
                Log.d( "bulletX", bullet.x + "" );
                Log.d( "bulletY", bullet.y + "" );
            }

            //canvas.drawBitmap( add.add,add.x,add.y,paint );

            getHolder().unlockCanvasAndPost( canvas );

        }
    }

    private void waitBeforExiting() {

        try {
            Thread.sleep( 3000 );
            gameActivity.startActivity( new Intent( gameActivity, MainActivity.class ) );
            gameActivity.finish();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void saveHighScore() {

        //scores=Paper.book().read( "scores" );
           // scores.add( score );
//        Log.d( "tt",scores.get( 0 ) +"");
        //Paper.book().write( "scores",scores );

        if (Paper.book().read( "score", -1 ) < score) {
            Paper.book().write( "score", score );
        }
    }

    private void sleep() {
        try {
            Thread.sleep( 17 );
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void resume() {
        isPlaying = true;
        thread = new Thread( this );
        thread.start();
    }

    public void pause() {
        try {
            isPlaying = false;
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
//                if (event.getX() < screenX / 2) {
//                    flight.isGoinUp = true;
//                }
                if (event.getX() > screenX / 2)
                    flight.toShoot++;
                break;
            case MotionEvent.ACTION_UP:
                flight.isGoinUp = false;
//                if (event.getX() < screenX / 2)
//                    flight.toShoot++;
                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getX() < screenX / 2) {
                    flight.isGoinUp = true;
                    flight.xFF = (int) (event.getX());
                    flight.yFF = (int) (event.getY());

                }
                if (event.getX() > screenX / 2)
                    flight.toShoot++;
                break;
        }
        return true;
    }

    public void newBullet() {

//        if(!sharedPreferences.getBoolean( "isMute",false ));
//        soundPool.play( sound,1,1,0,0,1 );
        if (!Paper.book().read( "isMute", false ))
            soundPool.play( sound, 1, 1, 0, 0, 1 );
        Bullet bullet = new Bullet( getResources() );
        bullet.x = flight.x + flight.width;
        bullet.y = flight.y + (flight.height / 2);

        bullets.add( bullet );
    }
}
