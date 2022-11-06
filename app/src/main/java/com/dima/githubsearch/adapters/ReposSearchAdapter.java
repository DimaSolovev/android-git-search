package com.dima.githubsearch.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dima.githubsearch.R;
import com.dima.githubsearch.activity.RepoDetailActivity;
import com.dima.githubsearch.models.ReposPayload;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class ReposSearchAdapter extends RecyclerView.Adapter<ReposSearchAdapter.ReposSearchViewHolder> {

    private ReposPayload mReposPayload;
    private Context context;
    private OnReachEndListener onReachEndListener;
    private OnClickListener onClickListener;

    public ReposSearchAdapter(Context context) {
        this.context = context;
        mReposPayload = new ReposPayload();
    }

    public interface OnReachEndListener {
        void onReachEnd();
    }

    public interface OnClickListener {
        void onClick(int id);
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void updateList(ReposPayload reposPayload) {
        mReposPayload = reposPayload;
        notifyDataSetChanged();
    }

    public ReposPayload getmReposPayload() {
        return mReposPayload;
    }

    @NonNull
    @Override
    public ReposSearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_item, parent, false);
        return new ReposSearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReposSearchViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Picasso.get().load(mReposPayload.getItems().get(position).getOwner().getAvatarUrl())
                .into(holder.imageViewAvatar);
        holder.textViewRepositoryName.setText(mReposPayload.getItems().get(position).getFullName());
        holder.textViewRepositoryDescription.setText(mReposPayload.getItems().get(position).getDescription());
        if (position == mReposPayload.getItems().size() - 1 && onReachEndListener != null) {
            onReachEndListener.onReachEnd();
        }
    }

    @Override
    public int getItemCount() {
        return mReposPayload.getItems().size();
    }

    class ReposSearchViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewAvatar;
        private TextView textViewRepositoryName;
        private TextView textViewRepositoryDescription;

        public ReposSearchViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.avatar);
            textViewRepositoryName = itemView.findViewById(R.id.repositoryName);
            textViewRepositoryDescription = itemView.findViewById(R.id.repositoryDescription);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onClickListener != null) {
                        onClickListener.onClick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
