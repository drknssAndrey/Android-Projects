package com.andreybalbino.olx.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;

import com.andreybalbino.olx.R;
import com.andreybalbino.olx.adapter.AdapterAnuncios;
import com.andreybalbino.olx.helper.ConfiguracaoFirebase;
import com.andreybalbino.olx.helper.RecyclerItemClickListener;
import com.andreybalbino.olx.model.Anuncio;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MeusAnunciosActivity extends AppCompatActivity {

    private List<Anuncio> anuncios = new ArrayList<>();
    private RecyclerView recyclerAnuncio;
    private AdapterAnuncios adapterAnuncios;
    private DatabaseReference anuncioUsuarioRef;
    private ValueEventListener valueEventListenerAnunciosUsuario;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_anuncios);
//configuracoes iniciais
        anuncioUsuarioRef = ConfiguracaoFirebase.getFirebaseDatabase().child("meus_anuncios").child(ConfiguracaoFirebase.getIdUsuarioLogado());
        inicializarComponentes();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MeusAnunciosActivity.this, CadastrarAnuncioActivity.class));
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //configurarRecycler
        recyclerAnuncio.setLayoutManager(new LinearLayoutManager(this));
        recyclerAnuncio.setHasFixedSize(true);
        adapterAnuncios = new AdapterAnuncios(anuncios, this);
        recyclerAnuncio.setAdapter(adapterAnuncios);

        //adiciona evento de click no recyclerView
        recyclerAnuncio.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerAnuncio, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {
                Anuncio anuncioSelecionado = anuncios.get(position);
                anuncioSelecionado.remover();

                adapterAnuncios.notifyDataSetChanged();
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));
    }

    private void recuperarAnuncios() {
        dialog = new SpotsDialog.Builder().setContext(this).setMessage("Recuperando anúncios").setCancelable(false).build();
        dialog.show();
      valueEventListenerAnunciosUsuario = anuncioUsuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                anuncios.clear();
                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    anuncios.add(ds.getValue(Anuncio.class));
                }
                Collections.reverse(anuncios );
                adapterAnuncios.notifyDataSetChanged();
                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void inicializarComponentes() {
        recyclerAnuncio = findViewById(R.id.recyclerAnuncios);

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarAnuncios();
    }

    @Override
    protected void onStop() {
        super.onStop();
        anuncioUsuarioRef.removeEventListener(valueEventListenerAnunciosUsuario);
    }
}
