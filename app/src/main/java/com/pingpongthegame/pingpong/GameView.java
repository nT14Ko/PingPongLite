package com.pingpongthegame.pingpong;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Handler;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import java.util.Random;

public class GameView extends View {
    Context context;
    float bX, bY;
    Velocity velocity = new Velocity(25, 32);
    Handler handl;
    final long UPDATE_MILLIS = 30;
    Runnable runnable;
    Paint tPaint = new Paint();
    Paint hPaint = new Paint();
    float TEXT_SIZE = 120;
    float pX, pY;
    float oldX, oPaddleX;
    int score = 0;
    int life = 3;
    Bitmap ball, paddle;
    int dWidth, dHeight;
    MediaPlayer mpHit, mpMiss;
    Random random;
    SharedPreferences sharedPreferences;
    Boolean stateAudio;

    public GameView(Context context) {
        super(context);
        this.context = context;
        ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        paddle = BitmapFactory.decodeResource(getResources(), R.drawable.paddle);
        handl = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        };
        mpHit = MediaPlayer.create(context, R.raw.hit);
        mpMiss = MediaPlayer.create(context, R.raw.miss);
        tPaint.setColor(Color.RED);
        tPaint.setTextSize(TEXT_SIZE);
        tPaint.setTextAlign(Paint.Align.LEFT);
        hPaint.setColor(Color.GREEN);
        Display display = ((Activity) getContext()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        dWidth = size.x;
        dHeight = size.y;
        random = new Random();
        bX = random.nextInt(dWidth);
        pY = (dHeight * 4) / 5;
        pX = dWidth / 2 - paddle.getWidth() /2;
        sharedPreferences = context.getSharedPreferences("my_pref", 0);
        stateAudio = sharedPreferences.getBoolean("audioState", true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.BLACK);
        bX += velocity.getX();
        bY += velocity.getY();
        if((bX >= dWidth - ball.getWidth()) || bX <= 0){
            velocity.setX(velocity.getX() * -1);
        }
        if(bY <= 0){
            velocity.setY(velocity.getY() * -1);
        }
        if(bY > pY + paddle.getHeight()){
            bX = 1 + random.nextInt(dWidth - ball.getWidth() -1);
            bY = 0;
            if(mpMiss != null && stateAudio){
                mpMiss.start();
            }
            velocity.setX(xVelocity());
            velocity.setY(32);
            life--;
            if(life == 0){
                Intent intent = new Intent(context, GameOver.class);
                intent.putExtra("points", score);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        }
        if(((bX +ball.getWidth()) >= pX)
        && (bX <= pX + paddle.getWidth())
        && (bY + ball.getHeight() >= pY)
        && (bY + ball.getHeight() <= pY + paddle.getHeight())){
            if(mpHit != null && stateAudio){
                mpHit.start();
            }
            velocity.setX(velocity.getX() + 1);
            velocity.setY((velocity.getY() + 1) * -1);
            score++;
        }
        canvas.drawBitmap(ball, bX, bY, null);
        canvas.drawBitmap(paddle, pX, pY, null);
        canvas.drawText(""+ score, 20, TEXT_SIZE, tPaint);
        if(life == 2){
            hPaint.setColor(Color.YELLOW);
        }else if(life == 1){
            hPaint.setColor(Color.RED);
        }
        canvas.drawRect(dWidth-200, 30,dWidth - 200 + 60*life, 80, hPaint);
        handl.postDelayed(runnable, UPDATE_MILLIS);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if(touchY >= pY){
            int action = event.getAction();
            if(action == MotionEvent.ACTION_DOWN){
                oldX = event.getX();
                oPaddleX = pX;
            }
            if(action == MotionEvent.ACTION_MOVE){
                float shift = oldX - touchX;
                float newPaddleX = oPaddleX - shift;
                if(newPaddleX <= 0)
                    pX = 0;
                else if(newPaddleX >= dWidth - paddle.getWidth())
                    pX = dWidth - paddle.getWidth();
                else
                    pX = newPaddleX;
            }
        }
        return true;
    }

    private int xVelocity() {
        int[] values = {-35, -30, -25, 25, 30, 35};
        int index = random.nextInt(6);
        return values[index];
    }


}
