package com.leapfrog.util;

import android.content.Context;
import android.os.Bundle;

import com.leapfrogandroid.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

/**
 * Sets up the toolbar
 */
public abstract class BaseActivity extends AppCompatActivity {
    private Toolbar toolbar;

    /**
     * attaches the base context to the base activity
     * @param newBase context for new base activity
     */
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Utils.adjustFontSize(newBase));
    }

    /**
     * Sets teh colorblind mode and adjusts the font
     * @param savedInstanceState the instance saved state
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (SettingsPreference.getColorBlindMode(this)) {
            setTheme(R.style.colorBlind);
        }

        super.onCreate(savedInstanceState);
        Utils.adjustFontSize(this);
    }

    /**
     * Sets the toolbar and displays it
     */
    public void setupToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * Sets the toolbar up with the support action bar
     */
    public void setupToolbarNoUp() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * Sets the toolbar with title
     * @param title title of the toolbar
     */
    public void setupToolbar(String title) {
        setupToolbar();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Sets up the toolbar and sets the title
     * @param title title of toolbar
     */
    public void setupToolbarNoUp(String title) {
        setupToolbarNoUp();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}
