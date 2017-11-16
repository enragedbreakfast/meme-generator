package com.cvancaeyzeele.memegenerator;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditImageActivity extends AppCompatActivity {

    RelativeLayout relLayout;
    EditText txtImage;
    String imgText;
    TextView textView;
    EditText txtImage2;
    String imgText2;
    TextView textView2;
    ImageView image;
    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        relLayout = (RelativeLayout)findViewById(R.id.relativeLayout);

        // Display image passed by intent
        Intent intent = getIntent();
        int imageID = intent.getIntExtra("image", 0);

        View v = new ImageView(getBaseContext());
        image = new ImageView(v.getContext());
        image.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), imageID));

        // Set layout params for ImageView
        image.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        relLayout.addView(image);

        // Create EditText for user to input text
        txtImage = (EditText)findViewById(R.id.txtImage);
        textView = new TextView(getApplicationContext());
        textView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        textView.setTextSize(24);
        relLayout.addView(textView);

        txtImage.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                imgText = txtImage.getText().toString();
                textView.setText(imgText);
            }
        });

        textView.setOnTouchListener(new View.OnTouchListener() {
            float lastX = 0, lastY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN):
                        lastX = event.getX();
                        lastY = event.getY();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = event.getX() - lastX;
                        float dy = event.getY() - lastY;
                        float finalX = v.getX() + dx;
                        float finalY = v.getY() + dy + v.getHeight();
                        v.setX(finalX);
                        v.setY(finalY);
                        break;
                }
                return true;
            }
        });

        txtImage2 = (EditText)findViewById(R.id.txtImage2);
        textView2 = new TextView(getApplicationContext());
        textView2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
        textView2.setTextSize(24);
        relLayout.addView(textView2);

        txtImage2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {}

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                imgText2 = txtImage2.getText().toString();
                textView2.setText(imgText2);
            }
        });

        textView2.setOnTouchListener(new View.OnTouchListener() {
            float lastX = 0, lastY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case (MotionEvent.ACTION_DOWN):
                        lastX = event.getX();
                        lastY = event.getY();

                        break;
                    case MotionEvent.ACTION_MOVE:
                        float dx = event.getX() - lastX;
                        float dy = event.getY() - lastY;
                        float finalX = v.getX() + dx;
                        float finalY = v.getY() + dy + v.getHeight();
                        v.setX(finalX);
                        v.setY(finalY);
                        break;
                }
                return true;
            }
        });

    }

    public void saveImage(View view) {
        // Check for permission to save files
        if (ContextCompat.checkSelfPermission(EditImageActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(EditImageActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
        } else { // Permission has been granted
            createBitmap();
        }
    }

    public void createBitmap() {
        // Get Bitmap of ImageView
        Bitmap bitmap = Bitmap.createBitmap(relLayout.getWidth(), relLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        relLayout.draw(c);

        // Create the file
        File sdCardDirectory = Environment.getExternalStorageDirectory();
        File imageFile = new File(sdCardDirectory, System.currentTimeMillis() + ".png");

        // Write the Bitmap
        boolean success = false;

        // Encode the file as a PNG image
        FileOutputStream outStream;

        try {
            outStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream); // 100 keeps full quality
            outStream.flush();
            outStream.close();
            success = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (success) {
            Toast.makeText(getApplicationContext(), "Image saved successfully", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Error saving image", Toast.LENGTH_LONG).show();
        }
    }
}
