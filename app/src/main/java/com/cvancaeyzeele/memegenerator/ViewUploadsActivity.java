package com.cvancaeyzeele.memegenerator;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class ViewUploadsActivity extends AppCompatActivity {

    ArrayList<String> urls = new ArrayList<>();
    GridView gridview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_uploads);

        // Get all download URLs and image ID associated
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("images");

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        collectDownloadURLs((Map<String,Object>) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        // Create Gridview
        gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapterGridView(this));
    }

    private void collectDownloadURLs(Map<String,Object> users) {
        // iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){

            // Get user map
            Map singleUser = (Map) entry.getValue();

            // Get download url and append to list
            urls.add((String) singleUser.get("downloadurl"));
        }

        System.out.println(urls.toString());
    }

    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;

        public ImageAdapterGridView(Context c) {
            mContext = c;
        }

        public int getCount() {
            return urls.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView mImageView;

            if (convertView == null) {
                mImageView = new ImageView(mContext);

                // Size of image thumbnails
                mImageView.setLayoutParams(new GridView.LayoutParams(380,380));
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageView.setPadding(16, 16, 16, 16);
            } else {
                mImageView = (ImageView) convertView;
            }

            URL url = null;

            try {
                url = new URL(urls.get(position));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            try {
                mImageView.setImageBitmap(BitmapFactory.decodeStream(url.openConnection().getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            return mImageView;
        }
    }
}
