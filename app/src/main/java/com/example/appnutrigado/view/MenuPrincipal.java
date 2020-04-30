package com.example.appnutrigado.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.example.appnutrigado.R;
import com.google.firebase.auth.FirebaseAuth;

public class MenuPrincipal extends AppCompatActivity {
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
                Intent i = new Intent(MenuPrincipal.this, Animais.class);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(MenuPrincipal.this, Login.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}