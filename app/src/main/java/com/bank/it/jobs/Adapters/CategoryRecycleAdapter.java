package com.bank.it.jobs.Adapters;

import android.content.Context;
import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bank.it.jobs.Activities.AllTabs;
import com.bank.it.jobs.Models.CategoryModelClass;
import com.bank.it.jobs.R;

import java.util.List;


public class CategoryRecycleAdapter extends RecyclerView.Adapter<CategoryRecycleAdapter.MyViewHolder> {

    Context context;


    private List<CategoryModelClass> OfferList;


    public class MyViewHolder extends RecyclerView.ViewHolder {


        ImageView image;
        TextView title;


        public MyViewHolder(View view) {
            super(view);

            image = (ImageView) view.findViewById(R.id.image);
            title = (TextView) view.findViewById(R.id.title);
        }

    }


    public CategoryRecycleAdapter(Context context, List<CategoryModelClass> offerList) {
        this.OfferList = offerList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category_list, parent, false);


        return new MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final CategoryModelClass lists = OfferList.get(position);
        holder.image.setImageResource(lists.getImage());
        holder.title.setText(lists.getTitle());


        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                    Intent intent = new Intent(context, AllTabs.class);
                    intent.putExtra("layout", position);
                    context.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return OfferList.size();

    }

}


