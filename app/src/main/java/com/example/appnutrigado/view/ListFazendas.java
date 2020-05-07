package com.example.appnutrigado.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.appnutrigado.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ListFazendas extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private ListView listViewFazendas;
    private List<String> listFazenda = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapterFazenda;
    private Button btnCad;

    private String fazendaSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_fazendas);

        inicializarComponentes();
        eventoBusca();
        eventoClick();
    }


    private void eventoClick() {
        //novamente e pego o email do user logado no momento
        final String email1 = user.getEmail();
        //ao clickar no botão de cadastrar o usuario e direcionado para a tela de cadastro de novas fazendas
        btnCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListFazendas.this, CadastroFazendas.class);
                startActivity(i);
            }
        });
        //a listView ira mostrar todas a fazendas cadastradas
        listViewFazendas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // a variavel fazendaSelecionada ira pega o nome da fazenda que foi selecionda
                fazendaSelecionada = (String) adapterView.getItemAtPosition(position);
                //foi feita uma consulta que ira pegar o numero da inscrição estadual da fazenda selecionada
                db.collection("Usuario").document(email1).collection("Fazendas").whereEqualTo("Nome da Fazenda", fazendaSelecionada)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                String incEstadual = null;
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        incEstadual = (String) document.get("Inscrição Estadual");
                                    }
                                }
                                //Foi criado um intent para pode passa a inscrição estadual da fazenda selecionada pra a proxima tela
                                Intent i = new Intent(ListFazendas.this, MenuPrincipal.class);
                                Bundle parms = new Bundle();
                                parms.putString("incEstadual", incEstadual);
                                i.putExtras(parms);
                                startActivity(i);
                            }

                        });
            }
        });
    }

    //neste metado e realiza uma busca por toda a coleção de fazenda do usuario logado no momento
    // e a cada fazenda encotrada e pego o seu nome e add na list.
    private void eventoBusca() {
        //na linha 92 e pego o email do user registrano no momento para pode fazer a busca
        String email1 = user.getEmail();
        db.collection("Usuario").document(email1).collection("Fazendas").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String nome = (String) documentSnapshot.get("Nome da Fazenda");
                        listFazenda.add(nome);
                    }
                }else {
                    alert("Faça o logout e realize novamento o login");
                }
                arrayAdapterFazenda = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_expandable_list_item_1, listFazenda);
                listViewFazendas.setAdapter(arrayAdapterFazenda);
            }
        });
    }

    private void inicializarComponentes() {
        listViewFazendas = (ListView) findViewById(R.id.listViewFazendas1);
        btnCad = (Button) findViewById(R.id.btnCadastra);
    }

    private void alert(String msg) {
        Toast.makeText(ListFazendas.this, msg, Toast.LENGTH_SHORT).show();
    }

    // os metados abaixo são para pode se efetuar o logout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(ListFazendas.this, Login.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
