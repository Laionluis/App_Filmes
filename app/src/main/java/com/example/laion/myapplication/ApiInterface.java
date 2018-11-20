package com.example.laion.myapplication;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    public static String DB_API = "b849f85b537d0c5fb344b42b4d45888c";

    @GET("movie/popular?api_key="+DB_API+"&language=pt-BR")
    Call<Movie> getPopular();

    @GET("movie/top_rated?api_key="+DB_API+"&language=pt-BR")
    Call<Movie> getTopRated();

    @GET("trending/all/day?api_key="+DB_API+"&language=pt-BR")
    Call<Movie> getTrending();

    @GET("search/movie?api_key="+DB_API+"&language=pt-BR")
    Call<Movie> getMovieByName(@Query("query") String query);

    @GET("movie/{id}?api_key="+DB_API)
    Call<Result> getMovieById(@Path("id") int id);

    @GET("genre/movie/list?api_key="+DB_API+"&language=pt-BR")
    Call<Genero> getGeneros();

}
