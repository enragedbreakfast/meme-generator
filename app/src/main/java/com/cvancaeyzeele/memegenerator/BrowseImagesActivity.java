package com.cvancaeyzeele.memegenerator;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class BrowseImagesActivity extends AppCompatActivity {

    GridView imagesGridView;
    Integer[] imageIDs = {
            R.drawable.arthur_fist_thumbnail, R.drawable.bob_the_builder_thumbnail, R.drawable.drake_thumbnail,
            R.drawable.expanding_brain_thumbnail, R.drawable.fbi_text_thumbnail, R.drawable.forehead_guy_thumbnail,
            R.drawable.kermit_thumbnail, R.drawable.math_lady_thumbnail
    };

    Integer[] fullImages = {
            R.drawable.arthur_fist, R.drawable.bob_the_builder, R.drawable.drake,
            R.drawable.expanding_brain, R.drawable.fbi_text, R.drawable.forehead_guy,
            R.drawable.kermit, R.drawable.math_lady
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_images);

        imagesGridView = (GridView) findViewById(R.id.gridview_browse_images);
        imagesGridView.setAdapter(new ImageAdapterGridView(this));

        imagesGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                // TODO: Start activity to edit image, pass image clicked on minus _thumbnail
                Intent intent = new Intent(BrowseImagesActivity.this, EditImageActivity.class);
                intent.putExtra("image", fullImages[position]);
                startActivity(intent);
            }
        });
    }

    public class ImageAdapterGridView extends BaseAdapter {
        private Context mContext;

        public ImageAdapterGridView(Context c) {
            mContext = c;
        }

        public int getCount() {
            return imageIDs.length;
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
                mImageView.setLayoutParams(new GridView.LayoutParams(380,380));
                mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                mImageView.setPadding(16, 16, 16, 16);
            } else {
                mImageView = (ImageView) convertView;
            }
            mImageView.setImageResource(imageIDs[position]);
            return mImageView;
        }
    }
}
