package com.theolympian.memoir;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class ProfileFragmentActivity extends Fragment {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;

    private EditText etName;
    private ImageView ivAvatar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        FirebaseUser user = mAuth.getCurrentUser();

        etName = view.findViewById(R.id.etDisplayName);
        ivAvatar = view.findViewById(R.id.ivAvatar);

        Uri photoUrl = user.getPhotoUrl();
        if (photoUrl != null && !photoUrl.toString().isEmpty()) {
            Glide.with(this).load(photoUrl).into(ivAvatar);
        }

        // TODO: Set etName with data from firestore
        mDb.collection("Users").document(user.getUid()).get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                etName.setText(task.getResult().get("name").toString());
            }
        });

        view.findViewById(R.id.btnSave).setOnClickListener(v -> updateProfile(user.getUid(), view));

        view.findViewById(R.id.btnSignOut).setOnClickListener(v -> signOut());
        return view;
    }

    private void updateProfile(String uid, View view) {
        Map<String, Object> data = new HashMap<>();
        data.put("name", etName.getText().toString());
        mDb.collection("Users")
                .document(uid)
                .set(data, SetOptions.merge())
                .addOnCompleteListener(dbTask -> {
                    if(dbTask.isSuccessful()) {
                        Snackbar.make(view, "Profile Updated!", Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }).addOnFailureListener(e -> {
                    Snackbar.make(view, "Couldn't update profile!", Snackbar.LENGTH_SHORT)
                            .show();
                });

    }

    private void signOut() {
        mAuth.signOut();
        if (mAuth.getCurrentUser() == null) {
            Intent intent = new Intent(getContext(), SignInActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
    }
}