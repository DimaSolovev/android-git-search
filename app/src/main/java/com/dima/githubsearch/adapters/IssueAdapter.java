package com.dima.githubsearch.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dima.githubsearch.R;
import com.dima.githubsearch.models.Issue;

import java.util.ArrayList;
import java.util.List;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.IssueViewHolder> {

    private List<Issue> issueList = new ArrayList<>();

    public void updateList(List<Issue> issues) {
        issueList = issues;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IssueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_item, parent, false);
        return new IssueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueViewHolder holder, int position) {
        Issue issue = issueList.get(position);
        holder.textViewIssueTitle.setText(issue.getTitle());
        holder.textViewIssueCreatedAt.setText(issue.getCreatedAt());
        String state = issue.getState();
        if (state.equals("open")) {
            holder.textViewIssueStatus.setTextColor(Color.RED);
        } else {
            holder.textViewIssueStatus.setTextColor(Color.GREEN);
        }
        holder.textViewIssueStatus.setText(state);
    }

    @Override
    public int getItemCount() {
        return issueList.size();
    }

    static class IssueViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewIssueTitle;
        private final TextView textViewIssueCreatedAt;
        private final TextView textViewIssueStatus;

        public IssueViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewIssueTitle = itemView.findViewById(R.id.textViewIssueTitle);
            textViewIssueCreatedAt = itemView.findViewById(R.id.textViewIssueCreatedAt);
            textViewIssueStatus = itemView.findViewById(R.id.textViewIssueStatus);
        }
    }
}
