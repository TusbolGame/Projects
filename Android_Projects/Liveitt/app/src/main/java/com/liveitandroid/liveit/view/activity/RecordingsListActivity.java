package com.liveitandroid.liveit.view.activity;

import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.isabsent.filepicker.SimpleFilePickerDialog;
import com.liveitandroid.liveit.Prefrences;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.view.nstplayer.NSTPlayerVodActivity;
import com.liveitandroid.liveit.view.utility.UtilsMethods;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecordingsListActivity extends AppCompatActivity implements SimpleFilePickerDialog.InteractionListenerInt {

    @BindView(R.id.appbar_toolbar)
    AppBarLayout appbarToolbar;
    @BindView(R.id.pb_loader)
    ProgressBar progressBar;
    @BindView(R.id.recordings_list)
    ListView recordingsListView;
    @BindView(R.id.tv_noRecording)
    TextView noRecordLabel;
    @BindView(R.id.recording_path)
    TextView filePath;  //Recording will be saved in:


    File recordingDir;
    File[] recordedFiles;
    RecordingsAdapter listAdapter;
    Prefrences prefrences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordings_list);

        ButterKnife.bind(this);
        prefrences = new Prefrences(this);
        if (this.appbarToolbar != null) {
            this.appbarToolbar.setBackground(getResources().getDrawable(R.drawable.layout_background_tv));
        }
        changeStatusBarColor();
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        recordingDir = prefrences.getRecordingDir();
        new loadFiles().execute(recordingDir);

        recordingsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showPopup(view, position);
            }
        });

        findViewById(R.id.change_dir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListItemDialog(
                        R.string.dialog_title,
                        "/storage/emulated/0/",
                        SimpleFilePickerDialog.CompositeMode.FOLDER_ONLY_SINGLE_CHOICE,
                        DialogTag
                );
            }
        });

        showRecDir();
    }

    private final int REQUEST_CODE = 9999;
    private final String DialogTag = "Pasta de Gravação";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            String selectedPath = data.getData().getPath();
            File tmpFile = new File(selectedPath);
            boolean tmp1 = tmpFile.exists();
            showRecDir();
        }
    }


    private void showRecDir(){
        filePath.setText("Gravar em: " + prefrences.getRecordingDir().getAbsolutePath());
    }

    @Override
    public void showListItemDialog(int titleResId, String folderPath, SimpleFilePickerDialog.CompositeMode mode, String dialogTag) {
        SimpleFilePickerDialog.build(folderPath, mode)
                .title(titleResId)
                .show(this, dialogTag);
    }

    @Override
    public boolean onResult(@NonNull String dialogTag, int which, @NonNull Bundle extras) {
        switch (dialogTag) {
            case DialogTag:
                if (extras.containsKey(SimpleFilePickerDialog.SELECTED_SINGLE_PATH)){
                    // TODO: save
                    prefrences.setRecordingPath(extras.getString(SimpleFilePickerDialog.SELECTED_SINGLE_PATH));
                    try{ Thread.sleep(100); }
                    catch (Exception e){e.printStackTrace();}
                    showRecDir();
                }
//                else if (extras.containsKey(SimpleFilePickerDialog.SELECTED_PATHS)) {
//                    //Do what you want with multiple selection
//                }
                break;
        }
        return false;
    }

    private class loadFiles extends AsyncTask<File, Void, Void>{

        @Override
        protected Void doInBackground(File... recordingDir) {
            recordedFiles = recordingDir[0].listFiles();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            listFiles();
        }
    }



    private void listFiles(){
        try{
            if(recordedFiles.length > 0){
                if(listAdapter == null) {
                    listAdapter = new RecordingsAdapter();
                    recordingsListView.setAdapter(listAdapter);
                }
                else{
                    listAdapter.notifyDataSetChanged();
                }
                noRecordLabel.setVisibility(View.INVISIBLE);
            }
            else{
                if (listAdapter != null)
                    listAdapter.notifyDataSetChanged();
                noRecordLabel.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    private void changeStatusBarColor() {
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= 19) {
            window.clearFlags(67108864);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            window.addFlags(Integer.MIN_VALUE);
        }
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));
        }
    }

    public void showPopup(View v, final int itemIndex) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.recording_menu, popup.getMenu());
        popup.show();

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.action_play){
//                    startRecording();

//                    Intent VlcPlayerIntent = new Intent(RecordingsListActivity.this, VLCPlayerVodActivity.class);
//                    VlcPlayerIntent.putExtra("OPENED_STREAM_ID", "");
//                    VlcPlayerIntent.putExtra("STREAM_TYPE", "");
//                    VlcPlayerIntent.putExtra("CONTAINER_EXTENSION", "");
//                    VlcPlayerIntent.putExtra("PLAY_RECORDING", true);
//                    VlcPlayerIntent.putExtra("FILE_PATH", recordedFiles[itemIndex].getAbsolutePath());
//                    startActivity(VlcPlayerIntent);

                    File clickedFile = recordedFiles[itemIndex];
                    Intent NSTPlayerVodIntent = new Intent(RecordingsListActivity.this, NSTPlayerVodActivity.class);
                    NSTPlayerVodIntent.putExtra("VIDEO_ID", "");
                    NSTPlayerVodIntent.putExtra("VIDEO_TITLE", clickedFile.getName());
                    NSTPlayerVodIntent.putExtra("EXTENSION_TYPE", "");
                    NSTPlayerVodIntent.putExtra("VIDEO_NUM", itemIndex);
                    NSTPlayerVodIntent.putExtra("PLAY_RECORDING", true);
                    NSTPlayerVodIntent.putExtra("FILE_PATH", recordedFiles[itemIndex].getAbsolutePath());
                    startActivity(NSTPlayerVodIntent);
                }
                else if(item.getItemId() == R.id.action_delete){
                    if(itemIndex < recordedFiles.length){
                        recordedFiles[itemIndex].delete();
                        recordedFiles = recordingDir.listFiles();
                        listFiles();
                    }
                }
                return false;
            }
        });
    }



    private class RecordingsAdapter extends BaseAdapter{

        SimpleDateFormat dateFormat;
        SimpleDateFormat timeFormat;

        public RecordingsAdapter() {
            this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            this.timeFormat = new SimpleDateFormat("hh:mm:ss");
        }

        @Override
        public int getCount() {
            return recordedFiles.length;
        }

        @Override
        public Object getItem(int position) {
            return recordedFiles[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;

            if(view == null)
                view = LayoutInflater.from(RecordingsListActivity.this).inflate(R.layout.recording_list_item, parent, false);

            File currentFile = recordedFiles[position];

            ((TextView) view.findViewById(R.id.recording_name)).setText(recordedFiles[position].getName());
            ((TextView) view.findViewById(R.id.file_size)).setText(getFileSizeMegaBytes(currentFile));
            ((TextView) view.findViewById(R.id.created_date)).setText(format(currentFile.lastModified()));
            ((TextView) view.findViewById(R.id.duration)).setText(getVideoLength(currentFile));
            return view;
        }

        private String format(long time){
            return dateFormat.format(new Date(time));
        }

        private String getFileSizeMegaBytes(File file) {
            try{
                return String.format("%.2f", (double) file.length() / (1024 * 1024)) + " MB";
            }
            catch (Exception e){
                e.printStackTrace();
                return "0 MB";
            }
        }

        private String getVideoLength(File videoFile){
            try{
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                //use one of overloaded setDataSource() functions to set your data source
                retriever.setDataSource(RecordingsListActivity.this, Uri.fromFile(videoFile));
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                long timeInMillisec = Long.parseLong(time);
                retriever.release();

                return UtilsMethods.getTime(timeInMillisec);
            }
            catch (Exception e){
                e.printStackTrace();
                return "00:00";
            }
        }
    }

}
