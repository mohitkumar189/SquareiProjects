package com.app.lunavista.activity;

import android.app.Activity;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;


import com.app.lunavista.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Random;

/**
 * Created by admin on 13-05-2016.
 */
public class DownLoadFile extends IntentService {

    private int result = Activity.RESULT_CANCELED;
    public static final String URL = "urlpath";
    public static final String FILENAME = "filename";
    public static final String FILEPATH = "filepath";
    public static final String RESULT = "result";
    Context context;
    public static final String NOTIFICATION = "service receiver";
    ProgressDialog dialog;

    public DownLoadFile() {

        super("DownLoadFile");
        context = this;

    }

    // Will be called asynchronously by OS.
    @Override
    protected void onHandleIntent(Intent intent) {
        String urlPath = intent.getStringExtra(URL);
        String fileName = intent.getStringExtra(FILENAME);

        Log.e("File path", urlPath);
        // new AsyncDownloadFile(context, urlPath, fileName);
        new DownloadFileFromURL(context, urlPath, fileName);
    }

    private void publishResults(String outputPath, int result) {
        //showNotification(outputPath);
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra(FILEPATH, outputPath);
        intent.putExtra(RESULT, result);
        sendBroadcast(intent);
    }

    public void showNotification(String FILEPATH) {

        Log.e("Local file path", FILEPATH);
        File file = new File(FILEPATH);
        MimeTypeMap map = MimeTypeMap.getSingleton();
        String ext = MimeTypeMap.getFileExtensionFromUrl(file.getName());
        String type = map.getMimeTypeFromExtension(ext);

        if (type == null)
            type = "*/*";

        Intent notificationIntent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.fromFile(file);

        notificationIntent.setDataAndType(data, type);

        //  startActivity(notificationIntent);
        // Intent notificationIntent = new Intent(Intent.ACTION_PACKAGE_INSTALL, Uri.parse(downloadFilepath));
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //   notification.setLatestEventInfo(context, title, message, intent);

        // Intent intent = new Intent(this, NotificationReceiver.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(context, (int) System.currentTimeMillis(), notificationIntent, 0);
        Random r = new Random();
        int when = r.nextInt(1000);
// build notification
// the addAction re-use the same intent to keep the example short
        Notification notification = new Notification.Builder(context)
                .setContentTitle("CCMH Notification")
                .setContentText("Download completed")
                .setSmallIcon(R.drawable.close)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;

        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");

        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(when, notification);
    }


    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */

        String urlPath = "";
        String fileName = "";

        public DownloadFileFromURL(Context context, String url, String fileName) {
            // TODO Auto-generated constructor stub
            this.urlPath = url;
            this.fileName = fileName;
            this.execute(new String[]{});
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // dialog = ProgressDialog.show(context, "", "Processing...");
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(urlPath);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                //   OutputStream output = new FileOutputStream("/sdcard/downloadedfile.jpg");
                OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/" + fileName);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();
                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded

            // Displaying downloaded image into image view
            // Reading image path from sdcard
            String imagePath = Environment.getExternalStorageDirectory().toString() + "/" + fileName;
            publishResults(imagePath, Activity.RESULT_OK);
            if (dialog != null)
                dialog.cancel();
            // setting downloaded into image view
        }

    }


}