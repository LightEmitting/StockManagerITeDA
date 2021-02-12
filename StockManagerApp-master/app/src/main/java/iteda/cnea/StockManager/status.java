package iteda.cnea.StockManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class status extends AppCompatActivity {

    EditText tv_cmp, tv_hrr, tv_csm, tv_ins, tv_err;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_status );

        tv_cmp = findViewById(R.id.tv_cmp);
        tv_hrr = findViewById(R.id.tv_hrr);
        tv_csm = findViewById(R.id.tv_csm);
        tv_ins = findViewById(R.id.tv_ins);
        tv_err = findViewById(R.id.tv_err);

        Response.Listener<String> respoListener = new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject( response );
                    boolean success = jsonResponse.getBoolean( "success" );
                    if (!success) {   //if false , error
                        Toast.makeText(status.this, "ERROR 10: comuniquese con el administrador", Toast.LENGTH_SHORT).show();
                    } else {   //si esta en la base de datos
                        tv_cmp.setText( "    " + jsonResponse.getString( "cmp")  + "%" );
                        tv_hrr.setText( "    " + jsonResponse.getString( "hrr" ) + "%");
                        tv_csm.setText( "    " + jsonResponse.getString( "csm" ) + "%");
                        tv_ins.setText( "    " + jsonResponse.getString( "ins" ) + "%");
                        tv_err.setText( "    " + jsonResponse.getString( "err" ) + "%");

                        tv_cmp.setFocusable( false );
                        tv_hrr.setFocusable( false );
                        tv_csm.setFocusable( false );
                        tv_ins.setFocusable( false );
                        tv_err.setFocusable( false );
                    }
                } catch (JSONException e) {
                    Toast.makeText(status.this, "ERROR 11: comuniquese con el administrador", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

        };

        StatusRequest SRequest = new StatusRequest( respoListener );
        RequestQueue queue = Volley.newRequestQueue( status.this );
        queue.add( SRequest );
        SRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }


}

class StatusRequest extends StringRequest {
    final private static AccessToComp.IP ip = new AccessToComp.IP();
    private static final String REGISTER_REQUEST_URL="http://"+ip.ifconfig+"/GetAvailability.php";
    private Map<String, String> params;

    StatusRequest( Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}