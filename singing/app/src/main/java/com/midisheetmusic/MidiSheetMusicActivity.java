/*
 * Copyright (c) 2011-2012 Madhav Vaidyanathan
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License version 2.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 */

package com.midisheetmusic;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.graphics.*;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

/**
 * @class MidiSheetMusicActivity
 * This is the launch activity for MidiSheetMusic.
 * It simply displays the splash screen, and a button to choose a song.
 */
public class MidiSheetMusicActivity extends Activity {

    ImageView iv_background;
    VideoView vv_background;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadImages();
        setContentView(R.layout.main);

        RelativeLayout rv_parent = (RelativeLayout) findViewById(R.id.rv_parent);
        rv_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseSong();
            }
        });

        vv_background = (VideoView) findViewById(R.id.vv_background);
        Uri video = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.produce);
        vv_background.setMediaController(new MediaController(this));
        vv_background.setVideoURI(video);
        vv_background.start();

        vv_background.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseSong();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    /**
     * Start the ChooseSongActivity when the "Choose Song" button is clicked
     */
    private void chooseSong() {
        Intent intent = new Intent(this, AllSongsActivity.class);
        startActivity(intent);
    }

    /**
     * Load all the resource images
     */
    private void loadImages() {
        ClefSymbol.LoadImages(this);
        TimeSigSymbol.LoadImages(this);
        MidiPlayer.LoadImages(this);
    }

    /**
     * Always use landscape mode for this activity.
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MidiSheetMusic Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.midisheetmusic/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "MidiSheetMusic Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.midisheetmusic/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }
}

