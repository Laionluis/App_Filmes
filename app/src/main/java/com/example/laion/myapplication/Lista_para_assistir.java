package com.example.laion.myapplication;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import net.skoumal.fragmentback.BackFragment;

import java.util.ArrayList;

public class Lista_para_assistir extends Fragment implements BackFragment {

    ArrayList<Estrutura_Lista> Estrutura_Lista;
    ListView listView;
    TextView registros_view;
    CustomAdapter adapter;
    Filmes dbFilmes;
    int aux;
    Button deletar;
    recy_movies fragment_filmes;

    public Lista_para_assistir() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lista_para_assistir, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbFilmes = new Filmes(getContext());
        listView = view.findViewById(R.id.list_view_registro);
        registros_view = view.findViewById(R.id.textView_registro);
        Estrutura_Lista = new ArrayList<>();
        if(savedInstanceState == null){
            Estrutura_Lista = dbFilmes.getAllFilmes(true);
        }
        else{
            Estrutura_Lista = dbFilmes.getAllFilmes(false);
        }
        adapter = new CustomAdapter(Estrutura_Lista, getContext());
        listView.setAdapter(adapter);
        deletar = view.findViewById(R.id.deletar);
        VisibilidadesBotoes();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                Estrutura_Lista dataModel= Estrutura_Lista.get(position);  //pega posicao do item na lista
                if (aux == 1){
                    dataModel.isItemSelected = !dataModel.isItemSelected;
                    adapter.notifyDataSetChanged();
                }else{
                    dataModel.checked = !dataModel.checked;                                      //se estiver marcado desmarca e viceversa
                    adapter.notifyDataSetChanged();                                              //avisa adaptador das mudanças
                }
                dbFilmes.updateFilme(position+1,dataModel.nome, dataModel.data, dataModel.checked ? 1 : 0, dataModel.isItemSelected ? 1 : 0);
                trataVisibilidadesBotoes(dataModel);
            }
        });

        // em um longo click seleciona o item da lista
        listView.setOnItemLongClickListener (new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView parent, final View view, final int position, long id) {
                deletar.setVisibility(View.VISIBLE);
                aux = 1;
                Estrutura_Lista item_lista = Estrutura_Lista.get(position);
                item_lista.isItemSelected = !item_lista.isItemSelected;
                adapter.notifyDataSetChanged();
                dbFilmes.updateFilme(position+1,item_lista.nome, item_lista.data, item_lista.checked ? 1 : 0, item_lista.isItemSelected ? 1 : 0);
                trataVisibilidadesBotoes(item_lista);
                return true; //faz só o click longo sem o click normal
            }
        });
        VerificaSeTemRegistros();
    }

    public void VisibilidadesBotoes(){
        Estrutura_Lista item_lista1;
        for (int i = 0; i < Estrutura_Lista.size(); i++){
            item_lista1 = Estrutura_Lista.get(i);
            if (item_lista1.isItemSelected){
                deletar.setVisibility(View.VISIBLE);
                aux = 1;
                trataVisibilidadesBotoes(item_lista1);
                break;
            }
        }
    }

    public void trataVisibilidadesBotoes(Estrutura_Lista item_lista){
        if (item_lista.isItemSelected){
            deletar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    showSimplePopUp();
                    deletar.setVisibility(View.GONE);
                }
            });
        }else{
            aux = 0;
            Estrutura_Lista item_lista1;
            for (int i = 0; i < Estrutura_Lista.size(); i++){
                item_lista1 = Estrutura_Lista.get(i);
                if (item_lista1.isItemSelected){
                    aux = 1;
                    break;
                }
            }
            if(aux == 0){
                deletar.setVisibility(View.GONE);
            }
        }
    }

    public void VerificaSeTemRegistros(){
        if(Estrutura_Lista.size() == 0)
            registros_view.setVisibility(View.VISIBLE);
        else{
            registros_view.setVisibility(View.GONE);
        }
    }

    private void showSimplePopUp() {
        AlertDialog.Builder helpBuilder = new AlertDialog.Builder(getContext());
        helpBuilder.setCancelable(true);
        helpBuilder.setTitle("EXCLUIR:");
        helpBuilder.setMessage("Depois que exluir não há como voltar: ");
        helpBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Estrutura_Lista item_lista1;
                        for (int i = (Estrutura_Lista.size()-1); i >= 0; i--){
                            item_lista1 = Estrutura_Lista.get(i);
                            if(item_lista1.isItemSelected){
                                adapter.remove(Estrutura_Lista.get(i));
                                dbFilmes.deleteFilme(item_lista1.nome);
                            }
                        }
                        aux = 0;
                        VerificaSeTemRegistros();
                    }
                });
        helpBuilder.setNegativeButton(android.R.string.cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Estrutura_Lista item_lista1;
                        for (int i = 0; i < Estrutura_Lista.size(); i++){
                            item_lista1 = Estrutura_Lista.get(i);
                            item_lista1.isItemSelected = false;
                        }
                        aux = 0;
                        adapter.notifyDataSetChanged();
                    }
                });
        AlertDialog helpDialog = helpBuilder.create();
        helpDialog.show();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ok", 1);
    }


    @Override
    public boolean onBackPressed() {
        if(deletar.getVisibility() == View.VISIBLE){
            Estrutura_Lista item_lista1;
            for (int i = 0; i < Estrutura_Lista.size(); i++){
                item_lista1 = Estrutura_Lista.get(i);
                item_lista1.isItemSelected = false;
            }
            aux = 0;
            adapter.notifyDataSetChanged();
            deletar.setVisibility(View.GONE);
            return true;
        }else
            return false;
    }

    @Override
    public int getBackPriority() {
        return LOW_BACK_PRIORITY;
    }

    interface Communicator{
        public void respond(Boolean carregar);
    }

    public void recarregarPagina(Boolean carregar){
        if(carregar){
            Estrutura_Lista = dbFilmes.getAllFilmes(true);
            adapter = new CustomAdapter(Estrutura_Lista, getContext());
            listView.setAdapter(adapter);
            VerificaSeTemRegistros();
        }
    }

}
