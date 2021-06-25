package com.example.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.R;
import com.example.flixster.models.CastMember;
import com.example.flixster.models.Movie;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class CastMemberAdapter extends RecyclerView.Adapter<CastMemberAdapter.ViewHolder> {

    Context context;
    List<CastMember> cast;

    public CastMemberAdapter(Context context, List<CastMember> cast){
        this.context = context;
        this.cast = cast;
    }

    // inflating a layout from XML and returning the holder
    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View castView = LayoutInflater.from(context).inflate(R.layout.item_cast_member, parent, false);
        return new ViewHolder(castView);
    }

    // populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        // get the movie at the passed in position
        CastMember castMember = cast.get(position);
        // Bind the movie into the VH
        holder.bind(castMember);
    }

    // Returns the total count of items
    @Override
    public int getItemCount() {
        return cast.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private int RADIUS = 50;
        private int MARGIN = 0;

        TextView tvName;
        TextView tvCharacter;
        ImageView ivProfile;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvCharacter = itemView.findViewById(R.id.tvCharacter);
            ivProfile = itemView.findViewById(R.id.ivProfile);
        }

        public void bind(CastMember castMember) {
            tvName.setText(castMember.getName());
            tvCharacter.setText(castMember.getCharacter());

            String imageUrl = castMember.getProfilePath();
            int placeholderUrl = R.drawable.flicks_movie_placeholder;

            Glide.with(context)
                    .load(imageUrl)
                    .centerCrop()
                    .transform(new RoundedCornersTransformation(RADIUS, MARGIN))
                    .placeholder(placeholderUrl)
                    .into(ivProfile);
        }
    }
}
