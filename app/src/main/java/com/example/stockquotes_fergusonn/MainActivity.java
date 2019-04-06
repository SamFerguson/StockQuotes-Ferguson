package com.example.stockquotes_fergusonn;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private TextView symbol;
    private TextView name;
    private TextView price;
    private TextView time;
    private TextView change;
    private TextView yearly;
    private EditText input;
    private View layout;
    private ListView listy;
    private ArrayList<String> previousStocks;
    private ArrayAdapter<String> stockAdapter;
    TextView a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_main_land);
        }
        symbol = (TextView)findViewById(R.id.s_result);
        name =(TextView) findViewById(R.id.c_result);
        price = (TextView)findViewById(R.id.lp_result);
        time = (TextView)findViewById(R.id.lt_result);
        change = (TextView)findViewById(R.id.ch_result);
        yearly = (TextView)findViewById(R.id.yr_result);
        input = (EditText) findViewById(R.id.Input);
        layout = (View)findViewById(R.id.activity);
        if(savedInstanceState != null){
            TextView a = (TextView) layout.findViewById(R.id.s_result);
            TextView b = layout.findViewById(R.id.c_result);
            TextView c = layout.findViewById(R.id.lp_result);
            TextView d = layout.findViewById(R.id.lt_result);
            TextView e = layout.findViewById(R.id.ch_result);
            TextView f = layout.findViewById(R.id.yr_result);
            a.setText(savedInstanceState.getString("stock"));
            b.setText(savedInstanceState.getString("company"));
            c.setText(savedInstanceState.getString("price"));
            d.setText(savedInstanceState.getString("time"));
            e.setText(savedInstanceState.getString("change"));
            f.setText(savedInstanceState.getString("year"));
            previousStocks = savedInstanceState.getStringArrayList("previous");
        }
        else{
            previousStocks = new ArrayList<>();
        }
        input.setOnKeyListener(inputListener);
        //make arraylist to pass
        //make listView
        listy = (ListView)findViewById(R.id.past_stocks);
        //make the adapter
        stockAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, previousStocks);
        //set adapter
        listy.setAdapter(stockAdapter);
        listy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //if they click on something
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //set the edittext to be what they clicked
                input.setText(previousStocks.get(position));
                //set the stock to send to be that thing
                String stockToSend = input.getText().toString();
                //make the async
                StockAsync mStockAsync = new StockAsync(stockToSend, (View)findViewById(R.id.activity), MainActivity.this);
                //send to the async
                mStockAsync.execute();
                //don't add stock to the array
                /*previousStocks.add(0,stockToSend);
                if(previousStocks.size()  > 4){
                    previousStocks.remove(4);
                }
                */
                //don't refresh the adapter
                //stockAdapter.notifyDataSetChanged();

            }
        });

    }


    private View.OnKeyListener inputListener = new View.OnKeyListener(){

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
             if(event.getAction() == KeyEvent.ACTION_UP && keyCode == 66) {
                 System.out.println("im in the onkey");
                 String stockToSend = input.getText().toString();
                 StockAsync mStockAsync = new StockAsync(stockToSend, (View)findViewById(R.id.activity), MainActivity.this);
                 System.out.println(symbol.getText().toString());
                 mStockAsync.execute();
                 //add stock to the array
                 previousStocks.add(0,stockToSend);
                 if(previousStocks.size()  > 4){
                     previousStocks.remove(4);
                 }
                 //refresh the adapter
                 stockAdapter.notifyDataSetChanged();
             }
            return false;
        }
    };


    static class StockAsync extends AsyncTask<Void, Void, String[]>{


        private Stock stock;
        private Context context;
        private View l;

        public StockAsync(String s, View layout,Context c){
            System.out.println("poopy");
            stock = new Stock(s);
            context = c;
            l = layout;
            System.out.println("poopy");

        }
        @Override
        protected String[] doInBackground(Void... voids) {

            try{
                System.out.println("poop");
                stock.load();
                System.out.println("poop again");
            }catch(Exception e){
                System.out.println(e.getMessage());
                return null;
            }

            String[] rtn = new String[6];
            rtn[0] = stock.getSymbol();
            rtn[1] = stock.getName();
            rtn[2] = stock.getLastTradePrice();
            rtn[3] = stock.getLastTradeTime();
            rtn[4] = stock.getChange();
            rtn[5] = stock.getRange();
            return rtn;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            super.onPostExecute(strings);

            TextView a = (TextView) l.findViewById(R.id.s_result);
            TextView b = l.findViewById(R.id.c_result);
            TextView c = l.findViewById(R.id.lp_result);
            TextView d = l.findViewById(R.id.lt_result);
            TextView e = l.findViewById(R.id.ch_result);
            TextView f = l.findViewById(R.id.yr_result);
            try {
                a.setText(strings[0]);
                b.setText(strings[1]);
                c.setText(strings[2]);
                d.setText(strings[3]);
                e.setText(strings[4]);
                f.setText(strings[5]);
            }catch(NullPointerException exception){
                showErrorAlert(context.getApplicationContext().getResources().getString(R.string.error), 0);
            }
        }
        private void showErrorAlert(String errorMessage, final int fieldId) {
            new AlertDialog.Builder(context)
                    .setTitle(context.getApplicationContext().getResources().getString(R.string.error))
                    .setMessage(errorMessage)
                    .setNeutralButton(context.getApplicationContext().getResources().getString(R.string.close),
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    l.findViewById(R.id.Input).requestFocus();
                                }
                            }).show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        TextView a = (TextView) layout.findViewById(R.id.s_result);
        TextView b = layout.findViewById(R.id.c_result);
        TextView c = layout.findViewById(R.id.lp_result);
        TextView d = layout.findViewById(R.id.lt_result);
        TextView e = layout.findViewById(R.id.ch_result);
        TextView f = layout.findViewById(R.id.yr_result);

        outState.putString("stock", a.getText().toString());
        outState.putString("company", b.getText().toString());
        outState.putString("price", c.getText().toString());
        outState.putString("time", d.getText().toString());
        outState.putString("change", e.getText().toString());
        outState.putString("year", f.getText().toString());
        outState.putStringArrayList("previous", previousStocks);

    }
}
