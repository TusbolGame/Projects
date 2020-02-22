package com.map.calendar;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.feed.manage.Article;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

import static com.firebase.ui.auth.AuthUI.TAG;

public class EditorFragment extends Fragment{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    Button attachButton;
    Button uploadButton;
    EditText title_edit;
    EditText comment_edit;

    DatabaseReference databaseArticles;
    FirebaseAuth firebaseAuth;
    String userEmail;
    String uID;
    String filePath;
    String fileType;

    GpsTracker gpsTracker;
    Geocoder geocoder;
    String time;

    StorageReference storageReference;
    String CHECK_BOX_STATE;

    private static final int PICK_IMAGE_REQUEST = 234;

    private OnFragmentInteractionListener mListener;

    public EditorFragment() {
        // Required empty public constructor
    }

    public static EditorFragment newInstance(String param1, String param2) {
        EditorFragment fragment = new EditorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        gpsTracker = new GpsTracker(getActivity().getApplicationContext());
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        databaseArticles = FirebaseDatabase.getInstance().getReference("articles");
        firebaseAuth = FirebaseAuth.getInstance();

        storageReference = FirebaseStorage.getInstance().getReference();
    }

    public String getAttachID(){
        return "AttachID";
    }
    public String getUserID(){
        return "UserID";
    }

    public String getLocation(){
        String address = "";
        geocoder = new Geocoder(getActivity().getApplicationContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
            time = DateFormat.getTimeInstance().format(gpsTracker.getLocation().getTime());
        }
        catch (Exception e){
            e.printStackTrace();
        }
        if(addresses != null){
            address += String.valueOf(gpsTracker.getLatitude());
            address += "#";
            address += String.valueOf(gpsTracker.getLongitude());
            return address;
        }
        return "";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editor, container, false);



        uID = firebaseAuth.getCurrentUser().getUid();
        userEmail = firebaseAuth.getCurrentUser().getEmail();

        attachButton = (Button)view.findViewById(R.id.attach_button);
        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.frameLayout, new FilePickerFragment(), "filepickerFragment");
                ft.commit();
                ft.addToBackStack(null);
            }
        });
        uploadButton = (Button)view.findViewById(R.id.upload_button);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri file = null;

                if(filePath != null){

                    File _file = new File(filePath);
                    long length = _file.length();
                    length = length/1024;

                    if(length > 50000){
                        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                        alertDialog.setTitle("Report");
                        alertDialog.setMessage("The file size is too big.");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        return;
                                    }
                                });
                        alertDialog.show();
                    }

                    file = Uri.fromFile(_file);
                    StorageReference riversRef = storageReference.child(fileType + "s/" + userEmail + "/" + file.getLastPathSegment());

                    final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                    progressDialog.setTitle("Uploading...");
                    progressDialog.show();

                    UploadTask uploadTask = riversRef.putFile(file);

                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "File Uploaded", Toast.LENGTH_SHORT).show();
                            final FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.replace(R.id.frameLayout, new FeedFragment(), "feedFragment");
                            ft.commit();
                        }
                    })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), exception.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                double progress = (100.0 * taskSnapshot.getBytesTransferred())/taskSnapshot.getTotalByteCount();
                                progressDialog.setMessage(((int)progress) + "% Uploaded");
                            }
                        });
                }
                String id = databaseArticles.push().getKey();
                String title = title_edit.getText().toString();
                String content = comment_edit.getText().toString();
                String attachID;
                if(file == null){
                    attachID = "";
                }
                else{
                    attachID = fileType + "s/" + userEmail + "/" + file.getLastPathSegment();
                }
                String Email = userEmail;
                String location;
                if(getLocation() != null){
                    location = getLocation();
                }
                else{
                    location = "";
                }


                /*DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
                ValueEventListener postListener = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get Post object and use the values to update the UI
                        Post post = dataSnapshot.getValue(Post.class);
                        // ...
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // Getting Post failed, log a message
                        Log.w("Cancel:", "loadPost:onCancelled", databaseError.toException());
                        // ...
                    }
                };*/
                Article article = new Article(id, title, content, attachID,  Email, location);
                databaseArticles.child(id).setValue(article);

            }
        });
        title_edit = (EditText)view.findViewById(R.id.title_edit);
        comment_edit = (EditText)view.findViewById(R.id.comment_edit);
        if (savedInstanceState != null) {
            // Restore last state for checked position.
            title_edit.setText(savedInstanceState.getString("title", ""));
            comment_edit.setText(savedInstanceState.getString("comment", ""));
        }
        try{
            filePath = getActivity().getIntent().getStringExtra("filePath");
            fileType = getActivity().getIntent().getStringExtra("type");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", title_edit.getText().toString());
        outState.putString("comment", comment_edit.getText().toString());
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
