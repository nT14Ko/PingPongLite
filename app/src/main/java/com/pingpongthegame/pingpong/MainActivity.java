package com.pingpongthegame.pingpong;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    Boolean stateAudio;
    ImageButton audioIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        audioIb = findViewById(R.id.ibAudio);
        sharedPreferences = getSharedPreferences("my_pref",0);
        stateAudio = sharedPreferences.getBoolean("audioState", true);
        if(stateAudio){
            audioIb.setImageResource(R.drawable.a_on);
        }else{
            audioIb.setImageResource(R.drawable.a_off);
        }
    }

    public void startGame(View view) {
        GameView gameView = new GameView(this);
        setContentView(gameView);
    }

    public void audioPref(View view) {
        if(stateAudio){
            stateAudio = false;
            audioIb.setImageResource(R.drawable.a_off);
        }else{
            stateAudio = true;
            audioIb.setImageResource(R.drawable.a_on);
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("audioState", stateAudio);
        editor.commit();
    }
}