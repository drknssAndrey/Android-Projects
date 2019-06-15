package com.andreybalbino.meuolx.activity;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.andreybalbino.meuolx.R;
import com.andreybalbino.meuolx.adapter.AdapterAnuncios;
import com.andreybalbino.meuolx.helper.ConfiguracaoFirebase;
import com.andreybalbino.meuolx.helper.RecyclerItemClickListener;
import com.andreybalbino.meuolx.model.Anuncio;
import com.blackcat.currencyedittext.CurrencyEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.santalu.maskedittext.MaskEditText;

import java.util.ArrayList;
import java.util.List;

public class MeusAnunciosActivity extends AppCompatActivity {

    private List<Anuncio> anuncios = new ArrayList<>();
    private RecyclerView recyclerView;
    private DatabaseReference meusAnuncios, databaseReference;
    private String idUsuarioLogado = ConfiguracaoFirebase.getIdUsuarioLogado();
    private AdapterAnuncios adapterAnuncios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_anuncios);
        recyclerView = findViewById(R.id.recyclerViewMeusAnuncios);

        databaseReference = ConfiguracaoFirebase.getDatabaseReference();

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

        configurarRecycler();

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }

            @Override
            public void onLongItemClick(View view, int position) {

                Anuncio anuncioSelecionado = anuncios.get(position);
                anuncioSelecionado.remover();
                anuncios.remove(anuncioSelecionado);
                adapterAnuncios.notifyDataSetChanged();
            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        }));

    }

    private void recuperarAnuncios() {
        meusAnuncios = databaseReference.child("meus_anuncios").child(idUsuarioLogado);
        meusAnuncios.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                anuncios.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Anuncio anuncio = ds.getValue(Anuncio.class);
                    anuncios.add(anuncio);
                }
                adapterAnuncios.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void configurarRecycler() {
        adapterAnuncios = new AdapterAnuncios(anuncios, MeusAnunciosActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterAnuncios);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarAnuncios();
    }
}
