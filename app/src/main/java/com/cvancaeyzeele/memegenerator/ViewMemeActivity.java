package com.cvancaeyzeele.memegenerator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ViewMemeActivity extends AppCompatActivity {

    RelativeLayout relLayout;
    ImageView image;
    FirebaseStorage storage;
    String imageLocation;
    Bitmap bitmap;
    File imgFile;
    boolean existingMeme;
    String url;
    URL imageURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Use the chosen theme
        SharedPreferences preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean("dark_theme", false);

        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meme);

        relLayout = (RelativeLayout) findViewById(R.id.relLayout);
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance();

        // Display image passed by intent
        Intent intent = getIntent();

        // Check if meme is from ViewUploadsActivity or EditImageActivity
        existingMeme = intent.getBooleanExtra("existingmeme", false);

        if (existingMeme) { // user selected meme in ViewUploadsActivity
            url = intent.getStringExtra("url");

            // Convert url string to URL
            try {
                imageURL = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            // Get bitmap from URL
            try {
                bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else { // user created meme in EditImageActivity
            imageLocation = intent.getStringExtra("filename");
            imgFile = new File(imageLocation);

            bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }

        View v = new ImageView(getBaseContext());
        image = new ImageView(v.getContext());

        image.setImageBitmap(bitmap);

        // Set layout params for ImageView
        image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        relLayout.addView(image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                Uri photoUri;

                if (existingMeme) { // share image via url
                    String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "", null);
                    photoUri = Uri.parse(path);
                } else { // share image via file path
                    photoUri = Uri.parse(imgFile.getAbsolutePath());
                }

                sharingIntent.setData(photoUri);
                sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, photoUri);
                sharingIntent.setType("image/*");
                startActivity(Intent.createChooser(sharingIntent, "Share Via"));
                return true;
            case R.id.settings:
                Intent i = new Intent(ViewMemeActivity.this, SettingsActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

