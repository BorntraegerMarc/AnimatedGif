package com.borntsch.animatedgif;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaCodec;
import android.media.MediaCrypto;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.webkit.WebView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class AnimationActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_animation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        InputStream stream = null;
        try {
            stream = getAssets().open("piggy.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Falls eine andere View gebraucht werden soll, muss diese hier auskommentiert werden.
        //  GifMovieView view = new GifMovieView(this, stream);
        //  GifDecoderView view = new GifDecoderView(this, stream);

        WebView webView = (WebView) findViewById(R.id.gifWebView);
        webView.loadUrl("file:///android_asset/piggy.gif");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.animation, menu);
        return true;
    }

    /**
     * @param item The menu item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param view
     */
    public void convertGif(View view) {
        long mMoviestart = 0;
        InputStream stream = null;
        try {

            stream = getAssets().open("piggy.gif");


//            File convertedFile = File.createTempFile("bla", ".dat", getDir("filez", 0));
//
//            FileOutputStream out = new FileOutputStream(convertedFile);
//
//            byte[] buffer = new byte[2913];
//            int length = 0;
//            while ((length = stream.read(buffer)) != -1) {
//                out.write(buffer, 0, length);
//            }
//
//            String path = convertedFile.getAbsolutePath();
//            MediaPlayer mediaPlayer = new MediaPlayer();
//            mediaPlayer.setDataSource(path);
//            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
//            mediaPlayer.prepare();
//            mediaPlayer.start();


            MediaMuxer muxer = new MediaMuxer(getFilesDir().getAbsolutePath().toString() + "/temp.mp4", MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            // More often, the MediaFormat will be retrieved from MediaCodec.getOutputFormat()
            // or MediaExtractor.getTrackFormat().
//            MediaCodec mediaCodec = MediaCodec.createDecoderByType("video/mp4v-es");
//
//            mediaCodec.configure(format, null, null, );
//            mediaCodec.start();

            MediaCodec mediaCodec = MediaCodec.createEncoderByType("video/avc");
            MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", 320, 240);
//            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 125000);
//            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 15);
//            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar);
//            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 5);
            mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mediaCodec.start();


            MediaFormat audioFormat = new MediaFormat();
            MediaFormat videoFormat = mediaCodec.getOutputFormat();

            int videoTrackIndex = muxer.addTrack(videoFormat);
            ByteBuffer inputBuffer = ByteBuffer.allocate(stream.read());
            //ByteBuffer inputBuffer = ByteBuffer.allocate(bufferSize);
            boolean finished = false;
            MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();

            muxer.start();
            while (!finished) {
                if (!finished) {
                    muxer.writeSampleData(videoTrackIndex, inputBuffer, bufferInfo);
                    break;
                }
            }
            muxer.stop();
            muxer.release();

            int i = 0;
            i++;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // Movie movie = Movie.decodeStream(stream);

        String filename = "myfile";
        String string = "Hello world!";
        FileOutputStream outputStream;


        try {
            outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(string.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
