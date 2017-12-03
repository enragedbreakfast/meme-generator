package com.cvancaeyzeele.memegenerator;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cvancaeyzeele.memegenerator.CheckNetworkConnectivity;

public class MainActivity extends AppCompatActivity {

    Button btnBrowse;
    Button btnViewUploads;
    Button btnRetry;
    TextView txtNoInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        btnBrowse = (Button) findViewById(R.id.btnBrowse);
        btnViewUploads = (Button) findViewById(R.id.btnViewUploads);
        btnRetry = (Button) findViewById(R.id.btnRetry);
        txtNoInternet = (TextView) findViewById(R.id.txtNoInternet);

        updateConnection();
    }

    public void openBrowseImages(View view) {
        Intent i = new Intent(this, BrowseImagesActivity.class);
        startActivity(i);
    }

    public void openViewUploads(View view) {
        Intent i = new Intent(this, ViewUploadsActivity.class);
        startActivity(i);
    }

    public void updateConnection() {
        if (CheckNetworkConnectivity.isDataConnectionAvailable(this)) {
            btnRetry.setVisibility(View.INVISIBLE);
            btnBrowse.setVisibility(View.VISIBLE);
            btnViewUploads.setVisibility(View.VISIBLE);
            txtNoInternet.setVisibility(View.INVISIBLE);
        } else {
            btnRetry.setVisibility(View.VISIBLE);
            btnBrowse.setVisibility(View.INVISIBLE);
            btnViewUploads.setVisibility(View.INVISIBLE);
            txtNoInternet.setVisibility(View.VISIBLE);
        }
    }

    public void retryConnection(View view) {
        updateConnection();
    }
}
