package com.dima.githubsearch.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dima.githubsearch.R;
import com.dima.githubsearch.models.Repo;
import com.dima.githubsearch.models.RepoPayload;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {

    private List<Repo> repoList = new ArrayList<>();
    private Context context;
    private OnReachEndListener onReachEndListener;
    private OnClickListener onClickListener;

    public RepoAdapter(Context context) {
        this.context = context;
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

    public void updateList(List<Repo> repos) {
        repoList = repos;
        notifyDataSetChanged();
    }

    public List<Repo> getRepoList() {
        return repoList;
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_item, parent, false);
        return new RepoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        Repo repo = repoList.get(position);
        Picasso.get().load(repo.getOwner().getAvatarUrl())
                .placeholder(R.drawable.def)
                .into(holder.imageViewAvatar);
        holder.textViewRepositoryName.setText(repo.getFullName());
        holder.textViewRepositoryDescription.setText(repo.getDescription());
        if (position == repoList.size() - 1 && onReachEndListener != null) {
            onReachEndListener.onReachEnd();
        }
    }

    @Override
    public int getItemCount() {
        return repoList.size();
    }

    class RepoViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageViewAvatar;
        private final TextView textViewRepositoryName;
        private final TextView textViewRepositoryDescription;

        public RepoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewAvatar = itemView.findViewById(R.id.avatar);
            textViewRepositoryName = itemView.findViewById(R.id.repositoryName);
            textViewRepositoryDescription = itemView.findViewById(R.id.repositoryDescription);
            itemView.setOnClickListener(v -> {
                if (onClickListener != null) {
                    onClickListener.onClick(getAdapterPosition());
                }
            });
        }
    }
}
