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
import com.example.appnutrigado.control.ConexaoAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    private EditText editE_mail, editSenha;
    private Button btnEntrar, btnCadastro;

    private FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializarComponentes();
        eventoClicks();
    }

    private void eventoClicks() {
        // Ao clickar no botão cadastro ele ira para tela onde irá poder se cadastrar
        btnCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, CadastroUsuario.class);
                startActivity(i);
            }
        });

        //Ao clickar no botão de entrar ele ira passar po um try catch onde se os campos não estiverem vazios
        //ele ira chamar o metado login que fara a auteticaçãop
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editE_mail.getText().toString().trim();
                String senha = editSenha.getText().toString().trim();
                try {
                    login(email, senha);
                } catch (IllegalArgumentException e) {
                    alerte("Campos e-mail ou senha estão vazios");
                }

            }
        });
    }

    // neste metado se usa a ferramenta do firebase para fazer a autenticação
    // onde caso a mesma esteja correta será redirecionada para a tela de fazendas.
    private void login(String email, String senha) {
        auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent i = new Intent(Login.this, ListFazendas.class);
                    startActivity(i);
                } else {
                    alerte("email ou senha errrados");
                }
            }
        });
    }

    private void alerte(String s) {
        Toast.makeText(Login.this, s, Toast.LENGTH_SHORT).show();
    }

    private void inicializarComponentes() {
        editE_mail = (EditText) findViewById(R.id.edit_Email);
        editSenha = (EditText) findViewById(R.id.editSenha);
        btnEntrar = (Button) findViewById(R.id.btnEntra);
        btnCadastro = (Button) findViewById(R.id.btnCadastro);
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth = ConexaoAuth.getFirebaseAuth();
    }
}
