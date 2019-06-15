package com.andreybalbino.meuolx.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreybalbino.meuolx.R;
import com.andreybalbino.meuolx.model.Anuncio;
import com.blackcat.currencyedittext.CurrencyEditText;
import com.santalu.maskedittext.MaskEditText;
import com.squareup.picasso.Picasso;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class VisualizarAnuncioActivity extends AppCompatActivity {
    private TextView titulo, descricao, estado, preco;
    private CarouselView carouselView;
    private Anuncio anuncio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_anuncio);
        inicializarComponentes();

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            anuncio = (Anuncio) bundle.getSerializable("anuncioSelecionado");
            titulo.setText(anuncio.getTitulo());
            descricao.setText(anuncio.getDescricao());
            estado.setText(anuncio.getEstado());
            preco.setText(anuncio.getValor());

            ImageListener imageListener = new ImageListener() {
                @Override
                public void setImageForPosition(int position, ImageView imageView) {
                    String urlString = anuncio.getImagens().get(position);
                    Picasso.get().load(urlString).into(imageView);
                }
            };
            carouselView.setPageCount(anuncio.getImagens().size());
            carouselView.setImageListener(imageListener);
        }
    }
    private void inicializarComponentes(){
        carouselView = findViewById(R.id.carouselView);
        titulo= findViewById(R.id.textTituloDetalhe);
        descricao = findViewById(R.id.textDescricaoDetalhe);
        estado = findViewById(R.id.textEstadoDetalhe);
        preco = findViewById(R.id.textPrecoDetalhe);
    }
    public void visualizarTelefone(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", anuncio.getTelefone(), null));
        startActivity(intent);
    }
}
