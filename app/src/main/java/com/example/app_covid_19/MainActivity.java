package com.example.app_covid_19;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {


    private Button buttonRecuperar;
    private TextView textResultado, textRecuperados, textSuspeitos, textMortos, textConfirmados, textAtualizacao,
            textConfirmadoBrasil, textDescartadosBrasil, textSuspeitosBrasil, textMortosBrasil, textAtualizacaoBrasil;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iniciarComponentes();

        dadosIniciais();

        buttonRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyTask task = new MyTask();
                String urlApi = "https://covid19-brazil-api.now.sh/api/report/v1/brazil/uf/";
                task.execute(urlApi);
            }
        });
    }


    void dadosIniciais(){
        MyTaskOpen myTaskOpen = new MyTaskOpen();
        String urlApiBrasil = "https://covid19-brazil-api.now.sh/api/report/v1/brazil";
        myTaskOpen.execute(urlApiBrasil);
    }
    void iniciarComponentes(){
        buttonRecuperar = findViewById(R.id.buttonRecuperar);

        textResultado= findViewById(R.id.textResultado);
        textRecuperados= findViewById(R.id.textRecuperados);
        textSuspeitos= findViewById(R.id.textSuspeitos);
        textMortos= findViewById(R.id.textMortos);
        textConfirmados= findViewById(R.id.textConfirmados);
        textAtualizacao= findViewById(R.id.textAtualizacao);

        textConfirmadoBrasil= findViewById(R.id.textConfirmadoBrasil);
        textDescartadosBrasil= findViewById(R.id.textDescartadosBrasil);
        textMortosBrasil= findViewById(R.id.textMortosBrasil);
        textSuspeitosBrasil= findViewById(R.id.textSuspeitosBrasil);
        textAtualizacaoBrasil = findViewById(R.id.textAtualizacaoBrasil);

        spinner = findViewById(R.id.spinner);

        String [] estados = getResources().getStringArray(R.array.estados);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, estados);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

//    Criando Thread async
//    Criando uma classe interna
//    AsyncTask
//    1 - URL
//    2 - Progresso
//    3 - Retorno

    class MyTask extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            String estado = spinner.getSelectedItem().toString();

            String stringUrl = strings[0];
            stringUrl = stringUrl + estado;
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;

            try {
                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
//              textRecuperado em Bytes
                inputStream = conexao.getInputStream();

//              Ler os dados em Bytes e decodifica para caracteres
                inputStreamReader = new InputStreamReader(inputStream);

//              Objeto utilizado para leitura de caracteres do InputStreamReader
                BufferedReader reader = new BufferedReader(inputStreamReader);

                buffer = new StringBuffer();

                String linha = "";

                while ((linha = reader.readLine()) != null){
                    buffer.append(linha);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            String pais = null;
            String suspeitos = null;
            String confirmados = null;
            String mortos = null;
            String recuperados = null;
            String atualizacao = null;
            JSONObject jsonObject  = null;

            try {
                jsonObject = new JSONObject(resultado);

                pais = jsonObject.getString("state");
                suspeitos = jsonObject.getString("suspects");
                confirmados = jsonObject.getString("cases");
                mortos = jsonObject.getString("deaths");
                recuperados = jsonObject.getString("refuses");

                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                Date data = formato.parse(jsonObject.getString("datetime"));
                formato.applyPattern("dd/MM/yyyy");
                String dataFormatada = formato.format(data);

                atualizacao = dataFormatada;

            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
            textResultado.setText(pais);
            textResultado.setVisibility(View.VISIBLE);
            textRecuperados.setText("♻ Recuperados: "+recuperados);
            textRecuperados.setVisibility(View.VISIBLE);
            textSuspeitos.setText("⚠ Suspeitos: "+suspeitos);
            textSuspeitos.setVisibility(View.VISIBLE);
            textConfirmados.setText("✅ Confirmados: "+confirmados);
            textConfirmados.setVisibility(View.VISIBLE);
            textMortos.setText("☠ Mortos: "+ mortos);
            textMortos.setVisibility(View.VISIBLE);
            textAtualizacao.setText("\uD83D\uDDD3 Atualizado em "+ atualizacao);
            textAtualizacao.setVisibility(View.VISIBLE);

        }
    }

    class MyTaskOpen extends AsyncTask<String, Void, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;

            try {
                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
//              textRecuperado em Bytes
                inputStream = conexao.getInputStream();

//              Ler os dados em Bytes e decodifica para caracteres
                inputStreamReader = new InputStreamReader(inputStream);

//              Objeto utilizado para leitura de caracteres do InputStreamReader
                BufferedReader reader = new BufferedReader(inputStreamReader);

                buffer = new StringBuffer();

                String linha = "";

                while ((linha = reader.readLine()) != null){
                    buffer.append(linha);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return buffer.toString();

        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            String suspeitos = null;
            String confirmados = null;
            String mortos = null;
            String recuperados = null;
            String atualizacao = null;
            JSONObject jsonObject  = null;

            try {
                jsonObject = new JSONObject(resultado);

                JSONObject data = jsonObject.getJSONObject("data");

                suspeitos = data.getString("cases");
                confirmados = data.getString("confirmed");
                mortos = data.getString("deaths");
                recuperados = data.getString("recovered");
                atualizacao = data.getString("updated_at");

                SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
                Date dataFormatada = formato.parse(atualizacao);
                formato.applyPattern("dd/MM/yyyy");

                atualizacao = formato.format(dataFormatada);;
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
            textDescartadosBrasil.setText(recuperados+"\n Descartados");
            textSuspeitosBrasil.setText(suspeitos+ "\n Suspeitos");
            textConfirmadoBrasil.setText(confirmados+ "\n Confirmados");
            textMortosBrasil.setText(mortos+ "\n Mortos");
            textAtualizacaoBrasil.setText("\uD83D\uDDD3 Atualizado em "+ atualizacao);
        }
    }
}
