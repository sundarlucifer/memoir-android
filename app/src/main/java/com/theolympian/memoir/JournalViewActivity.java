package com.theolympian.memoir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.view.View.GONE;

public class JournalViewActivity extends AppCompatActivity {

    private TextView tvTitle;
    private TextView tvContent;
    private TextView tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_view);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String content = intent.getStringExtra("content");
        Date date = new Date();
        date.setTime(intent.getLongExtra("date", 0));
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");

        tvTitle = findViewById(R.id.tvJournalTitle);
        tvContent = findViewById(R.id.tvJournalContent);
        tvDate = findViewById(R.id.tvJournalDate);

        tvTitle.setText(title);
        tvContent.setText(content);
        tvDate.setText(dateFormat.format(date));

        findViewById(R.id.ivHome).setOnClickListener(v -> this.finish());

        findViewById(R.id.ivDelete).setVisibility(GONE);
                //.setOnClickListener(v -> deleteJournal());

        //TODO: Add journal edit option
    }

    private void deleteJournal() {
        //TODO: Implement db delete action
        //TODO: Add document id to journal class
    }
}