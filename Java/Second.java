package com.example.hariom.khata;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Second extends AppCompatActivity {
    public static TextView dena,lena,net;
    ListView listView;
    public static Adapter adapter;
    ArrayList<String> stringArrayList;
    ArrayAdapter<String> arrayAdapter;
    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second);

        dena = (TextView) findViewById(R.id.dena);
        lena = (TextView) findViewById(R.id.lena);
        net = (TextView) findViewById(R.id.net_balance);
        listView = (ListView) findViewById(R.id.listview);
        stringArrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, R.layout.name_listview, stringArrayList);

        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = listView.getItemAtPosition(position).toString();
                Bundle bundle=new Bundle();
                bundle.putString("name",s);
                Intent intent=new Intent(Second.this,Details.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        adapter=new Adapter(this);
        load();
        loadDena();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String name = listView.getItemAtPosition(position).toString();
                delete(name);
                return false;
            }
        });
    }
    public void delete(final String string){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle("Delete Name");
        builder.setMessage("You may loose some data");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.deleteName(string);
                stringArrayList.remove(string);
                arrayAdapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"Entry Successfully Deleted",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                startActivity(new Intent(getApplicationContext(),Delete.class));
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }
    public void name(View view){
        final AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(true);
        LayoutInflater inflater=LayoutInflater.from(this);
        builder.setTitle("Enter name :");
        view=inflater.inflate(R.layout.name,null);

        final EditText n=(EditText)view.findViewById(R.id.editText);
        builder.setView(view);
        builder.setPositiveButton("Confirm",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        String naam=n.getText().toString();
                        String nam=String.valueOf(naam.charAt(0)).toUpperCase() + naam.substring(1, naam.length());
                        if(naam.length()!=0){
                            long id=adapter.insertData(nam);
                            if(id>=0)
                                Toast.makeText(getApplicationContext(),"Successfully inserted",Toast.LENGTH_SHORT).show();
                            else
                                Toast.makeText(getApplicationContext(),"Inserion failed",Toast.LENGTH_SHORT).show();
                        }else
                                Toast.makeText(getApplicationContext(),"Please Enter Proper Name",Toast.LENGTH_SHORT).show();
                        stringArrayList.add(nam);
                        arrayAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }).setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
    public void load(){
        stringArrayList.addAll(adapter.loadName());
    }
    public static void loadDena(){
        int x=0,y=0;
        x=adapter.denaLena(0);
        y=adapter.denaLena(1);
        dena.setText(String.valueOf(x));
        lena.setText(String.valueOf(y));
        int n=y-x;
        net.setText(String.valueOf(n));
    }
}
