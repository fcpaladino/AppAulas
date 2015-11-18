package filipepaladino.appaula.view.view;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import filipepaladino.appaula.R;

public class Base extends AppCompatActivity {

    private ImageView imvMapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base);

        imvMapa = (ImageView)findViewById(R.id.imvMapa);

        String imageUrl = "http://maps.googleapis.com/maps/api/staticmap?center=Rua+sergipe,+londrina&zoom=14&scale=1&size=600x300&maptype=roadmap&key=AIzaSyD7O7QDbeCBDCQwlrldLM5t2VQeqHH1Kjs&format=jpg&visual_refresh=true&markers=size:mid%7Ccolor:0x0000ff%7Clabel:A%7CRua+sergipe,+londrina";




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_base, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void executarPesquisa(View view) {
        final ProgressDialog progresso = new ProgressDialog(this);
        progresso.setTitle("Por favor espere!");
        progresso.setCancelable(false);

        progresso.show();

        String endereco = ((EditText)findViewById(R.id.edxPesquisa)).getText().toString();

        String query = null;
        try {
            query = URLEncoder.encode(endereco, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + query;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                "",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progresso.hide();

                        try {
                            StringBuilder builder = new StringBuilder();

                            JSONArray result = response.getJSONArray("results");
                            for (int i = 0; i < result.length(); i++)
                            {
                                JSONObject endereco = result.getJSONObject(i);
                                String formatedAddress =
                                        endereco.getString("formatted_address");
                                builder.append(formatedAddress + "\n");
                            }

                            ((TextView)findViewById(R.id.txvResultado)).setText(builder);
                        } catch (Exception ex) {
                            Toast.makeText(
                                    Base.this,
                                    ex.toString(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progresso.hide();

                        Toast.makeText(
                                Base.this,
                                error.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue fila = Volley.newRequestQueue(this);
        fila.add(request);
    }
}
