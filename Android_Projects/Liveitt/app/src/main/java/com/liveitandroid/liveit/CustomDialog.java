package com.liveitandroid.liveit;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.isabsent.filepicker.SimpleFilePickerDialog;
import com.liveitandroid.liveit.view.nstplayer.NSTPlayerSkyActivity;

public class CustomDialog extends Dialog implements View.OnClickListener {
    public NSTPlayerSkyActivity c;
    public Dialog d;
    public Button startRecBtn, closeBtn, choose_dir;
    EditText fileNameInput, durationInput, filePath;
    String streamUrl;
    Prefrences prefrences;

    public static final String DialogTag = "Pasta para as Gravações";

    public CustomDialog(NSTPlayerSkyActivity a, String streamUrl) {
        super(a);
        this.c = a;
        this.streamUrl = streamUrl;
        prefrences = new Prefrences(a);
    }


    private class OnFocusChangeAccountListener implements View.OnFocusChangeListener {
        private final View view;

        public OnFocusChangeAccountListener(View view) {
            this.view = view;
        }

        @SuppressLint({"ResourceType"})
        public void onFocusChange(View v, boolean hasFocus) {
            float to = 2.0f;
            if (hasFocus) {
                if (hasFocus) {
                    to = 1.09f;
                } else {
                    to = 1.0f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
               /* if (this.view.getTag().equals("3")) {
                    this.view.setBackgroundResource(R.drawable.live_tv_background);
                } else if (this.view.getTag().equals("4")) {
                    this.view.setBackgroundResource(R.drawable.on_demand_background);
                } else if (this.view.getTag().equals("5")) {
                    this.view.setBackgroundResource(R.drawable.catch_background);
                }*/

                this.view.setBackgroundResource(R.drawable.live_tv_background);
            } else if (!hasFocus) {
                if (hasFocus) {
                    to = 1.09f;
                } else {
                    to = 1.0f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                performAlphaAnimation(hasFocus);
                if (this.view.getTag().equals("3")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_01);
                } else if (this.view.getTag().equals("4")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_02);
                } else if (this.view.getTag().equals("5")) {
                    this.view.setBackgroundResource(R.drawable.background_color_gradient_03);
                }
            }
        }

        private void performScaleXAnimation(float to) {
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(this.view, "scaleX", to);
            scaleXAnimator.setDuration(150);
            scaleXAnimator.start();
        }

        private void performScaleYAnimation(float to) {
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(this.view, "scaleY", to);
            scaleYAnimator.setDuration(150);
            scaleYAnimator.start();
        }

        private void performAlphaAnimation(boolean hasFocus) {
            if (hasFocus) {
                float toAlpha = hasFocus ? 0.6f : 0.5f;
                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this.view, "alpha", toAlpha);
                alphaAnimator.setDuration(150);
                alphaAnimator.start();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custome_dailog);
        startRecBtn = findViewById(R.id.btn_start_recording);
        closeBtn = findViewById(R.id.btn_close);
        choose_dir = findViewById(R.id.choose_rec_dir);
        fileNameInput = findViewById(R.id.file_name_input);
        durationInput = findViewById(R.id.recording_duration_input);
        filePath = findViewById(R.id.file_location);

        makeButtonsClickable();
        fileNameInput.setText(System.currentTimeMillis()+"");
        this.startRecBtn.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.startRecBtn));
        this.closeBtn.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.closeBtn));
        this.choose_dir.setOnFocusChangeListener(new OnFocusChangeAccountListener(this.choose_dir));
        this.fileNameInput.requestFocus();
        showRecDir();
    }

    private void makeButtonsClickable() {
        this.startRecBtn.setOnClickListener(this);
        this.closeBtn.setOnClickListener(this);
        this.choose_dir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_recording:
                try{
                    String fileName = fileNameInput.getText().toString().trim();
                    String duration = durationInput.getText().toString().trim();

                    if(fileName == "" || duration.equals("") || Integer.parseInt(duration) <= 0){
                        Toast.makeText(c, "Por favor inserir um nome válido e uma duração.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    if(fileName.contains(" ") || fileName.contains("?") || fileName.contains("."))
                    {
                        Toast.makeText(c, "Por favor inserir um nome válido.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    fileName = fileName+".mp4";
                    startRecordingService(fileName, Integer.parseInt(duration));
                }
                catch (Exception e){
                    Toast.makeText(c, "Error !!", Toast.LENGTH_LONG).show();
                    dismiss();
                    e.printStackTrace();
                }
                break;
            case R.id.bt_close:
                dismiss();
                break;
            case R.id.choose_rec_dir:
                c.showListItemDialog(
                        R.string.dialog_title,
                        "/storage/emulated/0/",
                        SimpleFilePickerDialog.CompositeMode.FOLDER_ONLY_SINGLE_CHOICE,
                        DialogTag
                );
                break;
            default:
                break;
        }
        dismiss();
    }


    private void startRecordingService(String fileName, int duration){
        c.startService(
                new Intent(c, RecordingService.class)
                        .putExtra(RecordingService.STREAMING_URL, streamUrl)
                        .putExtra(RecordingService.RECORDING_FILE_NAME, fileName)
                        .putExtra(RecordingService.RECORDING_DURATION, duration)
        );
    }


    public void showRecDir(){
        filePath.setText(prefrences.getRecordingDir().getAbsolutePath());
    }

}
