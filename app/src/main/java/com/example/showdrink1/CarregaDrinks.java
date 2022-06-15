package com.example.showdrink1;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
//A classe permite operar tarefas em posição secundária ou background, fazendo mudanças na UI diretamente"
public class CarregaDrinks extends AsyncTaskLoader<String> {
    private String mQueryString;
    CarregaDrinks(Context context, String queryString) {
        super(context);
        mQueryString = queryString;
    }
    @Override
    //Inicia o processo das tarefas em background
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
    @Nullable
    @Override
    //Retorna os dados dos Drinks em operação de background
    public String loadInBackground() {
        return NetworkUtils.buscaInfosLivro(mQueryString);
    }
}
