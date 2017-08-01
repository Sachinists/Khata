package com.example.hariom.khata;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Details extends AppCompatActivity {
    static TextView textView_balance;
    public  static Adapter adapter;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<String> stringArrayList_amount,stringArrayList_reasons;
    ListView listView_details;
    public  static String name;
    ListViewAdapter lviewAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details);
        stringArrayList_amount = new ArrayList<>();
        stringArrayList_reasons = new ArrayList<>();
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        name=bundle.getString("name");
        adapter=new Adapter(this);
        loadTransactions(name);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        listView_details=(ListView)findViewById(R.id.listview_details);
        textView_balance=(TextView)findViewById(R.id.balance);

        try{
            getSupportActionBar().setTitle(name);

            myToolbar.setTitleTextColor(Color.WHITE);
            if(getSupportActionBar()!=null){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }}
        catch (NullPointerException e){
        }

        lviewAdapter = new ListViewAdapter(this,R.layout.details,stringArrayList_amount);
        listView_details.setAdapter(lviewAdapter);
        textView_balance.setText(adapter.loadBalance(name));
    }

    public static void setBalance(String name){
        textView_balance.setText(adapter.loadBalance(name));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        View view;
        switch (item.getItemId()) {
            case  android.R.id.home:
                finish();
                return true;
            case R.id.transactions:
                final AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setCancelable(true);
                LayoutInflater inflater=LayoutInflater.from(this);
                builder.setTitle("Ntr Trans Det:");
                view=inflater.inflate(R.layout.transaction,null);

                final EditText editText_rs=(EditText)view.findViewById(R.id.rupees_details);
                final EditText editText_reason=(EditText)view.findViewById(R.id.editText2_reason_input);
                final EditText editText_date=(EditText)view.findViewById(R.id.transaction_date);
                final RadioButton radioButton_borrow=(RadioButton)view.findViewById(R.id.borrow);
                final RadioButton radioButton_repay=(RadioButton)view.findViewById(R.id.repay);
                builder.setView(view);
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int whichButton) {
                                String rupees=editText_rs.getText().toString();
                                String date=editText_date.getText().toString();
                                String reason=editText_reason.getText().toString();
                                if(rupees.length()==0||date.length()==0||reason.length()==0){
                                    Toast.makeText(getApplicationContext(),"Please Fill The Necessary Details",Toast.LENGTH_SHORT).show();
                                }else {
                                    int rupee=Integer.parseInt(rupees);
                                    if(radioButton_borrow.isChecked()){
                                        rupee=rupee*-1;
                                    }
                                    adapter.insertTransaction(name,rupee,reason,date);

                                    StringBuilder stringBuilder=new StringBuilder();
                                    stringBuilder.append(rupees).append("$").append(date).append("@").append(reason);
                                    stringArrayList_amount.add(stringBuilder.toString());
                                    lviewAdapter.notifyDataSetChanged();
                                }
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
                return true;
            case R.id.contact_us:
                final AlertDialog.Builder builder1=new AlertDialog.Builder(this);
                builder1.setCancelable(true);
                builder1.setTitle("Developer Contact");
                LayoutInflater inflater1=LayoutInflater.from(this);
                view=inflater1.inflate(R.layout.contactus,null);
                builder1.setView(view);
                builder1.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder1.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
    public  void loadTransactions(String name){
        stringArrayList_amount.addAll(adapter.load(name));
    }

}
