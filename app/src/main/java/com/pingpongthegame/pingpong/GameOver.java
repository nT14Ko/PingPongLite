package com.pingpongthegame.pingpong;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class GameOver extends AppCompatActivity {

    TextView tPoints;
    TextView tHighest;
    SharedPreferences preferences;
    ImageView ivNewHighest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_over);
        tPoints = findViewById(R.id.tvPoints);
        tHighest = findViewById(R.id.tvHighest);
        ivNewHighest = findViewById(R.id.ivNewHighest);
        int points = getIntent().getExtras().getInt("points");
        tPoints.setText(""+points);
        preferences = getSharedPreferences("my_pref", 0);
        int highest = preferences.getInt("highest", 0);
        if(points > highest){
            ivNewHighest.setVisibility(View.VISIBLE);
            highest = points;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("highest", highest);
            editor.commit();
        }
        tHighest.setText(""+highest);
    }

    public void restart(View view) {
        Intent intent = new Intent(GameOver.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void exit(View view) {
        finish();
    }
}
