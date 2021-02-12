package iteda.cnea.StockManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.annotations.SerializedName;
import java.io.IOException;
import java.util.List;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okio.Buffer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class Stock extends AppCompatActivity implements SearchView.OnQueryTextListener, FoodAdapter.OnItemClickListener {
    //declaración de variables
    public static final String EXTRA_STOCK_ID = "stock_id";
    public static final String EXTRA_DESC = "description";
    public static final String EXTRA_MNUMBER = "mnumber";
    public static final String EXTRA_QTY = "qty";
    public static final String EXTRA_DKN = "dkn";
    public static final String EXTRA_LOC = "loc";
    private RecyclerView recyclerViewComponents;
    private ProgressDialog progressDialog;// = new ProgressDialog(this);
    ArrayList<FoodModel> listFood = new ArrayList<>();

    ListView myList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

        recyclerViewComponents = findViewById(R.id.recyclerview_food);
        progressDialog = new ProgressDialog(this);
        //      ab_send_file = (FloatingActionButton) findViewById( R.id.send_file );
        //      ab_filter = (FloatingActionButton) findViewById( R.id.filter );
        myList = findViewById(R.id.list);

        if(getActionBar() != null) {
            getActionBar().setDisplayHomeAsUpEnabled( true );//enables the up buttom in action bar (minSdkVersion is 11 or higher)
        }
        getAllData();
        invalidateOptionsMenu(); //reset onPrepareOptionsMenu
    }

    private void getAllData() {
        progressDialog.setTitle("Displaying data: Loading");
        progressDialog.show();
        invalidateOptionsMenu();

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<ListFoodModel> call = apiService.getAllData();
        call.enqueue(new Callback<ListFoodModel>() {
            @Override
            public void onResponse(Call<ListFoodModel> call, Response<ListFoodModel> response) {
                ListFoodModel listFoodModel = response.body();
                if (listFoodModel.getStatus() == 1) {
                    listFood = listFoodModel.getListFoodM();   //obtengo listas
                    FoodAdapter foodAdapter = new FoodAdapter(listFood);
                    recyclerViewComponents.setLayoutManager(new LinearLayoutManager(Stock.this));
                    recyclerViewComponents.setItemAnimator(new DefaultItemAnimator());
                    recyclerViewComponents.setAdapter(foodAdapter);
                    progressDialog.dismiss();
                    //begin
                    foodAdapter.setOnItemClickListener( Stock.this );
                    //end
                }
                else {
                    Toast.makeText(Stock.this, listFoodModel.getMessage(), Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ListFoodModel> call, Throwable t) {
                Toast.makeText(Stock.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    //begin buscador
    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        invalidateOptionsMenu();
        MenuItem item= menu.findItem(R.id.action_search);
        item.setVisible(true);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        invalidateOptionsMenu();
        getMenuInflater().inflate(R.menu.menu_items, menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(this);
        //       if(menuItem.getItemId() != '0')    return true;
        //       else    return false;
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
    @Override
    public boolean onQueryTextChange(String newText)
    {//aca agrega la lista
        newText = newText.toLowerCase();

        final ArrayList<FoodModel> newList = new ArrayList<>();

        for ( FoodModel component : listFood)
        {

            String description = component.getDescription().toLowerCase();
            String manufacter = component.getManufacter().toLowerCase();
            String quantity = component.getQty().toLowerCase();
            String dknumber = component.getdigikeyNumber().toLowerCase();
            String location = component.getLocation().toLowerCase();

            if (description.contains(newText) || manufacter.contains( newText ) || quantity.contains( newText ) || dknumber.contains( newText ) || location.contains( newText ) ){
                newList.add(component);
            }
        }

        FoodAdapter adapter = new FoodAdapter(listFood);
        recyclerViewComponents.setAdapter(adapter);
        adapter.setFilter(newList);

        //adapter.setOnItemClickListener( Stock.this );   //click en newList: lista filtrada

        adapter.setOnItemClickListener( new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                global.flag3 = true;
                Intent detailedIntent = new Intent( Stock.this, CompInfo.class );

                FoodModel clickedItem = newList.get( position );

                detailedIntent.putExtra( EXTRA_STOCK_ID, clickedItem.getStockID() );
                detailedIntent.putExtra( EXTRA_DESC, clickedItem.getDescription() );
                detailedIntent.putExtra( EXTRA_MNUMBER, clickedItem.getManufacter() );
                detailedIntent.putExtra( EXTRA_QTY, clickedItem.getQty() );
                detailedIntent.putExtra( EXTRA_DKN, clickedItem.getdigikeyNumber() );
                detailedIntent.putExtra( EXTRA_LOC, clickedItem.getLocation() );
                detailedIntent.putExtra( "flag1", "flag1" );

                global.desc = newList.get( position ).getDescription().toLowerCase();
                int i = 0;
                for ( FoodModel component : listFood)
                {
                    String description = component.getDescription().toLowerCase();
                    if(description.equals( global.desc )){
                        global.sickPosition = i;
                    }
                    i++;
                }
                startActivity( detailedIntent );

            }
        } );

        if (newList.size() == 0)
            Toast.makeText( Stock.this, "No se han encontrados componentes que se ajusten a su búsqueda", Toast.LENGTH_SHORT ).show();

        return true;
    }

    @Override
    public void onItemClick(int position) { //cuando se clickea un componente
        Intent detailedIntent = new Intent( this, CompInfo.class );
        FoodModel clickedItem = listFood.get( position );

        detailedIntent.putExtra( EXTRA_STOCK_ID, clickedItem.getStockID() );
        detailedIntent.putExtra( EXTRA_DESC, clickedItem.getDescription() );
        detailedIntent.putExtra( EXTRA_MNUMBER, clickedItem.getManufacter() );
        detailedIntent.putExtra( EXTRA_QTY, clickedItem.getQty() );
        detailedIntent.putExtra( EXTRA_DKN, clickedItem.getdigikeyNumber() );
        detailedIntent.putExtra( EXTRA_LOC, clickedItem.getLocation() );
        detailedIntent.putExtra( "flag1", "flag1" );

        startActivity( detailedIntent );
    }
    //end buscador

}

class FoodModel {
    @SerializedName("stock_id") //SerializedName es necesaria para que Json mapee las llaves JSON a campos de objeto Java.
    private String stock_id;
    @SerializedName("description")
    private String ComponentName;
    @SerializedName("m_number")
    private String manufacter;
    @SerializedName("quantity")
    private String foodQty;
    @SerializedName("dk_number")
    private String digikeyNumber;
    @SerializedName("location")
    private String location;


    public FoodModel(String stock_id, String description, String manufacter, String qty, String location, String digikeyNumber) {
        this.stock_id = stock_id;
        this.ComponentName = description;
        this.manufacter = manufacter;
        this.foodQty = qty;
        this.digikeyNumber = digikeyNumber;
        this.location = location;

    }
    String getDescription() {return ComponentName;}
    String getLocation() {return location;}
    String getStockID(){return stock_id;}
    String getManufacter() {return manufacter;}
    String getdigikeyNumber() { return digikeyNumber; }
    String getQty() {return foodQty;}

}


class ApiClient {

    final private static AccessToComp.IP ip = new AccessToComp.IP();
    public static final String URL      = "http://"+ip.ifconfig+"/";
    public static Retrofit RETROFIT     = null;

    static Retrofit getClient(){
        if(RETROFIT==null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new LoggingInterceptor())
                    .build();
            RETROFIT = new Retrofit.Builder()
                    .baseUrl(URL)
                    .client(client)
                    .addConverterFactory( GsonConverterFactory.create())
                    .build();
        }
        return RETROFIT;
    }
}

interface ApiService {
    @GET("DisplayJson.php")
    Call<ListFoodModel> getAllData();
}

class ListFoodModel {
    @SerializedName("food")
    private ArrayList<FoodModel> listFoodM;
    @SerializedName("status")
    private int status;
    @SerializedName("message")
    private String message;

    public ListFoodModel(ArrayList<FoodModel> listFoodM, int status, String message) {
        this.listFoodM = listFoodM;
        this.status = status;
        this.message = message;
    }

    ArrayList<FoodModel> getListFoodM() {return listFoodM;}
    int getStatus() {return status;}
    String getMessage() {return message;}

}

class LoggingInterceptor implements Interceptor {
    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        long t1 = System.nanoTime();
        String requestLog = String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers());
        //YLog.d(String.format("Sending request %s on %s%n%s",
        // request.url(), chain.connection(), request.headers()));
        if (request.method().compareToIgnoreCase("post") == 0) {
            requestLog = "\n" + requestLog + "\n" + bodyToString(request);
        }
        Log.d("TAG", "request" + "\n" + requestLog);

        okhttp3.Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        String responseLog = String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers());

        String bodyString = response.body().string();

        Log.d("TAG", "response" + "\n" + responseLog + "\n" + bodyString);

        return response.newBuilder()
                .body( ResponseBody.create(response.body().contentType(), bodyString))
                .build();
    }

    public static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }
}
class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolderDatos>{

    ArrayList<FoodModel> listFoodModel;

    //begin enter in components list
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }
    //end enter in components list


    FoodAdapter(ArrayList<FoodModel> listFoodModel)
    {
        this.listFoodModel = listFoodModel;
    }

    @Override
    public ViewHolderDatos onCreateViewHolder(ViewGroup parent, int viewType)
    {//enlaza adaptador con item_food.xml
        View view = LayoutInflater.from(parent.getContext()).inflate( R.layout.activity_items,null,false);
        return new ViewHolderDatos(view);
    }
    //CHEQUEAR
    @Override
    public void onBindViewHolder(ViewHolderDatos holder, int position)
    {
        //ESTABLECE LA COMUNICACION ENTRE ADAPTADOR Y LA CLASE VIEWHOLDERDATOS
/*
        if((position % 2) == 0) {
            holder.tvDescription.setBackgroundColor( Color.WHITE );
            holder.tvManufN.setBackgroundColor( Color.WHITE );
            holder.tvQty.setBackgroundColor( Color.WHITE );
        }
*/
        try{
            holder.tvDescription.setText(listFoodModel.get(position).getDescription());
            holder.tvQty.setText(listFoodModel.get(position).getQty());
            holder.tvLocation.setText(listFoodModel.get(position).getLocation());
            //holder.tvManufN.setText(listFoodModel.get(position).getManufacter());
           // holder.tvDkN.setText(listFoodModel.get(position).getdigikeyNumber());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listFoodModel.size();
    }

    class ViewHolderDatos extends RecyclerView.ViewHolder
    {
        TextView tvDescription;
        TextView tvManufN;
        TextView tvQty;
        TextView tvDkN;
        TextView tvLocation;

        ViewHolderDatos(View itemView) {
            super(itemView);
            tvDescription = itemView.findViewById(R.id.tv_description);
            //tvManufN = itemView.findViewById( R.id.tv_manufacter );
            tvQty = itemView.findViewById(R.id.tv_quantity);
            tvDkN = itemView.findViewById(R.id.tv_DKN);
            tvLocation = itemView.findViewById(R.id.tv_ubic);

            if(global.flag2) {   //flag2 avisa cuando el programa paso por compInfo
                if(global.flag3){    //lista filtrada
                    mListener.onItemClick( global.sickPosition );
                    //p.flag3 = false;
                } else {
                    mListener.onItemClick( global.position );
                    global.flag2=false;
                }
            }
            else {
                itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mListener != null)
                        {
                            int position = getAdapterPosition();
                            global.position = getAdapterPosition();
                            if(position != RecyclerView.NO_POSITION){
                                mListener.onItemClick( position );
                            }
                        }
                    }
                } );
            }
        }
    }

    void setFilter(List<FoodModel> newList)
    {
        listFoodModel = new ArrayList<>();
        listFoodModel.addAll(newList);
        notifyDataSetChanged();
        notifyDataSetChanged();
    }

}

//Global Variables
class global {
    static int position = 0;
    static int sickPosition = 0;
    static Boolean flag2 = false;
    static Boolean flag3 = false;
    static String desc = null;
    static String stock_id = null;
}