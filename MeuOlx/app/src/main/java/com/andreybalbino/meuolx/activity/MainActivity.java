package com.andreybalbino.meuolx.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.andreybalbino.meuolx.R;
import com.andreybalbino.meuolx.adapter.AdapterAnuncios;
import com.andreybalbino.meuolx.helper.ConfiguracaoFirebase;
import com.andreybalbino.meuolx.helper.RecyclerItemClickListener;
import com.andreybalbino.meuolx.model.Anuncio;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private Button botaoCategoria, botaoEstado;
    private DatabaseReference databaseReference;
    private List<Anuncio> anuncioList = new ArrayList<>();
    private AdapterAnuncios adapterAnuncios;
    private RecyclerView recyclerView;
    private Spinner spinnerEstado, spinnerCategoria;
    private boolean inseriuEstado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inicializarComponentes();
        //configuracoes iniciais
        auth = ConfiguracaoFirebase.getFirebaseAuth();
        databaseReference = ConfiguracaoFirebase.getDatabaseReference();

        //configurar recycler
        configurarRecycler();


        botaoEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configurarSpinnerEstado();

            }
        });
        botaoCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configurarSpinnerCategoria();
            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(MainActivity.this, VisualizarAnuncioActivity.class);
                Anuncio anuncio = anuncioList.get(position);
                intent.putExtra("anuncioSelecionado", anuncio);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (auth.getCurrentUser() != null) {
            menu.setGroupVisible(R.id.logado, true);
        } else {
            menu.setGroupVisible(R.id.deslogado, true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.entrar:
                startActivity(new Intent(MainActivity.this, CadastroActivity.class));
                break;
            case R.id.meus_anuncios:
                startActivity(new Intent(MainActivity.this, MeusAnunciosActivity.class));
                break;
            case R.id.sair:
                auth.signOut();
                invalidateOptionsMenu();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void recuperarAnuncios() {
        DatabaseReference anunciosRef = databaseReference.child("anuncios");
        anunciosRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                anuncioList.clear();
                for (DataSnapshot estados : dataSnapshot.getChildren()) {
                    for (DataSnapshot categorias : estados.getChildren()) {
                        for (DataSnapshot anuncios : categorias.getChildren()) {
                            Anuncio anuncio = anuncios.getValue(Anuncio.class);
                            anuncioList.add(anuncio);
                        }
                    }
                }
                adapterAnuncios.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void configurarRecycler() {
        adapterAnuncios = new AdapterAnuncios(anuncioList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterAnuncios);
    }

    private void inicializarComponentes() {
        recyclerView = findViewById(R.id.recyclerViewAnuncios);
        botaoEstado = findViewById(R.id.buttonEstado);
        botaoCategoria = findViewById(R.id.buttonCategoria);

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarAnuncios();
    }

    private void configurarSpinnerEstado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Buscar por estado");
        View view = getLayoutInflater().inflate(R.layout.spinner_filtro, null);
        spinnerEstado = view.findViewById(R.id.spinnerEst);
        builder.setView(view);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                recuperarAnunciosPorEstado();
            }
        });
        builder.create().show();
        String[] estados = getResources().getStringArray(R.array.estados);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEstado.setAdapter(adapter);
    }

    private void configurarSpinnerCategoria() {
        if (inseriuEstado) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Buscar por categoria");
            View view = getLayoutInflater().inflate(R.layout.spinner_filtro, null);
            spinnerCategoria = view.findViewById(R.id.spinnerEst);
            builder.setView(view);
            builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    recuperarAnunciosPorCategoria();
                }
            });
            builder.create().show();
            String[] categorias = getResources().getStringArray(R.array.categorias);
            ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerCategoria.setAdapter(adapter);
        } else {
            Toast.makeText(this, "É necessário inserir o estado para continuar!", Toast.LENGTH_SHORT).show();
        }
    }

    private void recuperarAnunciosPorEstado() {
        DatabaseReference anunciosEstadoRef = databaseReference.child("anuncios").child(spinnerEstado.getSelectedItem().toString());
        anunciosEstadoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                anuncioList.clear();
                for (DataSnapshot categorias : dataSnapshot.getChildren()) {
                    for (DataSnapshot anuncios : categorias.getChildren()) {
                        Anuncio anuncio = anuncios.getValue(Anuncio.class);
                        anuncioList.add(anuncio);

                    }
                }
                inseriuEstado = true;
                adapterAnuncios.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void recuperarAnunciosPorCategoria() {
        DatabaseReference anunciosEstadoRef = databaseReference.child("anuncios").child(spinnerEstado.getSelectedItem().toString()).child(spinnerCategoria.getSelectedItem().toString());
        anunciosEstadoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                anuncioList.clear();
                for (DataSnapshot anuncios : dataSnapshot.getChildren()) {
                    Anuncio anuncio = anuncios.getValue(Anuncio.class);
                    anuncioList.add(anuncio);
                }
                adapterAnuncios.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

}
