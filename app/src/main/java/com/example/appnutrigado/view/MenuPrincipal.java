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
    private ImageButton btnFechar, btnAnimais, btnGerenciaNutri, btnGerenciaDesmame;
    private String incEstadual;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //faço a inicialização do itente e pego a inscrição estadual que foi passado na tela anterior
        Intent i = getIntent();
        if (i!= null){
            Bundle parms = i.getExtras();
            if (parms != null){
                incEstadual = parms.getString("incEstadual");
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

        // ao clickar no botão Animais o usuario vai pra tela que ele poderar ver todos animais cadastrados
        // e tambem podera cadastra novos animais como tbm deletar e atualizar
        btnAnimais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //é chamada a proxima tela passando a inscrição estadual da fazenda selecionada na tela anterior
                Intent i = new Intent(MenuPrincipal.this, List_Animais_Cad.class);
                Bundle parms = new Bundle();
                parms.putString("incEstadual", incEstadual);
                i.putExtras(parms);
                startActivity(i);
            }
        });

        //ao clickar no botão de nutrição o usuario vai pra tela que vai lista todos animais cadastrado na fazenda selecionada no inicio
        btnGerenciaNutri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //é chamada a proxima tela passando a inscrição estadual da fazenda selecionada na tela anterior
                Intent i = new Intent(MenuPrincipal.this, List_Animais_Nutri.class);
                Bundle parms = new Bundle();
                parms.putString("incEstadual", incEstadual);
                i.putExtras(parms);
                startActivity(i);
            }
        });

        //ao clickar no botão de desmame vai mostrar todos animais prontos para fazer o desmame
        btnGerenciaDesmame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //é chamada a proxima tela passando a inscrição estadual da fazenda selecionada na tela anterior
                Intent i = new Intent(MenuPrincipal.this, Desmame.class);
                Bundle parms = new Bundle();
                parms.putString("incEstadual", incEstadual);
                i.putExtras(parms);
                startActivity(i);
            }
        });
    }

    private void inicializarComponentes() {
        btnFechar = (ImageButton) findViewById(R.id.btnfechar1);
        btnAnimais = (ImageButton) findViewById(R.id.btncadastraanimais);
        btnGerenciaNutri = (ImageButton) findViewById(R.id.btnGerenciaNutri);
        btnGerenciaDesmame = (ImageButton) findViewById(R.id.btnGerenciaDesmame);

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
