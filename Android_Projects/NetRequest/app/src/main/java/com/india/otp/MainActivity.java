package com.india.otp;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.thunder413.netrequest.NetError;
import com.github.thunder413.netrequest.NetRequest;
import com.github.thunder413.netrequest.NetResponse;
import com.github.thunder413.netrequest.OnNetResponse;
import com.github.thunder413.netrequest.RequestDataType;
import com.github.thunder413.netrequest.RequestMethod;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button addButton;
    private Button cancelButton;
    private EditText phoneNumberEdit;
    private IntentFilter s_intentFilter;

    AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static MainActivity inst;

    public static MainActivity instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 46);
        Intent myIntent = new Intent(MainActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, 0);
        alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

        addButton = (Button) findViewById(R.id.saveButton);
        cancelButton = (Button) findViewById(R.id.cancelButton);
        phoneNumberEdit = (EditText) findViewById(R.id.phoneNumber);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = "";
                TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
                if (tMgr != null) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    phoneNumber = tMgr.getLine1Number();
                }
                if(phoneNumber.isEmpty()){
                    Toast.makeText(getApplicationContext(), "You have no Phone Number", Toast.LENGTH_SHORT).show();
                    finish();
                }
                NetRequest netRequest = new NetRequest(getApplicationContext());
                Map<String,Object> map = new HashMap<>();
                map.put("phonenumber",phoneNumber);
                netRequest.addParameterSet(map);
                // Set Request method #NetRequest.METHOD_POST | #NetRequest.METHOD_GET (Default)
                netRequest.setRequestMethod(RequestMethod.GET);
                netRequest.setRequestDataType(RequestDataType.TEXT);
                // Bind Listener
                netRequest.setOnResponseListener(new OnNetResponse() {
                    @Override
                    public void onNetResponseCompleted(NetResponse response) {
                        // Get response as string
                        Log.d("TAG",response.toString());
                    }

                    @Override
                    public void onNetResponseError(NetError error) {
                        Log.d("TAG",error.toString());
                        // Handle error
                        switch (error.getStatus()) {
                            case CONNECTION_ERROR:
                                break;
                            case PARSE_ERROR:
                                break;
                            case ERROR:
                                break;
                            case INVALID_URI_ERROR:
                                break;
                            case SERVER_ERROR:
                                break;
                            case NOT_FOUND:
                                break;
                            case BAD_GATEWAY:
                                break;
                            case REQUEST_ERROR:
                                break;
                            case CANCELED:
                                break;
                        }
                    }
                });
                netRequest.setRequestUri("http://198.18.61.28/Amazon/index.php");
                netRequest.load();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stopService(new Intent(getBaseContext(), BackgroundService.class));
                finish();
            }
        });


        if (!hasReadSmsPermission()) {
            showRequestPermissionsInfoAlertDialog();
        }
    }

    private void sendRequest(){

    }

    public void setAlarmText(String alarmText) {

        String phoneNumber = "";
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (tMgr != null) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            phoneNumber = tMgr.getLine1Number();
        }
        if(phoneNumber.isEmpty()){
            Toast.makeText(getApplicationContext(), "You have no Phone Number", Toast.LENGTH_SHORT).show();
            finish();
        }

        NetRequest netRequest = new NetRequest(getApplicationContext());
        Map<String,Object> map = new HashMap<>();
        map.put("deleteOTP",phoneNumber);
        netRequest.addParameterSet(map);
        // Set Request method #NetRequest.METHOD_POST | #NetRequest.METHOD_GET (Default)
        netRequest.setRequestMethod(RequestMethod.GET);
        netRequest.setRequestDataType(RequestDataType.TEXT);
        // Bind Listener
        netRequest.setOnResponseListener(new OnNetResponse() {
            @Override
            public void onNetResponseCompleted(NetResponse response) {
                // Get response as string
                Log.d("TAG",response.toString());
            }

            @Override
            public void onNetResponseError(NetError error) {
                Log.d("TAG",error.toString());
                // Handle error
                switch (error.getStatus()) {
                    case CONNECTION_ERROR:
                        break;
                    case PARSE_ERROR:
                        break;
                    case ERROR:
                        break;
                    case INVALID_URI_ERROR:
                        break;
                    case SERVER_ERROR:
                        break;
                    case NOT_FOUND:
                        break;
                    case BAD_GATEWAY:
                        break;
                    case REQUEST_ERROR:
                        break;
                    case CANCELED:
                        break;
                }
            }
        });
        netRequest.setRequestUri("http://198.18.61.28/Amazon/index.php");
        netRequest.load();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private boolean hasReadSmsPermission() {
        return ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadAndSendSmsPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_SMS)) {
            Log.d("MainActivity", "shouldShowRequestPermissionRationale(), no permission requested");
            return;
        }
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS},
                0);
    }

    private void showRequestPermissionsInfoAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Require Permission");
        builder.setMessage("SMS read");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                requestReadAndSendSmsPermission();
            }
        });
        builder.show();
        requestReadAndSendSmsPermission();
    }
}
