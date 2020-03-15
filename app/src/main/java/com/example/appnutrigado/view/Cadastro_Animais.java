package com.example.appnutrigado.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appnutrigado.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class Cadastro_Animais extends AppCompatActivity {
    private String id;
    private EditText editNumBrinco, editNomeAnimais, editRaca, editDataNasc, editValorAnimal, editPesoAnimal;
    private Button btnCadastra;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__animais);
        inicializarComponentes();


        eventoClicks();
    }

    private void eventoClicks() {
        Intent i = getIntent();
        if (i != null) {
            Bundle parms = i.getExtras();
            if (parms != null) {
                id = parms.getString("id");
                System.out.println(id);
            }
        }

        btnCadastra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    String email = user.getEmail();
                    Map<String, Object> animais = new HashMap<>();
                    animais.put("Numero do Brinco", editNumBrinco.getText().toString());
                    animais.put("Nome do Animal", editNomeAnimais.getText().toString());
                    animais.put("Raça", editRaca.getText().toString());
                    animais.put("Data de Nascimento", editDataNasc.getText().toString());
                    animais.put("Valor do Animal", editValorAnimal.getText().toString());
                    animais.put("Peso do Animal", editPesoAnimal.getText().toString());
                    System.out.println(id);
                    Log.i("ITALAC", "Incrição estadual: " + id);

                    db.collection("Usuario").document(email).collection("Fazendas")
                            .document(id).collection("Animais").document(editNumBrinco.getText().toString())
                            .set(animais)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    alert("Sucesso ao Cadastra");
                                    Intent i = new Intent(Cadastro_Animais.this, Animais.class);
                                    Bundle parms = new Bundle();
                                    parms.putString("id", id);
                                    i.putExtras(parms);
                                    startActivity(i);
                                }
                            });
                }
            }
        });
    }

    private void inicializarComponentes() {
        editDataNasc = (EditText) findViewById(R.id.edtDataNasc);
        editNumBrinco = (EditText) findViewById(R.id.edtNumBrinco);
        editNomeAnimais = (EditText) findViewById(R.id.edtNomeAnimais);
        editRaca = (EditText) findViewById(R.id.edtRaca);
        editValorAnimal = (EditText) findViewById(R.id.edtValorAnimal);
        editPesoAnimal = (EditText) findViewById(R.id.edtPesoDoAnimal);
        btnCadastra = (Button) findViewById(R.id.btnCadastra);
    }

    private void alert(String msg) {
        Toast.makeText(Cadastro_Animais.this, msg, Toast.LENGTH_SHORT).show();
    }
}
