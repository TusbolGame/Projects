package com.liveitandroid.liveit.view.adapter;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.liveitandroid.liveit.view.activity.HitsInfoSelectActivity;
import com.liveitandroid.liveit.view.activity.JornaisActivitySelect;
import com.liveitandroid.liveit.view.activity.PraiasActivitySelect;
import com.liveitandroid.liveit.view.activity.ProgramasActivitySelect;
import com.liveitandroid.liveit.view.activity.RadiosActivitySelect;
import com.liveitandroid.liveit.R;
import com.liveitandroid.liveit.helper.SessionManager;
import com.liveitandroid.liveit.helper.Urls;
import com.liveitandroid.liveit.helper.XMLParser;
import com.liveitandroid.liveit.miscelleneious.common.Utils;
import com.liveitandroid.liveit.model.ListMoviesSetterGetter;
import com.liveitandroid.liveit.view.activity.HitsInfoSelectActivity;
import com.liveitandroid.liveit.view.activity.JornaisActivitySelect;
import com.liveitandroid.liveit.view.activity.PraiasActivitySelect;
import com.liveitandroid.liveit.view.activity.ProgramasActivitySelect;
import com.liveitandroid.liveit.view.activity.RadiosActivitySelect;

import org.json.JSONObject;
import org.json.XML;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VodAdapterRest extends Adapter<VodAdapterRest.MyViewHolder> {
    private List<ListMoviesSetterGetter> completeList;
    private Context context;
    private List<ListMoviesSetterGetter> filterList;
    private List<ListMoviesSetterGetter> moviesListl;
    public int text_last_size;
    public int text_size;
    public String categoryName, grupo;
    public Intent intent;
    public SessionManager mSessionManager;
    public String program_url = "";
    public VodAdapterRest() {

    }

    public static class MyViewHolder extends ViewHolder {
        @BindView(R.id.iv_foraward_arrow)
        ImageView ivForawardArrow;
        @BindView(R.id.iv_tv_icon)
        ImageView ivTvIcon;
        @BindView(R.id.pb_paging_loader)
        ProgressBar pbPagingLoader;
        @BindView(R.id.rl_list_of_categories)
        RelativeLayout rlListOfCategories;
        @BindView(R.id.rl_outer)
        RelativeLayout rlOuter;
        @BindView(R.id.tv_movie_category_name)
        TextView tvMovieCategoryName;
        @BindView(R.id.tv_sub_cat_count)
        TextView tvXubCount;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            setIsRecyclable(false);
        }
    }

    private class OnFocusChangeAccountListener implements OnFocusChangeListener {
        private final View view;

        public OnFocusChangeAccountListener(View view) {
            this.view = view;
        }

        @SuppressLint({"ResourceType"})
        public void onFocusChange(View v, boolean hasFocus) {
            float to = 1.09f;
            if (hasFocus) {
                if (!hasFocus) {
                    to = 1.0f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                Log.e("id is", "" + this.view.getTag());
                this.view.setBackgroundResource(R.drawable.shape_list_categories_focused);
            } else if (!hasFocus) {
                if (!hasFocus) {
                    to = 1.0f;
                }
                performScaleXAnimation(to);
                performScaleYAnimation(to);
                performAlphaAnimation(hasFocus);
                this.view.setBackgroundResource(R.drawable.shape_list_categories);
            }
        }

        private void performScaleXAnimation(float to) {
            ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(this.view, "scaleX", new float[]{to});
            scaleXAnimator.setDuration(150);
            scaleXAnimator.start();
        }

        private void performScaleYAnimation(float to) {
            ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(this.view, "scaleY", new float[]{to});
            scaleYAnimator.setDuration(150);
            scaleYAnimator.start();
        }

        private void performAlphaAnimation(boolean hasFocus) {
            if (hasFocus) {
                float toAlpha = hasFocus ? 0.6f : 0.5f;
                ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(this.view, "alpha", new float[]{toAlpha});
                alphaAnimator.setDuration(150);
                alphaAnimator.start();
            }
        }
    }

    public VodAdapterRest(List<ListMoviesSetterGetter> completeList, Context context) {
        this.filterList = new ArrayList();
        this.filterList.addAll(completeList);
        this.completeList = completeList;
        this.moviesListl = completeList;
        this.context = context;
        mSessionManager = new SessionManager(context);
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_vod_new_flow_list_item, parent, false));
    }

    public ListMoviesSetterGetter ObjSelect;
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {
        categoryName = ((ListMoviesSetterGetter) this.completeList.get(listPosition)).getPraias_name();
        grupo = ((ListMoviesSetterGetter) this.completeList.get(listPosition)).getPraias_group();
        holder.tvMovieCategoryName.setText(categoryName);
        holder.rlOuter.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if(grupo.equals("praias"))
                {
                    intent = new Intent(context, PraiasActivitySelect.class);
                }else if(grupo.equals("radios"))
                {
                    intent = new Intent(context, RadiosActivitySelect.class);
                }else if(grupo.equals("hits"))
                {
                    intent = new Intent(context, HitsInfoSelectActivity.class);
                    intent.putExtra("selGrupoID", completeList.get(listPosition).getPraias_id());
                    intent.putExtra("selGrupoImg", completeList.get(listPosition).getPraias_imagem());
                } else if(grupo.equals("programas"))
                {
                    intent = new Intent(context, ProgramasActivitySelect.class);
                }
                else if(grupo.equals("jornais"))
                {
                    intent = new Intent(context, JornaisActivitySelect.class);
                    intent.putExtra("selGrupoURL", completeList.get(listPosition).getPraias_url());
                }

                if(grupo.equals("programasSelect"))
                {
                    String urlData = completeList.get(listPosition).getPraias_url();
                    ObjSelect = completeList.get(listPosition);
                    int splitInd = urlData.indexOf(";;;");
                    if (splitInd > -1) {
                        String targetURL = urlData.substring(splitInd + 3);
                        String canalType = urlData.substring(0, splitInd);
                        program_url = mSessionManager.getUserAcess_Token() + "PHP/liveit/searchurl.php?url=" + targetURL + "&canal=" + canalType + "&client_secret=" + mSessionManager.getClient_Secret();
                        new GetProgramURL().execute();

                    } else {
                        Utils.PlayerSetNovo(completeList.get(listPosition), context, "FilmesAPP");
                    }
                }else
                {
                    intent.putExtra("selType", completeList.get(listPosition).getPraias_name());
                    context.startActivity(intent);
                }
            }
        });
        holder.rlOuter.setOnFocusChangeListener(new OnFocusChangeAccountListener(holder.rlOuter));
        int count = 0;
        if (count == 0 || count == -1) {
            holder.tvXubCount.setText("");
        } else {
            holder.tvXubCount.setText(String.valueOf(count));
        }
    }

    JSONObject jsonURLList;
    public class GetProgramURL extends AsyncTask<String, String, JSONObject> {

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                XMLParser parser = new XMLParser();
                String xml = parser.getXmlFromUrl(program_url);    // getting XML
                jsonURLList = null;
                jsonURLList = XML.toJSONObject(xml);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonURLList;
        }

        @Override
        protected void onPostExecute(JSONObject jsonURLList) {
            super.onPostExecute(jsonURLList);

            String realURL = "";
            JSONObject urlInfoObj = null;
            try {
                if (jsonURLList == null || jsonURLList.length() == 0) {
                    //   Toast.makeText(PraiasActivity.this, "Wrong User Id or Password", Toast.LENGTH_SHORT).show();
                } else {
                    JSONObject rootInfoObj = jsonURLList.getJSONObject("root");
                    urlInfoObj = null;
                    if (rootInfoObj != null) {
                        urlInfoObj = rootInfoObj.getJSONObject("info");
                    }
                    if (urlInfoObj != null) {
                        realURL = urlInfoObj.optString("url");
                    }

                    if (realURL != "") {
                        byte[] data = Base64.decode(realURL, Base64.DEFAULT);
                        String Reideus = new String(data, "UTF-8");
                        ObjSelect.setPraias_url(Reideus);
                        Utils.PlayerSetNovo(ObjSelect, context, "Programas");
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int getItemCount() {
        return this.moviesListl.size();
    }

    public void setVisibiltygone(ProgressBar pbPagingLoader) {
        if (pbPagingLoader != null) {
            pbPagingLoader.setVisibility(View.GONE);
        }
    }
}
