package iteda.cnea.StockManager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
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

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Integer.parseInt;

public class CompInfo extends AppCompatActivity implements View.OnClickListener {
    EditText etDescription, etQuantity, etLocation, etDKNumber, etMFNumber, etQty, etModLoc, etModDesc;
    Button AddDB=null, Addcmp=null, Remcmp=null, mod_loc=null, mod_desc=null;
    TableLayout tl_popup2;
    Boolean ADD = TRUE, REMOVE = FALSE, MODDESC = true, MODLOC = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_comp_info );

        etDescription = findViewById( R.id.tv_desc );
        etQuantity = findViewById( R.id.tv_cant );
        etLocation = findViewById( R.id.tv_ubic );
        //etLocation = findViewById( R.id.tv_ubic );
        etDKNumber = findViewById( R.id.tv_DKN );
        etMFNumber = findViewById( R.id.tv_MFN );
        etQty = findViewById( R.id.et_qty );
        etModDesc = findViewById( R.id.et_desc);
        etModLoc = findViewById( R.id.et_loc );
        AddDB = findViewById( R.id.btn_addDB );
        Addcmp = findViewById( R.id.btn_add );
        Remcmp = findViewById( R.id.btn_remove );
        tl_popup2 = findViewById( R.id.tl_popup2 );
        mod_desc = findViewById( R.id.btn_mod_desc );
        mod_loc = findViewById( R.id.btn_mod_loc );

        etDescription.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }else {
                    InputMethodManager imm = (InputMethodManager) CompInfo.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( etDescription, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });
        etQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }else {
                    InputMethodManager imm = (InputMethodManager) CompInfo.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( etQuantity, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });
        etLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }else {
                    InputMethodManager imm = (InputMethodManager) CompInfo.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( etLocation, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });
        etDKNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }else {
                    InputMethodManager imm = (InputMethodManager) CompInfo.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( etDKNumber, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });
        etMFNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }else {
                    InputMethodManager imm = (InputMethodManager) CompInfo.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( etMFNumber, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });
        etQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }else {
                    InputMethodManager imm = (InputMethodManager) CompInfo.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( etQty, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });

        etModDesc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }else {
                    InputMethodManager imm = (InputMethodManager) CompInfo.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( etModDesc, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });

        etModLoc.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }else {
                    InputMethodManager imm = (InputMethodManager) CompInfo.this.getSystemService( Context.INPUT_METHOD_SERVICE );
                    assert imm != null;
                    imm.showSoftInput( etModLoc, InputMethodManager.SHOW_IMPLICIT );
                }
            }
        });

        Intent intent = getIntent();
        String flag_manual = intent.getStringExtra( "flag" );   //manualmente
        String flag_stock1 = intent.getStringExtra( "flag1" );   //stock
        String flag_stock2 = intent.getStringExtra( "flag2" );   //barcode local

        if(flag_stock1 != null){    //INGESO: por "stock"
            Intent intent1 = getIntent();
            global.stock_id = intent1.getStringExtra( Stock.EXTRA_STOCK_ID );
            etDescription.setText( intent1.getStringExtra( Stock.EXTRA_DESC ) );
            etMFNumber.setText( intent1.getStringExtra( Stock.EXTRA_MNUMBER ) );
            etQuantity.setText( intent1.getStringExtra( Stock.EXTRA_QTY ) );
            etDKNumber.setText( intent1.getStringExtra( Stock.EXTRA_DKN ) );
            etLocation.setText( intent1.getStringExtra( Stock.EXTRA_LOC ) );
            etQty.setVisibility( View.VISIBLE );//habilito textview de modificar cantidad
            etModDesc.setVisibility( View.VISIBLE );
            etModLoc.setVisibility( View.VISIBLE );
            tl_popup2.setVisibility( View.VISIBLE );//habilito botones de modificar cantidad
            mod_desc.setVisibility( View.VISIBLE );
            mod_loc.setVisibility( View.VISIBLE );
        } else if (flag_manual != null) {       //INGRESO: manualmente sin codigo
            AddDB.setVisibility( View.VISIBLE );
        } else if (flag_stock2 != null){        //ingreso: barcode local
            Intent intent2 = getIntent();
            global.stock_id = intent2.getStringExtra( "id" );
            etDescription.setText( intent2.getStringExtra( "description") );
            etMFNumber.setText( intent2.getStringExtra( "m_number"));
            etQuantity.setText( intent2.getStringExtra( "quantity"));
            etDKNumber.setText( intent2.getStringExtra( "dk_number") );
            etLocation.setText( intent2.getStringExtra( "location"));
            etQty.setVisibility( View.VISIBLE );//habilito textview de modificar cantidad
            etModDesc.setVisibility( View.VISIBLE );
            etModLoc.setVisibility( View.VISIBLE );
            tl_popup2.setVisibility( View.VISIBLE );//habilito botones de modificar cantidad
            mod_desc.setVisibility( View.VISIBLE );
            mod_loc.setVisibility( View.VISIBLE );
        } else {                       //INGESO: por codigo QR/barcode (manualmente o por camara)
            JSONObject json;
            String extra = intent.getStringExtra( "json" );
            try {
                json = new JSONObject( extra );
                etDescription.setText( json.getString( "Description" ) );
                etQuantity.setText( json.getString( "Quantity" ) );
                etMFNumber.setText( json.getString( "ManufacturerPartNumber" ) );

                Response.Listener<String> respoListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject( response );
                            boolean success = jsonResponse.getBoolean( "success" );
                            if (!success) {   //si NO esta en la base de datos
                                AddDB.setVisibility( View.VISIBLE );    //habilito boton de DB
                                etLocation.setFocusable( true );
                            } else {   //si esta en la base de datos
                                etQty.setVisibility( View.VISIBLE );//habilito textview de modificar cantidad
                                etModDesc.setVisibility( View.VISIBLE );
                                etModLoc.setVisibility( View.VISIBLE );
                                tl_popup2.setVisibility( View.VISIBLE );//habilito botones de modificar cantidad
                                mod_desc.setVisibility( View.VISIBLE );
                                mod_loc.setVisibility( View.VISIBLE );
                                int quantity = jsonResponse.getInt( "quantity" );   //acceso a la cant y ubicacion desde la base de datos
                                String location = jsonResponse.getString( "location" );
                                String description = jsonResponse.getString( "description" );
                                global.stock_id = jsonResponse.getString( "stock_id" );
                                //Toast.makeText(CompInfo.this, "stock_id:"+global.stock_id, Toast.LENGTH_LONG).show(); //para debugear
                                Toast.makeText(CompInfo.this, "Actualmente hay "+ quantity +" unidades. ¿Querés modificar la cantidad de la base de datos?", Toast.LENGTH_LONG).show();
                                etQuantity.setText( String.valueOf( quantity ));  //muestra la cantidad etQuantity que figura en la DB
                                etDescription.setText( String.valueOf( description ));  //muestra la cantidad etQuantity que figura en la DB
                                etLocation.setText( location );  //muestra la ubicacion que figura en la DB
                                etLocation.setFocusable( false );
                            }
                        } catch (JSONException e) {
                            Toast.makeText(CompInfo.this, "ERROR 1: comuniquese con el administrador", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                };
                FindStringRequest FSRequest = new FindStringRequest( json.getString( "ManufacturerPartNumber" ), respoListener );
                RequestQueue queue = Volley.newRequestQueue( CompInfo.this );
                queue.add( FSRequest );
                FSRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //solo una vez hace el request
                etDKNumber.setText( json.getString( "DigiKeyPartNumber" ) );
            } catch (JSONException e) {
                final Response.Listener<String> respoListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                };
                //casi termino FALTA INGRESAR EL REFRESH_TOKEN que esta en la tabla DKToken de la DB
//

                final AccessToComp.DKvars dkvars = new AccessToComp.DKvars();
                final com.android.volley.Response.Listener<String> respListener = new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject( response );
                            dkvars.state = jsonResponse.getBoolean( "success" );

                            if (dkvars.state) {
                                dkvars.url = jsonResponse.getString( "url" );
                                dkvars.client_id = jsonResponse.getString( "client_id" );
                                dkvars.access_token = jsonResponse.getString( "access_token" );
                                dkvars.refresh_token = jsonResponse.getString( "refresh_token" );
                                dkvars.client_secret = jsonResponse.getString( "client_secret" );

                                APIRequest apiRequest = new APIRequest(dkvars.refresh_token, respoListener);
                                RequestQueue queue = Volley.newRequestQueue(CompInfo.this);
                                queue.add(apiRequest);
                                apiRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //solo una vez hace el request

                                Toast.makeText(CompInfo.this, "ERROR 2: Por Favor Intentá Nuevamente", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText( CompInfo.this, "error en request: dkvars.state = false", Toast.LENGTH_SHORT ).show();
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                };
                    getRequest getrequest = new getRequest( "Bearer", respListener );
                    RequestQueue queue1 = Volley.newRequestQueue( CompInfo.this );
                    queue1.add( getrequest );
                    //getRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            }
        }

        if( (flag_stock1 != null && flag_manual == null ) || (flag_stock1 == null && flag_manual == null)){  //inhabilito edittexts; flag_manual = manualmente y flag_stock = stock
            etDescription.setFocusable( false );
            etQuantity.setFocusable( false );
            //etLocation.setFocusable( false );
            etMFNumber.setFocusable( false );
            etDKNumber.setFocusable( false );
        }
        AddDB.setOnClickListener( new View.OnClickListener() {  //carga en base de datos
            @Override
            public void onClick(View v) {   //si se clickea el boton "agregar a base de datos"
                final String description = etDescription.getText().toString();
                final String quantity = etQuantity.getText().toString();
                final String Location = etLocation.getText().toString();
                final String DKNumber = etDKNumber.getText().toString();
                final String MFNumber = etMFNumber.getText().toString();

                //chequeo "campos completos"
                if (description.equals( "" )) {
                    Toast.makeText(CompInfo.this, "Ingrese una descripción del elemento", Toast.LENGTH_SHORT).show();
                } else if (quantity.equals( "" )) {
                    Toast.makeText(CompInfo.this, "Ingrese la cantidad del elemento", Toast.LENGTH_SHORT).show();
                } else if (Location.equals( "" )) {
                    Toast.makeText(CompInfo.this, "Ingrese la ubicación del elemento", Toast.LENGTH_SHORT).show();
                }
                else {
                    com.android.volley.Response.Listener<String> respoListener = new com.android.volley.Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject( response );
                                boolean success = jsonResponse.getBoolean( "success" );

                                if (success) {
                                    Toast.makeText(CompInfo.this, "El componente se agregó exitosamente", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent( CompInfo.this, AccessToComp.class );
                                    CompInfo.this.startActivity( intent );

                                } else {
                                    Toast.makeText(CompInfo.this, "Error en el registro", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(CompInfo.this, "ERROR 3: comuniquese con el administrador", Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    };
                    SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
                    String user = sharedpreferences.getString(MainActivity.User, "0");

                    LoadCompRequest LCRequest = new LoadCompRequest( description, quantity, Location, DKNumber, MFNumber, user, respoListener );
                    RequestQueue queue = Volley.newRequestQueue( CompInfo.this );
                    queue.add( LCRequest );
                    LCRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //solo una vez hace el request
                }

                    //reload activity (F5)
                    finish();
                    startActivity(getIntent());

            }
        } );

//begin logica modificar valores
        mod_desc.setOnClickListener( new View.OnClickListener() { //Agregar cantidad
            @Override
            public void onClick(View v) {    //si se clickea el boton modificar descripcion
                final String newDesc = etModDesc.getText().toString();
                if (newDesc.equals( "" )) {
                    Toast.makeText(CompInfo.this, "Ingrese un nueva descripción.", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder( CompInfo.this );
                    builder.setMessage( "¿Realmente desea modificar la descripción?" )
                            .setPositiveButton( "Si", new Dialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    modificarstring( MODDESC );   //MODDESC / true : modificar componente
                                }
                            } )
                            .setNegativeButton( "No", null )
                            .create().show();

                }
            }
        } );

        Addcmp.setOnClickListener( new View.OnClickListener() { //Agregar cantidad
            @Override
            public void onClick(View v) {    //si se clickea el boton agregar cantidad
                final String qty = etQty.getText().toString();
                if (qty.equals( "" )) {
                    Toast.makeText(CompInfo.this, "Ingrese la cantidad a modificar.", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder( CompInfo.this );
                    builder.setMessage( "¿Desea agregar " + qty + " unidades?" )
                            .setPositiveButton( "Si", new Dialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    modificarQty( ADD );
                                }
                            } )
                            .setNegativeButton( "No", null )
                            .create().show();

                }
            }
        } );

        Remcmp.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {   //si se clickea el boton borrar cantidad
                final String qty = etQty.getText().toString();
                if ("".equals( qty )) {
                    Toast.makeText(CompInfo.this, "Ingrese la cantidad a eliminar.", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder( CompInfo.this );
                    builder.setMessage( "¿Desea retirar " + qty + " unidades?" )
                            .setPositiveButton( "Si", new Dialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    modificarQty( REMOVE );
                                }
                            } )
                            .setNegativeButton( "No", null )
                            .create().show();
                }
            }
        } );

        mod_loc.setOnClickListener( new View.OnClickListener() { //Agregar cantidad
            @Override
            public void onClick(View v) {    //si se clickea el boton modificar ubicacion
                final String newLoc= etModLoc.getText().toString();
                if (newLoc.equals( "" )) {
                    Toast.makeText(CompInfo.this, "Ingrese la cantidad a modificar.", Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder( CompInfo.this );
                    builder.setMessage( "¿Realmente desea modificar la ubicación?" )
                            .setPositiveButton( "Si", new Dialog.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    modificarstring( MODLOC );   //MODLOC / false: modificar ubicacion
                                }
                            } )
                            .setNegativeButton( "No", null )
                            .create().show();

                }
            }
        } );
    }

    private void modificarstring(final Boolean option)
    {
        final String desc_prev = etDescription.getText().toString();
        final String desc_act = etModDesc.getText().toString();    //cantidad a agregar
        final String loc_prev = etLocation.getText().toString();
        final String loc_act = etModLoc.getText().toString();    //cantidad a agregar
        final String qty = etQty.getText().toString();

        if (option == MODDESC) { //moddesc = true
            Response.Listener<String> respoListener1 = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        new JSONObject( response );
                    } catch (JSONException e) {
                        Toast.makeText(CompInfo.this, "ERROR 5: comuniquese con el administrador", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            };
            SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            String user = sharedpreferences.getString(MainActivity.User, "0");
            UpdateDescRequest MDRequest = new UpdateDescRequest( user, desc_prev, desc_act, qty, loc_prev, global.stock_id, respoListener1 );
            RequestQueue queue1 = Volley.newRequestQueue( CompInfo.this );
            queue1.add( MDRequest );
            MDRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //solo una vez hace el request
        } else {
            Response.Listener<String> respoListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        new JSONObject( response );
                    } catch (JSONException e) {
                        Toast.makeText(CompInfo.this, "ERROR 6: comuniquese con el administrador", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            };
            SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            String user = sharedpreferences.getString(MainActivity.User, "0");
            UpdateLocRequest MLRequest  = new UpdateLocRequest( user, desc_prev, qty, loc_prev, loc_act, global.stock_id, respoListener );
            RequestQueue queue1 = Volley.newRequestQueue( CompInfo.this );
            queue1.add( MLRequest );
            MLRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //solo una vez hace el request
        }

//reload activity (F5)
            finish();
            startActivity(getIntent());
            Intent intent = getIntent();
            String flag_stock = intent.getStringExtra( "flag1" );    //stock
            if(flag_stock != null){
                Intent intent2 = new Intent(CompInfo.this, Stock.class);
                CompInfo.this.startActivity(intent2);
                global.flag2 = true;
            }
    }

    private void modificarQty(final Boolean option)
    {
        final int valor = parseInt( etQuantity.getText().toString() );
        final int cambioint = parseInt( etQty.getText().toString() );    //cantidad a agregar
        int qty;
        int aux = 0;
        Boolean flg = false;
        String action;

        if (option == ADD) {
            qty = valor + cambioint ;
            action = "add";
        } else {
            qty = valor - cambioint ;
            if(qty < 0)
            {
                qty = qty + cambioint;
                flg = true;
            }
            action = "remove";
        }

        Response.Listener<String> respoListener1 = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    new JSONObject( response );
                } catch (JSONException e) {
                    Toast.makeText(CompInfo.this, "ERROR 4: comuniquese con el administrador", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        };
        final String new_quantity = String.valueOf( qty );
       //final String cambio = String.valueOf( cambioint );

        if(cambioint != aux && !flg) {
            String location= etLocation.getText().toString();
            SharedPreferences sharedpreferences = getSharedPreferences(MainActivity.MyPREFERENCES, Context.MODE_PRIVATE);
            String user = sharedpreferences.getString(MainActivity.User, "0");
            String description = etDescription.getText().toString();
            String vlr = String.valueOf( valor );
            UpdateQuantityRequest UCRequest = new UpdateQuantityRequest( new_quantity, global.stock_id, user, action, description, vlr, location , respoListener1 );
            RequestQueue queue1 = Volley.newRequestQueue( CompInfo.this );
            queue1.add( UCRequest );
            UCRequest.setRetryPolicy(new DefaultRetryPolicy( 0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));   //solo una vez hace el request
//reload activity (F5)
            finish();
            startActivity(getIntent());
            Intent intent = getIntent();
            String flag_stock = intent.getStringExtra( "flag1" );    //stock
            if(flag_stock != null){
                Intent intent2 = new Intent(CompInfo.this, Stock.class);
                CompInfo.this.startActivity(intent2);
                global.flag2 = true;
            }
            //  Toast.makeText(CompInfo.this, "La cantidad se modificó exitosamente", Toast.LENGTH_SHORT).show();
        } else if(flg){
            Toast.makeText(CompInfo.this, "¡No puede retirar más componentes de los que hay!", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(CompInfo.this, "¿0 unidades?", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
    }
//end logica modificar cantidad

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService( Activity.INPUT_METHOD_SERVICE);
        assert inputMethodManager != null;
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}

class FindStringRequest extends StringRequest {
    final private static AccessToComp.IP ip = new AccessToComp.IP();
    private static final String REGISTER_REQUEST_URL="http://"+ip.ifconfig+"/ComponentFinder.php";
    private Map<String, String> params;

    FindStringRequest(String etMFNumber, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("etMFNumber", etMFNumber);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

class LoadCompRequest extends StringRequest {
    final private static AccessToComp.IP ip = new AccessToComp.IP();
    private static final String REGISTER_REQUEST_URL="http://"+ip.ifconfig+"/LoadComp.php";
    private Map<String, String> params;
    LoadCompRequest(String description, String  quantity, String Location, String DKNumber, String MFNumber, String User, com.android.volley.Response.Listener<String> listener) {    //public CompInfoRequest(String description, String quantity, com.android.volley.Response.Listener<String> listener) {

        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("description", description);
        params.put("quantity", quantity);
        params.put("Location", Location);
        params.put("dk_number", DKNumber);
        params.put("MFNumber", MFNumber);
        params.put("user", User);
    }

    @Override
    public Map<String, String> getParams() {    return params;  }
}

class UpdateQuantityRequest extends StringRequest {
    final private static AccessToComp.IP ip = new AccessToComp.IP();
    private static final String REGISTER_REQUEST_URL="http://"+ip.ifconfig+"/UpdateQuantity.php";
    private Map<String, String> params;
    UpdateQuantityRequest(String new_quantity, String stock_id, String user, String action, String description, String qty_prev, String location, Response.Listener<String> listener) {

        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("new_qty", new_quantity);
        params.put("stock_id", stock_id);

        params.put("user", user);
        params.put("action", action);
        params.put("description", description);
        params.put("qty_prev", qty_prev);
        params.put("location", location);
    }
    @Override
    public Map<String, String> getParams() { return params; }
}

class UpdateDescRequest extends StringRequest {
    final private static AccessToComp.IP ip = new AccessToComp.IP();
    private static final String REGISTER_REQUEST_URL="http://"+ip.ifconfig+"/UpdateDesc.php";
    private Map<String, String> params;
    UpdateDescRequest(String user, String desc_prev, String desc_act, String qty, String location, String stock_id, com.android.volley.Response.Listener<String> listener) {    //public CompInfoRequest(String description, String quantity, com.android.volley.Response.Listener<String> listener) {

        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user", user);
        params.put("desc_prev", desc_prev);
        params.put("desc_act", desc_act);
        params.put("qty", qty);
        params.put("location", location);
        params.put("stock_id", stock_id);
    }

    @Override
    public Map<String, String> getParams() {    return params;  }
}

class UpdateLocRequest extends StringRequest {
    final private static AccessToComp.IP ip = new AccessToComp.IP();
    private static final String REGISTER_REQUEST_URL="http://"+ip.ifconfig+"/UpdateLoc.php";
    private Map<String, String> params;

    UpdateLocRequest(String user, String description, String qty, String location_prev, String location_act, String stock_id, com.android.volley.Response.Listener<String> listener) {    //public CompInfoRequest(String description, String quantity, com.android.volley.Response.Listener<String> listener) {

        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("user", user);
        params.put("description", description);
        params.put("qty", qty);
        params.put("location_prev", location_prev);
        params.put("location_act", location_act);
        params.put("stock_id", stock_id);
    }
    @Override
    public Map<String, String> getParams() {    return params;  }
}

class getRequest extends StringRequest {
    final private static AccessToComp.IP ip = new AccessToComp.IP();
    private static final String REGISTER_REQUEST_URL = "http://"+ip.ifconfig+"/TokenFinder.php";
    private Map<String, String> params;

    getRequest(String token_type, com.android.volley.Response.Listener<String> listener) {
        super( Method.POST, REGISTER_REQUEST_URL, listener, null );
        params = new HashMap<>();
        params.put( "token_type", token_type );
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

class APIRequest extends StringRequest {
    final private static AccessToComp.IP ip = new AccessToComp.IP();
    private static final String REGISTER_REQUEST_URL="http://"+ip.ifconfig+"/DK_API.php";
    private Map<String, String> params;
    APIRequest(String refresh_token, Response.Listener<String> listener) {
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("refresh_token", refresh_token);
    }
    @Override
    public Map<String, String> getParams() {
        return params;
    }
}