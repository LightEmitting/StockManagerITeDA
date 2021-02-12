package iteda.cnea.StockManager;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import android.content.Context;
import java.util.HashMap;
import java.util.Map;

public class Suggestions extends AppCompatActivity implements View.OnClickListener{

    EditText et_sug;
    Button btn_iniciar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_suggestions);

        et_sug = findViewById(R.id.ET_sug);
        btn_iniciar = findViewById(R.id.Btn_iniciar);
        btn_iniciar.setOnClickListener(this);

        et_sug.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        final String suggestion = et_sug.getText().toString();
        if (suggestion.equals("") ) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Suggestions.this);
            builder.setMessage("Complete el campo")
                    .setNegativeButton("Ok", null)
                    .create().show();
        }else {

            Response.Listener<String> respoListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");

                        if (success) {
                            Intent intent = new Intent(Suggestions.this, AccessToComp.class);
                            Suggestions.this.startActivity(intent);
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Suggestions.this);
                            builder.setMessage("Error")
                                    .setNegativeButton("Retry", null)
                                    .create().show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
//leer user
            SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            String user = sharedpreferences.getString(MainActivity.User, "0");
//leer user
            SuggestionsRequest suggestionsRequest = new SuggestionsRequest(suggestion, user, respoListener);
            RequestQueue queue = Volley.newRequestQueue(Suggestions.this);
            queue.add(suggestionsRequest);
            suggestionsRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //solo una vez hace el request

        }
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService( Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}


class SuggestionsRequest extends StringRequest {
    final private static AccessToComp.IP ip = new AccessToComp.IP();
    private static final String REGISTER_REQUEST_URL="http://"+ip.ifconfig+"/Suggestion.php";
    private Map<String, String> params;
    SuggestionsRequest(String suggestion, String user, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("suggestion", suggestion);
        params.put("user", user);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}