package com.example.showdrink1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    //Constantes dos elementos da layout + conexao da API
    private final String URL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=";
    private Retrofit retrofitDrink;
    private EditText NameDrink;
    private ImageView ImageViewFotoDrink;
    private TextView txtnameDrink;
    private TextView nomeIntrucoes;
    private Button btnConsultarDrink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Definindo os valores para as variáveis dos elementos da layout
        NameDrink = findViewById(R.id.NameDrink);
        txtnameDrink = findViewById(R.id.txtnameDrink);
        nomeIntrucoes = findViewById(R.id.nomeInstrucoes);
        ImageViewFotoDrink = findViewById(R.id.ImageViewFotoDrink);
       /*retrofitDrink = new Retrofit.Builder()
                .baseUrl(URL) //endereço do webservice
                .addConverterFactory(GsonConverterFactory.create()) //conversor
                .build();*/

        Button btnConsultarDrink = (Button) findViewById(R.id.btnConsultarDrink);

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }
    }
    //Método que irá trazer os dados dos Drinks de acordo com que foi digitado no campo 
    public void buscaDrinks(View view) {
        // Recupera a string de busca.
        String queryString = NameDrink.getText().toString();
        // esconde o teclado quando o botão é clicado
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(view.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        // Verifica o status da conexão de rede
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }
        /* Se a rede estiver disponivel e o campo de busca não estiver vazio
         iniciar o Loader CarregaLivros */
        if (networkInfo != null && networkInfo.isConnected()
                && queryString.length() != 0) {
            Bundle queryBundle = new Bundle();
            queryBundle.putString("queryString", queryString);
            getSupportLoaderManager().restartLoader(0, queryBundle, this);
            //Altera para "carregando" enquanto a API faz seu processo
            nomeIntrucoes.setText(R.string.str_empty);
            txtnameDrink.setText(R.string.loading);
        }
        // atualiza a textview para informar que não há conexão ou termo de busca
        else {
            if (queryString.length() == 0) {
                nomeIntrucoes.setText(R.string.str_empty);
                txtnameDrink.setText(R.string.no_search_term);
            } else {
                nomeIntrucoes.setText(" ");
                txtnameDrink.setText(R.string.no_network);
            }
        }
    }
    @NonNull
    @Override
    //Cria o Loader para tarefa, recebendo o termo da busca
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";
        if (args != null) {
            queryString = args.getString("queryString");
        }
        return new CarregaDrinks(this, queryString);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {

        try {
            // Converte a resposta em Json
            JSONObject jsonObject = new JSONObject(data);
            // Obtem o JSONArray dos itens de livros
            JSONArray itemsArray = jsonObject.getJSONArray("drinks");
            // inicializa o contador
            int i = 0;
            String nome = null;
            String instrucao = null;
            String imgDrink = null;
            // Procura pro resultados nos itens do array
            while (i < itemsArray.length() &&
                    (instrucao == null && nome == null && imgDrink == null)) {
                // Obtem a informação
                JSONObject drink = itemsArray.getJSONObject(i);
                //  Obter autor e titulo para o item,
                // erro se o campo estiver vazio
                try {
                    nome = drink.getString("strDrink");
                    instrucao = drink.getString("strInstructions");
                    imgDrink = drink.getString("strDrinkThumb");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // move para a proxima linha
                i++;
            }
            //mostra o resultado qdo possivel.
            if (nome != null && instrucao != null) {
                txtnameDrink.setText(nome);
                nomeIntrucoes.setText(instrucao);
                ImageViewFotoDrink.setImageURI(Uri.parse(imgDrink));

                //nmLivro.setText(R.string.str_empty);
            } else {
                // Caso nada for encontrado, os itens ficarão vazios
                txtnameDrink.setText(R.string.no_results);
                nomeIntrucoes.setText(R.string.str_empty);
            }
        } catch (Exception e) {
            // Se não receber um JSOn valido, informa ao usuário
            txtnameDrink.setText(R.string.no_results);
            nomeIntrucoes.setText(R.string.str_empty);
            e.printStackTrace();
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
        /* btnConsultarDrink.setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View v) {

                consultarDrink();

            }

        });
    }

    private void consultarDrink() {

        // Verifica o status da conexão de rede
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected()) {
            //pega nickname
            String Drink = NameDrink.getText().toString().trim();

            String link = URL + Drink;

            //instanciando a interface
            RESTService restService = retrofitDrink.create(RESTService.class);

            //passando os dados para consulta
            Call<DrinkGit> call = restService.consultaDrink(Drink);
            //Log.i("Link da Consulta", link);

            call.enqueue(new Callback<DrinkGit>() {
                @Override
                public void onResponse(Call<DrinkGit> call, Response<DrinkGit> response) {
                    if (response.isSuccessful()) {
                        DrinkGit drinkGit = response.body();

                        //mostra foto via url
                        Picasso.get().load(drinkGit.getStrDrinkThumb()).transform(new CropCircleTransformation()).into(ImageViewFotoDrink);

                            txtnameDrink.setText(drinkGit.getStrDrink());
                            nameCategory.setText(drinkGit.getStrCategory());

                        Toast.makeText(getApplicationContext(), "Drink encontrado", Toast.LENGTH_LONG).show();
                    } else {
                        //alert para dizer que deu erro
                        //erro de limiti de pesquisas- Limite de 60 por hora
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setTitle("Ops :/");
                        alert.setMessage("Drink não encontrado");
                        alert.setPositiveButton("OK", null);
                        alert.show();

                        onPause();
                    }
                }

                @Override
                public void onFailure(Call<DrinkGit> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Ocorreu um erro ao tentar consultar o Drink. Erro: " + t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(), "Sem Conexão", Toast.LENGTH_LONG).show();
        }*/


    }

