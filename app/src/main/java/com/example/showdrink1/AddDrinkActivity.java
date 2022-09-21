package com.example.showdrink1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class AddDrinkActivity extends AppCompatActivity {

    private ImageButton btnvoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        btnvoltar = findViewById(R.id.imgbtnvoltar);


        btnvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main);
            }
        });
        setContentView(R.layout.activity_add_drink);
    }
}