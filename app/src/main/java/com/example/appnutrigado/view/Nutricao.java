package com.example.appnutrigado.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appnutrigado.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Nutricao extends AppCompatActivity {
    private String incEstadual, numeroBrinco, nomeAnimal, pesoAoNascer, nomeRacas, sexo, montada, dataNasc, pesoAtual, ifPesoAtual, ifSobra, ifColocada;
    private TextView txtNomeAnimal, txtNumBrinco;
    private EditText edtPesoAtual, edtQuantidadeSobras, edtRacaoColocada;
    private Button btnCadastra;

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutricao);

        Intent i = getIntent();
        if (i != null) {
            Bundle parms = i.getExtras();
            if (parms != null) {
                incEstadual = parms.getString("incEstadual");
                numeroBrinco = parms.getString("numerobrinco");
            }
        }

        inicializarComponentes();
        buscarDados();
        eventoClick();
    }

    private void eventoClick() {
        btnCadastra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    String email = user.getEmail();
                    //mesmo adicionando so mas 3 campos e nescessario setar todos se não eles serão excluidos
                    Map<String, Object> animais = new HashMap<>();
                    animais.put("Numero do Brinco", numeroBrinco);
                    animais.put("Nome do Animal", nomeAnimal);
                    animais.put("Raça", nomeRacas);
                    animais.put("sexo", sexo);
                    animais.put("montada", montada);
                    animais.put("Data de Nascimento", dataNasc);
                    animais.put("Peso Ao Nascer", pesoAoNascer);
                    ifPesoAtual = edtPesoAtual.getText().toString();
                    ifSobra = edtQuantidadeSobras.getText().toString();
                    ifColocada = edtRacaoColocada.getText().toString();
                    animais.put("Peso Atual", ifPesoAtual);
                    animais.put("Sobras", ifSobra );
                    animais.put("Ração Colocada", ifColocada );
                    if (ifPesoAtual.equals("")|| ifSobra.equals("") || ifColocada.equals("")) {
                        alert("Algum campo está vazio");
                    } else

                        db.collection("Usuario").document(email).collection("Fazendas")
                                .document(incEstadual).collection("Animais").document(numeroBrinco)
                                .set(animais)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        alert("Sucesso ao Cadastra");
                                        Intent i = new Intent(Nutricao.this, List_Animais_Nutri.class);
                                        Bundle parms = new Bundle();
                                        parms.putString("incEstadual", incEstadual);
                                        i.putExtras(parms);
                                        startActivity(i);
                                    }
                                });
                }
            }
        });
    }

    private void buscarDados() {
        String email = user.getEmail();
        db.collection("Usuario").document(email).collection("Fazendas").document(incEstadual)
                .collection("Animais").whereEqualTo("Numero do Brinco", numeroBrinco)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        pesoAoNascer = (String) document.get("Peso Ao Nascer");
                        nomeAnimal = (String) document.get("Nome do Animal");
                        nomeRacas = (String) document.get("Raça");
                        sexo = (String) document.get("sexo");
                        montada = (String) document.get("montada");
                        dataNasc = (String) document.get("Data de Nascimento");
                        pesoAtual = (String) document.get("Peso Atual");
                        txtNomeAnimal.setText(nomeAnimal);
                        edtPesoAtual.setText(pesoAtual);
                        txtNumBrinco.setText(numeroBrinco);
                    }
                }
            }
        });
    }

    private void inicializarComponentes() {
        txtNomeAnimal = (TextView) findViewById(R.id.txtNomeAnimal);
        txtNumBrinco = (TextView) findViewById(R.id.textNumBrinco);
        edtPesoAtual = (EditText) findViewById(R.id.edtPesoAtual);
        edtQuantidadeSobras = (EditText) findViewById(R.id.quantidadeSobras);
        edtRacaoColocada = (EditText) findViewById(R.id.RacaoColocada);
        btnCadastra = (Button) findViewById(R.id.btnCadastra);
    }

    private void alert(String msg) {
        Toast.makeText(Nutricao.this, msg, Toast.LENGTH_SHORT).show();
    }

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
                Intent i = new Intent(Nutricao.this, Login.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}