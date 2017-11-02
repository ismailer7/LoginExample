package com.example.ismailrzouki.loginexample;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

public class Login extends AppCompatActivity {

    final String DBNAME = "myDB";
    final String TABLENAME = "account";
    final String OK = "done";
    final String ERROR_INPUTS = "Email or Password Incorrect..please check your inputs";
    Button annuler, ok;
    FloatingActionButton add;
    EditText email, password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

       init();

        annuler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email.setText(null);
                password.setText(null);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater layoutInflater = LayoutInflater.from(Login.this);
                View promtView = layoutInflater.inflate(R.layout.create_account, null);
                final EditText et1 = (EditText) promtView.findViewById(R.id.text1);
                final EditText et2 = (EditText) promtView.findViewById(R.id.text2);
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setView(promtView)
                        .setCancelable(false)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(DBNAME,MODE_PRIVATE,null);
                                sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLENAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR, password VARCHAR);");
                                String username = et1.getText().toString();
                                String pass = et2.getText().toString();
                                //Log.e("USERNAME:",username);
                                //Log.e("PASSWORD",pass);
                                sqLiteDatabase.execSQL("INSERT INTO "+TABLENAME+"(username,password) VALUES('"+username+"','"+pass+"')");
                                Toast.makeText(Login.this, "Added", Toast.LENGTH_SHORT).show();
                                //Log.e("DB:","clicked");

                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });



        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(DBNAME, MODE_PRIVATE, null);
                //sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLENAME+"(id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR, password VARCHAR);");
                String query = "SELECT * FROM "+TABLENAME+" WHERE username='"+email.getText().toString()+"' AND password='"+password.getText().toString()+"'";
                Cursor resultSet = sqLiteDatabase.rawQuery(query,null);
                if(resultSet.moveToFirst()) {
                    Toast.makeText(getApplicationContext(), OK, Toast.LENGTH_SHORT).show();
                    //String s = resultSet.getString(1);
                    //Log.e("EMAIL:",s);
                } else {
                    AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
                    alertDialog.setTitle("Error");
                    alertDialog.setMessage(ERROR_INPUTS);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }

            }
        });


    }


    public void init() {
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#E0FFFF"));
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        annuler = (Button) findViewById(R.id.annuler);
        ok = (Button) findViewById(R.id.ok);
        add = (FloatingActionButton) findViewById(R.id.fab);

    }



}
