package com.wuttinunt.led;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class MainActivity extends Activity {

    EditText writeAPI;
    EditText codeCommand;
    ProgressBar mProgressBar;
    CountDownTimer mCountDownTimer;
    Boolean check = false;
    int i = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btSend (View v) {

        final Button button = (Button)findViewById(R.id.send);


        i = 0;
        writeAPI = (EditText) findViewById(R.id.txtWriteAPI);
        codeCommand = (EditText) findViewById(R.id.txtCode);

        String s = writeAPI.getText().toString();
        String c = codeCommand.getText().toString();



        if (s.isEmpty() || s == "Your Write API Key" || s == "") {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Please insert API Key !!!");
                alert.setCancelable(false);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            alert.show();
        }else if(!s.isEmpty() && s.length() != 16){
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("API Key format is not valid !!!");
                alert.setCancelable(false);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();

        }

        else if(c.isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Please insert your Code !!!");
                alert.setCancelable(false);
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();
                check = false;
        }

        else {
            check = true;



            OkHttpClient okHttpClient = new OkHttpClient();
            Request.Builder builder = new Request.Builder();
            Request request = builder.url("https://api.thingspeak.com/update?api_key=" + s + "&field1=" + c).build();
            /////
            if(check == true) {

                mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
                mProgressBar.setProgress(i);
                mCountDownTimer = new CountDownTimer(15000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        Log.v("Log_tag", "Tick of Progress" + i + millisUntilFinished);
                        i++;
                        mProgressBar.setProgress(i);
                        button.setEnabled(false);
                        writeAPI.setEnabled(false);
                        codeCommand.setEnabled(false);



                    }

                    @Override
                    public void onFinish() {
                        //Do what you want
                        button.setEnabled(true);
                        writeAPI.setEnabled(true);
                        codeCommand.setEnabled(true);
                        i++;
                        mProgressBar.setProgress(i);

                    }
                };
                mCountDownTimer.start();
            }
            ////

            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    updateView("Error - " + e.getMessage());
                }

                @Override
                public void onResponse(Response response) {
                    if (response.isSuccessful()) {
                        try {
                            updateView(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                            updateView("Error - " + e.getMessage());
                        }
                    } else {
                        updateView("Not Success - code : " + response.code());
                    }
                }

                public void updateView(final String strResult) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {



                        }
                    });
                }
            });


        }
    }





}
