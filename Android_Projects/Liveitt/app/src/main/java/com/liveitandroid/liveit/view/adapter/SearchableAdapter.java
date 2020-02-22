package com.liveitandroid.liveit.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.miscelleneious.common.AppConst;
import com.liveitandroid.liveit.model.FavouriteDBModel;
import com.liveitandroid.liveit.model.LiveStreamsDBModel;
import com.liveitandroid.liveit.model.database.DatabaseHandler;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class SearchableAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private String currentPlayingNum;
    private DatabaseHandler database;
    private ArrayList<LiveStreamsDBModel> filteredData = null;
    public ViewHolder holder;
    private ItemFilter mFilter = new ItemFilter();
    private LayoutInflater mInflater;
    public ArrayList<LiveStreamsDBModel> originalData;

    private class ItemFilter extends Filter {
        private ItemFilter() {
        }

        protected FilterResults performFiltering(CharSequence constraint) {
            String filterString = constraint.toString().toLowerCase();
            FilterResults results = new FilterResults();
            ArrayList<LiveStreamsDBModel> list = SearchableAdapter.this.originalData;
            int count = list.size();
            ArrayList<LiveStreamsDBModel> nlist = new ArrayList(count);
            for (int i = 0; i < count; i++) {
                LiveStreamsDBModel filterableString = (LiveStreamsDBModel) list.get(i);
                if (filterableString.getName().toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }
            results.values = nlist;
            results.count = nlist.size();
            return results;
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            SearchableAdapter.this.filteredData = (ArrayList) results.values;
            SearchableAdapter.this.notifyDataSetChanged();
        }
    }

    static class ViewHolder {
        TextView channel_number;
        ImageView favourite;
        ImageView image;
        LinearLayout ll_list_view;
        TextView text;
        ImageView tv_currently_playing;

        ViewHolder() {
        }
    }
    private SessionManager mSessionManager;
    public SearchableAdapter(Context context, ArrayList<LiveStreamsDBModel> data) {
        this.filteredData = data;
        this.originalData = data;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        this.database = new DatabaseHandler(context);
        mSessionManager = new SessionManager(context);
    }

    public void fetchCurrentlyPlayingChannel() {
        this.currentPlayingNum = this.context.getSharedPreferences(AppConst.LOGIN_PREF_CURRENTLY_PLAYING_VIDEO, 0).getString(AppConst.LOGIN_PREF_CURRENTLY_PLAYING_VIDEO, "");
    }

    public int getCount() {
        return this.filteredData.size();
    }

    public Object getItem(int position) {
        return this.filteredData.get(position);
    }

    public ArrayList<LiveStreamsDBModel> getFilteredData() {
        return this.filteredData;
    }

    public long getItemId(int position) {
        return (long) position;
    }

    @SuppressLint({"InflateParams"})
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            try {
                convertView = this.mInflater.inflate(R.layout.channel_list, null);
            } catch (Exception e) {
                e.getStackTrace();
            }
            if (convertView != null) {
                this.holder = new ViewHolder();
                this.holder.channel_number = (TextView) convertView.findViewById(R.id.tv_channel_number);
                this.holder.text = (TextView) convertView.findViewById(R.id.list_view);
                this.holder.image = (ImageView) convertView.findViewById(R.id.tv_logo);
                this.holder.ll_list_view = (LinearLayout) convertView.findViewById(R.id.ll_list_view);
                this.holder.favourite = (ImageView) convertView.findViewById(R.id.iv_favourite);
                this.holder.tv_currently_playing = (ImageView) convertView.findViewById(R.id.tv_currently_playing);
                convertView.setTag(this.holder);
            }
        } else {
            this.holder = (ViewHolder) convertView.getTag();
        }
        this.holder.text.setText(((LiveStreamsDBModel) this.filteredData.get(position)).getName());
        ArrayList<FavouriteDBModel> checkFavourite = this.database.checkFavourite(Integer.parseInt(((LiveStreamsDBModel) this.filteredData.get(position)).getStreamId()), ((LiveStreamsDBModel) this.filteredData.get(position)).getCategoryId(), "live");
        if (checkFavourite == null || checkFavourite.size() <= 0) {
            try {
                this.holder.favourite.setVisibility(4);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        } else {
            try {
                this.holder.favourite.setVisibility(0);
            } catch (Exception e22) {
                e22.printStackTrace();
            }
        }

        if(mSessionManager.getOrdena().equals("0")){
            this.holder.channel_number.setText(((LiveStreamsDBModel) this.filteredData.get(position)).getStreamId());
        }else if(mSessionManager.getOrdena().equals("1"))
        {
            this.holder.channel_number.setText(((LiveStreamsDBModel) this.filteredData.get(position)).getNum());
        }else{
            this.holder.channel_number.setText(((LiveStreamsDBModel) this.filteredData.get(position)).getNum());
        }

        fetchCurrentlyPlayingChannel();
        if (this.currentPlayingNum.equals("") || !this.currentPlayingNum.equals(((LiveStreamsDBModel) this.filteredData.get(position)).getNum())) {
            this.holder.tv_currently_playing.setVisibility(8);
        } else {
            this.holder.tv_currently_playing.setVisibility(0);
        }
        if (((LiveStreamsDBModel) this.filteredData.get(position)).getStreamIcon() != null && !((LiveStreamsDBModel) this.filteredData.get(position)).getStreamIcon().equals("")) {
            Picasso.with(this.context).load(((LiveStreamsDBModel) this.filteredData.get(position)).getStreamIcon()).placeholder((int) R.drawable.logo).into(this.holder.image);
        } else if (VERSION.SDK_INT >= 21) {
            this.holder.image.setImageDrawable(this.context.getResources().getDrawable(R.drawable.logo, null));
        }
        return convertView;
    }

    public Filter getFilter() {
        return this.mFilter;
    }
}
