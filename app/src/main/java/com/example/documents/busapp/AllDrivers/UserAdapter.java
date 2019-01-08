package com.example.documents.busapp.AllDrivers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.documents.busapp.R;

import java.util.Collections;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    public List<Buss> data = Collections.emptyList();
    Buss current;


    private UserClick lOnClickListener;
    public UserAdapter(UserClick listener) {
        lOnClickListener = listener;
    }
    public void setUsersData(List<Buss> recipesIn, Context context) {
        data = recipesIn;
        mContext = context;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.bus_sigle_layout;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder,final int position) {

        MyViewHolder myHolder = (MyViewHolder) holder;
        current = data.get(position);
        myHolder.number.setText(current.getBusNum());
        myHolder.avaSeats.setText(current.getAva_seats());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView number , avaSeats;
        RelativeLayout relativeLayout;

        public MyViewHolder(View itemView) {
            super(itemView);

           number = (TextView)itemView.findViewById(R.id.driver_info);
           avaSeats = (TextView)itemView.findViewById(R.id.driver_ava);
           imageView = (ImageView)itemView.findViewById(R.id.def_image);
           relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relative);
           itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            lOnClickListener.asd(data.get(clickedPosition));
        }
    }
    public void clear() {
        final int size = data.size();
        data.clear();
        notifyItemRangeRemoved(0, size);
    }
}
