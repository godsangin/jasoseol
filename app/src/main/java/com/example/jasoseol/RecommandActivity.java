package com.example.jasoseol;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class RecommandActivity extends AppCompatActivity {
    ListView myListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommand);
        myListView = findViewById(R.id.board_list);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(Color.parseColor("#ff7f50"));
        }

        ArrayList<CompanyBoard> boards = new ArrayList<>();
        AssetManager assetManager = getResources().getAssets();
        try{
            AssetManager.AssetInputStream assetInputStream = (AssetManager.AssetInputStream)assetManager.open("Android.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(assetInputStream));
            StringBuilder stringBuilder = new StringBuilder();

            int bufferSize = 1024 * 1024;
            char[] readBuf = new char[bufferSize];
            int resultSize = 0;

            while((resultSize = bufferedReader.read(readBuf)) != -1){
                if(resultSize == bufferSize){
                    stringBuilder.append(readBuf);
                }else{
                    for(int i = 0; i < resultSize; i++){
                        stringBuilder.append(readBuf[i]);
                    }
                }
            }
            String jString = stringBuilder.toString();
            JSONArray jsonArray = new JSONArray(jString);
            Log.d("jsonObjectLength==", jsonArray.length() + "");
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("company_name");
                String position = jsonObject.getString("fields");
                String imgURL = jsonObject.getString("image");
                String until = jsonObject.getString("end_time");
                CompanyBoard myBoard = new CompanyBoard(imgURL, title, position, until);
                Log.d("myBoard==", myBoard.toString());
                boards.add(myBoard);
            }
        }catch(JSONException e){
            e.printStackTrace();
            Log.d("json파싱에러==","발생");
        }catch (Exception e){
            e.printStackTrace();
            Log.d("예외==", "발생");
        }
        BoardListAdapter myAdapter = new BoardListAdapter(boards, this);
        myListView.setAdapter(myAdapter);
    }
}
