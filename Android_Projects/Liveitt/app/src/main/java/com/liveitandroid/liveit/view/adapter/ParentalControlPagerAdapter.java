package com.liveitandroid.liveit.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.liveitandroid.liveit.view.fragment.ParentalControlCategoriesFragment;
import com.liveitandroid.liveit.view.fragment.ParentalControlSERCatFragment;
import com.liveitandroid.liveit.view.fragment.ParentalControlSettingFragment;
import com.liveitandroid.liveit.view.fragment.ParentalControlVODCatFragment;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.view.fragment.ParentalControlCategoriesFragment;
import com.liveitandroid.liveit.view.fragment.ParentalControlSettingFragment;
import com.liveitandroid.liveit.view.fragment.ParentalControlVODCatFragment;
import java.util.ArrayList;

public class ParentalControlPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    boolean flag = false;
    int mNumOfTabs = 4;
    private ArrayList<Integer> tabServicesTotalCount = new ArrayList();
    private String[] tabTitles = new String[4];

    public ParentalControlPagerAdapter(FragmentManager fm, int NumOfTabs, Context myContext, ArrayList<Integer> tabServicesTotalCount) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.context = myContext;
        this.tabServicesTotalCount = tabServicesTotalCount;
        this.tabTitles[0] = this.context.getResources().getString(R.string.categories);
        this.tabTitles[1] = this.context.getResources().getString(R.string.vod_categories);
        this.tabTitles[2] = this.context.getResources().getString(R.string.ser_categories);
        this.tabTitles[3] = this.context.getResources().getString(R.string.settings);
    }

    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ParentalControlCategoriesFragment();
            case 1:
                return new ParentalControlVODCatFragment();
            case 2:
                return new ParentalControlSERCatFragment();
            case 3:
                return new ParentalControlSettingFragment();
            default:
                return null;
        }
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(this.context).inflate(R.layout.tablayout_invoices, null);
        ((TextView) v.findViewById(R.id.tv_tab_service_name)).setText(this.tabTitles[position]);
        return v;
    }

    public int getCount() {
        return this.mNumOfTabs;
    }

    public void selectPaidTabView(View view, Typeface fontOPenSansBold, int position) {
        if (view != null) {
            TextView serviceName = (TextView) view.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansBold);
            serviceName.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public void selectUnpaidTabView(View view, Typeface fontOPenSansBold) {
        if (view != null) {
            TextView serviceName = (TextView) view.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansBold);
            serviceName.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public void unselectPaidTabView(View view, Typeface fontOPenSansRegular) {
        if (view != null) {
            TextView serviceName = (TextView) view.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansRegular);
            serviceName.setTextColor(Color.parseColor("#000000"));
        }
    }

    public void unselectUnpaidTabView(View view, Typeface fontOPenSansRegular) {
        if (view != null) {
            TextView serviceName = (TextView) view.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansRegular);
            serviceName.setTextColor(Color.parseColor("#000000"));
        }
    }

    public void setDefaultOpningViewTab(View viewDefaultOPeningTab, Typeface font) {
        if (viewDefaultOPeningTab != null) {
            TextView serviceName = (TextView) viewDefaultOPeningTab.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(font);
            serviceName.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public void setSecondViewTab(View viewSecondTab, Typeface fontOPenSansBold) {
        if (viewSecondTab != null) {
            TextView serviceName = (TextView) viewSecondTab.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansBold);
            serviceName.setTextColor(Color.parseColor("#000000"));
        }
    }

    public void setTirdViewTab(View viewtirdTab, Typeface fontOPenSansBold) {
        if (viewtirdTab != null) {
            TextView serviceName = (TextView) viewtirdTab.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansBold);
            serviceName.setTextColor(Color.parseColor("#000000"));
        }
    }

    public void selectVODCatTabView(View view, Typeface fontOPenSansBold, int position) {
        if (view != null) {
            TextView serviceName = (TextView) view.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansBold);
            serviceName.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public void selectSERCatTabView(View view, Typeface fontOPenSansBold, int position) {
        if (view != null) {
            TextView serviceName = (TextView) view.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansBold);
            serviceName.setTextColor(Color.parseColor("#ffffff"));
        }
    }

    public void unSelectVODCatTabView(View view, Typeface fontOPenSansRegular) {
        if (view != null) {
            TextView serviceName = (TextView) view.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansRegular);
            serviceName.setTextColor(Color.parseColor("#000000"));
        }
    }

    public void unSelectSERCatTabView(View view, Typeface fontOPenSansRegular) {
        if (view != null) {
            TextView serviceName = (TextView) view.findViewById(R.id.tv_tab_service_name);
            serviceName.setTypeface(fontOPenSansRegular);
            serviceName.setTextColor(Color.parseColor("#000000"));
        }
    }
}
