package com.example.threadingexample.retrofit_example;

import androidx.appcompat.app.AppCompatActivity;

import android.media.SoundPool;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.threadingexample.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitActivity extends AppCompatActivity {
    SoundPool soundPool;
    Retrofit retrofit;
    BittrexAPI bittrexAPI;
    List<Markets> data;
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
/*        soundPool = new SoundPool.Builder().build();
        soundPool.load(this, R.raw.fff,0);
        soundPool.play(R.raw.fff,1,1,1,0,1);*/

        setContentView(R.layout.activity_retrofit);
        ListView listView = findViewById(R.id.listview);
        data = new ArrayList<>();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.bittrex.com/api/v1.1/public/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bittrexAPI = retrofit.create(BittrexAPI.class);
        adapter = new MyAdapter(data);
        listView.setAdapter(adapter);
        Call<ResultObject> call = bittrexAPI.getMarkets();
        call.enqueue(new Callback<ResultObject>() {
            @Override
            public void onResponse(Call<ResultObject> call, Response<ResultObject> response) {
                if(response.body()!=null){
                    if(response.isSuccessful()) {
                        data.addAll(response.body().result);
                        adapter.notifyDataSetChanged();
                    }
                }
                //((TextView)findViewById(R.id.tv_errorlog)).setText(response.message()+"\n"
                //+response.isSuccessful()+"\nbody="+response.body().result);
                ((TextView)findViewById(R.id.tv_errorlog)).setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ResultObject> call, Throwable t) {
                ((TextView)findViewById(R.id.tv_errorlog)).setVisibility(View.VISIBLE);
                ((TextView)findViewById(R.id.tv_errorlog)).setText(t.getMessage());
            }
        });
    }

    public class MyAdapter extends BaseAdapter{
        public MyAdapter(List<Markets> data) {
            this.data = data;
        }

        List<Markets> data;

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Markets getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v = view;
            if(v == null){
                v =((LayoutInflater) viewGroup.getContext().getSystemService(LAYOUT_INFLATER_SERVICE))
                        .inflate(R.layout.list_item, viewGroup,false);
            }
            ImageView imageView = v.findViewById(R.id.iv_logo);
            Picasso.get().load(data.get(i).LogoUrl).into(imageView);
            TextView textView = v.findViewById(R.id.tv_market_name);
            textView.setText(data.get(i).MarketName);
            TextView textView2 = v.findViewById(R.id.tv_trade_size);
            textView2.setText(data.get(i).MinTradeSize);
            return v;
        }
    }

    void foo(){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("uri","klfmlfdgmdlkmfldgdfg");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
