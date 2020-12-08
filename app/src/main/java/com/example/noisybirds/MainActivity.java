package com.example.noisybirds;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    private boolean isMute;
    private Button btnShowScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        Paper.init(this);

        getWindow().setFlags( WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView( R.layout.activity_main );

        findViewById( R.id.play ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent( MainActivity.this, GameActivity.class ) );
            }
        } );

        btnShowScore=findViewById( R.id.btnShowScore );
        btnShowScore.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent(MainActivity.this,ScoreActivity.class) );
            }
        } );

        TextView highScoretxt = findViewById( R.id.txtHighScore );
        int score=Paper.book().read( "score",0 );
        highScoretxt.setText( "HighScore:"+ score);
//        SharedPreferences sharedPreferences = getSharedPreferences( "game", MODE_PRIVATE );
//        highScoretxt.setText("HighScore: " + sharedPreferences.getInt( "highscore", 0 ) );

//        isMute=sharedPreferences.getBoolean( "isMute",false );

        isMute=Paper.book().read( "isMute" ,false);

        ImageView volumCtrl=findViewById( R.id.volumCtrl );
        if(isMute)
            volumCtrl.setImageResource( R.drawable.ic_volume_off );
        else
            volumCtrl.setImageResource( R.drawable.ic_volume_up );

        volumCtrl.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isMute= !isMute;
                if(isMute)
                    volumCtrl.setImageResource( R.drawable.ic_volume_off );
                else
                    volumCtrl.setImageResource( R.drawable.ic_volume_up );

                Paper.book().write("isMute", isMute);
            }
        } );

    }
}