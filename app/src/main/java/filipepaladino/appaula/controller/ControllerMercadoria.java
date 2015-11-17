package filipepaladino.appaula.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import filipepaladino.appaula.model.ModelMercadoria;

import java.util.ArrayList;
import java.util.Random;


public class ControllerMercadoria extends SQLiteOpenHelper {

    private static final String BANCO    = "AppMercadoria_one.db";
    private static final String TABLE    = "lista_mercadoria";
    private static final Integer VERSAO  = 1;
    private Cursor cursor;

    private String SQL_CREATE = "CREATE TABLE "+TABLE+" (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nome TEXT, " +
            "descricao TEXT, " +
            "fabricante TEXT, " +
            "ativo INTEGER DEFAULT 1, " +
            "foto BLOB, " +
            "preco REAL" +
            ");";

    private String SQL_DROP = "DROP TABLE " + TABLE;



    public ControllerMercadoria(Context context) {
        super(context, BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public void reset(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }

    public void createRegisters(){
        for (int i = 1; i < 11; i++) {

            Random r = new Random();
            double valor = (r.nextInt(80 - 65) + i) * 100;

            ModelMercadoria item = new ModelMercadoria();
            item.setNome("Mercadoria " + i);
            item.setDescricao("Descrição " + i);
            item.setFabricante("Fabricante " + i);
            item.setPreco(valor);

            insert(item);
        }
    }

    public int insert(ModelMercadoria item) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome",      item.getNome());
        values.put("preco",     item.getPreco());
        values.put("descricao", item.getDescricao());
        values.put("fabricante",item.getFabricante());
        values.put("foto",      item.getFoto());

        int cod = (int)db.insert(TABLE, null, values);
        db.close();

        return cod;
    }

    public void update(ModelMercadoria item){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("nome",      item.getNome());
        values.put("preco",     item.getPreco());
        values.put("descricao", item.getDescricao());
        values.put("fabricante", item.getFabricante());

        db.update(TABLE, values, "id = ?", new String[]{String.valueOf(item.getId())});
        db.close();
    }

    public boolean delete(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE, "id = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public boolean deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE, null, null) > 0;
    }

    public ModelMercadoria find(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        cursor = db.query(TABLE,
                new String[]{"id", "nome", "preco", "descricao", "fabricante", "foto"},
                "id = ?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        ModelMercadoria item = new ModelMercadoria();
        item.setId(columnInt("id"));
        item.setNome(columnString("nome"));
        item.setDescricao(columnString("descricao"));
        item.setFabricante(columnString("fabricante"));
        item.setPreco(columnDouble("preco"));
        item.setFoto(columnBlob("foto"));

        return item;
    }


    public ArrayList<ModelMercadoria> search(String search) {
        ArrayList<ModelMercadoria> retorno = new ArrayList<ModelMercadoria>();

        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + TABLE + " WHERE nome like '%"+search+"%' ", null);

        if (cursor.moveToFirst()) {
            do {
                ModelMercadoria item = new ModelMercadoria();
                                item.setId(columnInt("id"));
                                item.setNome(columnString("nome"));
                                item.setDescricao(columnString("descricao"));
                                item.setFabricante(columnString("fabricante"));
                                item.setPreco(columnDouble("preco"));
                                item.setFoto(columnBlob("foto"));

                retorno.add(item);
            } while (cursor.moveToNext());
        }

        return retorno;
    }


    public ArrayList<ModelMercadoria> all() {
        ArrayList<ModelMercadoria> retorno = new ArrayList<ModelMercadoria>();

        SQLiteDatabase db = this.getWritableDatabase();
        cursor = db.rawQuery("SELECT * FROM " + TABLE, null);

        if (cursor.moveToFirst()) {
            do {
                ModelMercadoria item = new ModelMercadoria();
                                item.setId(columnInt("id"));
                                item.setNome(columnString("nome"));
                                item.setDescricao(columnString("descricao"));
                                item.setFabricante(columnString("fabricante"));
                                item.setPreco(columnDouble("preco"));
                item.setFoto(columnBlob("foto"));

                retorno.add(item);
            } while (cursor.moveToNext());
        }

        return retorno;
    }






    private byte[] columnBlob(String column){
        return cursor.getBlob(cursor.getColumnIndex(column));
    }

    private int columnInt(String column){
        return cursor.getInt(cursor.getColumnIndex(column));
    }

    private float columnFloat(String column){
        return cursor.getFloat(cursor.getColumnIndex(column));
    }

    private String columnString(String column){
        return cursor.getString(cursor.getColumnIndex(column));
    }

    private double columnDouble(String column){
        return cursor.getDouble(cursor.getColumnIndex(column));
    }

    private long columnLong(String column){
        return cursor.getLong(cursor.getColumnIndex(column));
    }

}
