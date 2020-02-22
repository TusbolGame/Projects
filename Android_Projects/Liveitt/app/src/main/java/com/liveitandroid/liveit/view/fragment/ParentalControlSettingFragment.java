package com.liveitandroid.liveit.view.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.model.database.LiveStreamDBHandler;
import com.liveitandroid.liveit.model.database.PasswordDBModel;
import com.liveitandroid.liveit.model.database.SeriesStreamsDatabaseHandler;

import java.util.ArrayList;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ParentalControlSettingFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @BindView(R.id.bt_save_password)
    Button btSavePassword;
    private String confirmPassword = "";
    private Context context;
    private LiveStreamDBHandler liveStreamDBHandler;
    private SeriesStreamsDatabaseHandler liveStreamDBHandler1;
    private OnFragmentInteractionListener mListener;
    private String mParam1;
    private String mParam2;
    private String newPassword = "";
    private String oldPassword = "";
    @BindView(R.id.tv_confirm_password)
    EditText tvConfirmPassword;
    @BindView(R.id.tv_new_password)
    EditText tvNewPassword;
    @BindView(R.id.tv_old_password)
    EditText tvOldPassword;
    Unbinder unbinder;

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public static ParentalControlSettingFragment newInstance(String param1, String param2) {
        ParentalControlSettingFragment fragment = new ParentalControlSettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.mParam1 = getArguments().getString(ARG_PARAM1);
            this.mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parental_control_setting, container, false);
        this.unbinder = ButterKnife.bind(this, view);
        initialize();
        return view;
    }

    private void initialize() {
        this.context = getContext();
        this.liveStreamDBHandler = new LiveStreamDBHandler(this.context);
        this.liveStreamDBHandler1 = new SeriesStreamsDatabaseHandler(this.context);
        this.oldPassword = String.valueOf(this.tvOldPassword.getText());
        this.newPassword = String.valueOf(this.tvNewPassword.getText());
        this.confirmPassword = String.valueOf(this.tvConfirmPassword.getText());
        this.tvOldPassword.requestFocus();
        ((InputMethodManager) this.context.getSystemService(Context.INPUT_METHOD_SERVICE)).showSoftInput(this.tvOldPassword, 1);
    }

    public void onButtonPressed(Uri uri) {
        if (this.mListener != null) {
            this.mListener.onFragmentInteraction(uri);
        }
    }

    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            return;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    public void onDetach() {
        super.onDetach();
        this.mListener = null;
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.unbinder.unbind();
    }

    @OnClick({R.id.bt_save_password})
    public void onViewClicked() {
        if (this.context != null) {
            this.oldPassword = String.valueOf(this.tvOldPassword.getText());
            this.newPassword = String.valueOf(this.tvNewPassword.getText());
            this.confirmPassword = String.valueOf(this.tvConfirmPassword.getText());
            String username = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, Context.MODE_PRIVATE).getString("username", "");
            if (!passwordValidationCheck(username, this.oldPassword)) {
                if (this.context != null) {
                    Toast.makeText(this.context, getResources().getString(R.string.invalid_old_password), Toast.LENGTH_SHORT).show();
                }
                clearEditText();
            } else if (!compNewConfirmPassword(this.newPassword, this.confirmPassword)) {
            } else {
                if (this.newPassword.equals(this.confirmPassword)) {
                    updateSuccessfull(this.liveStreamDBHandler.upDatePassword(username, this.newPassword));
                    updateSuccessfull(this.liveStreamDBHandler1.upDatePassword(username, this.newPassword));
                    getActivity().finish();
                    return;
                }
                if (this.context != null) {
                    Toast.makeText(this.context, getResources().getString(R.string.parental_setting_new_confirm_password_error), Toast.LENGTH_SHORT).show();
                }
                clearEditText();
            }
        }
    }

    private void clearEditText() {
        if (this.tvOldPassword != null && this.tvConfirmPassword != null && this.tvNewPassword != null) {
            this.tvOldPassword.getText().clear();
            this.tvConfirmPassword.getText().clear();
            this.tvNewPassword.getText().clear();
        }
    }

    private void updateSuccessfull(boolean updatePassword) {
        if (updatePassword) {
            if (this.context != null) {
                Toast.makeText(this.context, getResources().getString(R.string.password_updated), Toast.LENGTH_SHORT).show();
            }
            clearEditText();
            return;
        }
        if (this.context != null) {
            Toast.makeText(this.context, getResources().getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
        }
        clearEditText();
    }

    private boolean compNewConfirmPassword(String newPassword, String confirmPassword) {
        if (newPassword == null || newPassword.equals("") || newPassword.isEmpty()) {
            if (this.context == null) {
                return false;
            }
            Toast.makeText(this.context, getResources().getString(R.string.enter_new_password_error), Toast.LENGTH_SHORT).show();
            return false;
        } else if ((newPassword == null || newPassword.isEmpty() || newPassword.equals("") || confirmPassword != null || !confirmPassword.isEmpty()) && !confirmPassword.equals("")) {
            if ((newPassword == null || newPassword.isEmpty() || newPassword.equals("") || confirmPassword == null || confirmPassword.isEmpty()) && confirmPassword.equals("")) {
                return false;
            }
            return true;
        } else if (this.context == null) {
            return false;
        } else {
            Toast.makeText(this.context, getResources().getString(R.string.enter_confirm_password_error), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private String password;
    private SharedPreferences loginPreferencesAfterLogin;
    public String getUserPassword() {
        if (this.context == null) {
            return this.password;
        }
        this.loginPreferencesAfterLogin = this.context.getSharedPreferences(AppConst.LOGIN_SHARED_PREFERENCE, 0);
        return this.loginPreferencesAfterLogin.getString("password", "");
    }

    private boolean passwordValidationCheck(String username, String oldPassword) {
        ArrayList<PasswordDBModel> userPasswordList = new LiveStreamDBHandler(this.context).getAllPassword();
        boolean isUserExist = false;
        String userPasswordDB = "";
        if (userPasswordList != null) {
            Iterator it = userPasswordList.iterator();
            while (it.hasNext()) {
                PasswordDBModel listIem = (PasswordDBModel) it.next();
                if (listIem.getUserDetail().equals(username) && !listIem.getUserPassword().isEmpty()) {
                    isUserExist = true;
                    userPasswordDB = listIem.getUserPassword();
                }
            }
        }
        if (!isUserExist || oldPassword == null || oldPassword.isEmpty() || oldPassword.equals("") || userPasswordDB.equals("") || !userPasswordDB.equals(oldPassword)) {
            return getUserPassword().equals(oldPassword);
        }
        return true;
    }
}
