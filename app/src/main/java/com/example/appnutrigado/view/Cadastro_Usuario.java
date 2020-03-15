package com.example.appnutrigado.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.appnutrigado.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Cadastro_Usuario extends AppCompatActivity {
    private EditText editNome, editTelefone, editEmail, editSenha;
    private Button btnCadastrar, btnVoltar;
    private FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__usuario);
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();

        inicializarComponentes();
        eventosClicks();
    }

    private void eventosClicks() {
        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editEmail.getText().toString().trim();
                String senha = editSenha.getText().toString().trim();
                try {
                    InsertBD(email,senha);
                }catch (IllegalArgumentException e){
                    alert("Algum Campo Está Vazio");
                }
            }
        });
    }

    private void InsertBD(String email, String senha) {
        auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(Cadastro_Usuario.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Map<String, Object> user = new HashMap<>();
                    user.put("Nome", editNome.getText().toString());
                    user.put("Telefone", editTelefone.getText().toString());
                    user.put("E-mail", editEmail.getText().toString());
                    db.collection("Usuario").document(editEmail.getText().toString())
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    alert("Cadastrado com Sucesso!!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    alert("Error no Banco de Dados");
                                }
                            });
                    Intent i = new Intent(Cadastro_Usuario.this, ListFazendas.class);
                    startActivity(i);
                }else{
                        alert("que porra aconteceu");
                }
            }
        });
    }

    private void FireBaseFS() {

    }

    private void inicializarComponentes() {
        editEmail = (EditText) findViewById(R.id.editEmail);
        editNome = (EditText) findViewById(R.id.editNome);
        editSenha = (EditText) findViewById(R.id.editSenha);
        editTelefone = (EditText) findViewById(R.id.editTelefone);
        btnCadastrar = (Button) findViewById(R.id.btnCadastraCU);
        btnVoltar = (Button) findViewById(R.id.btnVolta);
    }

    private void alert(String msg) {
        Toast.makeText(Cadastro_Usuario.this, msg, Toast.LENGTH_SHORT).show();
    }
}
