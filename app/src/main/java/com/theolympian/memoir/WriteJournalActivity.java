package com.theolympian.memoir;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WriteJournalActivity extends AppCompatActivity {
    public static int RESULT_SUCCESS = 1;
    public static int RESULT_CANCELLED = 2;

    private TextView tvDate;
    private EditText etTitle;
    private EditText etContent;

    private View layout;

    private Date date = new Date();
    private final Calendar myCalendar = Calendar.getInstance();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy");
    DatePickerDialog.OnDateSetListener dateSetListener;

    private FirebaseUser user;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_journal);

        user = FirebaseAuth.getInstance().getCurrentUser();
        mDb = FirebaseFirestore.getInstance();

        layout = findViewById(R.id.layout);
        tvDate = findViewById(R.id.tvDate);
        etTitle = findViewById(R.id.etTitle);
        etContent = findViewById(R.id.etContent);

        tvDate.setText(dateFormat.format(date));

        findViewById(R.id.dateView).setOnClickListener(v -> datePicker());

        findViewById(R.id.btnSave).setOnClickListener(v -> saveJournal());

        findViewById(R.id.ivHome).setOnClickListener(v -> {
            setResult(RESULT_CANCELLED);
            this.finish();
        });

        dateSetListener = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            date = myCalendar.getTime();
            tvDate.setText(dateFormat.format(date));
        };
    }

    private void datePicker() {
        new DatePickerDialog(this, dateSetListener, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void saveJournal() {
        String title = etTitle.getText().toString();
        String content = etContent.getText().toString();

        boolean validated = true;
        if (title.isEmpty()) {
            validated = false;
        }

        if (content.isEmpty()) {
            validated = false;
        }

        if(date == null) {
            validated = false;
        }

        if (!validated) {
            Snackbar.make(layout, "Date, Title, Content can't be empty", Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }

        mDb.collection("Journals")
                .document(user.getUid())
                .collection("Journals")
                .add(new Journal(date, title, content))
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        setResult(RESULT_SUCCESS);
                        this.finish();
                    }
                }).addOnFailureListener(e -> {
            Snackbar.make(layout, "Something went wrong! Save failed!", Snackbar.LENGTH_SHORT)
                    .show();
        });
    }
}