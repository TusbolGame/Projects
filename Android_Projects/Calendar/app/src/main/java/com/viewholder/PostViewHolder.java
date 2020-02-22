package com.viewholder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.feed.manage.Article;
import com.feed.manage.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.map.calendar.GpsTracker;
import com.map.calendar.R;
import com.models.Post;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

public class PostViewHolder extends RecyclerView.ViewHolder{
	public ImageView starView;
	private TextView authorView;
	private TextView bodyView;
	private TextView numStarsView;
	private TextView titleView;
	private View superView;

	GpsTracker gpsTracker;
	Geocoder geocoder;
	String time;
	String location;

	int mWidth;
	int mHeight;
	int mMargin_top;
	int mMaring_bottom;
	private BroadcastReceiver mReceiver;

	public PostViewHolder(View itemView) {
		super(itemView);
		superView = itemView;
		titleView = itemView.findViewById(R.id.post_title);
		authorView = itemView.findViewById(R.id.post_author);
		starView = itemView.findViewById(R.id.star);
		numStarsView = itemView.findViewById(R.id.post_num_stars);
		bodyView = itemView.findViewById(R.id.post_body);
		gpsTracker = new GpsTracker(itemView.getContext());

		mWidth = superView.getMeasuredWidth();
		mHeight = superView.getMeasuredHeight();
		ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) superView.getLayoutParams();
		mMargin_top = lp.topMargin;
		mMaring_bottom = lp.bottomMargin;

		gpsTracker = new GpsTracker(itemView.getContext().getApplicationContext());

	}

	public void bindToPost(final Article article, View.OnClickListener starClickListener) {
		String lat = article.getLocation().substring(0, article.getLocation().indexOf("#"));
		String lon = article.getLocation().substring(article.getLocation().indexOf("#") + 1);
		double dLat = Double.valueOf(lat);
		double dLon = Double.valueOf(lon);
		setProperty(dLat, dLon);
		final double latd = Double.valueOf(lat);
		final double lond = Double.valueOf(lon);
		final String userEmail = article.getUserEmail();
		List<User> user;
		FirebaseDatabase.getInstance().getReference().child("users").orderByChild("userEmail").equalTo(userEmail).addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
				List<User> users = new ArrayList<>();
				while (dataSnapshots.hasNext()) {
					DataSnapshot dataSnapshotChild = dataSnapshots.next();
					User user = dataSnapshotChild.getValue(User.class);
					users.add(user);
				}
				titleView.setText(users.get(0).getUserLocation() + ":" + users.get(0).getUserTime());
			}
			@Override
			public void onCancelled(DatabaseError databaseError) {

			}
		});

		authorView.setText(article.getUserEmail());
		numStarsView.setText("1");
		bodyView.setText(article.getArticleContent());
		starView.setOnClickListener(starClickListener);

		final Handler ha=new Handler();
		ha.postDelayed(new Runnable() {

			@Override
			public void run() {
				setProperty(latd, lond);
				ha.postDelayed(this, 5000);
			}
		}, 5000);
	}
	private void setProperty(double lat, double lon){
		//superView.setVisibility(View.GONE);

		double latitude = 0.0d;
		double longtitude = 0.0d;
		geocoder = new Geocoder(superView.getContext(), Locale.getDefault());
		List<Address> addresses = null;
		try {
			addresses = geocoder.getFromLocation(gpsTracker.getLatitude(), gpsTracker.getLongitude(), 1);
			time = DateFormat.getTimeInstance().format(gpsTracker.getLocation().getTime());
		}
		catch (Exception e){
			e.printStackTrace();
		}
		if(addresses != null){
			latitude = gpsTracker.getLatitude();
			longtitude = gpsTracker.getLongitude();
		}
		LatLng latLngA = new LatLng(lat,lon);
		LatLng latLngB = new LatLng(latitude,longtitude);

		Location locationA = new Location("point A");
		locationA.setLatitude(latLngA.latitude);
		locationA.setLongitude(latLngA.longitude);
		Location locationB = new Location("point B");
		locationB.setLatitude(latLngB.latitude);
		locationB.setLongitude(latLngB.longitude);

		double distance = locationA.distanceTo(locationB);
		if(distance > 1000.0d){
			superView.getLayoutParams().height = 0;
			superView.getLayoutParams().width = 0;

			ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) superView.getLayoutParams();
			p.setMargins(0, 0, 0, 0);

			superView.requestLayout();
		}
		else{
			superView.setVisibility(View.VISIBLE);
		}
	}
}