package com.example.appnutrigado.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appnutrigado.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.appnutrigado.R.id.spinnerRaca;


public class CadastroAnimais extends AppCompatActivity {
    private String id;
    private EditText editNumBrinco, editNomeAnimais, editDataNasc, editValorAnimal, editPesoAnimal;
    private Button btnCadastra;
    private Spinner racas;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__animais);
        inicializarComponentes();
        inicializarSpinner();
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
                                    Intent i = new Intent(CadastroAnimais.this, Animais.class);
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
        editNumBrinco = (EditText) findViewById(R.id.edtNumBrincu);
        editNomeAnimais = (EditText) findViewById(R.id.edtNomeAnimais);
        editValorAnimal = (EditText) findViewById(R.id.edtValorAnimal);
        editPesoAnimal = (EditText) findViewById(R.id.edtPesoDoAnimal);
        btnCadastra = (Button) findViewById(R.id.btnCadastra);
        racas = (Spinner)findViewById(spinnerRaca);


    }

    public void inicializarSpinner(){

    }

    private void alert(String msg) {
        Toast.makeText(CadastroAnimais.this, msg, Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(CadastroAnimais.this, Login.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
