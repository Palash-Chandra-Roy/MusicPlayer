package com.example.music_player;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.icu.text.Transliterator;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    Button btn_paush,btn_next,btn_previous;
    TextView songTextLabel;
    SeekBar songSeekBer;
    String sName;

    static MediaPlayer myMediaPlayer;
    int position;

    ArrayList<File> mySongs;

    Thread updateSeekBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        btn_paush = (Button) findViewById(R.id.paushId);
        btn_next = (Button) findViewById(R.id.nextId);
        btn_previous = (Button) findViewById(R.id.previousId);

        songTextLabel = (TextView) findViewById(R.id.songTextLabelId);
        songSeekBer = (SeekBar) findViewById(R.id.seekBirId);

        getSupportActionBar().setTitle("Not Playing");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        updateSeekBar = new Thread() {

            @Override
            public void run() {
                int totalDuration = myMediaPlayer.getDuration();
                int currentPrositon = 0;

                while (currentPrositon < totalDuration) {
                    try {
                        sleep(500);
                        currentPrositon = myMediaPlayer.getCurrentPosition();
                        songSeekBer.setProgress(currentPrositon);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        };

        if(myMediaPlayer !=null) {
            myMediaPlayer.stop();
            myMediaPlayer.release();

        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();


        mySongs = (ArrayList) bundle.getParcelableArrayList("song");
        sName= mySongs.get(position).getName().toString();

        String songName =i.getStringExtra("songName");
        songTextLabel.setText(songName);
        songTextLabel.setSelected(true);

        position = bundle.getInt("pos",0);

        Uri u = Uri.parse(mySongs.get(position).toString());

        myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
        myMediaPlayer.start();

        songSeekBer.setMax(myMediaPlayer.getDuration());

        updateSeekBar.start();

        songSeekBer.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        songSeekBer.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);

        songSeekBer.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMediaPlayer.seekTo(seekBar.getProgress());

            }
        });


        btn_paush.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songSeekBer.setMax(myMediaPlayer.getDuration());
                if(myMediaPlayer.isPlaying()){

                    btn_paush.setBackgroundResource(R.drawable.icon_play);
                    myMediaPlayer.pause();

                }
                else {
                    btn_paush.setBackgroundResource(R.drawable.icon_paush);
                    myMediaPlayer.start();
                }

            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer.stop();
                myMediaPlayer.release();

                position=((position+1)%mySongs.size());

                Uri u= Uri.parse(mySongs.get(position).toString());

                myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sName =mySongs.get(position).getName().toString();
                songTextLabel.setText(sName);
                myMediaPlayer.start();


            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMediaPlayer.stop();
                myMediaPlayer.release();

                position = ((position-1)<0)?(mySongs.size()-1):(position-1);
                Uri u= Uri.parse(mySongs.get(position).toString());

                myMediaPlayer=MediaPlayer.create(getApplicationContext(),u);
                sName =mySongs.get(position).getName().toString();
                songTextLabel.setText(sName);
                myMediaPlayer.start();

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }
}
