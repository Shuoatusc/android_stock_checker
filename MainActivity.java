package com.ryanliu.hw9_v13;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.json.JSONArray;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.facebook.FacebookSdk;

public class MainActivity extends AppCompatActivity {
    String acUrl;
    JSONObject mJSONObject_ac;
    AutoCompleteTextView auto_tv;
    ArrayList<String> names;
    List<String> Show_names;
    ArrayAdapter<String> adapter;
    private static final String TAG_RESULT = "Symbol";
    private static final String TAG="MainActivity";
    public ProgressBar acProgressbar;
    public static ArrayList<Stock> stocks_for_fav=new ArrayList<Stock>();
    public ArrayList<Stock> default_stock = new ArrayList<Stock>();
    public String receiver;
    public String selectedItemSymbol;
    public int refresh_progress_counter;
    private Handler mTimerHandler;
    public Spinner mSpinner_sort;
    public Spinner mSpinner_order;
    public String sorting_status="Sort by";
    public String order_status="Order";
    public boolean neversort = true;


    //public ArrayAdapter<CharSequence> adapter_mSpinner_sort;
    //public ArrayAdapter<CharSequence> adapter_mSpinner_order;
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSpinner_sort = findViewById(R.id.spinner_sort_by);
        mSpinner_order=findViewById(R.id.spinner_order);
        String[] sortby = getResources().getStringArray(R.array.Sort_by);
        String[] orderby = getResources().getStringArray(R.array.order);
        final List<String> sortsList = new ArrayList<>(Arrays.asList(sortby));
        final List<String> orderlist = new ArrayList<>(Arrays.asList(orderby));


        ArrayAdapter<String> adapter_mSpinner_sort = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,sortsList){
            @Override
            public boolean isEnabled(int position) {
                if(position==0){
                    return false;
                }
                else if(Objects.equals(sortsList.get(position), sorting_status)){
                    Log.d("sorting_debug",""+position);
                    return false;
                }
                return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position==0){
                    tv.setTextColor(Color.GRAY);
                }
                else if(Objects.equals(sortsList.get(position), sorting_status)){
                    tv.setTextColor(Color.GRAY);
                }
                else{
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };




        ArrayAdapter<String> adapter_mSpinner_order = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,orderlist){
            @Override
            public boolean isEnabled(int position) {
                if(position==0){
                    return false;
                }
                else if(Objects.equals(orderlist.get(position), order_status)){
                    return false;
                }
                return true;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position==0){
                    tv.setTextColor(Color.GRAY);
                }
                else if(Objects.equals(orderlist.get(position), order_status)){
                    tv.setTextColor(Color.GRAY);
                }
                else{
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };



        //adapter_mSpinner_sort = ArrayAdapter.createFromResource(this,R.array.Sort_by,android.R.layout.simple_spinner_item);
        //adapter_mSpinner_order=ArrayAdapter.createFromResource(this,R.array.order,android.R.layout.simple_spinner_item);
        adapter_mSpinner_sort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter_mSpinner_order.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner_sort.setAdapter(adapter_mSpinner_sort);
        mSpinner_order.setAdapter(adapter_mSpinner_order);

        mSpinner_sort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Log.d(TAG,parent.getItemAtPosition(position).toString());

                    sorting_status=parent.getItemAtPosition(position).toString();
                    if(!Objects.equals(parent.getItemAtPosition(position).toString(), "Sort by")){
                        if(!Objects.equals(order_status, "Order")){
                            if(neversort){
                                neversort=false;
                                for(int index=0;index<stocks_for_fav.size();index++){
                                    default_stock.add(stocks_for_fav.get(index));
                                }
                            }
                            sorting();

                        }
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mSpinner_order.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Log.d(TAG,parent.getItemAtPosition(position).toString());
                order_status=parent.getItemAtPosition(position).toString();
                if(!Objects.equals(parent.getItemAtPosition(position).toString(), "Order")){
                    if(!Objects.equals(sorting_status, "Sort by")){
                        if(neversort){
                            neversort=false;
                            for(int index=0;index<stocks_for_fav.size();index++){
                                default_stock.add(stocks_for_fav.get(index));
                            }
                        }
                        sorting();

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        auto_tv = (AutoCompleteTextView) findViewById(R.id.stockInput);
        auto_tv.setThreshold(0);
        names = new ArrayList<String>();
        fav_ListAdapter fav_listAdapter = new fav_ListAdapter(this,R.layout.fav_list_row,stocks_for_fav);
        if(getIntent().getStringExtra("isfavorited")!=null){
            if(Objects.equals(getIntent().getStringExtra("isfavorited"), "yup")){

                add_fav_list();
            }
            else if(Objects.equals(getIntent().getStringExtra("isfavorited"), "nope")){

                reload_fav_list();
            }
        }
        auto_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() <= 5) {
                    names = new ArrayList<String>();
                    Show_names= new ArrayList<String>();
                    updateList(s.toString());
                    acProgressbar=findViewById(R.id.progressBar_autocomplete);
                    acProgressbar.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });//addTextChangedListener
        auto_tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s=((TextView) view).getText().toString();
                //auto_tv.setText(s.split(" ")[0]);
                auto_tv.setText(s);
            }
        });
    /*
        ListView listView = findViewById(R.id.favListView);
        fav_item = String[](){"123",}
        ArrayAdapter adapter= new ArrayAdapter(this,android.R.layout.simple_list_item_1,test);
        listView.setAdapter(adapter);*/
        Switch mswich = (Switch) findViewById(R.id.auto_refresh_switch);
        mswich.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    auto_refresh();
                }
                else {
                    mTimerHandler.removeCallbacksAndMessages(null);
                }
            }
        });//switch


    }//onCreate()

    public void updateList(String place){
        String acUrl ="http://stockchecker.us-west-1.elasticbeanstalk.com/?ACticker="+place;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, acUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONArray jsonArr = new JSONArray(response);
                    for(int i=0;i<jsonArr.length();i++){
                        JSONObject jsonObj = jsonArr.getJSONObject(i);
                        String temp = jsonObj.getString("Symbol")+" - "+jsonObj.getString("Name")+"("+jsonObj.getString("Exchange")+")";
                        names.add(temp);
                    }
                    if(names.size()<6){
                        Show_names=names;
                    }
                    else {
                        Show_names=names.subList(0,5);
                    }
                    adapter = new ArrayAdapter<String>(
                            getApplicationContext(),
                            android.R.layout.simple_list_item_1, Show_names) {
                        @Override
                        public View getView(int position,
                                            View convertView, ViewGroup parent) {
                            View view = super.getView(position,
                                    convertView, parent);
                            TextView text = (TextView) view
                                    .findViewById(android.R.id.text1);
                            text.setTextColor(Color.BLACK);
                            return view;
                        }
                    };
                    auto_tv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }catch (Exception e) {
                }
            acProgressbar=findViewById(R.id.progressBar_autocomplete);
                acProgressbar.setVisibility(View.GONE);
            }

        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }
        );

        MyApplication.getInstance().addToReqQueue(stringRequest, "jreq");


    }//updateList

    public void getQuoteClick(View view) {
            AutoCompleteTextView autoCompleteTextView = findViewById(R.id.stockInput);

            String input_ticker = autoCompleteTextView.getText().toString().trim();
            input_ticker=input_ticker.split("-")[0];
            Log.d("input-check",input_ticker);
            if(input_ticker.isEmpty()){
                Toast.makeText(MainActivity.this,"Please enter a stock name or symbol",Toast.LENGTH_LONG).show();
            }
            else{
                Intent intent = new Intent(this,DetailsActivity.class);
                intent.putExtra("symbol_name",input_ticker);
                intent.putExtra("favornot","N");
                startActivity(intent);
            }

    }//getQuoteClick()


    public void getClearClick(View view) {
        AutoCompleteTextView autoCompleteTextView = findViewById(R.id.stockInput);
        autoCompleteTextView.setText("");

    }//getClearClick()
    public void add_fav_list(){

        SharedPreferences sharedPreferences = getSharedPreferences("fav_stock", Context.MODE_PRIVATE);
        String favl_ticker = sharedPreferences.getString("stock_symbol","");
        String favl_price  = sharedPreferences.getString("stock_price","");
        String favl_change =sharedPreferences.getString("stock_change","");
        String favl_changePercent = sharedPreferences.getString("stock_change_percent","");
        Stock favl_stock = new Stock(favl_ticker,Double.parseDouble(favl_price),Double.parseDouble(favl_change),Double.parseDouble(favl_changePercent));
        stocks_for_fav.add(favl_stock);

        fav_ListAdapter fav_listAdapter = new fav_ListAdapter(this,R.layout.fav_list_row,stocks_for_fav);
        final ListView mlistview = (ListView) findViewById(R.id.favListView);
        mlistview.setAdapter(fav_listAdapter);
        registerForContextMenu(mlistview);

        mlistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                TextView textView = (TextView) v.findViewById(R.id.favList_symbol);
                String text = textView.getText().toString();
                selectedItemSymbol=text;
                return false;
            }
        });
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                Log.d("shuo'stext",stocks_for_fav.get(position).symbol);
                Intent intent = new Intent(getBaseContext(),DetailsActivity.class);
                intent.putExtra("symbol_name",stocks_for_fav.get(position).symbol);
                intent.putExtra("favornot","Y");
                startActivity(intent);

            }
        });



    }
    public void reload_fav_list(){
        fav_ListAdapter fav_listAdapter = new fav_ListAdapter(this,R.layout.fav_list_row,stocks_for_fav);
        ListView mlistview = (ListView) findViewById(R.id.favListView);
        mlistview.setAdapter(fav_listAdapter);
        registerForContextMenu(mlistview);
        mlistview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                TextView textView = (TextView) v.findViewById(R.id.favList_symbol);
                String text = textView.getText().toString();
                selectedItemSymbol=text;

                return false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void delete_fav_list(String ticker){
        for(int i=0;i<stocks_for_fav.size();i++){
            if(Objects.equals(stocks_for_fav.get(i).symbol, ticker)){
                stocks_for_fav.remove(i);
            }
            reload_fav_list();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.fav_list_menu,menu);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()){
            case R.id.Yes_button:
                delete_fav_list(selectedItemSymbol);
                Toast.makeText(MainActivity.this,"Selected Yes",Toast.LENGTH_LONG).show();
                return true;

            case R.id.No_button:
                Toast.makeText(MainActivity.this,"Selected No",Toast.LENGTH_LONG).show();
                return true;


        }
        return super.onContextItemSelected(item);
    }

    public void getRefreshClick(View view) {
        refresh();

    }//getRefreshClick

    public void refresh(){
        refresh_progress_counter=0;
        ProgressBar progressBar_refresh = findViewById(R.id.progressBar_autocomplete);
        progressBar_refresh.setVisibility(View.VISIBLE);
        for( int index=0;index<stocks_for_fav.size();index++){
            String details_url = "http://stockchecker.us-west-1.elasticbeanstalk.com/?submit="+stocks_for_fav.get(index).symbol;
            final int finalIndex = index;
            StringRequest stringRequest = new StringRequest(Request.Method.GET, details_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        Stock stock_temp=new Stock(jsonObject);
                        Stock refresh_stock = new Stock(stock_temp.symbol,stock_temp.today_close,stock_temp.change,stock_temp.change_percent);
                        stocks_for_fav.set(finalIndex,refresh_stock);
                    }catch (Exception e) {

                    }
                    refresh_progress_counter++;
                    if(refresh_progress_counter==stocks_for_fav.size()){
                        reload_fav_list();
                        ProgressBar progressBar_refresh = findViewById(R.id.progressBar_autocomplete);
                        progressBar_refresh.setVisibility(View.GONE);
                    }

                }//onResponse

            },new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d(TAG,error.toString());
                }
            }

            );
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(8000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApplication.getInstance().addToReqQueue(stringRequest, "req_refresh"+index);//end of current_part


        }//forloop


    }

    public void auto_refresh(){
        mTimerHandler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                refresh();
                mTimerHandler.postDelayed(this,5000);
            }
        };
        mTimerHandler.postDelayed(runnable,5000);
    }//auto_refresh()

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void sorting(){


    if(Objects.equals(order_status, "Ascending")){
            switch (sorting_status) {
                case "Symbol":
                    Collections.sort(stocks_for_fav, new symbol_comparator());
                    break;

                case "Price":
                    Collections.sort(stocks_for_fav, new price_comparator());
                    break;

                case "Change":
                    Collections.sort(stocks_for_fav, new change_comparator());
                    break;
                case "Default":
                    for(int index=0;index<default_stock.size();index++){
                        stocks_for_fav.set(index,default_stock.get(index));
                    }

                    break;
            }
    }
    else if(Objects.equals(order_status, "Descending")){
        switch (sorting_status) {
            case "Symbol":
                Collections.sort(stocks_for_fav, new symbol_comparator());
                Collections.reverse(stocks_for_fav);
                break;

            case "Price":
                Collections.sort(stocks_for_fav, new price_comparator());
                Collections.reverse(stocks_for_fav);
                break;

            case "Change":
                Collections.sort(stocks_for_fav, new change_comparator());
                Collections.reverse(stocks_for_fav);
                break;

            case "Default":
                for(int index=0;index<default_stock.size();index++){
                    stocks_for_fav.set(index,default_stock.get(index));
                }
                break;
        }
    }
        reload_fav_list();

    }//sorting


}//main
