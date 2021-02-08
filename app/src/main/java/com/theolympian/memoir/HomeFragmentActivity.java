package com.theolympian.memoir;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class HomeFragmentActivity extends Fragment {
    private String TAG = HomeFragmentActivity.class.getSimpleName();
    private final int RC_NEW_JOURNAL = 1;

    private FirebaseUser mUser;
    private FirebaseFirestore mDb;
    private Query query;

    private TextView tvMessage;

    private final JournalListAdapter recyclerAdapter = new JournalListAdapter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        RecyclerView journalRecyclerView = view.findViewById(R.id.journalRecyclerView);
        journalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        journalRecyclerView.setAdapter(recyclerAdapter);

        query = mDb
                .collection("Journals")
                .document(mUser.getUid())
                .collection("Journals")
                .orderBy("date");

        tvMessage = view.findViewById(R.id.tvMessage);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), WriteJournalActivity.class);
                startActivityForResult(intent, RC_NEW_JOURNAL);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                Log.d(TAG, "Journal snapshot: Got " + querySnapshot.size() + " journal documents");

                if(querySnapshot.size() == 0) {
                    tvMessage.setVisibility(View.VISIBLE);
                    return;
                } else {
                  tvMessage.setVisibility(View.GONE);
                }

                List<Journal> journalList = querySnapshot.toObjects(Journal.class);
                journalList.forEach(journal -> {
                    Log.d(TAG, "Journal content:" + journal.getTitle());
                });
                recyclerAdapter.updateList(journalList);
                return;
            }
            if (task.isComplete()) {
                Log.w(TAG, "Listen failed.", task.getException());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_NEW_JOURNAL && resultCode == WriteJournalActivity.RESULT_SUCCESS) {
            Snackbar.make(getView(), "New journal saved!", Snackbar.LENGTH_SHORT).show();
        }
    }
}