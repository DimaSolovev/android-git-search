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
import com.dima.githubsearch.models.RepoPayload;
import com.squareup.picasso.Picasso;

public class RepoAdapter extends RecyclerView.Adapter<RepoAdapter.RepoViewHolder> {

    private RepoPayload mRepoPayload;
    private Context context;
    private OnReachEndListener onReachEndListener;
    private OnClickListener onClickListener;

    public void adapterClear() {
        mRepoPayload.clearItems();
        notifyDataSetChanged();
    }

    public RepoAdapter(Context context) {
        this.context = context;
        mRepoPayload = new RepoPayload();
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

    public void updateList(RepoPayload repoPayload) {
        mRepoPayload = repoPayload;
        notifyDataSetChanged();
    }

    public RepoPayload getReposPayload() {
        return mRepoPayload;
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.repo_item, parent, false);
        return new RepoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        holder.setIsRecyclable(false);
        Picasso.get().load(mRepoPayload.getItems().get(position).getOwner().getAvatarUrl())
                .into(holder.imageViewAvatar);
        holder.textViewRepositoryName.setText(mRepoPayload.getItems().get(position).getFullName());
        holder.textViewRepositoryDescription.setText(mRepoPayload.getItems().get(position).getDescription());
        if (position == mRepoPayload.getItems().size() - 1 && onReachEndListener != null) {
            onReachEndListener.onReachEnd();
        }
    }

    @Override
    public int getItemCount() {
        return mRepoPayload.getItems().size();
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
