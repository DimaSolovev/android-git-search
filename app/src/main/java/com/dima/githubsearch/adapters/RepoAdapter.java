package com.dima.githubsearch.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dima.githubsearch.R;
import com.dima.githubsearch.models.Repo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {

    private final List<Repo> repos = new ArrayList<>();

    private OnReachEndListener onReachEndListener;
    private OnRepoClickListener onRepoClickListener;

    public interface OnReachEndListener {
        void onReachEnd();
    }

    public interface OnRepoClickListener {
        void onRepoClick(Repo repo);
    }

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    public void setOnRepoClickListener(OnRepoClickListener onRepoClickListener) {
        this.onRepoClickListener = onRepoClickListener;
    }

    public void setRepos(List<Repo> reposList) {
        repos.clear();
        repos.addAll(reposList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.repo_item, parent, false);
        return new RepoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        Repo repo = repos.get(position);
        Picasso.get().load(repo.getOwner().getAvatarUrl())
                .placeholder(R.drawable.def)
                .into(holder.imageViewAvatar);
        holder.textViewRepositoryName.setText(repo.getFullName());
        holder.textViewRepositoryDescription.setText(repo.getDescription());
        if (position == repos.size() - 10 && onReachEndListener != null && repos.size() > 10) {
            onReachEndListener.onReachEnd();
        }
        holder.itemView.setOnClickListener(v -> onRepoClickListener.onRepoClick(repo));
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    static class RepoViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewAvatar;
        private final TextView textViewRepositoryName;
        private final TextView textViewRepositoryDescription;

        public RepoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.avatar);
            textViewRepositoryName = itemView.findViewById(R.id.repositoryName);
            textViewRepositoryDescription = itemView.findViewById(R.id.repositoryDescription);
        }
    }
}
