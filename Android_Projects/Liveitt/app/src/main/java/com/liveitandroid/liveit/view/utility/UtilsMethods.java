package com.liveitandroid.liveit.view.utility;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Paint;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;

import java.io.File;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class UtilsMethods {
    public static int CHECK_CONNECTION;
    public static InputFilter EMOJI_FILTER = new C20853();
    public static DatePickerDialog datePicker;
    static ImageView imgLeft;
    static ImageView imgRight;
    public static RelativeLayout relLeft;
    public static RelativeLayout relRight;
    public static SimpleDateFormat sdf;
    public static TimePickerDialog timePicker;
    public static Toolbar toolbar;
    public static TextView txtRight;

    static class C20842 implements InputFilter {
        C20842() {
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }
    }

    static class C20853 implements InputFilter {
        C20853() {
        }

        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {
                if (Character.getType(source.charAt(index)) == 19) {
                    return "";
                }
            }
            return null;
        }
    }

    public static String getCurrentDate(String format) {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        return new SimpleDateFormat(format).format(c.getTime());
    }

    public static String getCurrentDatefromMilli(String milli, String format) {
        System.out.println("Current time => " + Calendar.getInstance().getTime());
        return new SimpleDateFormat(format).format(new Date(Long.parseLong(milli)));
    }

    public static boolean CheckPermissions(Context c, String permission, int requestcode) {
        if (ContextCompat.checkSelfPermission(c, permission) == 0) {
            return true;
        }
        Log.e("checkSelfPermission", "PERMISSION_GRANTED");
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) c, permission)) {
            Log.e("checkSelfPermission", "shouldShowRequestPermissionRationale");
            if (c instanceof AppCompatActivity) {
                Log.e("AppCompatActivity", "true");
                ActivityCompat.requestPermissions((AppCompatActivity) c, new String[]{permission}, requestcode);
            } else if (c instanceof FragmentActivity) {
                Log.e("FragmentActivity", "true");
                ActivityCompat.requestPermissions((FragmentActivity) c, new String[]{permission}, requestcode);
            }
        } else {
            if (c instanceof AppCompatActivity) {
                ActivityCompat.requestPermissions((AppCompatActivity) c, new String[]{permission}, requestcode);
            } else if (c instanceof FragmentActivity) {
                ActivityCompat.requestPermissions((FragmentActivity) c, new String[]{permission}, requestcode);
            }
        }
        return false;
    }

    public static void hideSoftKeyboard(Activity activity) {
        ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void ShakeEditText(View view) {
        view.requestFocus(33);
    }

    public static void ViewAnimate(View view) {
        view.requestFocus(33);
    }

    public static boolean compareDates(String d1, String d2) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);
            System.out.println("Date1" + sdf.format(date1));
            System.out.println("Date2" + sdf.format(date2));
            System.out.println();
            if (date1.after(date2)) {
                System.out.println("Date1 is after Date2");
                return false;
            } else if (date1.before(date2)) {
                System.out.println("Date1 is before Date2");
                return true;
            } else if (date1.equals(date2)) {
                System.out.println("Date1 is equal Date2");
                return false;
            } else {
                System.out.println();
                return true;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
            return false;
        }

    }

    public static String ValidateString(String string) {
        return (string.equals("") || string == null || string.equals("null")) ? "" : string;
    }

    public static boolean isMyServiceRunning(Context c, Class<?> serviceClass) {
        for (RunningServiceInfo service : ((ActivityManager) c.getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String ConvertTimeZone(String OurDate, String timeZone, String presentformat) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(presentformat);
            formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
            Date value = formatter.parse(OurDate);
            SimpleDateFormat dateFormatter = new SimpleDateFormat(presentformat);
            dateFormatter.setTimeZone(TimeZone.getDefault());
            OurDate = dateFormatter.format(value);
            return OurDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "00-00-0000 00:00";
        }
    }

    public static boolean CheckPermissions(Context c, Fragment fragment, String permission, int requestcode) {
        if (VERSION.SDK_INT < 23) {
            return true;
        }
        if (ContextCompat.checkSelfPermission(c, permission) == 0) {
            return true;
        }
        Log.e("checkSelfPermission", "PERMISSION_GRANTED");
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) c, permission)) {
            fragment.requestPermissions(new String[]{permission}, requestcode);
            return false;
        }
        fragment.requestPermissions(new String[]{permission}, requestcode);
        return false;
    }

    public static boolean IsLocationEnabled(Context context) {
        LocationManager lm = (LocationManager) context.getSystemService("location");
        boolean gps_enabled = false;
        boolean network_enabled = false;
        try {
            gps_enabled = lm.isProviderEnabled("gps");
        } catch (Exception e) {
        }
        try {
            network_enabled = lm.isProviderEnabled("network");
        } catch (Exception e2) {
        }
        return gps_enabled || network_enabled;
    }

    public static Snackbar ShowSnackBar(View vieww, String Message) {
        Snackbar snack = Snackbar.make(vieww, Message, 0);
        View view = snack.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(-1);
        LayoutParams params = (LayoutParams) view.getLayoutParams();
        params.gravity = 48;
        view.setLayoutParams(params);
        snack.show();
        return snack;
    }

    public static void printHashKey(Context context) {
        try {
            for (Signature signature : context.getPackageManager().getPackageInfo("com.ulawn_customer", 64).signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
            }
        } catch (NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e2) {
        }
    }

    public static String getDateFormat(String presentfromat, String date, String reqFormat) throws ParseException {
        String strNewDate = "";
        try {
            Date d = new SimpleDateFormat(presentfromat, Locale.ENGLISH).parse(date);
            sdf = new SimpleDateFormat(reqFormat, Locale.ENGLISH);
            strNewDate = sdf.format(d);
            return strNewDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return strNewDate;
        }
    }

    public static long getDateFormatinMilli(String presentfromat, String date, String reqFormat) throws ParseException {
        long timeInMilliseconds = 0;
        try {
            timeInMilliseconds = new SimpleDateFormat(presentfromat, Locale.ENGLISH).parse(date).getTime();
            return timeInMilliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
            return timeInMilliseconds;
        }
    }

    public static void setupParent(final Context context, View view) {
        try {
            if (!(view instanceof EditText)) {
                view.setOnTouchListener(new OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        UtilsMethods.hideSoftKeyboard(context);
                        return false;
                    }
                });
            }
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    setupParent(context, ((ViewGroup) view).getChildAt(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideSoftKeyboard(Context context) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService("input_method");
            if (inputMethodManager != null && ((AppCompatActivity) context).getCurrentFocus() != null) {
                inputMethodManager.hideSoftInputFromWindow(((AppCompatActivity) context).getCurrentFocus().getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean validateEmailId(String email) {
        return Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+").matcher(email).matches();
    }

    public static void requestFocus(Activity context, View view) {
        if (view.requestFocus()) {
            context.getWindow().setSoftInputMode(5);
        }
    }

    public static String formatToYesterdayOrToday(long timestampInMilliSeconds, boolean day) throws ParseException {
        Date date = new Date();
        date.setTime(timestampInMilliSeconds);
        String formattedDate = new SimpleDateFormat("MMM dd, yyyy").format(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestampInMilliSeconds);
        Calendar today = Calendar.getInstance();
        Calendar.getInstance().add(5, -1);
        if (day) {
            String timeFormatter = new SimpleDateFormat("EEE, MMM dd, yyyy").format(calendar.getTime());
            return timeFormatter;
        }
        return formattedDate;
    }

    public static void colorStatusBar(Activity activity, int color) {
        if (VERSION.SDK_INT >= 21) {
            Window windoww = activity.getWindow();
            windoww.addFlags(Integer.MIN_VALUE);
            windoww.setStatusBarColor(color);
        }
    }

    public static void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    public static void setScaleAnimation(View view) {
        ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, 1, 0.5f, 1, 0.5f);
        anim.setDuration(500);
        view.startAnimation(anim);
    }

    public static boolean setNumberPickerTextColor(NumberPicker numberPicker, int color) {
        int count = numberPicker.getChildCount();
        int i = 0;
        while (i < count) {
            View child = numberPicker.getChildAt(i);
            if (child instanceof EditText) {
                try {
                    Field selectorWheelPaintField = numberPicker.getClass().getDeclaredField("mSelectorWheelPaint");
                    selectorWheelPaintField.setAccessible(true);
                    ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
                    ((EditText) child).setTextColor(color);
                    numberPicker.invalidate();
                    return true;
                } catch (NoSuchFieldException e) {
                    Log.w("setNumbPickerTextColor", e);
                } catch (IllegalAccessException e2) {
                    Log.w("setNuerPickerTextColor", e2);
                } catch (IllegalArgumentException e3) {
                    Log.w("setNuerPickerTextColor", e3);
                }
            } else {
                i++;
            }
        }
        return false;
    }

    public static String GetString(EditText editText) {
        return editText.getText().toString();
    }

    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = "_data";
        try {
            cursor = context.getContentResolver().query(uri, new String[]{"_data"}, selection, selectionArgs, null);
            if (cursor == null || !cursor.moveToFirst()) {
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            }
            String string = cursor.getString(cursor.getColumnIndexOrThrow("_data"));
            return string;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static void hideKeyboard(Activity activity, View view) {
        try {
            ((InputMethodManager) activity.getSystemService("input_method")).hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter != null) {
            int totalHeight = 0;
            int s = myListAdapter.getCount();
            int desiredWidth = MeasureSpec.makeMeasureSpec(myListView.getWidth(), Integer.MIN_VALUE);
            View view = null;
            for (int size = 0; size < s; size++) {
                view = myListAdapter.getView(size, view, myListView);
                if (size == 0) {
                    view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, -2));
                }
                view.measure(desiredWidth, 0);
                totalHeight += view.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = myListView.getLayoutParams();
            params.height = (myListView.getDividerHeight() * (myListAdapter.getCount() - 1)) + totalHeight;
            myListView.setLayoutParams(params);
        }
    }

    public static boolean isPackageExisted(Context c, String targetPackage) {
        try {
            c.getPackageManager().getPackageInfo(targetPackage, 128);
            return true;
        } catch (NameNotFoundException e) {
            return false;
        }
    }

    public static void Block_SpaceInEditText(EditText edittext) {
        edittext.setFilters(new InputFilter[]{new C20842()});
    }


    public static File getRecordingDir(){
        File recDir = new File(Environment.getExternalStorageDirectory(), "Liveit-Rec");
//        Log.d("Test", recDir.getAbsolutePath());
        if(!recDir.exists())
            recDir.mkdirs();
        return recDir;
    }


    public static String getTime(long timeMillisec){

        // convert milli-sec to time
        float totalSec = timeMillisec/1000;
        int sec = (int) (totalSec % 60);
        int min = (int) (totalSec / 60);
        int hrs = (min / 60);

//            if(hrs < 9) hrs = "0" + hrs;
        return  format(hrs) + ":" +   // hours
                format(min) + ":" +     // min
                format(sec);          // sec
    }


    private static String format(int n){
        if(n < 10)
            return "0"+n;
        else
            return String.valueOf(n);
    }
}
