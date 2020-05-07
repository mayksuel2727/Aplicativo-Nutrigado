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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appnutrigado.R;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Atualiza extends AppCompatActivity {

    private String nomeRacas, sexo, montada, incEstadual, numeroBrinco, nomeDoAnimal, dataNacimento, pesoAoNacer, racas, pesoAtual, racaoColocada, sobras;
    private EditText editNomeAnimais, editDataNasc, editPesoAnimal;
    private TextView texNumBrinco;
    private Spinner spinnerRacas;
    private RadioButton masculino, feminino, natural, artificial;
    private Button atualizar, btnDeletar;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar);

        Intent i = getIntent();
        if (i != null) {
            Bundle parms = i.getExtras();
            if (parms != null) {
                incEstadual = parms.getString("incEstadual");
                numeroBrinco = parms.getString("numerobrinco");
            }
        }

        inicializarComponentes();
        spinner();
        buscarDados();
        eventoClick();
    }

    // este metado e feio para busca todos os dados para setalo nos seus respectivos campos
    // dados que são buscados pela consulta que compara todos os animais com o numero do brinco do animal selecionado
    private void buscarDados() {
        String email1 = user.getEmail();
        db.collection("Usuario").document(email1).collection("Fazendas").document(incEstadual)
                .collection("Animais").whereEqualTo("Numero do Brinco", numeroBrinco)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        nomeDoAnimal = (String) document.get("Nome do Animal");
                        numeroBrinco = (String) document.get("Numero do Brinco");
                        dataNacimento = (String) document.get("Data de Nascimento");
                        pesoAoNacer = (String) document.get("Peso Ao Nascer");
                        pesoAtual = (String) document.get("Peso Atual");
                        sexo = (String) document.get("sexo");
                        montada = (String) document.get("montada");
                        racas = (String) document.get("Raça");
                        racaoColocada = (String) document.get("Ração Colocada");
                        sobras = (String) document.get("Sobras");
                        editNomeAnimais.setText(nomeDoAnimal);
                        texNumBrinco.setText(numeroBrinco);
                        editDataNasc.setText(dataNacimento);
                        editPesoAnimal.setText(pesoAoNacer);
                        if (sexo.equals("Macho"))
                            masculino.setChecked(true);
                        else
                            feminino.setChecked(true);
                        if (montada.equals("natural"))
                            natural.setChecked(true);
                        else
                            artificial.setChecked(true);
                        if (racas.equals("Holandesa"))
                            spinnerRacas.setSelection(0);
                        else if (racas.equals("Jersey"))
                            spinnerRacas.setSelection(1);
                        else if (racas.equals("Gir"))
                            spinnerRacas.setSelection(2);
                        else if (racas.equals("Girolando"))
                            spinnerRacas.setSelection(3);
                        else
                            spinnerRacas.setSelection(4);
                    }
                }
            }
        });
    }

    private void eventoClick() {
        //e feita uma mascara para da um formato padrão a data de nacimento
        SimpleMaskFormatter simpleMaskFormatter = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher maskTextWatcher = new MaskTextWatcher(editDataNasc, simpleMaskFormatter);
        editDataNasc.addTextChangedListener(maskTextWatcher);
        //fim da mascara
        // ao clickar em atualizar e atualizados todos os novos valores
        atualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user != null) {
                    Radio_Button();
                    String email = user.getEmail();
                    Map<String, Object> animais = new HashMap<>();
                    animais.put("Numero do Brinco", texNumBrinco.getText().toString());
                    animais.put("Nome do Animal", editNomeAnimais.getText().toString());
                    animais.put("Raça", nomeRacas);
                    animais.put("sexo", sexo);
                    animais.put("montada", montada);
                    animais.put("Data de Nascimento", editDataNasc.getText().toString());
                    animais.put("Peso Ao Nascer", editPesoAnimal.getText().toString());
                    animais.put("Peso Atual", pesoAtual);
                    animais.put("Ração Colocada", racaoColocada);
                    animais.put("Sobras", sobras);
                    System.out.println(incEstadual);
                    try {
                        if (sexo != null && montada != null) {
                            db.collection("Usuario").document(email).collection("Fazendas")
                                    .document(incEstadual).collection("Animais").document(numeroBrinco)
                                    .set(animais)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            alert("Sucesso ao Cadastra");
                                            Intent i = new Intent(Atualiza.this, List_Animais_Cad.class);
                                            Bundle parms = new Bundle();
                                            parms.putString("incEstadual", incEstadual);
                                            i.putExtras(parms);
                                            startActivity(i);
                                        }
                                    });
                        }else
                            alert("Agum Campo está vazio");
                    } catch (IllegalArgumentException e) {
                        alert("Agum Campo está vazio");
                    }

                }
            }
        });

        //e este metado e feito para deletar um animal quando clickado no botão de delete
        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user.getEmail();
                if (user != null) {
                    db.collection("Usuario").document(email).collection("Fazendas")
                            .document(incEstadual).collection("Animais").document(numeroBrinco)
                            .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            alert("deletado com sucesso");
                            Intent i = new Intent(Atualiza.this, List_Animais_Cad.class);
                            Bundle parms = new Bundle();
                            parms.putString("incEstadual", incEstadual);
                            i.putExtras(parms);
                            startActivity(i);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            alert("falha ao deletar");
                        }
                    });
                }
            }
        });
    }

    //pega o intem selecionado no spinner e passar para uma string
    private void spinner() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.racas, android.R.layout.simple_spinner_item);
        spinnerRacas.setAdapter(adapter);
        AdapterView.OnItemSelectedListener escolha = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nomeRacas = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        spinnerRacas.setOnItemSelectedListener(escolha);
    }

    private void inicializarComponentes() {
        texNumBrinco = (TextView) findViewById(R.id.textNumBrinco);
        editNomeAnimais = (EditText) findViewById(R.id.edtNomeAnimais);
        spinnerRacas = (Spinner) findViewById(R.id.spinnerRaca);
        editDataNasc = (EditText) findViewById(R.id.edtDataNasc);
        editPesoAnimal = (EditText) findViewById(R.id.edtPesoDoAnimal);
        masculino = (RadioButton) findViewById(R.id.masculino);
        feminino = (RadioButton) findViewById(R.id.femenino);
        natural = (RadioButton) findViewById(R.id.natural);
        artificial = (RadioButton) findViewById(R.id.artificial);
        atualizar = (Button) findViewById(R.id.btnAtualizar);
        btnDeletar = (Button) findViewById(R.id.btnDeletar);
    }

    private void alert(String msg) {
        Toast.makeText(Atualiza.this, msg, Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(Atualiza.this, Login.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
