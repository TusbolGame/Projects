package com.map.calendar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SplashFragment.OnFragmentInteractionListener ,
        FeedFragment.OnFragmentInteractionListener,
        EditorFragment.OnFragmentInteractionListener,
        SignupFragment.OnFragmentInteractionListener,
        FilePickerFragment.OnFragmentInteractionListener,
        AuthFragment.OnFragmentInteractionListener
{

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;

    private Location loc;

    GpsTracker gpsTracker;
    Geocoder geocoder;
    DatabaseReference databaseUsers;
    FirebaseAuth firebaseAuth;
    String userEmail;
    String uID;
    String time;

    FragmentManager fm;
    FragmentTransaction fragmentTransaction;
    boolean cam_init = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        loc = new Location("");
        loc.setLatitude(0.0d);
        loc.setLongitude(0.0d);

        gpsTracker = new GpsTracker(getApplicationContext());
        databaseUsers = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("")
                .setMessage("We are going to charge every 1st of your cloud usage of the plataform. A billing will be sent to your email.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(Manifest.permission.CAMERA, 1);

                        fm = getSupportFragmentManager();
                        fragmentTransaction = fm.beginTransaction();
                        SplashFragment splashFragment = new SplashFragment();

                        findViewById(R.id.frameLayout).setLayerType(View.LAYER_TYPE_HARDWARE, null);

                        fragmentTransaction.replace(R.id.frameLayout, splashFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                start_camera();
                            }
                        }, 200);

                    }
                });
        builder.create().show();


        /*fm = getSupportFragmentManager();
        fragmentTransaction = fm.beginTransaction();
        SplashFragment splashFragment = new SplashFragment();

        findViewById(R.id.frameLayout).setLayerType(View.LAYER_TYPE_HARDWARE, null);

        fragmentTransaction.replace(R.id.frameLayout, splashFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }

    public String getLocation(){
        String address = "";
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
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

    public void userDataUpdate(){
        String location;
        if(getLocation() != null){
            location = getLocation();
        }
        else{
            location = "";
        }
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            //databaseUsers.child("users").equalTo(firebaseUser.getEmail()).u
            databaseUsers.child("users").child(firebaseUser.getEmail()).child("userLocation").setValue(getLocation());
            //databaseUsers.child("users").child(firebaseUser.getEmail()).child("userLocation").setValue(getLocation())
        }

    }

    public void onPause(){
        super.onPause();
        stop_camera();
    }
    public void onResume(){
        super.onResume();
        if(!cam_init){
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    start_camera();
                }
            }, 200);
        }
        cam_init = false;


    }
    private void start_camera()
    {
        try{
            camera = Camera.open();
        }catch(RuntimeException e){
            Log.e("Error:", "init_camera: " + e);
            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();
        param.setPreviewFrameRate(20);
        camera.setDisplayOrientation(90);
        camera.setParameters(param);
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            Log.e("Error:", "init_camera: " + e);
            return;
        }
    }

    private void stop_camera()
    {
        if(camera != null){
            camera.stopPreview();
            camera.release();
        }

    }
    public void onDestroy(){
        super.onDestroy();
    }
    public void onFragmentInteraction(Uri uri){

    }
}
