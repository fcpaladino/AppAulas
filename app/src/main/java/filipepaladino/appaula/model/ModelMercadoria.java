package filipepaladino.appaula.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.sql.Blob;

public class ModelMercadoria {

    private long id;
    private String nome;
    private Double preco;
    private Integer ativo;
    private String descricao;
    private String fabricante;
    private byte[] foto;
    private Bitmap fotoAsBitmap;


    public ModelMercadoria() {}


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getAtivo() {
        return ativo;
    }

    public void setAtivo(Integer ativo) {
        this.ativo = ativo;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }


    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }


    public Bitmap getFotoAsBitmap() {
        if (foto == null || foto.length == 0) {
            return null;
        }
        return BitmapFactory.decodeByteArray(foto, 0, foto.length);
    }

}
