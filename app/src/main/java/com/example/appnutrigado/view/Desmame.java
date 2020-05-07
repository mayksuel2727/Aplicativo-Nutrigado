package com.example.appnutrigado.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

public class Desmame extends AppCompatActivity {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private ListView listViewAnimais;
    private List<String> listAnimais = new ArrayList<String>();
    private ArrayAdapter<String> arrayAdapterAnimais;
    //private String animalSelecionado;
    private String incEstadual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desmame);
        Intent i = getIntent();
        if (i != null) {
            Bundle parms = i.getExtras();
            if (parms != null) {
                incEstadual = parms.getString("incEstadual");
                System.out.println(incEstadual);
            }
        }
        eventoBusca();
        inicializarComponentes();
    }

    private void eventoBusca() {
        String email1 = user.getEmail();
        System.out.println(incEstadual);
        db.collection("Usuario").document(email1).collection("Fazendas")
                .document(incEstadual).collection("Animais").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // aki e percorrido todos animais e é pego nome, peso de atual e de quando nasceu, e o ração consumida e sobras do dia anterior
                    // assim e feito o calculo de o bezerro ter o dobro de peso e comer mas de 1000 g de ração por dia
                    // ao obedecer esses dois criterios o nome dele e colocado numa list para poder aparecer na list view como pronto para o desmame
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String nome = (String) documentSnapshot.get("Nome do Animal");
                        String pesoNacerString = (String) documentSnapshot.get("Peso Ao Nascer");
                        String pesoAtualString = (String) documentSnapshot.get("Peso Atual");
                        String racaoColocadaString = (String) documentSnapshot.get("Ração Colocada");
                        String sobraString = (String) documentSnapshot.get("Sobras");
                        //para não passar valores null pro parseDouble
                        if (pesoAtualString!= null && pesoNacerString!=null && racaoColocadaString != null && sobraString != null) {
                            double pesoAtual = Double.parseDouble(pesoAtualString);
                            double pesoNacer = Double.parseDouble(pesoNacerString);
                            double resPeso = pesoAtual / pesoNacer;
                            double racaoColocada = Double.parseDouble(racaoColocadaString);
                            double sobras = Double.parseDouble(sobraString);
                            double resRacao = racaoColocada - sobras;
                            if (resPeso >= 2 && resRacao >= 1000) {
                                listAnimais.add(nome);
                            }
                        }
                    }
                }
                arrayAdapterAnimais = new ArrayAdapter<String>(getApplication(), android.R.layout.simple_expandable_list_item_1, listAnimais);
                listViewAnimais.setAdapter(arrayAdapterAnimais);
            }
        });
    }

    private void inicializarComponentes() {
        listViewAnimais = (ListView) findViewById(R.id.listViewAnimais);
    }

    /*private void alert(String msg) {
        Toast.makeText(Desmame.this, msg, Toast.LENGTH_SHORT).show();
    }*/

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
                Intent i = new Intent(Desmame.this, Login.class);
                startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

}
