package com.andreybalbino.meuolx.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreybalbino.meuolx.R;
import com.andreybalbino.meuolx.model.Anuncio;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterAnuncios extends RecyclerView.Adapter<AdapterAnuncios.MyViewHolder> {
    private List<Anuncio> anuncios;
    private Context context;

    public AdapterAnuncios(List<Anuncio> anuncios, Context context) {
        this.anuncios = anuncios;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_anuncios, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Anuncio anuncio = anuncios.get(i);
        myViewHolder.titulo.setText(anuncio.getTitulo());
        myViewHolder.preco.setText(anuncio.getValor());

        List<String> urlFotos = anuncio.getImagens();
        String urlCapa = urlFotos.get(0);
        Picasso.get().load(urlCapa).into(myViewHolder.imagemAnuncio);
    }

    @Override
    public int getItemCount() {
        return anuncios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView imagemAnuncio;
        private TextView titulo, preco;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imagemAnuncio = itemView.findViewById(R.id.imagemAnuncioId);
            titulo = itemView.findViewById(R.id.textTitulo);
            preco = itemView.findViewById(R.id.textPreco);
        }
    }
}
