package com.example.noisybirds;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

import static com.example.noisybirds.GameView.screenRatioX;
import static com.example.noisybirds.GameView.screenRatioY;

public class Flight {

    public boolean isGoinUp = false;

    public int xFF,yFF;
    int toShoot = 0;

    int x, y, wingCounter = 0, shootCounter = 1;

    int width, height;
    Bitmap flight1, flight2;

    Bitmap shot1, shot2, shot3, shot4, shot5 ,dead;

    private GameView gameView;

    Flight(GameView gameView, int screenY, Resources res) {

        this.gameView = gameView;
        flight1 = BitmapFactory.decodeResource( res, R.drawable.fly1 );
        flight2 = BitmapFactory.decodeResource( res, R.drawable.fly2 );

        width = flight1.getWidth();
        height = flight2.getHeight();
//        Log.d( "width++", width + "" );
//        Log.d( "height++", height + "" );

        width /= 4;
        height /= 4;
//        Log.d( "width/4", width + "" );
//        Log.d( "height/4", height + "" );


        width *= (int) screenRatioX;
        height *= (int) screenRatioY;
//        Log.d( "width", width + "" );
//        Log.d( "height", height + "" );
//        Log.d( "screenRatioX", screenRatioX + "" );
//        Log.d( "screenRatioY", screenRatioY + "" );

        flight1 = Bitmap.createScaledBitmap( flight1, width , height, false );
        flight2 = Bitmap.createScaledBitmap( flight2, width, height, false );

        shot1 = BitmapFactory.decodeResource( res, R.drawable.shoot1 );
        shot2 = BitmapFactory.decodeResource( res, R.drawable.shoot2 );
        shot3 = BitmapFactory.decodeResource( res, R.drawable.shoot3 );
        shot4 = BitmapFactory.decodeResource( res, R.drawable.shoot4 );
        shot5 = BitmapFactory.decodeResource( res, R.drawable.shoot5 );

        shot1 = Bitmap.createScaledBitmap( shot1, width, height, false );
        shot2 = Bitmap.createScaledBitmap( shot2, width, height, false );
        shot3 = Bitmap.createScaledBitmap( shot3, width, height, false );
        shot4 = Bitmap.createScaledBitmap( shot4, width, height, false );
        shot5 = Bitmap.createScaledBitmap( shot5, width, height, false );

        dead=BitmapFactory.decodeResource( res,R.drawable.dead );
        dead=Bitmap.createScaledBitmap( dead,width,height,false );


        y = screenY / 2;
        x = (int) (64 * screenRatioX);

//        Log.d( "xxxxx", x + "" );
//        Log.d( "yyyyy", y + "" );

    }

    Bitmap getFlight() {

        if (toShoot != 0) {
            if (shootCounter == 1) {
                shootCounter++;
                return shot1;
            }
            if (shootCounter == 2) {
                shootCounter++;
                return shot2;
            }
            if (shootCounter == 3) {
                shootCounter++;
                return shot3;
            }
            if (shootCounter == 4) {
                shootCounter++;
                return shot4;
            }
            shootCounter = 1;
            toShoot--;
            gameView.newBullet();
            return shot5;
        }
        if (wingCounter == 0) {
            wingCounter++;
            return flight1;
        }
        wingCounter--;
        return flight2;
    }
    Rect getCollisionShape () {
        return new Rect(x, y, x + width, y + height);
    }

    Bitmap getDead(){
        return dead;
    }

}
