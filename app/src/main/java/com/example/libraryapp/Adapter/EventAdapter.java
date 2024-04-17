package com.example.libraryapp.Adapter;

import android.content.Context;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.libraryapp.Model.Events;
import com.example.libraryapp.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.SlideViewHolder>{

    List<Events> eventsList;
    int layout;
    Context context;
    ViewPager2 viewPager2;

    public EventAdapter(List<Events> eventsList, int id, Context context,ViewPager2 viewPager2) {
        this.eventsList = eventsList;
        layout = id;
        this.context = context;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View slideViewHolder = LayoutInflater.from(context).inflate(layout,parent,false);
        return new SlideViewHolder(slideViewHolder);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.setImg(eventsList.get(position));
        if(position == eventsList.size() -2)
        {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return eventsList.size();
    }

    class SlideViewHolder extends RecyclerView.ViewHolder
    {
        private RoundedImageView img;

        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.slide_event);
        }
        private void setImg(Events events)
        {
            img.setImageResource(events.getImg());
        }
    }
    private Runnable runnable= new Runnable() {
        @Override
        public void run() {
            eventsList.addAll(eventsList);
            notifyDataSetChanged();
        }
    };
}
