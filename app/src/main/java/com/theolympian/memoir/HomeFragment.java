package com.theolympian.memoir;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class HomeFragment extends Fragment {
    private String TAG = HomeFragment.class.getSimpleName();

    private FirebaseUser mUser;
    private FirebaseFirestore mDb;

    private ListenerRegistration journalRegistration;

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

        Query query = mDb
                .collection("Journals")
                .document(mUser.getUid())
                .collection("Journals")
                .orderBy("date");

        journalRegistration = query.addSnapshotListener((querySnapshot, e) -> {
            if (e != null) {
                Log.w(TAG, "Listen failed.", e);
                return;
            }

            Log.d(TAG, "Journal snapshot: Got " + querySnapshot.size() + " journal documents");

            List<Journal> chats = querySnapshot.toObjects(Journal.class);
            chats.forEach(journal -> {
                Log.d(TAG, "Journal content:" + journal.toString());
            });
        });

        /*
        FirestoreRecyclerOptions<Journal> options = new FirestoreRecyclerOptions.Builder<Journal>()
                .setQuery(query, Journal.class)
                .build();

        FirestoreRecyclerAdapter adapter = new FirestoreRecyclerAdapter<Journal, ChatHolder>(options) {
            @Override
            public void onBindViewHolder(ChatHolder holder, int position, Journal model) {
                // Bind the Chat object to the ChatHolder
                // ...
            }

            @Override
            public ChatHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.message, group, false);

                return new ChatHolder(view);
            }
        };
        */

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        journalRegistration.remove();
    }
}