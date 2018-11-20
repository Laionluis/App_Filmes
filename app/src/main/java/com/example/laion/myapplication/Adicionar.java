package com.example.laion.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import retrofit2.Callback;
import retrofit2.Response;

public class Adicionar extends AppCompatActivity {
    Result filme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar);
        this.setFinishOnTouchOutside(true);

        Intent intent = getIntent();
        filme = (Result) intent.getSerializableExtra("Result");
        TextView descricao = findViewById(R.id.Descricao_text_view);
        if(filme.getOverview().isEmpty())
            descricao.setText("Não Disponivel");
        else
            descricao.setText(filme.getOverview());
        descricao.setMovementMethod(new ScrollingMovementMethod());
        setTitle(filme.getTitle());

        TextView generos = findViewById(R.id.genero_textView);
        StringBuilder genero_text = new StringBuilder();
        for(int i = 0; i < filme.getGenre_names().size() - 1; ++i){
            genero_text.append(String.format("%s, ", filme.getGenre_names().get(i)));
        }
        genero_text.append(String.format("%s", filme.getGenre_names().get(filme.getGenre_names().size()-1)));
        generos.setText(genero_text);
        generos.setMovementMethod(new ScrollingMovementMethod());

        TextView data = findViewById(R.id.data_textView);
        data.setText(filme.getReleaseDate());

        Button btn = findViewById(R.id.salvar);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("titulo", filme.getTitle());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }


}
