package com.theolympian.memoir;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class JournalListAdapter extends RecyclerView.Adapter<JournalListAdapter.ViewHolder> {
    private List<Journal> journalsList;

    public JournalListAdapter() {
        journalsList = new ArrayList<>();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Journal journal;
        private final TextView tvTitle;
        private final TextView tvDate;

        public ViewHolder(View view) {
            super(view);
            Context context = view.getContext();

            tvTitle = (TextView) view.findViewById(R.id.tvJournalItemTitle);
            tvDate = (TextView) view.findViewById(R.id.tvJournalItemDate);

            view.setOnClickListener(v -> {
                Intent intent = new Intent(context, JournalViewActivity.class);
                intent.putExtra("title", journal.getTitle());
                intent.putExtra("date", journal.getDate().getTime());
                intent.putExtra("content", journal.getContent());
                context.startActivity(intent);
            });
        }

        public void setJournal(Journal newJournal) {
            journal = newJournal;
        }

        public TextView getTitleView() {
            return tvTitle;
        }

        public TextView getDateView() {
            return tvDate;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.journal_list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Journal journal = journalsList.get(position);
        viewHolder.setJournal(journal);
        viewHolder.getTitleView().setText(journal.getTitle());

        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
        String dateText = dateFormat.format(journal.getDate());
        viewHolder.getDateView().setText(dateText);
    }

    public void updateList(List<Journal> newList) {
        journalsList = newList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(journalsList == null)
            return -1;
        return journalsList.size();
    }

}
