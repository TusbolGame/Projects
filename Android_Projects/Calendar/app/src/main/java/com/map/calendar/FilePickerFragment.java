package com.map.calendar;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.ihhira.android.filechooser.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URI;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilePickerFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilePickerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilePickerFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Button browserButton;
    Button selectButton;
    TextView filepath;

//    private ExoVideoView videoView;
    VideoView videoView;
    TextView textView;
    ImageView imageView;

    Uri fileUri;
    String filePath;
    String fileType;


    private OnFragmentInteractionListener mListener;

    public FilePickerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilePickerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilePickerFragment newInstance(String param1, String param2) {
        FilePickerFragment fragment = new FilePickerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_file_picker, container, false);

        browserButton = (Button)view.findViewById(R.id.browse_button);
        selectButton = (Button)view.findViewById(R.id.select_button);
        filepath = (TextView)view.findViewById(R.id.text_fileuri);

        //videoView = (ExoVideoView) view.findViewById(R.id.videoView);
        videoView = (VideoView) view.findViewById(R.id.videoView);
        imageView = (ImageView)view.findViewById(R.id.imageView);
        textView = (TextView)view.findViewById(R.id.textView);

        videoView.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.INVISIBLE);
        textView.setVisibility(View.INVISIBLE);

        videoView.setZOrderOnTop(true);

        browserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser fileChooser = new FileChooser(getActivity(), "Pick a file", FileChooser.DialogType.SELECT_FILE, null);
                FileChooser.FileSelectionCallback callback = new FileChooser.FileSelectionCallback() {
                    @Override
                    public void onSelect(File file) {

                        videoView.setVisibility(View.INVISIBLE);
                        imageView.setVisibility(View.INVISIBLE);
                        textView.setVisibility(View.INVISIBLE);

                        filePath = file.getAbsolutePath();
                        filepath.setText(filePath);

                        fileUri = Uri.fromFile(new File(filePath));

                        String type;

                        String extension = MimeTypeMap.getFileExtensionFromUrl(fileUri.toString());
                        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);

                        String mediaType = type.substring(0, type.indexOf("/"));

                        fileType = mediaType;

                        if(mediaType.equals("text")){
                            textView.setVisibility(View.VISIBLE);
                            FileInputStream is;
                            BufferedReader reader;
                            try{
                                is = new FileInputStream(file);
                                reader = new BufferedReader(new InputStreamReader(is));
                                String line = reader.readLine();
                                textView.append(line);
                                while(line != null){
                                    line = reader.readLine();
                                    textView.append(line);
                                }
                            }
                            catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                        else if(mediaType.equals("image")){
                            imageView.setVisibility(View.VISIBLE);
                            Bitmap image = BitmapFactory.decodeFile(filePath);
                            imageView.setImageBitmap(image);
                        }
                        else if(mediaType.equals("video") || mediaType.equals("audio")){
                            videoView.setVisibility(View.VISIBLE);
                            final MediaController mediaController = new MediaController(getActivity());
                            final MediaPlayer mediaPlayer = new MediaPlayer();
                            videoView.setVideoURI(fileUri);
                            videoView.setMediaController(mediaController);
                            mediaController.setAnchorView(videoView);
                            mediaController.show();

                            videoView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(mediaPlayer.isPlaying()){
                                        mediaController.show();
                                    }
                                }
                            });

                            videoView.requestFocus();
                            videoView.start();
                        }
                        else {
                            Toast.makeText(getActivity(), "Please select other file.", Toast.LENGTH_LONG).show();
                        }
                    }
                };
                fileChooser.show(callback);
            }
        });

        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditorFragment ldf = new EditorFragment ();
                Bundle args = new Bundle();
                args.putString("filePath", filePath);
                args.putString("type", fileType);
                ldf.setArguments(args);
                //getFragmentManager().beginTransaction().replace(R.id.frameLayout, ldf).commit();
                getActivity().getIntent().putExtra("filePath", filePath);
                getActivity().getIntent().putExtra("type", fileType);
                getFragmentManager().popBackStackImmediate();
            }
        });



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
