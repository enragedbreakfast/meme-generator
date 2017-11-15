package com.cvancaeyzeele.memegenerator;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class EditImageActivity extends AppCompatActivity {

    RelativeLayout relLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        relLayout = (RelativeLayout)findViewById(R.id.relativeLayout);

        // Display image passed by intent
        Intent intent = getIntent();
        int imageID = intent.getIntExtra("image", 0);

        View v = new ImageView(getBaseContext());
        ImageView image = new ImageView(v.getContext());
        image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), imageID));

        // Set layout params for ImageView
        image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        relLayout.addView(image);
        
    }
}
