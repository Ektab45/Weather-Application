package com.example.weatherforecast;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;

    private ArrayList<ForecastModel> data;

    public RecyclerViewAdapter(Context context, ArrayList<ForecastModel> data){
        this.context = context;
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

       private TextView  dayTv;

       private TextView timeTv;
private TextView   smallTempTv;
       private ImageView iconTv;

        public ViewHolder(@NonNull View itemView){
            super(itemView);


            dayTv = itemView.findViewById(R.id.dayTv);
            timeTv = itemView.findViewById(R.id.timeTv);
            smallTempTv = itemView.findViewById(R.id.smallTempTv);
            iconTv = itemView.findViewById(R.id.iconTv);

        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.forecast_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ForecastModel item = data.get(position);

        holder.timeTv.setText(item.getTime());
        holder.dayTv.setText(item.getDayTv());
        holder.smallTempTv.setText(item.getTemperature());

        Picasso.get().load(item.getImageUrl()).into(holder.iconTv);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setPosts(ArrayList<ForecastModel> data) {
        this.data = data;

        notifyDataSetChanged();
    }
}
