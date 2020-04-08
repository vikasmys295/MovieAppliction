package com.example.movieapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.movieapplication.R;

public class PlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // create an object of media controller
        MediaController mediaController = new MediaController(this);
        // initiate a video view
        VideoView simpleVideoView = (VideoView) findViewById(R.id.simpleVideoView);
        // set media controller object for a video view
        simpleVideoView.setMediaController(mediaController);
        Uri uri = Uri.parse("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4");
        simpleVideoView.setVideoURI(uri);
        simpleVideoView.start();
    }
}

