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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.feed.manage.Article;
import com.feed.manage.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignupFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText editEmail;
    private EditText editPassword;
    private Button signupButton;

    GpsTracker gpsTracker;
    Geocoder geocoder;

    OnFragmentInteractionListener mListener;
    DatabaseReference databaseUsers;
    FirebaseAuth firebaseAuth;
    String userEmail;
    String uID;
    String time;

    @Override
    public void onStart() {
        super.onStart();
        gpsTracker = new GpsTracker(getActivity().getApplicationContext());
    }

    public SignupFragment() {
        // Required empty public constructor
    }

    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
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
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
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
            String countryName = addresses.get(0).getCountryName();
            if(countryName == null || countryName == ""){
                address +=  "";
            }
            else{
                address+= countryName;
            }
            String adminName = addresses.get(0).getAdminArea();
            if(adminName == null || adminName == ""){
                address +=  "";
            }
            else{
                address+= adminName;
            }
            String subadminName = addresses.get(0).getSubAdminArea();
            if(subadminName == null || subadminName == ""){
                address +=  "";
            }
            else{
                address+= subadminName;
            }
        }
        else{
            address = "";
        }

        return address;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        editEmail = (EditText)view.findViewById(R.id.edit_signupemail);
        editPassword = (EditText)view.findViewById(R.id.edit_signuppassword);
        signupButton = (Button)view.findViewById(R.id.register_button);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString();
                String password = editPassword.getText().toString();
                SignUp(email, password);

            }
        });

        return view;
    }

    private void SignUp(final String email, final String password){
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Sign Up...");
        progressDialog.show();

        firebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                boolean check =! task.getResult().getSignInMethods().isEmpty();
                if (!check){
                    firebaseAuth.createUserWithEmailAndPassword(email, password);

                    String id = databaseUsers.push().getKey();
                    String email = editEmail.getText().toString();
                    String location;
                    if(getLocation() != null){
                        location = getLocation();
                    }
                    else{
                        location = "";
                    }

                    String userPic = "";
                    User article = new User(id, email, location, time,  userPic);
                    databaseUsers.child(id).setValue(article);
                    progressDialog.dismiss();
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, new FeedFragment(), "feedFragment");
                    ft.commit();
                }
                else {
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Alert");
                    alertDialog.setMessage("The Email already Exist.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.replace(R.id.frameLayout, new FeedFragment(), "feedFragment");
                                    ft.commit();
                                }
                            });
                    alertDialog.show();
                }
            }
        });



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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
