package com.dima.githubsearch.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dima.githubsearch.R;
import com.dima.githubsearch.models.Issue;
import com.dima.githubsearch.utils.TimeUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class IssueAdapter extends RecyclerView.Adapter<IssueAdapter.IssueViewHolder> {

    private static final String TYPE_OPEN = "open";
    private List<Issue> issues = new ArrayList<>();

    public void updateList(List<Issue> issuesList) {
        issues = issuesList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IssueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.issue_item, parent, false);
        return new IssueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IssueViewHolder holder, int position) {
        Issue issue = issues.get(position);
        holder.textViewIssueTitle.setText(issue.getTitle());
        try {
            holder.textViewIssueCreatedAt.setText(TimeUtils.convertToNewFormat(issue.getCreatedAt()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.textViewIssueStatus.setText(issue.getState());
        String state = issue.getState();
        int colorResId = android.R.color.holo_green_light;
        if (state.equals(TYPE_OPEN)) {
            colorResId = android.R.color.holo_red_light;
        }
        int color = ContextCompat.getColor(holder.itemView.getContext(), colorResId);
        holder.constraintLayoutIssue.setBackgroundColor(color);
    }

    @Override
    public int getItemCount() {
        return issues.size();
    }

    static class IssueViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewIssueTitle;
        private final TextView textViewIssueCreatedAt;
        private final TextView textViewIssueStatus;
        private final ConstraintLayout constraintLayoutIssue;

        public IssueViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewIssueTitle = itemView.findViewById(R.id.textViewIssueTitle);
            textViewIssueCreatedAt = itemView.findViewById(R.id.textViewIssueCreatedAt);
            textViewIssueStatus = itemView.findViewById(R.id.textViewIssueStatus);
            constraintLayoutIssue = itemView.findViewById(R.id.constraintLayoutIssue);
        }
    }
}
