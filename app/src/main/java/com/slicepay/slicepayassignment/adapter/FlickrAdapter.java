package com.slicepay.slicepayassignment.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.slicepay.slicepayassignment.R;
import com.slicepay.slicepayassignment.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to set the adapter for recyclerview.
 */

public class FlickrAdapter extends RecyclerView.Adapter<FlickrAdapter.MyViewHolder> implements Filterable {

    private final Activity activity;
    private final List<Photo> photoList;
    private List<Photo> photoListFiltered;

    public FlickrAdapter(Activity mainActivity, List<Photo> photoList) {
        this.activity = mainActivity;
        this.photoList = photoList;
        this.photoListFiltered = photoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_photo_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        Photo photo = photoListFiltered.get(position);
        holder.txtTitle.setText(photo.getTitle());
        String img = photo.getPhotoURL();

        Glide.with(activity).load(img).asBitmap().placeholder(R.drawable.flickr_logo).dontAnimate().into(new BitmapImageViewTarget(holder.mPhoto) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable bitmapDrawable =
                        RoundedBitmapDrawableFactory.create(activity.getResources(), resource);
                holder.mPhoto.setImageDrawable(bitmapDrawable);
            }
        });

    }

    @Override
    public int getItemCount() {
        int size = 0;
        if (photoListFiltered != null) {
            size = photoListFiltered.size();
        }
        return size;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    photoListFiltered = photoList;
                } else {
                    List<Photo> filteredList = new ArrayList<>();
                    for (Photo row : photoList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    photoListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = photoListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                photoListFiltered = (ArrayList<Photo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public final TextView txtTitle;
        public final ImageView mPhoto;

        public MyViewHolder(View view) {
            super(view);
            mPhoto = view.findViewById(R.id.flickr_image);
            txtTitle = view.findViewById(R.id.flickr_title);

        }
    }
}