package com.liveitandroid.liveit;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;

import java.io.File;

public class RecordingService extends Service {
    public RecordingService() {
    }

    //    private boolean timeUp = false;
    private static final String TAG_FOREGROUND_SERVICE = "RECORDING_SERVICE";
    private final int NOTIFICATION_ID = (int) System.currentTimeMillis();

    public static final String STREAMING_URL = "com.liveitandroid.liveit.RecordingService.STREAMING_URL";
    public static final String RECORDING_FILE_NAME = "com.liveitandroid.liveit.RecordingService.RECORDING_FILE_NAME";
    public static final String RECORDING_DURATION = "com.liveitandroid.liveit.RecordingService.RECORDING_DURATION";

    //    Recorder recorder;
    private File recordingDirectory;

    private String appName = "";
    private int recordingDuration = 0; // minutes
    private final String CHANNEL_ID = "channel_9381";// The id of the channel.

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Log.d(TAG_FOREGROUND_SERVICE, "My foreground service onCreate().");

        appName = getResources().getString(R.string.app_name);
        recordingDirectory = new Prefrences(this).getRecordingDir();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "A gravação começou...", Toast.LENGTH_LONG).show();
        createNotificationChannel(null);
        startForeground(NOTIFICATION_ID, getNotification("A preparar gravação..."));

        final String urlToStream = intent.getStringExtra(STREAMING_URL);
        recordingDuration = intent.getIntExtra(RECORDING_DURATION, 0);
        String recordingFileName = intent.getStringExtra(RECORDING_FILE_NAME);
        //Log.d(TAG_FOREGROUND_SERVICE, "recording: " + urlToStream);
        initFfmpeg(urlToStream, new File(recordingDirectory, recordingFileName));

        return super.onStartCommand(intent, flags, startId);
    }

    /* Used to build and start foreground service. */
    private Notification getNotification(String text)
    {
        // Create notification builder.
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle(appName + " - Tempo: " + recordingDuration + " min");
        builder.setContentText(text);

        // Disable sound
        builder.setDefaults(0);
        builder.setSound(null);

        builder.setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setChannelId(CHANNEL_ID);


        return builder.build();
    }

    private void updateNotification(String text) {
        Notification notification = getNotification(text);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        createNotificationChannel(mNotificationManager);

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }


    private void createNotificationChannel(NotificationManager mNotificationManager){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if(mNotificationManager == null)
                mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, "Channel_Name-test", NotificationManager.IMPORTANCE_LOW);
            mChannel.setLightColor(Color.BLUE);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }
    FFmpeg ffmpeg;
    private void initFfmpeg(final String urlToStream, final File recordingFile){
        ffmpeg = FFmpeg.getInstance(this);
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {

                @Override
                public void onStart() {
                    log("Ffmpeg started\n");
                }

                @Override
                public void onFailure() {
                    //log("Ffmpeg failed to start\n");
                    toast("Falha ao começar a gravação. Tente com internet mais estável.");
                    stopForegroundService();
                }

                @Override
                public void onSuccess() {
                    log("Ffmpeg success\n");
                }

                @Override
                public void onFinish() {
                    // "-bsf:a", "aac_adtstoasc" => it is added to fix aac audio issue
                    // -framerate 5 (170 was good but still issues)
//                    String tmp = urlToStream.replace("https", "udp");
//                    tmp = urlToStream.replace("http", "udp");
//                    Log.d(TAG_FOREGROUND_SERVICE, "Recording: " + tmp);
                    // ffmpeg -i rtsp://192.168.XXX.XXX:554/live.sdp -vcodec copy -acodec copy -f mp4 -y MyVideoFFmpeg.mp4
//                    String[] cmd = {"-use_wallclock_as_timestamps", "1", "-i", urlToStream, "-t", String.valueOf(recordingDuration*60), "-c", "copy", "-y", "-bsf:a", "aac_adtstoasc", "-f", "mp4", recordingFile.getAbsolutePath()};
                    // ffmpeg -i rtsp://192.168.XXX.XXX:554/live.sdp -b 900k -vcodec copy -r 60 -y MyVdeoFFmpeg.avi
                    String[] cmd = { "-i", urlToStream, "-t", String.valueOf(recordingDuration*60), "-b", "900k", "-vcodec", "copy", "-r", "30", "-y", recordingFile.getAbsolutePath()};
                    FfmpegExecCmd(cmd);
                    //log("Ffmpeg finished\n");
                }
            });
        } catch (FFmpegNotSupportedException e) {
            e.printStackTrace();
            //log("Ffmpeg excep:\n" + e.getMessage() + "\n");
        }
    }


    private void FfmpegExecCmd(String[] cmd){
        try {
            // to execute "ffmpeg -version" command you just need to pass "-version"
            ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler() {

                @Override
                public void onStart() {
                    //log("Command started: ");
                    updateNotification("A carregar...");
                }

                @Override
                public void onProgress(String message) {
                    log("Progresso: " + message);
                    updateProgress(message);
                }

                @Override
                public void onFailure(String message) {
                    //log("Command-Failed: " + message);
                    toast("Erro a gravar. Tente com internet mais estável.");
                    stopForegroundService();
                }

                @Override
                public void onSuccess(String message) {
                    //log("Command-Success: " + message);
                    toast("Gravação concluída.");
                    stopForegroundService();
                }

                @Override
                public void onFinish() {
                    //log("Command Finished");
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // Handle if FFmpeg is already running
        }
    }


    private void updateProgress(String msg){
        try{
            if(msg.contains("time=")){
                int index = msg.indexOf("time=");
                updateNotification("A gravar " + msg.substring(index+5, index+17));
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void toast(String msg){
        Toast.makeText(RecordingService.this, msg, Toast.LENGTH_SHORT).show();
    }

    private void log(String log){
//        logs.append(log + "\n");
    }


    private void stopForegroundService()
    {
        // Stop foreground service and remove the notification.
        stopForeground(true);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);

        // Stop the foreground service.
        stopSelf();
    }


}
