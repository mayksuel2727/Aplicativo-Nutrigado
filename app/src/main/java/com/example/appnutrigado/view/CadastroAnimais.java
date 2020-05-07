package com.example.appnutrigado.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appnutrigado.R;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class CadastroAnimais extends AppCompatActivity {
    private String incEstadual, nomeRacas, sexo, montada;
    private EditText editNumBrinco, editNomeAnimais, editDataNasc, editPesoAnimal;
    private Button btnCadastra;
    private Spinner racas;
    private RadioButton masculino, feminino, natural, artificial;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro__animais);

        Intent i = getIntent();
        if (i != null) {
            Bundle parms = i.getExtras();
            if (parms != null) {
                incEstadual = parms.getString("incEstadual");
                System.out.println(incEstadual);
            }
        }

        inicializarComponentes();
        spinner();
        eventoClicks();
    }


    private void Radio_Button() {
        if (masculino.isChecked())
            sexo = "Macho";
        if (feminino.isChecked())
            sexo = "femea";
        if (natural.isChecked())
            montada = "natural";
        if (artificial.isChecked())
            montada = "artificial";
    }

    private void eventoClicks() {
        // macara para padronizar a data
        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(editDataNasc, simpleMaskFormatter);
        editDataNasc.addTextChangedListener(maskTextWatcher);
        //fim da mascara

        // e feita toda a exerção dos dados inseridos pelo usuario dos seus animais
        btnCadastra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user != null ) {

                    Radio_Button();
                    String email = user.getEmail();
                    Map<String, Object> animais = new HashMap<>();
                    animais.put("Numero do Brinco", editNumBrinco.getText().toString());
                    animais.put("Nome do Animal", editNomeAnimais.getText().toString());
                    animais.put("Raça", nomeRacas);
                    animais.put("sexo", sexo);
                    animais.put("montada", montada);
                    animais.put("Data de Nascimento", editDataNasc.getText().toString());
                    animais.put("Peso Ao Nascer", editPesoAnimal.getText().toString());
                    animais.put("Peso Atual", null);
                    animais.put("Ração Colocada", null);
                    animais.put("Sobras", null);
                    System.out.println(incEstadual);
                    Log.i("ITALAC", "Incrição estadual: " + incEstadual);

                    try {
                        if (montada != null && sexo != null){
                        db.collection("Usuario").document(email).collection("Fazendas")
                                .document(incEstadual).collection("Animais").document(editNumBrinco.getText().toString())
                                .set(animais)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        alert("Sucesso ao Cadastra");
                                        Intent i = new Intent(CadastroAnimais.this, List_Animais_Cad.class);
                                        Bundle parms = new Bundle();
                                        parms.putString("incEstadual", incEstadual);
                                        i.putExtras(parms);
                                        startActivity(i);
                                    }
                                });}else
                                    alert("Algum campo está vazio");
                    }catch (IllegalArgumentException e){
                        alert("Algum campo está vazio");
                    }

                }else
                    alert("usuario não logado, faça o logout no canto superior direito");
            }
        });
    }

    private void inicializarComponentes() {
        editDataNasc = (EditText) findViewById(R.id.edtDataNasc);
        editNumBrinco = (EditText) findViewById(R.id.edtNumBrincu);
        editNomeAnimais = (EditText) findViewById(R.id.edtNomeAnimais);
        editPesoAnimal = (EditText) findViewById(R.id.edtPesoDoAnimal);
        btnCadastra = (Button) findViewById(R.id.btnCadastra);
        racas = (Spinner) findViewById(R.id.spinnerRaca);
        masculino = (RadioButton) findViewById(R.id.masculino);
        feminino = (RadioButton) findViewById(R.id.femenino);
        natural = (RadioButton) findViewById(R.id.natural);
        artificial = (RadioButton) findViewById(R.id.artificial);
    }

    public void spinner() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.racas, android.R.layout.simple_spinner_item);
        racas.setAdapter(adapter);
        AdapterView.OnItemSelectedListener escolha = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nomeRacas = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        racas.setOnItemSelectedListener(escolha);
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
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(CadastroAnimais.this, Login.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
