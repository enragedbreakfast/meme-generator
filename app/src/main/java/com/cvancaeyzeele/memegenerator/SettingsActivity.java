package com.cvancaeyzeele.memegenerator;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences preferences;
    Button btnSave;
    EditText etFontSize;
    Switch swTheme;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Use the chosen theme
        preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean useDarkTheme = preferences.getBoolean("dark_theme", false);

        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        preferences = getSharedPreferences("Settings", MODE_PRIVATE);
        editor = preferences.edit();

        btnSave = (Button) findViewById(R.id.btnSave);
        swTheme = (Switch) findViewById(R.id.swTheme);
        etFontSize = (EditText) findViewById(R.id.etFontSize);

        swTheme.setChecked(useDarkTheme);
    }

    public void onClickBtnSave(View v) {
        editor.putBoolean("dark_theme", swTheme.isChecked());

        if(etFontSize.length() != 0) {
            String fontSize = etFontSize.getText().toString();
            editor.putInt("font_size", Integer.parseInt(fontSize));
        }

        editor.commit();

        finish();
    }
}
