package com.example.dleyy.playwithplan;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.dleyy.playwithplan.widget.PlaneGameView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.startGame).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlaneGameView planeGameView = new PlaneGameView(MainActivity.this);

                setContentView(planeGameView);
            }
        });
    }
}
