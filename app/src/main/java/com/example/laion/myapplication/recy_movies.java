package com.example.laion.myapplication;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;

import net.skoumal.fragmentback.BackFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

public class recy_movies extends Fragment implements BackFragment, MainActivity.OnAboutDataReceivedListener, MainActivity.OnChangeCategory {
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 0;
    RecyclerView mView;
    MovieApadpter adapterMovie;
    List<Result> results;
    TextView textView_semConexao;
    MainActivity mActivity;
    Movie movie_search;
    Genero generos;
    Lista_para_assistir.Communicator comm;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        comm = (Lista_para_assistir.Communicator)context;
    }

    public recy_movies() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainActivity) getActivity();
        mActivity.setAboutDataListener(this);
        mActivity.setChangeCategoryListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recy_movies, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view.findViewById(R.id.movieView);
        mView.setLayoutManager(new GridLayoutManager(getContext(),2));

        if(!isNetworkAvailable()){
            textView_semConexao = view.findViewById(R.id.textView_semConexao);
            textView_semConexao.setVisibility(View.VISIBLE);
        }
        movieLoad();
        genreLoad();
    }

    private void genreLoad(){
        ApiInterface api = AppClient.getRetrofit().create(ApiInterface.class);
        retrofit2.Call<Genero> call = api.getGeneros();
        call.enqueue(new Callback<Genero>() {
            @Override
            public void onResponse(retrofit2.Call<Genero> call, Response<Genero> response) {
                generos = response.body();
            }

            @Override
            public void onFailure(retrofit2.Call<Genero> call, Throwable t) {

            }
        });
    }

    private void movieLoad(){
        ApiInterface api = AppClient.getRetrofit().create(ApiInterface.class);
        retrofit2.Call<Movie> call = api.getPopular();
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(retrofit2.Call<Movie> call, Response<Movie> response) {
                Movie movie = response.body();
                adapterMovie = new MovieApadpter(results);
                adapterMovie.setData(movie.getResults());
                mView.setAdapter(adapterMovie);
                trataClick(movie);
            }

            @Override
            public void onFailure(retrofit2.Call<Movie> call, Throwable t) {

            }
        });
    }

    private void trataClick(final Movie movie) {
        adapterMovie.setOnItemClickListener(new MovieApadpter.ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d("TAG", "Elemento " + movie.getResults().get(position).getTitle() + " clicado.");
                AbrirInformacoes(movie.getResults().get(position));
            }
        });
    }

    public void loadTrendings(){
        ApiInterface api = AppClient.getRetrofit().create(ApiInterface.class);
        retrofit2.Call<Movie> call = api.getTrending();
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(retrofit2.Call<Movie> call, Response<Movie> response) {
                Movie movie = response.body();
                adapterMovie = new MovieApadpter(results);
                adapterMovie.setData(movie.getResults());
                mView.setAdapter(adapterMovie);
                trataClick(movie);
            }

            @Override
            public void onFailure(retrofit2.Call<Movie> call, Throwable t) {

            }
        });
    }

    public void loadToprated(){
        ApiInterface api = AppClient.getRetrofit().create(ApiInterface.class);
        retrofit2.Call<Movie> call = api.getTopRated();
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(retrofit2.Call<Movie> call, Response<Movie> response) {
                Movie movie = response.body();
                adapterMovie = new MovieApadpter(results);
                adapterMovie.setData(movie.getResults());
                mView.setAdapter(adapterMovie);
                trataClick(movie);
            }

            @Override
            public void onFailure(retrofit2.Call<Movie> call, Throwable t) {

            }
        });
    }

    public void TrataSearchView(String newQuery){
        ApiInterface api = AppClient.getRetrofit().create(ApiInterface.class);
        retrofit2.Call<Movie> call = api.getMovieByName(newQuery);
        call.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(retrofit2.Call<Movie> call, Response<Movie> response) {
                movie_search = response.body();
                if(movie_search != null && movie_search.getTotalResults() > 0){
                    adapterMovie = new MovieApadpter(results);
                    adapterMovie.setData(movie_search.getResults());
                    mView.setAdapter(adapterMovie);
                    trataClick(movie_search);
                }else
                    movieLoad();
            }

            @Override
            public void onFailure(retrofit2.Call<Movie> call, Throwable t) {

            }
        });
    }

    public void AbrirInformacoes(Result filme) {
        List<String> generosNames = new ArrayList<>();
        for(int i = 0 ; i < generos.getGenres().size(); ++i){
            Genre genero = generos.getGenres().get(i);
            if(filme.getGenreIds().contains(genero.getId())){
                generosNames.add(genero.getName());
            }
        }
        filme.setGenre_names(generosNames);
        Intent intent = new Intent(getContext(), Adicionar.class);
        intent.putExtra("Result", filme);
        startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE) {
            if (resultCode == MainActivity.RESULT_OK) {
                // pega a string do intent
                String titulo = data.getStringExtra("titulo");
                SimpleDateFormat mdformat = new SimpleDateFormat("dd / MM / yyyy ");
                String date = mdformat.format(Calendar.getInstance().getTime());

                Filmes dbFilmes = new Filmes(getContext());
                dbFilmes.insertFilme(titulo, date, 0,0);

                comm.respond(true);
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        return false;
    }

    @Override
    public int getBackPriority() {
        return 0;
    }

    @Override
    public void onDataReceived(String query) {
        TrataSearchView(query);
    }


    @Override
    public void OnChangeCat(int cat) {
        if(cat == 0)
            movieLoad();
        if(cat == 1)
            loadTrendings();
        if(cat == 2)
            loadToprated();
    }
}
