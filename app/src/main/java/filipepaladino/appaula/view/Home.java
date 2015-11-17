package filipepaladino.appaula.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import filipepaladino.appaula.R;
import filipepaladino.appaula.adapter.AdapterMercadoria;
import filipepaladino.appaula.controller.ControllerMercadoria;
import filipepaladino.appaula.model.ModelMercadoria;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    private EditText edxSearch;
    private ListView lsvMercadoria;
    private AdapterMercadoria adapter;
    private ArrayList<ModelMercadoria> listamercadoria;
    private ControllerMercadoria controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_mercadoria);

        lsvMercadoria   = (ListView)findViewById(R.id.lsvMercadoria);
        edxSearch       = (EditText)findViewById(R.id.edxSearch);

        controller      = new ControllerMercadoria(this);
        listamercadoria = controller.all();
        adapter         = new AdapterMercadoria(this, listamercadoria);

        lsvMercadoria.setAdapter(adapter);


        edxSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchMercadoria(s.toString());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        searchMercadoria("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home_mercadoria, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int acao = item.getItemId();

        switch (acao){
            case R.id.menu_cadastrar:
                menuCadastrar();
                break;

            case R.id.menu_deletar_todos:
                menuDeletarTodos();
                break;

            case R.id.menu_cria_registro:
                controller.createRegisters();
                break;

            case R.id.menu_reset_table:
                controller.reset();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void menuDeletarTodos() {
        if(controller.deleteAll()){
            Toast.makeText(Home.this, "Todos os itens removidos.", Toast.LENGTH_SHORT).show();
        }
    }

    private void menuCadastrar() {
        Intent cadastro = new Intent(this, Cadastrar.class);
        cadastro.putExtra("action", "create");
        startActivityForResult(cadastro, 100);
    }

    private void searchMercadoria(String nome) {
        listamercadoria.clear();
        listamercadoria.addAll(controller.search(nome));
        adapter.notifyDataSetChanged();
    }


}
