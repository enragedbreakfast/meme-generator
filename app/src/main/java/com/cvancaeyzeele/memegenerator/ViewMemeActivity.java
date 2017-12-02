package com.cvancaeyzeele.memegenerator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class ViewMemeActivity extends AppCompatActivity {

    RelativeLayout relLayout;
    ImageView image;
    FirebaseStorage storage;
    String filename;
    File localFile;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_meme);

        relLayout = (RelativeLayout) findViewById(R.id.relLayout);
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance();

        // Display image passed by intent
        Intent intent = getIntent();
        String imageLocation = intent.getStringExtra("filename");
        File imgFile = new File(imageLocation);

        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

        View v = new ImageView(getBaseContext());
        image = new ImageView(v.getContext());

        image.setImageBitmap(bitmap);

        // Set layout params for ImageView
        image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        relLayout.addView(image);
    }
}

