package com.tvscs.ilead.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.tvscs.ilead.helper.SharedPreference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import ilead.tvscs.com.ilead.BuildConfig;

public class ApkFileDownload extends AsyncTask<String, String, String> {

    private final static String charset = "UTF-8";
    private static HttpURLConnection urlConnection;
    public ProgressDialog pDialog;
    SharedPreference mPrefs;
    private String Name = null;
    private Context context;

    public ApkFileDownload(Context ctx, String name) {
        context = ctx;
        Name = name;
        mPrefs = new SharedPreference(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Downloading file. Please wait…");
        pDialog.setIndeterminate(false);
        pDialog.setMax(100);
        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pDialog.setCancelable(false);
        pDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {
        String result = "";
        try {

            if (Name.equalsIgnoreCase("downloadupdate")) {
                if (params[0].length() > 0) {
                    URL url = new URL(params[0]);
                    HttpURLConnection c = (HttpURLConnection) url
                            .openConnection();
                    c.setRequestMethod("GET");
                    c.setDoOutput(false);
                    c.connect();
                    String PATH = Environment.getExternalStorageDirectory()
                            + "/Download/";
                    File file = new File(PATH);
                    file.mkdirs();
                    File outputFile = new File(file,
                            Constants.UpdateApkFilename);
                    if (outputFile.exists()) {
                        outputFile.delete();
                    }
                    FileOutputStream fos1 = new FileOutputStream(outputFile);
                    int status = c.getResponseCode();
                    InputStream is = c.getInputStream();
                    int fileLength = c.getContentLength();
                    long total = 0;
                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    while ((len1 = is.read(buffer)) != -1) {
                        total += len1;
                        String percentage = ""
                                + (int) ((total * 100) / fileLength);
                        publishProgress(percentage);
                        fos1.write(buffer, 0, len1);
                    }
                    fos1.close();
                    is.close();
                    File toInstall = new File(Environment.getExternalStorageDirectory() + "/Download/" + Constants.UpdateApkFilename);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        try {
                            Uri apkUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", toInstall);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(apkUri);
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            mPrefs.checkLogin();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            Uri apkUri = Uri.fromFile(toInstall);
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                            mPrefs.checkLogin();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "EXCEPTION";
        }

        return result;
    }

    /**
     * Updating progress bar
     */
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
        pDialog.setProgress(Integer.parseInt(progress[0]));
    }

    @Override
    protected void onPostExecute(String result) {
        pDialog.dismiss();
    }

}
