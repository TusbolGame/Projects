package com.evilkingmedia;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.evilkingmedia.cartoon.CartoonCategoryActivity;
import com.evilkingmedia.chat.ContactActivity;
import com.evilkingmedia.chat.User;
import com.evilkingmedia.demand.FilmCategoryActivity;
import com.evilkingmedia.epg.EpgAndGuideActivity;
import com.evilkingmedia.libri.LibriActivity;
import com.evilkingmedia.livetv.LiveActivityCategory;
import com.evilkingmedia.musica.MusicMainActivity;
import com.evilkingmedia.mywebcaster.MyWebCasterActivity;
import com.evilkingmedia.sports.SportsCategoryActivity;
import com.evilkingmedia.utility.CheckXml;
import com.evilkingmedia.utility.Option;
import com.evilkingmedia.videoteca.VideoTecaActivity;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class CategoryActivity extends AppCompatActivity {

    TextView txtBottom, txt_dt_time, txtlogout;
    TextView dropdownview;
    LinearLayout rlMovies, rlCartoon, rlSports, rlMusic, rlMeteo, rlepg, rllive, rllibri, rlVideoTeca, rlmywebcaster;
    private ImageView download,wvc, dns, email, vpn, txtsetting, chat, bot;
    private CircleImageView profile_imageview;
    private AdView adView;
    private Uri profile_image_uri = null;
    PopupWindow dropdown_listview;

    DatabaseReference drUserInfo;
    FirebaseAuth mAuth;
    StorageReference srImage;

    final String Expn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@" +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\." +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?" +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|" +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";

    TextView txt_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_main);

            email = findViewById(R.id.txtemail);
            txtsetting = findViewById(R.id.txtsetting);
            download = findViewById(R.id.download);
            wvc = findViewById(R.id.wvc);
            dns = findViewById(R.id.dns);
            vpn = findViewById(R.id.vpn);
            chat = findViewById(R.id.chat);
            bot = findViewById(R.id.bot);

            wvc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WVC_playstore)));
                }
            });

            dns.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.playDNSChanger(CategoryActivity.this);
                }
            });

            vpn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Constant.playVPNChanger(CategoryActivity.this);
                }
            });

            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.evilkingmedia.com/download/")));
                }
            });

            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                            "mailto","evilkodi@libero.it", null));
                    startActivity(Intent.createChooser(emailIntent, "Send email..."));
                }
            });

            txtsetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=co.wuffy.player")));
                }
            });

            chat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToChat();
                }
            });

            bot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.TELEGRAM_BOT_URL)));
                }
            });

            setFocusEvent();

        } else {
            setContentView(R.layout.activity_main_portrait);
            dropdownview = findViewById(R.id.dropdownview);
            List<Option> options = new ArrayList<>();
//            options.add(new Option("Chat", R.drawable.chat));
            options.add(new Option("Bot Assistenza", R.drawable.telegram));
            options.add(new Option("Update", R.drawable.download));
            options.add(new Option("E-mail", R.drawable.email));
            options.add(new Option("Wuffy", R.drawable.ic_play));
            options.add(new Option("Web Caster", R.drawable.wvc));
            options.add(new Option("DNS", R.drawable.dns));
            options.add(new Option("VPN", R.drawable.vpn));
            dropdownview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDropDownList(v, options);
                }
            });

        }

        drUserInfo = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        srImage = FirebaseStorage.getInstance().getReference();

        adView = findViewById(R.id.adView);
        Constant.showAdmob(this, adView);

        txtBottom = findViewById(R.id.txt);
        String styledText = "Sito Ufficiale: evilkingmedia.com - Assistenza Web: androidaba.com - Assistenza Telegram:<font color='blue'> https://t.me/evilkingmedia</font> ";
        txtBottom.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        rlMovies = findViewById(R.id.rlMovies);
        rlCartoon = findViewById(R.id.rlCartoon);
        rlSports = findViewById(R.id.rlSports);
        rlVideoTeca = findViewById(R.id.rlVideoTeca);
        rlepg = findViewById(R.id.rlepg);
        rlMusic = findViewById(R.id.rlMusic);
        rlMeteo = findViewById(R.id.rlMeteo);
        rllive = findViewById(R.id.rllive);
        rllibri = findViewById(R.id.rllibri);
        rlmywebcaster = findViewById(R.id.rlMyWebCaster);
        txt_dt_time = findViewById(R.id.dt_time);
        txtlogout = findViewById(R.id.txtlogout);

        txtlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            @Override
            public void run() {
                final Date date = new Date();
                final DateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
                CategoryActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        txt_dt_time.setText(df.format(date));
                    }
                });

            }
        }, 0, 1000);

        rlMovies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoryActivity.this, FilmCategoryActivity.class);
                startActivity(i);
            }
        });

        rlCartoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoryActivity.this, CartoonCategoryActivity.class);
                startActivity(i);
            }
        });

         rlSports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoryActivity.this, SportsCategoryActivity.class);
                startActivity(i);
            }
        });

        rlepg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CategoryActivity.this, EpgAndGuideActivity.class);
                startActivity(i);
            }
        });

        rlMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CategoryActivity.this, MusicMainActivity.class);
                startActivity(intent);
            }
        });

       rlMeteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Constant.playInWuffyOrXmtv(CategoryActivity.this,Constant.METEOURL );
                Constant.openExternalBrowser(CategoryActivity.this, Constant.METEOURL);
            }
        });

        rllive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoryActivity.this, LiveActivityCategory.class));
            }
        });

        rlVideoTeca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoryActivity.this, VideoTecaActivity.class));
            }
        });

        rllibri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CategoryActivity.this, LibriActivity.class));
            }
        });

        rlmywebcaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CategoryActivity.this, MyWebCasterActivity.class));
            }
        });

        Constant.setFocusEvent(this, rlMovies, rlCartoon, rlSports, rlMusic, rlMeteo, rlepg, rllive, rllibri, rlVideoTeca, rlmywebcaster);
        txtlogout.setFocusable(true);
        final boolean[] flag_txtlogout = {true};
        txtlogout.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @SuppressLint("ResourceAsColor")
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_txtlogout[0]){
                    txtlogout.setBackgroundColor(Color.RED);
                    flag_txtlogout[0] = false;
                } else {
                    txtlogout.setBackgroundColor(0x0000FF00 );
                    txtlogout.invalidate();
                    txtlogout.setTextColor(Color.parseColor("#FFFFFF"));
                    flag_txtlogout[0] = true;
                }
            }
        });


        txt_version = (TextView) findViewById(R.id.version);

        try {
            PackageInfo pInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0);
            String version = pInfo.versionName;
            txt_version.setText("v"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


    }

    private void showDropDownList(View view, List<Option> options) {

        // initialize a pop up window type
        dropdown_listview = new PopupWindow(this);

        // the drop down list is a list view
        ListView listView = new ListView(this);

        // set our adapter and pass our pop up window contents
        listView.setAdapter(new DropDownListAdapter(CategoryActivity.this, 0, options));
        listView.setSelector(R.drawable.dropdown_item_background);

        // some other visual settings
        dropdown_listview.setFocusable(true);
        dropdown_listview.setWidth(650);
        dropdown_listview.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        dropdown_listview.setContentView(listView);
        dropdown_listview.showAsDropDown(view, -5, 0);
    }

    private void goToChat(){
        SharedPreferences sharedPreferences = getSharedPreferences("login_pref", Context.MODE_PRIVATE);
        if (sharedPreferences.getAll().size() == 0){
            showLoginDialog();
        } else {
            String email = sharedPreferences.getString("email", "");
            String password = sharedPreferences.getString("password", "");
            assert email != null;
            assert password != null;
            if (email.isEmpty() && password.isEmpty()){
                showLoginDialog();
            } else {
                final ProgressDialog progressDialog = new ProgressDialog(CategoryActivity.this, R.style.MyProgressDialogStyle);
                progressDialog.setMessage("Logging in...");
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        final boolean[] flag = {false};
                        drUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()){
                                    User user = ds.getValue(User.class);
                                    assert user != null;
                                    if (user.getUserid().equals(mAuth.getUid())){
                                        flag[0] = true;
                                    }
                                }
                                if (flag[0]){
                                    progressDialog.dismiss();
                                    SharedPreferences.Editor editor = getSharedPreferences("login_pref", Context.MODE_PRIVATE).edit();
                                    editor.putString("email", email);
                                    editor.putString("password", password);
                                    editor.apply();
                                    Intent intent = new Intent(CategoryActivity.this, ContactActivity.class);
                                    startActivity(intent);
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(CategoryActivity.this, "You was deleted by admin. You will can't use this email and password more!", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }
        }
    }

    private void showLoginDialog() {
        Dialog login_dialog = new Dialog(this);
        login_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        login_dialog.setContentView(R.layout.activity_login);

        EditText et_email, et_password;
        TextView login_button, create_account_button;
        et_email = login_dialog.findViewById(R.id.login_email);
        et_password = login_dialog.findViewById(R.id.login_password);
        login_button = login_dialog.findViewById(R.id.btn_login);
        create_account_button = login_dialog.findViewById(R.id.create_account);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                if(email.isEmpty()){
                    Toast.makeText(CategoryActivity.this, "You must enter email!", Toast.LENGTH_LONG).show();
                    return;
                }

                if(!email.matches(Expn)){
                    Toast.makeText(CategoryActivity.this, "Email Address is incorrect!", Toast.LENGTH_LONG).show();
                    return;
                }

                if(password.isEmpty()){
                    Toast.makeText(CategoryActivity.this, "You must enter valid password!", Toast.LENGTH_LONG).show();
                    return;
                }

                final ProgressDialog progressDialog = new ProgressDialog(CategoryActivity.this, R.style.MyProgressDialogStyle);
                progressDialog.setMessage("Logging in...");
                progressDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(CategoryActivity.this, "Don't exist this user. You must register!", Toast.LENGTH_LONG).show();
                        }else{
                            gotoChatPage(progressDialog, login_dialog, email, password);
                        }
                    }
                });
            }
        });

        create_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRegisterDialog();
            }
        });

        login_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        login_dialog.getWindow().setLayout(getResources().getDimensionPixelOffset(R.dimen.popup_width), WindowManager.LayoutParams.WRAP_CONTENT);
        login_dialog.show();
    }

    private void gotoChatPage(ProgressDialog dialog, Dialog login_dialog, String email, String password) {
        final boolean[] flag = {false};
        drUserInfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    assert user != null;
                    if (user.getUserid().equals(mAuth.getUid())){
                        flag[0] = true;
                    }
                }
                if (flag[0]){
                    dialog.dismiss();
                    login_dialog.dismiss();
                    SharedPreferences.Editor editor = getSharedPreferences("login_pref", Context.MODE_PRIVATE).edit();
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.apply();
                    Intent intent = new Intent(CategoryActivity.this, ContactActivity.class);
                    startActivity(intent);
                } else {
                    dialog.dismiss();
                    Toast.makeText(CategoryActivity.this, "This user was deleted by admin. You will can't use this email and password more!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showRegisterDialog(){
        profile_image_uri = null;
        Dialog register_dialog = new Dialog(this);
        register_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        register_dialog.setContentView(R.layout.activity_register);

        EditText et_username, et_email, et_password, et_confirmpassword;
        TextView register_button, go_loginpage_button;
        FloatingActionButton photo_capture_button;

        et_username = register_dialog.findViewById(R.id.register_username);
        et_email = register_dialog.findViewById(R.id.register_email);
        et_password = register_dialog.findViewById(R.id.register_password);
        et_confirmpassword = register_dialog.findViewById(R.id.register_confirmpassword);
        register_button = register_dialog.findViewById(R.id.btn_register);
        go_loginpage_button = register_dialog.findViewById(R.id.goLogin);
        photo_capture_button = register_dialog.findViewById(R.id.capture_btn);
        profile_imageview = register_dialog.findViewById(R.id.user_profile_imageview);

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = et_username.getText().toString().trim();
                String email = et_email.getText().toString().trim();
                String password = et_password.getText().toString().trim();
                String confirm_password = et_confirmpassword.getText().toString().trim();
                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirm_password.isEmpty()){
                    Toast.makeText(CategoryActivity.this, "Fill all fields!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!email.matches(Expn)){
                    Toast.makeText(CategoryActivity.this, "Please enter a valid email address!", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!password.equals(confirm_password)){
                    Toast.makeText(CategoryActivity.this, "Password is not match!", Toast.LENGTH_LONG).show();
                    return;
                }

                doRegister(username, email, password, register_dialog);
            }
        });

        go_loginpage_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register_dialog.dismiss();
            }
        });

        photo_capture_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).start(CategoryActivity.this);
            }
        });

        register_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        register_dialog.getWindow().setLayout(getResources().getDimensionPixelOffset(R.dimen.popup_width), WindowManager.LayoutParams.WRAP_CONTENT);
        register_dialog.show();
    }

    private void doRegister(String username, String email, String password, Dialog register_dialog){
        final ProgressDialog progressDialog = new ProgressDialog(CategoryActivity.this, R.style.MyProgressDialogStyle);
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (!task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(CategoryActivity.this, "This email and password are deleted by admin!", Toast.LENGTH_LONG).show();
                } else {
                    final FirebaseUser newUser = task.getResult().getUser();
                    newUser.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                        @Override
                        public void onComplete(@NonNull Task<GetTokenResult> task) {
                            if (task.isSuccessful()){
                                String token = task.getResult().getToken();
                                final UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();

                                newUser.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            addUsertoDB(newUser.getUid(), token,  username, newUser.getEmail(), progressDialog, register_dialog);
                                        } else {
                                            Toast.makeText(CategoryActivity.this, "Error " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }

    private void addUsertoDB(String uid, String usertoken, String username, String email, ProgressDialog progressDialog, Dialog register_dialog) {

        if(profile_image_uri != null){
            final StorageReference imagePath = srImage.child(uid);

            imagePath.putFile(profile_image_uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return imagePath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()){
                        Uri downUri = task.getResult();
                        String user_profile_image_path = downUri.toString();
                        User user = new User();
                        user.setUserid(uid);
                        user.setUsername(username);
                        user.setUseremail(email);
                        user.setUserimage(user_profile_image_path);
                        user.setUsertype("customer");
                        user.setUsertoken(usertoken);
                        user.setBlock(false);
                        drUserInfo.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (!task.isSuccessful()) {
                                    progressDialog.dismiss();
                                    Toast.makeText(CategoryActivity.this, "Register failed! Please try again.", Toast.LENGTH_LONG).show();
                                } else {
                                    progressDialog.dismiss();
                                    register_dialog.dismiss();
                                    Toast.makeText(CategoryActivity.this, "Register successful! Please log in.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            });
        }
        else{
            User user = new User();
            user.setUserid(uid);
            user.setUsername(username);
            user.setUseremail(email);
            user.setUserimage("");
            user.setUsertype("customer");
            user.setUsertoken(usertoken);
            user.setBlock(false);
            drUserInfo.child(uid).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(CategoryActivity.this, "Register failed! Please try again.", Toast.LENGTH_LONG).show();
                    } else {
                        progressDialog.dismiss();
                        register_dialog.dismiss();
                        Toast.makeText(CategoryActivity.this, "Register successful! Please log in.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                profile_image_uri = result.getUri();
                File file = new File(profile_image_uri.toString());
                String image_path = file.getPath();
                Picasso.get().load(image_path).into(profile_imageview);
            }
        }
    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {
                CheckXml.checkXml(CategoryActivity.this);
            }
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {}
        }).check();
        rlMovies.requestFocus();
    }

    private void setFocusEvent(){

        // telegram bot
        bot.setFocusable(true);
        final boolean[] flag_bot = {true};
        bot.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_bot[0]){
                    bot.setBackgroundColor(Color.RED);
                    flag_bot[0] = false;
                } else {
                    bot.setBackgroundColor(0x0000FF00 );
                    bot.invalidate();
                    flag_bot[0] = true;
                }
            }
        });

        // download
        download.setFocusable(true);
        final boolean[] flag_download = {true};
        download.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_download[0]){
                    download.setBackgroundColor(Color.RED);
                    flag_download[0] = false;
                } else {
                    download.setBackgroundColor(0x0000FF00 );
                    download.invalidate();
                    flag_download[0] = true;
                }
            }
        });

        //email
        email.setFocusable(true);
        final boolean[] flag_txtemail = {true};
        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_txtemail[0]){
                    email.setBackgroundColor(Color.RED);
                    flag_txtemail[0] = false;
                } else {
                    email.setBackgroundColor(0x0000FF00 );
                    email.invalidate();
                    flag_txtemail[0] = true;
                }
            }
        });

        //txtsetting
        txtsetting.setFocusable(true);
        final boolean[] flag_txtsetting = {true};
        txtsetting.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_txtsetting[0]){
                    txtsetting.setBackgroundColor(Color.RED);
                    flag_txtsetting[0] = false;
                } else {
                    txtsetting.setBackgroundColor(0x0000FF00 );
                    txtsetting.invalidate();
                    flag_txtsetting[0] = true;
                }
            }
        });

        //wvc
        wvc.setFocusable(true);
        final boolean[] flag_wvc = {true};
        wvc.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_wvc[0]){
                    wvc.setBackgroundColor(Color.RED);
                    flag_wvc[0] = false;
                } else {
                    wvc.setBackgroundColor(0x0000FF00 );
                    wvc.invalidate();
                    flag_wvc[0] = true;
                }
            }
        });

        //dns
        dns.setFocusable(true);
        final boolean[] flag_dns = {true};
        dns.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_dns[0]){
                    dns.setBackgroundColor(Color.RED);
                    flag_dns[0] = false;
                } else {
                    dns.setBackgroundColor(0x0000FF00 );
                    dns.invalidate();
                    flag_dns[0] = true;
                }
            }
        });

        //vpn
        vpn.setFocusable(true);
        final boolean[] flag_vpn = {true};
        vpn.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_vpn[0]){
                    vpn.setBackgroundColor(Color.RED);
                    flag_vpn[0] = false;
                } else {
                    vpn.setBackgroundColor(0x0000FF00 );
                    vpn.invalidate();
                    flag_vpn[0] = true;
                }
            }
        });

        chat.setFocusable(true);
        final boolean[] flag_chat = {true};
        chat.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (flag_chat[0]){
                    chat.setBackgroundColor(Color.RED);
                    flag_chat[0] = false;
                } else {
                    chat.setBackgroundColor(0x0000FF00 );
                    chat.invalidate();
                    flag_chat[0] = true;
                }
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            new AlertDialog.Builder(this)
                    .setTitle("Exit this app?")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class DropDownListAdapter extends ArrayAdapter<Option> {

        private DropDownListAdapter(@NonNull Context context, int resource, @NonNull List<Option> options) {
            super(context, resource, options);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View v = convertView;
            if(convertView == null) {
                LayoutInflater inf = LayoutInflater.from(getContext());
                v = inf.inflate(R.layout.dropdownlist_item, parent, false);
            }
            Option option = getItem(position);
            LinearLayout item_view = v.findViewById(R.id.item_view);
            TextView title_view = v.findViewById(R.id.title);
            ImageView icon_view = v.findViewById(R.id.icon);
            assert option != null;
            title_view.setText(option.getTitle());
            icon_view.setBackgroundResource(option.getIcon());

            item_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
                    fadeInAnimation.setDuration(10);
                    v.startAnimation(fadeInAnimation);

                    switch (option.getTitle()){
                        case "Chat":
                            goToChat();
                            break;
                        case "Update":
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.evilkingmedia.com/download/")));
                            break;
                        case "E-mail":
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                    "mailto","evilkodi@libero.it", null));
                            startActivity(Intent.createChooser(emailIntent, "Send email..."));
                            break;
                        case "Wuffy":
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=co.wuffy.player")));
                            break;
                        case "Web Caster":
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.WVC_playstore)));
                            break;
                        case "DNS":
                            Constant.playDNSChanger(CategoryActivity.this);
                            break;
                        case "VPN":
                            Constant.playVPNChanger(CategoryActivity.this);
                            break;
                        case "Bot Assistenza":
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.TELEGRAM_BOT_URL)));
                            break;
                    }
                    dropdown_listview.dismiss();
                }
            });

            return v;
        }
    }
}
