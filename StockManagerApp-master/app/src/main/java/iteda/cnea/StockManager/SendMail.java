package iteda.cnea.StockManager;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class SendMail extends AppCompatActivity implements View.OnClickListener {

    String Email = null;
    String Subject = null;
    String Body = null;
    Context Context = null;
    private RequestQueue mRequestQueue;

    //Class Constructor
    SendMail(View.OnClickListener context, String email, String subject, String body) {
        Email = email;
        Subject = subject;
        Body = body;
        Context = (android.content.Context) context;
}

    @Override
    public void onClick(View v) {
    }

    public void execute() {
        Response.Listener<String> respoListener = new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            try {
                JSONObject jsonResponse = new JSONObject( response );

                boolean success = jsonResponse.getBoolean( "success" );

                if (success) {
                    Toast.makeText( SendMail.this, "Success", Toast.LENGTH_LONG ).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder( SendMail.this );
                    builder.setMessage( "Error" )
                            .setNegativeButton( "Retry", null )
                            .create().show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
        EmailRequest emailRequest = new EmailRequest( Email, Subject, Body, respoListener );
        mRequestQueue = Volley.newRequestQueue( Context );
        mRequestQueue.add( emailRequest );
        emailRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //obliga a que envie un solo email
    }
}
class EmailRequest extends StringRequest {
    final private static AccessToComp.IP ip = new AccessToComp.IP();
    private static final String REGISTER_REQUEST_URL="http://"+ip.ifconfig+"/index.php";
    private Map<String, String> params;
    EmailRequest(String email, String subject, String body, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("email", email);
        params.put("subject", subject);
        params.put("body", body);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

