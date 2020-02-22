package com.map.calendar;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ColorSpace;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.feed.manage.Article;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.models.Post;
import com.viewholder.PostViewHolder;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FeedFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    TextView tvLongitude;
    FrameLayout frameLayout;
    FirebaseAuth firebaseAuth;

    GpsTracker gpsTracker;
    Geocoder geocoder;
    String time;
    String location;

    private Activity mActivity;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Article, PostViewHolder> mAdapter;
    private RecyclerView mRecycler;

    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance(String param1, String param2) {
        FeedFragment fragment = new FeedFragment();
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
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void updateUI(List<Address> addresses) {
        String address;
        if(addresses.size() > 0){
            String countryName = addresses.get(0).getCountryName();
            String adminName = addresses.get(0).getAdminArea();
            String subadminName = addresses.get(0).getSubAdminArea();
            String thoroughfareName = addresses.get(0).getThoroughfare();
            String featureName = addresses.get(0).getFeatureName();
            String latitude = Double.toString(addresses.get(0).getLatitude());
            String longtitude = Double.toString(addresses.get(0).getLongitude());

            address = countryName + "  " + adminName + "  " + subadminName + "\n"
                    + thoroughfareName + "  " + featureName + "\n"
                    + latitude + "  " + longtitude + "\n" + time;
            location = address;
        }
        else {
            address = "";
        }
        try{
            tvLongitude.setText(address);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();

        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        tvLongitude = (TextView)view.findViewById(R.id.tvLongitude);
        frameLayout = (FrameLayout)view.findViewById(R.id.feedFrameLayout);


        mRecycler = view.findViewById(R.id.articles_list);
        //mRecycler.setHasFixedSize(true);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("articles");

        // Attach a listener to read the data at our posts reference
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Article article = dataSnapshot.getValue(Article.class);
                System.out.println(article);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        view.findViewById(R.id.fab_new_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity().getApplicationContext(), "New Article", Toast.LENGTH_SHORT).show();
                if(firebaseUser != null){
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, new EditorFragment(), "editorFragment");
                    ft.commit();
                    ft.addToBackStack(null);
                }
                else{
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, new AuthFragment(), "authFragment");
                    ft.commit();
                    ft.addToBackStack(null);
                }
            }
        });
        view.findViewById(R.id.fab_search).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "New Search", Toast.LENGTH_SHORT).show();
            }
        });
        view.findViewById(R.id.fab_auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(), "New Auth", Toast.LENGTH_SHORT).show();
                if(firebaseUser != null){
                    firebaseAuth.signOut();
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Report");
                    alertDialog.setMessage("SignOut!");
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
                else{
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, new AuthFragment(), "authFragment");
                    ft.commit();
                    ft.addToBackStack(null);
                }
            }
        });

        //registerForContextMenu(frameLayout);
        registerForContextMenu(mRecycler);
        registerForContextMenu(view);
        GridLayout gridLayout = (GridLayout)view.findViewById(R.id.gridLayout);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        gpsTracker = new GpsTracker(getActivity().getApplicationContext());
        if (mAdapter != null) {
            mAdapter.startListening();
        }

        final Handler ha=new Handler();
        ha.postDelayed(new Runnable() {

            @Override
            public void run() {

                ha.postDelayed(this, 5000);
            }
        }, 100);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }
    private void onStarClicked(DatabaseReference postRef) {
        postRef.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Post p = mutableData.getValue(Post.class);
                if (p == null) {
                    return Transaction.success(mutableData);
                }

                if (p.stars.containsKey(getEmail())) {
                    // Unstar the post and remove self from stars
                    p.starCount = p.starCount - 1;
                    p.stars.remove(getEmail());
                } else {
                    // Star the post and add self to stars
                    p.starCount = p.starCount + 1;
                    p.stars.put(getEmail(), true);
                }

                // Set value and report transaction success
                mutableData.setValue(p);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean b, DataSnapshot dataSnapshot) {
                Log.d("postTransaction", "onComplete:" + dataSnapshot.getKey());
            }
        });
    }

    public String getEmail() {
        String mEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        return mEmail;
    }

    public Query getQuery(DatabaseReference databaseReference){
        Query mQuery = databaseReference.child("articles");
        return mQuery;
    };


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
    public void onCreateContextMenu(final ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Editor");
        menu.add(0, v.getId(), 0, "Search");
        final FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser != null){
            menu.add(0, v.getId(), 0, "SignOut");
        }
        else{
            menu.add(0, v.getId(), 0, "SignIn");
        }

        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if(firebaseUser != null){
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, new EditorFragment(), "editorFragment");
                    ft.commit();
                    ft.addToBackStack(null);
                }
                else{
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, new AuthFragment(), "authFragment");
                    ft.commit();
                    ft.addToBackStack(null);
                }
                return false;
            }

        });
        menu.getItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return false;
            }
        });
        menu.getItem(2).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                if(firebaseUser != null){
                    firebaseAuth.signOut();
                    item.setTitle("SignIn");
                }
                else{
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.frameLayout, new AuthFragment(), "authFragment");
                    ft.commit();
                    ft.addToBackStack(null);
                }
                return false;
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();

        final Dialog mDialog = new Dialog(mActivity, R.style.NewDialog);
        mDialog.addContentView(
                new ProgressBar(mActivity),
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        );
        mDialog.setCancelable(true);
        mDialog.show();

        // Set up Layout Manager, reverse layout
        LinearLayoutManager mManager = new LinearLayoutManager(mActivity);
        mManager.setReverseLayout(true);
        mManager.setStackFromEnd(true);
        mRecycler.setLayoutManager(mManager);

        // Set up FirebaseRecyclerAdapter with the Query
        Query postsQuery = getQuery(mDatabase);

        FirebaseRecyclerOptions<Article> options = new FirebaseRecyclerOptions.Builder<Article>()
                .setQuery(postsQuery, Article.class)
                .build();

        mAdapter = new FirebaseRecyclerAdapter<Article, PostViewHolder>(options) {
            @Override
            protected void onBindViewHolder(PostViewHolder viewHolder, int position, final Article model) {
                final DatabaseReference postRef = getRef(position);

                viewHolder.bindToPost(model, new View.OnClickListener() {
                    @Override
                    public void onClick(View starView) {
                        // Need to write to both places the post is stored
                        DatabaseReference globalPostRef = mDatabase.child("articles").child(postRef.getKey());
                        DatabaseReference userPostRef = mDatabase.child("articles").child(model.getArticleID()).child(postRef.getKey());

                        // Run two transactions
                        onStarClicked(globalPostRef);
                        onStarClicked(userPostRef);
                    }
                });

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /*Intent intent = new Intent(mActivity, PostDetailActivity.class);
                        intent.putExtra(PostDetailActivity.EXTRA_POST_KEY, postRef.getKey());
                        startActivity(intent);*/
                    }
                });
            }

            @Override
            public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                return new PostViewHolder(inflater.inflate(R.layout.item_post, viewGroup, false));
            }

            @Override
            public void onDataChanged() {
                super.onDataChanged();
                mDialog.dismiss();
            }
        };
        mRecycler.setAdapter(mAdapter);
    }
}
