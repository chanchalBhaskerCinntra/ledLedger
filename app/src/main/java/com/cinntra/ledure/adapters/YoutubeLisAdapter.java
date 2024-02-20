package com.cinntra.ledure.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cinntra.ledure.R;
import com.cinntra.ledure.activities.ExoPlayerActivity;
import com.cinntra.ledure.activities.MainActivity_B2C;
import com.cinntra.ledure.activities.Reports;
import com.cinntra.ledure.globals.Globals;


import java.util.ArrayList;


public class YoutubeLisAdapter extends RecyclerView.Adapter<YoutubeLisAdapter.ViewHolder> {
    Context context;
    ArrayList<String> youtubelist;
    public YoutubeLisAdapter(Context context, ArrayList<String> youtubelist)
      {
    this.context    = context;
    this.youtubelist    = youtubelist;

        }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
      {
     View rootView = LayoutInflater.from(context).inflate(R.layout.youtube_adapter,parent,false);
     return new ViewHolder(rootView);
       }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
      {

         String url =  "https://img.youtube.com/vi/"+ youtubelist.get(position) + "/1.jpg";
          Glide.with(context).load(url).into(holder.youtubeview);


          holder.icon.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
             /*     try{
             Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + youtubelist.get(position)));
                      context.startActivity(i);
                  }catch (Exception e){
                      Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v="+youtubelist.get(position)));
                      context.startActivity(browserIntent);                  }
*/
//                  Globals.videoUrl = "vnd.youtube:" + youtubelist.get(position);

                //context.startActivity(new Intent(context, ExoPlayerActivity.class));

              }
          });

    }
    @Override
    public int getItemCount()
      {
    return youtubelist.size();
      }



    class ViewHolder extends RecyclerView.ViewHolder
        {

        ImageView youtubeview,icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            youtubeview = itemView.findViewById(R.id.youtubelist);
            icon = itemView.findViewById(R.id.icon);


        }
    }
}
