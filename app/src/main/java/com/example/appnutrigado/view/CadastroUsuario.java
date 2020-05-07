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
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CadastroUsuario extends AppCompatActivity {
    private EditText editNome, editTelefone, editEmail, editSenha;
    private Button btnCadastrar;
    private FirebaseAuth auth = FirebaseAuth.getInstance();;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__usuario);

        inicializarComponentes();
        eventosClicks();
    }

    //Ao clickar no botão de cadastrar ele ira passar po um try catch onde se os campos não estiverem vazios
    //ele ira chamar o metado insertBD que fara o seu registro como usuario caso não exista nenhum email igual o dele ja cadastrado
    //depois de registra o usuario sera feita a insessão do dados no BD
    private void eventosClicks() {
        //Aqui foi criada uma mascara para o campo de cadastro de telefone
        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(editTelefone, simpleMaskFormatter);
        editTelefone.addTextChangedListener(maskTextWatcher);
        //Fim da mascara
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
        //na linha de baixo e feito o registro de usuario
        auth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(CadastroUsuario.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //apos o registro ser efetuado com sucesso sera feita a insersão no BD
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
                    Intent i = new Intent(CadastroUsuario.this, ListFazendas.class);
                    startActivity(i);
                }else{
                        alert("Email invalido ou ja cadastrado");
                }
            }
        });
    }

    private void inicializarComponentes() {
        editEmail = (EditText) findViewById(R.id.editEmail);
        editNome = (EditText) findViewById(R.id.editNome);
        editSenha = (EditText) findViewById(R.id.editSenha);
        editTelefone = (EditText) findViewById(R.id.editTelefone);
        btnCadastrar = (Button) findViewById(R.id.btnCadastraCU);
    }

    private void alert(String msg) {
        Toast.makeText(CadastroUsuario.this, msg, Toast.LENGTH_SHORT).show();
    }
}
