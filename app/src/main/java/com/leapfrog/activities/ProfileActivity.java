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
import com.leapfrog.database.ProfileTable;
import com.leapfrog.util.Authentication;
import com.leapfrog.util.BaseActivity;
import com.leapfrogandroid.R;
/**
 * This class holds all the necessary components to allow the user to view Profile
 */
public class ProfileActivity extends BaseActivity {
    private CircleImageView imageView;
    private TextView fullNameView;
    private TextView userNameView;
    private EditText bioView;
    private Button saveButton;

    private ProfileTable profileTable;
    /**
     * Sets up Profile activity
     * Initialize the profile picture and user information
     * @param savedInstanceState Information saved about current activity
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_screen);

        setupToolbar("Your profile");

        profileTable = LeaperDatabase.getInstance(this).getProfileTable();
        profileTable.getProfile(Authentication.getUsername(this));

        imageView = findViewById(R.id.profile_image);
        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> profileTable.updateBio(Authentication.getUsername(ProfileActivity.this), bioView.getText().toString()));

        String userName = Authentication.getUsername(this);
        String fullName = profileTable.getName(userName);

        fullNameView = findViewById(R.id.full_name);
        fullNameView.setText(fullName);

        userNameView = findViewById(R.id.Username);
        userNameView.setText(userName);

        bioView = findViewById(R.id.Bio);
        bioView.setText(profileTable.getBio(userName));

        String profileTablePath = profileTable.getProfilePicture(userName);

        if (!profileTablePath.equals("")) {
            Glide.with(this).load(profileTable.getProfilePicture(userName)).into(imageView);
        }
    }
    /**
     * User enters an image to be used as new profile picture
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * Checks for valid request
         */
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            Glide.with(this).load(image.getPath()).into(imageView);
            LeaperDatabase.getInstance(ProfileActivity.this).getProfileTable().updateProfilePicture(Authentication.getUsername(ProfileActivity.this), image.getPath());
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * Allows user to pick an image from phone
     */
    public void selectImage(View view) {
        ImagePicker.create(this).single().start();
    }
}
