package com.example.mycontactapp;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mycontactapp.R;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName;
    EditText editPhone;
    EditText editAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyContactApp", "MainActivity: setting up the layout");
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText_name);
        editPhone = findViewById(R.id.editText_phone);
        editAddress = findViewById(R.id.editText_address);

        myDb = new DatabaseHelper(this);
        Log.d("MyContactApp", "MainActivity: instantiated DatabaseHelper");
    }

    public void addData(View view){

        Log.d("MyContactApp", "MainActivity: adding data into database");

        boolean isInserted = myDb.insertData(editName.getText().toString(), editPhone.getText().toString(), editAddress.getText().toString());

        if(isInserted){
            Toast.makeText(MainActivity.this, "SUCCESS - contact inserted", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(MainActivity.this, "FAILED - contact not inserted", Toast.LENGTH_LONG).show();
        }
    }

    public void viewData(View view){
        Cursor result = myDb.getAllData();
        Log.d("MyContactApp", "MainActivity: viewing data");

        if(result.getCount() == 0)
        {
            showMessage("Error", "No data found in database");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while(result.moveToNext()){
            //Append res columns to the buffer - see StringBuffer and Cursor api
            buffer.append("ID: " + result.getString(0)+"\n");
            buffer.append("Name: " + result.getString(1)+"\n");
            buffer.append("Phone: " + result.getString(2)+"\n");
            buffer.append("Address: " + result.getString(3)+"\n\n");
        }

        showMessage("Data", buffer.toString());
    }

    public void searchData(View view){
        Cursor result = myDb.getAllData();
        Log.d("MyContactApp", "MainActivity: searching data");

        if(result.getCount() == 0)
        {
            showMessage("Error: No Contacts", "No data found in database");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while(result.moveToNext()){
            //Append res columns to the buffer - see StringBuffer and Cursor api
            if((result.getString(1).contains(editName.getText().toString()) && result.getString(1) != null) ||
                    (result.getString(2).contains(editPhone.getText().toString()) && result.getString(2) != null) ||
                    (result.getString(3).contains(editAddress.getText().toString()) && result.getString(3) != null))
            {
                buffer.append("ID: " + result.getString(0)+"\n");
                buffer.append("Name: " + result.getString(1)+"\n");
                buffer.append("Phone: " + result.getString(2)+"\n");
                buffer.append("Address: " + result.getString(3)+"\n\n");
            }

        }

        if(buffer.toString().equals(null))
        {
            showMessage("No Search Query", "Please enter a search query");
            return;
        }

        showMessage("Data", buffer.toString());
    }

    public void showMessage(String title, String message){
        Log.d("MyContactApp", "MainActivity: showing message");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
