package com.example.retrofit;

import android.app.ProgressDialog;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class RetroFitEx extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<Main> dataArrayList=new ArrayList<>();
    MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retro_fit);

        recyclerView=findViewById(R.id.recyclerView);

        adapter=new MainAdapter(RetroFitEx.this,dataArrayList);

        recyclerView.setLayoutManager(new GridLayoutManager(this,4));

        recyclerView.setAdapter(adapter);

        getData();

    }

    private void getData() {

        ProgressDialog d=new ProgressDialog(RetroFitEx.this);
        d.setMessage("Wait....");
        d.setCancelable(false);
        d.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://picsum.photos")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        MainInterface MIF=retrofit.create(MainInterface.class);

        Call<String> stringCall=MIF.STRING_CALL();
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
if (response.isSuccessful()&&response.body()!=null){
    d.dismiss();
    try{
        JSONArray json= new JSONArray(response.body());

        parseArray(json);

    }catch (JSONException e){
        e.printStackTrace();
    }

}
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });



}

    private void parseArray(JSONArray json) {
        dataArrayList.clear();
        for (int i=0; i<json.length(); i++){
            try {
                JSONObject ob=json.getJSONObject(i);
                Main main=new Main();
                main.setImage(ob.getString("download_url"));
                main.setName(ob.getString("author"));

                dataArrayList.add(main);

            }catch(JSONException e){
                e.printStackTrace();
            }

            adapter=new MainAdapter(RetroFitEx.this,dataArrayList);
            recyclerView.setAdapter(adapter);

        }

    }
}