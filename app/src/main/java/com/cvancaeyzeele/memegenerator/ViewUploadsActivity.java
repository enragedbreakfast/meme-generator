package com.cvancaeyzeele.memegenerator;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("urls");

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                            Map<String, String> map = (Map) child.getValue();
                            urls.add(map.get("url").toString());
                        }
                        // Create Gridview
                        gridview = (GridView)findViewById(R.id.gridview);
                        gridview.setAdapter(new ImageAdapterGridView(ViewUploadsActivity.this));

                        // When image thumbnail is clicked, start EditImageActivity and pass ID of image clicked (full image, not thumbnail)
                        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            public void onItemClick(AdapterView<?> parent,
                                                    View v, int position, long id) {
                                Intent intent = new Intent(ViewUploadsActivity.this, ViewMemeActivity.class);
                                intent.putExtra("url", urls.get(position));
                                intent.putExtra("existingmeme", true); // used in ViewMemeActivity to determine whether to look for url or filename
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
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
            Log.d("courtney", "test");

            try {
                url = new URL(urls.get(position));
                Log.d("courtney", urls.get(position));
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
