package com.example.laion.myapplication;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.arlib.floatingsearchview.FloatingSearchView;

import net.skoumal.fragmentback.BackFragmentAppCompatActivity;
import net.skoumal.fragmentback.BackFragmentHelper;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends BackFragmentAppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, Lista_para_assistir.Communicator {
    FloatingSearchView searchView;
    DrawerLayout mDrawerLayout;
    BottomNavigationView bottomNavigationView;
    private ViewPager mSlideViewPager;
    private SliderAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSlideViewPager = findViewById(R.id.slider_viewPager);
        sliderAdapter = new SliderAdapter(getSupportFragmentManager());
        mSlideViewPager.setAdapter(sliderAdapter);

        searchView = findViewById(R.id.search_view_floating);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        TrataSearchView();

        bottomNavigationView = findViewById(R.id.bottom_app_bar);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.toWatch:
                        mSlideViewPager.setCurrentItem(1, true);
                        break;
                    case R.id.inicio:
                        mSlideViewPager.setCurrentItem(0, true);
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });

        mSlideViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if(i == 0)
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                if(i == 1)
                    bottomNavigationView.getMenu().getItem(1).setChecked(false);


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public void TrataSearchView(){
        searchView.attachNavigationDrawerToMenuButton(mDrawerLayout);
        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                searchView.setSearchHint("Procurar filmes...");
            }
            @Override
            public void onFocusCleared(){
                searchView.setSearchHint("Procurar filmes");
            }
        });


        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                mAboutDataListener.onDataReceived(newQuery);


                /*List<Filme_suggestion> filme_suggestionList = new ArrayList<>();
                if(movie != null && movie.getTotalResults() > 0){
                    int tamanho;
                    if (movie.getTotalResults() < 20)
                        tamanho = movie.getTotalResults();
                    else tamanho = 20;
                    for(int i = 0; i < tamanho ; i ++){
                        filme_suggestionList.add(new Filme_suggestion(movie.getResults().get(i).getTitle(), movie.getResults().get(i).getId()));
                    }
                    searchView.swapSuggestions(filme_suggestionList);
                    searchView.hideProgress();
                }*/
            }
        });
    }

    /*public void callSearch(String query) {
        ArrayList<Estrutura_Lista> registrosFilmes = dbFilmes.ObterFilmesPorNome(query);
        Estrutura_Lista = registrosFilmes;
        adapter = new CustomAdapter(Estrutura_Lista, getApplicationContext());
        listView.setAdapter(adapter);
        VerificaSeTemRegistros();
    }*/



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ok", 1);
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else if(!BackFragmentHelper.fireOnBackPressedEvent(this)) {
            // lets do the default back action if fragments don't consume it
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        switch (menuItem.getItemId()) {
            case R.id.Populares:
                mChangeCategoryListener.OnChangeCat(0);
            case R.id.Tendencia:
                mChangeCategoryListener.OnChangeCat(1);
            case R.id.Mais_Votados:
                mChangeCategoryListener.OnChangeCat(2);
            default:
                return true;
        }
    }

    @Override
    public void respond(Boolean carregar) {
        Lista_para_assistir lista_para_assistir = (Lista_para_assistir) sliderAdapter.getRegisteredFragment(1);
        lista_para_assistir.recarregarPagina(true);
    }

    private OnAboutDataReceivedListener mAboutDataListener;

    public interface OnAboutDataReceivedListener {
        void onDataReceived(String query);
    }

    public void setAboutDataListener(OnAboutDataReceivedListener listener) {
        this.mAboutDataListener = listener;
    }

    private OnChangeCategory mChangeCategoryListener;

    public interface OnChangeCategory{
        void OnChangeCat(int cat);
    }

    public void setChangeCategoryListener(OnChangeCategory listener) {
        this.mChangeCategoryListener = listener;
    }

}

