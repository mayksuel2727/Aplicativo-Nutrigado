package com.example.appnutrigado.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.appnutrigado.R;

public class Menu extends AppCompatActivity {
    private ImageButton btnFechar, btnCadastraAnimais;
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Intent i = getIntent();
        if (i!= null){
            Bundle parms = i.getExtras();
            if (parms != null){
                id = parms.getString("id");
            }
        }
        inicializarComponentes();
        enventoClicks();

    }

    private void enventoClicks() {
        btnFechar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCadastraAnimais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println(id);
                Intent i = new Intent(Menu.this, Animais.class);
                Bundle parms = new Bundle();
                parms.putString("id", id);
                i.putExtras(parms);
                startActivity(i);
            }
        });
    }

    private void inicializarComponentes() {
        btnFechar = (ImageButton) findViewById(R.id.btnfechar1);
        btnCadastraAnimais = (ImageButton) findViewById(R.id.btncadastraanimais);
    }
}
