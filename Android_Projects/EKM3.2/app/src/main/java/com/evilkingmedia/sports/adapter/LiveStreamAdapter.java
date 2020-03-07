package com.evilkingmedia.sports.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import com.evilkingmedia.Constant;
import com.evilkingmedia.R;
import com.evilkingmedia.demand.WebViewActivity;
import com.evilkingmedia.model.SportsModel;

public class LiveStreamAdapter extends RecyclerView.Adapter<LiveStreamAdapter.myview>  {
    private List<SportsModel> sportsModels;
    private Context context;


    public class myview extends RecyclerView.ViewHolder {

        private CardView card_view;
        private TextView txtTeam, txtTime;

        public myview(View view) {
            super(view);
            card_view = view.findViewById(R.id.sport_card_view);
            txtTeam = view.findViewById(R.id.sportDate);
            txtTime = view.findViewById(R.id.sportTime);
        }
    }

    public LiveStreamAdapter(List<SportsModel> sportsModels, Context context) {
        this.context = context;
        this.sportsModels = sportsModels;
    }

    @NonNull
    @Override
    public LiveStreamAdapter.myview onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sport_item, parent, false);

        return new LiveStreamAdapter.myview(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final LiveStreamAdapter.myview holder, final int position) {

        SportsModel model = sportsModels.get(position);
        holder.txtTeam.setText(model.getTeam1());
        holder.txtTime.setText(model.getTime());

        String tempUrl = model.getLinkNodeString();
        String splitStr = tempUrl.split(",")[0].split("/")[1];
        String videoPath = splitStr.substring(0, splitStr.length()-1);

        holder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("videoUrl", Constant.SPORTS_HULK_STREAM_URL + videoPath);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return sportsModels.size();
    }

}

