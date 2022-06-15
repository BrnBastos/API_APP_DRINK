package com.example.showdrink1;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
//Classe utilizada para conexão com a API de Drinks
public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();
    // Constantes utilizadas pela API
   
    // URL que adquire acesso a API.
    private static final String DRINKS_URL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?";
    
    
    // Parametros da string de Busca
    private static final String QUERY_PARAM = "s";
    /*   DESATIVADOS
        // Limitador da qtde de resultados
        //private static final String MAX_RESULTS = "maxResults";
        // Parametro do tipo de impressão
        //private static final String TIPO_IMPRESSAO = "printType";
    
    */
    
    //Utilizado para adaptar o arquivo JSON recebido
    static String buscaInfosLivro(String queryString) {

        HttpURLConnection urlConnection = null;
        //Permite a leitura de texto por meio de uma abertura de transmissão (inputStream)
        BufferedReader reader = null;
        String bookJSONString = null;
        try {
            // Construção da URI para buscar o drink específico
            Uri builtURI = Uri.parse(DRINKS_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .build();
            // Converte a URI para a URL.
            URL requestURL = new URL(builtURI.toString());
            // Abre a conexão de rede
            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            // Busca o InputStream, este que permite a leitura de bytes de um arquivo.
            InputStream inputStream = urlConnection.getInputStream();
            // Cria o buffer para o input stream
            reader = new BufferedReader(new InputStreamReader(inputStream));
            // Usa o StringBuilder para receber a resposta.
            StringBuilder builder = new StringBuilder();
            String linha;
            while ((linha = reader.readLine()) != null) {
                // Adiciona a linha a string.
                builder.append(linha);
                builder.append("\n");
            }
            if (builder.length() == 0) {
                // se o stream estiver vazio não faz nada
                return null;
            }
            bookJSONString = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // fecha a conexão e o buffer.
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        // escreve o Json no log
        Log.d(LOG_TAG, bookJSONString);
        return bookJSONString;
    }

}
