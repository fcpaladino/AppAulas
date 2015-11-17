package filipepaladino.appaula.view;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import filipepaladino.appaula.R;
import filipepaladino.appaula.controller.ControllerMercadoria;
import filipepaladino.appaula.model.ModelMercadoria;

public class Cadastrar extends AppCompatActivity {

    private TextView edxId;
    private TextView edxNome;
    private TextView edxPreco;
    private TextView edxDescricao;
    private TextView edxFabricante;
    private ImageView imvFoto;
    private byte[] foto = null;

    private Button atualizar;
    private Button cadastrar;

    private ControllerMercadoria controller;
    private ModelMercadoria model;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastrar_mercadoria);

        edxId           = (TextView)findViewById(R.id.edxId);
        edxNome         = (TextView)findViewById(R.id.edxNome);
        edxPreco        = (TextView)findViewById(R.id.edxPreco);
        edxDescricao    = (TextView)findViewById(R.id.edxDescricao);
        edxFabricante   = (TextView)findViewById(R.id.edxFabricante);
        imvFoto         = (ImageView)findViewById(R.id.imgview);

        controller  = new ControllerMercadoria(this);

        Intent intent       = getIntent();
        String action       = intent.getStringExtra("action");

        switch (action){
            case "create":

                edxId.setText("");
                edxNome.setText("");
                edxPreco.setText("");
                edxDescricao.setText("");
                edxFabricante.setText("");

                enableButtonStore();
                break;

            case "edit":
                Integer id = Integer.valueOf( intent.getStringExtra("id") );
                ModelMercadoria item = controller.find(id);

                edxId.setText( String.valueOf(item.getId()) );
                edxNome.setText( String.valueOf(item.getNome()) );
                edxPreco.setText( String.valueOf(item.getPreco()) );
                edxDescricao.setText( String.valueOf(item.getDescricao()) );
                edxFabricante.setText( String.valueOf(item.getFabricante()) );
                imvFoto.setImageBitmap(item.getFotoAsBitmap());

                enableButtonUpdate();
                break;
        }



        imvFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
    }


    // Cria um novo registro
    public void actionStore(View view) {
        model   = new ModelMercadoria();
        model.setNome(edxNome.getText().toString());
        model.setDescricao(edxDescricao.getText().toString());
        model.setFabricante(edxFabricante.getText().toString());
        model.setPreco(Double.parseDouble(edxPreco.getText().toString()));
        model.setFoto(recuperaFotoAsByteArray());

        int id = (int)controller.insert(model);

        controller.close();

//        Intent home = new Intent();
//        home.putExtra("id", id);
//        setResult(100, home);
        finish();

    }

    // Atualiza o registro
    public void actionUpdate(View view){

        model   = new ModelMercadoria();
        model.setId(Integer.parseInt(edxId.getText().toString()));
        model.setNome(edxNome.getText().toString());
        model.setDescricao(edxDescricao.getText().toString());
        model.setFabricante(edxFabricante.getText().toString());
        model.setPreco(Double.parseDouble(edxPreco.getText().toString()));

        model.setFoto(recuperaFotoAsByteArray());

        controller.update(model);
        controller.close();

//        Intent home = new Intent();
//        setResult(200, home);
        finish();
    }

    // Volta pra tela inicial
    public void actionCancel(View view) {
        finish();
    }

    // habilita o botão cadastrar, desabilita o botão atualizar
    private void enableButtonStore() {
        atualizar = (Button)findViewById(R.id.button_atualizar);
        atualizar.setVisibility(View.INVISIBLE);

        cadastrar = (Button)findViewById(R.id.button_cadastrar);
        cadastrar.setVisibility(View.VISIBLE);
    }

    // habilita o botão atualizar, desabilita o botão cadastrar
    private void enableButtonUpdate(){
        atualizar = (Button)findViewById(R.id.button_atualizar);
        atualizar.setVisibility(View.VISIBLE);

        cadastrar = (Button)findViewById(R.id.button_cadastrar);
        cadastrar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode== RESULT_OK && data != null){
            Bundle extras = data.getExtras();
            Bitmap bitMap = (Bitmap) extras.get("data");

            imvFoto.setImageBitmap(bitMap);
        }
    }


    private byte[] recuperaFotoAsByteArray() {
        try {
            BitmapDrawable bitmapDrawable = ((BitmapDrawable) imvFoto.getDrawable());
            Bitmap bitmap = bitmapDrawable.getBitmap();

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

            return stream.toByteArray();
        } catch (Exception e) {
            return null;
        }

    }



}
