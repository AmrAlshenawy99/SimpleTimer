package com.amr.timer;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    SeekBar timerSeekBar;
    TextView timerTv;
    CountDownTimer countDownTimer;
    Button btnStart;
    Boolean counterIsActive = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        controlSeekBar();


    }
    //----------------------------------------------------------------------------------------------------------
    public void startTimer(View v) {//using countdown class


        if (counterIsActive == false) {
            //so that layout doesn't change if timer is 0
            if (timerSeekBar.getProgress() != 0) {

                updateLayout("reset", false, true, R.drawable.btnpause);
            }
            //The main idea to loop with known time (1sec) and update the timerTv by the next sec every onTick (so it seems like counting down)
            countDownTimer = new CountDownTimer(timerSeekBar.getProgress() * 1000 + 100, 1000) {

                @Override
                public void onTick(long secondUntilFinishing) {
                    updateTime((int) secondUntilFinishing / 1000);

                }

                @Override
                public void onFinish() {
                    //so that not making voice if button is pressed at 0
                    if (timerSeekBar.getProgress() != 0) {
                        playMedia();
                    }
                    resetTimer();
                }
            }.start();

        } else {
            //if the button is pressed while timer count
            countDownTimer.cancel();
            resetTimer();
        }
    }


    //----------------------------------------------------------------------------------------------------
    public void controlSeekBar() {
        btnStart = findViewById(R.id.btnStart);
        timerSeekBar = findViewById(R.id.timerSb);
        timerTv = findViewById(R.id.timerTv);
        timerSeekBar.setMax(1800);// consider it have 3600 second = 1h
        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                updateTime(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


    }
    //----------------------------------------------------------------------------------------------------
    public void updateTime(int newTimeValue) {

        int mnt = newTimeValue / 60;
        int sec = newTimeValue % 60;
        String secString = Integer.toString(sec);
        if (sec < 10) {
            secString = "0" + secString;
        }
        timerTv.setText(Integer.toString(mnt) + ":" + secString);

    }
    //----------------------------------------------------------------------------------------------------
    public void updateLayout(String btnStart, boolean enableSeekBar, boolean counterActive, int btnBg) {

        this.btnStart.setText(btnStart);
        timerSeekBar.setEnabled(enableSeekBar);
        counterIsActive = counterActive;
        this.btnStart.setBackgroundResource(btnBg);


    }
    //----------------------------------------------------------------------------------------------------
    public  void resetTimer(){
        updateLayout("start", true, false, R.drawable.btnstart);
        timerTv.setText("0:00");
        timerSeekBar.setProgress(0);
    }
    //----------------------------------------------------------------------------------------------------
    public  void playMedia(){
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
        mediaPlayer.start();
    }
}
