package com.example.m7sob;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    String user = "",name = "";
    EditText et,pass;
    int goOut = 0;
    UserModelView vm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        vm = ViewModelProviders.of(this).get(UserModelView.class);
        et = findViewById(R.id.et);
        pass = findViewById(R.id.pass_et);
        user = PreferenceManager.getDefaultSharedPreferences(this).getString("user", "no user");
        name = PreferenceManager.getDefaultSharedPreferences(this).getString("name", "no name");
        if(haveInternetConnection()){
            if (!user.equals("no user")){
                Intent intent = new Intent(MainActivity.this, innerActivity.class);
                startActivity(intent);
                finish();
            }
            else {
                et.setVisibility(View.VISIBLE);
                ((TextView) findViewById(R.id.login)).setVisibility(View.VISIBLE);
            }
        }
        else{
            ((ConstraintLayout)findViewById(R.id.components)).setVisibility(View.GONE);
            ((ConstraintLayout)findViewById(R.id.no_connection)).setVisibility(View.VISIBLE);
        }


    }

    public boolean haveInternetConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info: networkInfos){
            if(info.getTypeName().equalsIgnoreCase("WIFI") || info.getTypeName().equalsIgnoreCase("MOBILE"))
                if(info.isConnected())
                    return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(goOut == 0) {
            goOut++;
            Toast.makeText(this, getResources().getString(R.string.back), Toast.LENGTH_SHORT).show();
        }
        else{
            finish();
        }
    }

    public void onComponentClick(View view){
        int id = view.getId();
        if(id == R.id.login) {
            if(!TextUtils.isEmpty(et.getText().toString()) && !TextUtils.isEmpty(pass.getText().toString())){
                user = et.getText().toString();
                vm.getUser(user);
                vm.getJsonObjectLiveData().observe(this, new Observer<JsonObject>() {
                    @Override
                    public void onChanged(JsonObject object) {
                        String password = object.get("password").getAsString();
                        name = object.get("name").getAsString();
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).edit();
                        editor.putString("name",name);
                        editor.putString("user",user);
                        editor.commit();
                        name = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("name", "no name");
                        if(password.equals(pass.getText().toString())){
                            Intent intent = new Intent(MainActivity.this,innerActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            Toast.makeText(MainActivity.this, getResources().getString(R.string.active), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            else{
                Toast.makeText(this, getResources().getString(R.string.req), Toast.LENGTH_SHORT).show();
            }
        }
    }
}