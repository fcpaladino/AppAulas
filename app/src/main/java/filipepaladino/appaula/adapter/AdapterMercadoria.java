package filipepaladino.appaula.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import filipepaladino.appaula.R;
import filipepaladino.appaula.controller.ControllerMercadoria;
import filipepaladino.appaula.model.ModelMercadoria;
import filipepaladino.appaula.view.Cadastrar;

import java.util.ArrayList;

public class AdapterMercadoria extends BaseAdapter {

    private Activity activity;
    private ArrayList<ModelMercadoria> listamercadoria;


    public AdapterMercadoria(Activity activity, ArrayList<ModelMercadoria> listacompra){
        this.activity       = activity;
        this.listamercadoria    = listacompra;
    }

    @Override
    public int getCount() {
        return listamercadoria.size();
    }

    @Override
    public Object getItem(int position) {
        return listamercadoria.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listamercadoria.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        View v                          = convertView;
        final ModelMercadoria item      = listamercadoria.get(position);

        if (v == null) {

            LayoutInflater inflater = activity.getLayoutInflater();
            v = inflater.inflate(R.layout.lista_mercadoria_item, null, true);

            holder = new ViewHolder();
            holder.itemId       = (TextView) v.findViewById(R.id.itemId);
            holder.texto1       = (TextView) v.findViewById(R.id.texto1);
            holder.texto2       = (TextView) v.findViewById(R.id.texto2);
            //holder.imagem       = (ImageView) v.findViewById(R.id.itemFoto);
            holder.btnDeletar   = (Button) v.findViewById(R.id.btnItemDelete);
            holder.listItem     = (RelativeLayout) v.findViewById(R.id.listItem);

            holder.itemId.setText( String.valueOf(item.getId()) );
            holder.texto1.setText( String.valueOf(item.getId()) +" - "+ item.getNome() );
            holder.texto2.setText( String.valueOf(item.getPreco()) );

        } else {
            holder = (ViewHolder) v.getTag();
        }


        holder.listItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent tela = new Intent(activity, Cadastrar.class);
                tela.putExtra("action", "edit");
                tela.putExtra("id", String.valueOf(item.getId()));
                activity.startActivityForResult(tela, 200);
            }
        });
        holder.btnDeletar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                long codigo = item.getId();

                ControllerMercadoria controller     = new ControllerMercadoria(activity);
                boolean result                      = controller.delete(item.getId());

                if(result){
                    listamercadoria.remove(item);
                    notifyDataSetChanged();

                    Toast.makeText(activity, "Item "+codigo+" deletado.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(activity, "Erro ao deletar o item.", Toast.LENGTH_LONG).show();
                }
            }
        });


        v.setTag(holder);

        return v;
    }

    protected static class ViewHolder{
        TextView itemId;
        TextView texto1;
        TextView texto2;
        Button btnDeletar;
        //ImageView imagem;
        RelativeLayout listItem;
    }
}
