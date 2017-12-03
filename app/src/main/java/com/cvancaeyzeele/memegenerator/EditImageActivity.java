package com.cvancaeyzeele.memegenerator;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class EditImageActivity extends AppCompatActivity {

    RelativeLayout relLayout;
    EditText txtImage;
    String imgText;
    TextView textView;
    EditText txtImage2;
    String imgText2;
    TextView textView2;
    ImageView image;
    FirebaseStorage storage;
    FirebaseDatabase database;
    byte[] data;
    Bitmap bitmap;
    String imageLocation;
    String timeCreated;

    String imageID;
    String fileName;
    String firebaseDownloadURL;

    private final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_image);

        relLayout = (RelativeLayout)findViewById(R.id.relativeLayout);

        // Get Firebase Storage and Database instances
        FirebaseApp.initializeApp(this);
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

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

        // TODO: touch input is off by few mm
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

    /**
     * Upload image to Firebase Storage and save filename to Firebase Database
     * @param view
     */
    public void uploadImage(View view) {
        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to image to upload
        imageID = String.valueOf(System.currentTimeMillis());
        fileName = imageID + ".png";
        timeCreated = DateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis()));
        StorageReference imageRef = storageRef.child("images/" + fileName);

        // Create bitmap of image
        bitmap = getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos); // 100 keeps full quality
        data = baos.toByteArray();

        // Upload image to Firebase Storage
        UploadTask uploadTask = imageRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.d("courtney", "upload failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                firebaseDownloadURL = downloadUrl.toString();
                Log.d("courtney", "upload successful " + firebaseDownloadURL);

                // Add data to Firebase database with filename
                createDatabaseEntry(imageID, fileName, timeCreated, firebaseDownloadURL);
            }
        });

        // Display toast with Database info
        DatabaseReference databaseRef = database.getReference(imageID);
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Switch to ViewMemeActivity and display finished meme
                Intent i = new Intent(EditImageActivity.this, ViewMemeActivity.class);

                createBitmap();

                // Pass imageID
                i.putExtra("filename", imageLocation);

                startActivity(i);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("FirebaseTest", "Failed to read value.", error.toException());
            }
        });
    }

    /**
     * Create an entry for image in Firebase Database
     * @param filename
     */
    public void createDatabaseEntry(String imageID, String filename, String timeCreated, String downloadURLMethod) {
        // Get database references
        DatabaseReference databaseRef = database.getReference();
        DatabaseReference imagesRef = databaseRef.child("images");

        // Create image objects and add to hashmap
        Map<String, Image> images = new HashMap<>();
        images.put(imageID, new Image(filename, timeCreated, downloadURLMethod));
        Log.d("courtney", "URL - " + downloadURLMethod);

        // Push hashmap containing image to database
        imagesRef.push().setValue(images);
    }

    /**
     * Create the bitmap image and save to device
     */
    public void createBitmap() {
        Bitmap bitmap = getBitmap();

        // Create the file
        File sdCardDirectory = Environment.getExternalStorageDirectory();
        File imageFile = new File(sdCardDirectory, System.currentTimeMillis() + ".png");
        imageLocation = imageFile.getAbsolutePath();

        boolean success = false;

        // Write the bitmap and encode the file as a PNG image
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

    /**
     * Create a bitmap from the relLayout canvas
     * @return bitmap
     */
    public Bitmap getBitmap() {
        // Get Bitmap of ImageView
        Bitmap bitmap = Bitmap.createBitmap(relLayout.getWidth(), relLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);
        relLayout.draw(c);

        return bitmap;
    }

    /**
     * Class to store image data
     */
    public static class Image {
        public String filename;
        public String timeCreated;
        public String downloadurl;

        private Image() {}

        public Image(String filename, String timeCreated, String downloadurl) {
            this.filename = filename;
            this.timeCreated = timeCreated;
            this.downloadurl = downloadurl;
        }

        public String getFilename() {
            return filename;
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }

        public String getTimeCreated() {
            return timeCreated;
        }

        public void setTimeCreated(String timeCreated) {
            this.timeCreated = timeCreated;
        }

        public String getDownloadurl() {
            return downloadurl;
        }

        public void setDownloadurl(String downloadurl) {
            this.downloadurl = downloadurl;
        }

        public String toString() {
            return downloadurl;
        }

    }
}
