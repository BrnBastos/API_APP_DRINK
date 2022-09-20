package com.example.showdrink1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    //Constantes dos elementos da layout + conexao da API
    private final String URL = "https://www.thecocktaildb.com/api/json/v1/1/search.php?s=";
    private Retrofit retrofitDrink;
    private EditText NameDrink;
    //private ImageView ImageViewFotoDrink;
    private TextView txtnameDrink, nomeIntrucoes, ingredient1, ingredient2, ingredient3, ingredient4,
    ingredient5;
    private ImageButton btnConsultarDrink;
    private Button logout;

    //localização do usuario
    LocationManager locationMangaer = null;
    LocationListener locationListener = null;

    //sensor
    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;
    int mov = 0;
    Vibrator vibrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        //chamando os componentes
        Componentes();

        //adicionando o sensor
        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(sensor == null)
            finish();
        vibrar = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        sensorEventListener = new SensorEventListener(){
            @Override
            public void onSensorChanged(SensorEvent sensorevent) {
                float x = sensorevent.values[0];
                float y = sensorevent.values[1];
                float z = sensorevent.values[2];
                System.out.println("Valor GiroX" + x);
                if(x<-5 && mov == 0) {
                    vibrar.vibrate(100);
                    Toast.makeText(MainActivity.this, "This app does not support landscape mode", Toast.LENGTH_SHORT).show();
                    mov++;
                } else if(x>-5 && mov == 1) {
                    vibrar.vibrate(100);
                    Toast.makeText(MainActivity.this, "This app does not support landscape mode", Toast.LENGTH_SHORT).show();
                    mov++;

                }

                if(mov == 2) {
                    vibrar.vibrate(100);
                    Toast.makeText(MainActivity.this, "This app does not support landscape mode", Toast.LENGTH_SHORT).show();
                    mov = 0;
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }


        };
        Start();

        ImageButton btnConsultarDrink = (ImageButton) findViewById(R.id.btnConsultarDrink);

        if (getSupportLoaderManager().getLoader(0) != null) {
            getSupportLoaderManager().initLoader(0, null, this);
        }



        this.NameDrink = findViewById(R.id.NameDrink);

        this.NameDrink.setOnKeyListener(new View.OnKeyListener() {
            @Override            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER || keyCode == KeyEvent.KEYCODE_ENTER) {
                        btnConsultarDrink.performClick();
                        //buscaDrinks(NameDrink);
                        return true;
                    }

                }
                return false;
            }
        });
    }

    public void MostrarLoc(View v){
        Toast.makeText(getApplicationContext(), "Hold Up", Toast.LENGTH_LONG).show();
        Boolean flag = displayGpsStatus();
        if (flag) {

            if(ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {

                LocationListener locationListener = new MyLocation();
                locationMangaer.requestLocationUpdates(LocationManager
                        .GPS_PROVIDER, 5000, 10, locationListener);

            } else {
                Toast.makeText(getApplicationContext(), "PERMISSION DENIED ", Toast.LENGTH_LONG).show();
                checkLocationPermission();
            }

        } else {
            Log.i("Gps Status!!", "Your GPS is: OFF");
        }
    }

    //verifica se o gps ta ligado
    public Boolean displayGpsStatus(){
        ContentResolver contentResolver= getBaseContext().getContentResolver();
        boolean gpsStaus= Settings.Secure.isLocationProviderEnabled(contentResolver,LocationManager.GPS_PROVIDER);

        return gpsStaus;
    }

    //recebe as cordenadas
    public  class  MyLocation implements LocationListener{

        @Override
        public void onLocationChanged(@NonNull Location location) {

            String longitude = "Longitude: " +location.getLongitude();
            Log.i("Longitude: ", longitude);
            String latitude = "Latitude: " +location.getLatitude();
            Log.v("Latitude: ", latitude);

            String cidade=null;
            Geocoder geocoder= new Geocoder(getBaseContext(), Locale.getDefault());

            List<Address> addresses;
            try{
                addresses=geocoder.getFromLocation(location.getLatitude(), location.getLongitude(),1);
                if(addresses.size()>0){
                    cidade=addresses.get(0).getLocality();
                    Log.v("city: ", "city: "+cidade);
                    String scity="city: "+cidade;

                    //alert para dizer coodenadas
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Your coordinate: ");
                    alert.setMessage(longitude+"\n "+latitude + "\n"+scity);
                    alert.setPositiveButton("OK",null);
                    alert.show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //checando a permissão da localização
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(this)
                        .setTitle("Necessary Location Permission")
                        .setMessage("This function needs localization to work")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
            } else {
                //soliita permissao
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }
    }
    //MÉTODOS DO GIROSCOPIO
    private void Start() {
        sensorManager.registerListener(sensorEventListener,sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void Stop() { sensorManager.unregisterListener(sensorEventListener); }

    @Override
    protected void onPause() {
        super.onPause();
        Stop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Start();
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
            String stringredient1 = null;
            String stringredient2 = null;
            String stringredient3 = null;
            String stringredient4 = null;
            String stringredient5 = null;

            // Procura pro resultados nos itens do array
            while (i < itemsArray.length() &&
                    (instrucao == null && nome == null && imgDrink == null &&
                            stringredient1 == null && stringredient2 == null && stringredient3 == null &&
                            stringredient4 == null && stringredient5 == null)) {
                // Obtem a informação
                JSONObject drink = itemsArray.getJSONObject(i);
                //  Obter autor e titulo para o item,
                // erro se o campo estiver vazio
                try {
                    nome = drink.getString("strDrink");
                    instrucao = drink.getString("strInstructions");
                    imgDrink = drink.getString("strDrinkThumb");
                    stringredient1 = drink.getString("strIngredient1");
                    stringredient2 = drink.getString("strIngredient2");
                    stringredient3 = drink.getString("strIngredient3");
                    stringredient4 = drink.getString("strIngredient4");
                    stringredient5 = drink.getString("strIngredient5");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // move para a proxima linha
                i++;
            }
            //mostra o resultado qdo possivel.
            if (nome != null && instrucao != null && ingredient1 != null) {

                ingredient1.setText(stringredient1);
                ingredient2.setText(stringredient2);
                ingredient3.setText(stringredient3);
                ingredient4.setText(stringredient4);
                ingredient5.setText(stringredient5);
                txtnameDrink.setText(nome);
                nomeIntrucoes.setText(instrucao);
                //ImageViewFotoDrink.setImageURI(Uri.parse(imgDrink));

                //nmLivro.setText(R.string.str_empty);
            } else {
                // Caso nada for encontrado, os itens ficarão vazios
                txtnameDrink.setText(R.string.no_results);
                nomeIntrucoes.setText(R.string.str_empty);
                ingredient1.setText(R.string.str_empty);
                ingredient2.setText(R.string.str_empty);
                ingredient3.setText(R.string.str_empty);
                ingredient4.setText(R.string.str_empty);
                ingredient5.setText(R.string.str_empty);

            }
        } catch (Exception e) {
            // Se não receber um JSOn valido, informa ao usuário
            txtnameDrink.setText(R.string.no_results);
            nomeIntrucoes.setText(R.string.str_empty);
            ingredient1.setText(R.string.str_empty);
            ingredient2.setText(R.string.str_empty);
            ingredient3.setText(R.string.str_empty);
            ingredient4.setText(R.string.str_empty);
            ingredient5.setText(R.string.str_empty);
            e.printStackTrace();
        }

    }

    private void Componentes(){
        //Definindo os valores para as variáveis dos elementos da layout
        NameDrink = findViewById(R.id.NameDrink);
        txtnameDrink = findViewById(R.id.txtnameDrink);
        ingredient1 = findViewById(R.id.txtingredient1);
        ingredient2 = findViewById(R.id.txtingredient2);
        ingredient3 = findViewById(R.id.txtingredient3);
        ingredient4 = findViewById(R.id.txtingredient4);
        ingredient5 = findViewById(R.id.txtingredient5);
        nomeIntrucoes = findViewById(R.id.nomeInstrucoes);
        //Localização
        locationMangaer = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }


    }

