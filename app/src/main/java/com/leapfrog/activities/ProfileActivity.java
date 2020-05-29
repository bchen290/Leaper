package com.leapfrog.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.leapfrog.database.LeaperDatabase;
import com.leapfrog.util.Authentication;
import com.leapfrogandroid.R;

public class ProfileActivity extends AppCompatActivity {
    private CircleImageView imageView;
    private TextView fullNameView;
    private TextView userNameView;
    private EditText bioView;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);
        imageView = findViewById(R.id.profile_image);
        saveButton = findViewById(R.id.Save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaperDatabase.getInstance(ProfileActivity.this).getProfileTable().updateBio(Authentication.getUsername(ProfileActivity.this), bioView.getText().toString());
            }
        });
        String full_name = "";
        String user_name = "";
        String bio = "";
        user_name = Authentication.getUsername(this);
        full_name = LeaperDatabase.getInstance(this).getProfileTable().getName(Authentication.getUsername(this));
        fullNameView = findViewById(R.id.full_name);
        fullNameView.setText(full_name);
        userNameView = findViewById(R.id.Username);
        userNameView.setText(user_name);
        bioView.setText(LeaperDatabase.getInstance(this).getProfileTable().getBio(Authentication.getUsername(this)));
        Glide.with(this).load(LeaperDatabase.getInstance(this).getProfileTable().getPP(Authentication.getUsername(this))).into(imageView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            Glide.with(this).load(image.getPath()).into(imageView);
            LeaperDatabase.getInstance(ProfileActivity.this).getProfileTable().updatePP(Authentication.getUsername(ProfileActivity.this), image.getPath());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void selectImage(View view) {
        ImagePicker.create(this).single().start();
    }
}
