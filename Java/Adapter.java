package com.example.hariom.khata;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Adapter  {
    MyHelper helper;

    public Adapter(Context context) {
        helper=new MyHelper(context);
    }
    //insert from first pagee name
    public long insertData(String name){
        SQLiteDatabase sq=helper.getWritableDatabase();
        ContentValues cn=new ContentValues();
        cn.put(MyHelper.col2,name);
        cn.put(MyHelper.col8,0);
        long id=sq.insert(MyHelper.TABLE_NAME_ONE,null,cn);
        Log.d("messageiiiiiiiiiii",String.valueOf(id));
        return id;
    }
    //inserting from details page
    public long insertTransaction(String name,int amount,String reason,String date){
        SQLiteDatabase sq=helper.getWritableDatabase();
        ContentValues cn=new ContentValues();
        cn.put(MyHelper.col6,amount);
        cn.put(MyHelper.col4,name);
        cn.put(MyHelper.col5,reason);
        cn.put(MyHelper.col9,date);
        if(amount<0){
            cn.put(MyHelper.col7,"BORROW");
        }else{
            cn.put(MyHelper.col7,"REPAY");
        }
        long id=sq.insert(MyHelper.TABLE_NAME_TWO,null,cn);
        if(amount<0)
            calculate(name,0,amount);
        else
            calculate(name,1,amount);
        return id;
    }
    //calculate the net balance of indidvidu
    public void calculate(String name,int a,int amt){
        int x;
        String[] projectionLoad = {
                helper.col8
        };
        String selectionLoad = helper.col2 + " = ?";
        String[] selectionArgsLoad = { name };

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursorLoad=db.query(helper.TABLE_NAME_ONE,projectionLoad,selectionLoad,selectionArgsLoad,null,null,null);
        cursorLoad.moveToFirst();
            x=Integer.parseInt(cursorLoad.getString(0));
        cursorLoad.close();
        x=x+amt;
        updateBalance(name,x);
        Details.setBalance(name);
        Second.loadDena();
    }
    public void updateBalance(String name,int x){
        SQLiteDatabase sqLiteDatabase=helper.getWritableDatabase();
        String selectionD = helper.col2 + " LIKE ?";
        String[] selectionArgsDe = { name };
        sqLiteDatabase.delete(helper.TABLE_NAME_ONE, selectionD, selectionArgsDe);
        ContentValues cn=new ContentValues();
        cn.put(helper.col2,name);
        cn.put(helper.col8,x);
        long id=sqLiteDatabase.insert(MyHelper.TABLE_NAME_ONE,null,cn);
    }
    public void deleteName(String name){
        SQLiteDatabase sqLiteDatabase=helper.getWritableDatabase();
        String selectionD = helper.col2 + " LIKE ?";
        String[] selectionArgsDe = { name };
        sqLiteDatabase.delete(helper.TABLE_NAME_ONE, selectionD, selectionArgsDe);
        String selectionD2 = helper.col4 + " LIKE ?";
        sqLiteDatabase.delete(helper.TABLE_NAME_TWO, selectionD2, selectionArgsDe);
    }
    public List loadName(){
        String[] projection = {
                helper.col2,
        };
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor=db.query(helper.TABLE_NAME_ONE,projection,null,null,null,null,helper.col2+" ASC");
        List itemIds = new ArrayList<String>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            itemIds.add(name);
        }
        cursor.close();
        return itemIds;
    }
    //toload the transaction
    public List load(String name){
        String[] projection = {
                helper.col6,
                helper.col9,
                helper.col5
        };
        String selection = helper.col4 + " = ?";
        String[] selectionArgs = { name };

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor=db.query(helper.TABLE_NAME_TWO,projection,selection,selectionArgs,null,null,helper.col9+" DESC");
        List itemIds = new ArrayList<String>();
        while (cursor.moveToNext()) {
            String amount = cursor.getString(0);
            String date = cursor.getString(1);
            String id=cursor.getString(2);
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(amount).append("$").append(date).append("@").append(id);
            itemIds.add(stringBuilder.toString());
        }
        cursor.close();
        return  itemIds;
    }
    //to load the net balance of each individual
    public String loadBalance(String name){
        String[] projectionLoad = {
                helper.col8
        };
        String selectionLoad = helper.col2 + " = ?";
        String[] selectionArgsLoad = { name };

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursorLoad=db.query(helper.TABLE_NAME_ONE,projectionLoad,selectionLoad,selectionArgsLoad,null,null,null);
        cursorLoad.moveToFirst();
        int x=Integer.parseInt(cursorLoad.getString(0));
        cursorLoad.close();
        return String.valueOf(x);
    }
    public int denaLena(int ctr){
        Log.i("vvvvvvvv","inside adapter dena lena");
        Log.i("counter",String.valueOf(ctr));
        int x=0;
        int sum=0;
        String[] projection = {
                helper.col8
        };
        SQLiteDatabase sqLiteDatabase=helper.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.query(helper.TABLE_NAME_ONE,projection,null,null,null,null,null);
        if(cursor==null)
            Log.d("NNNNNNNNNNNNNNn","null");
        //cursor.moveToFirst();
        while(cursor.moveToNext()){
            String temp=cursor.getString(0);
            x=Integer.parseInt(temp);
            if(ctr==0) {            //dena k liye
                if(x<0)
                    sum=sum-x;
            }
            else if(ctr==1){        //lena kliye{
                if(x>0)
                    sum=sum+x;
            }

        }
        return sum;
    }
    static class MyHelper extends SQLiteOpenHelper{
        public static final String DATABASE_NAME="EXCHANGE.db";

        public static final String TABLE_NAME_ONE="NAMETABLE";
        public static final String col1="ID";
        public static final String col2="NAME";
        public static final String col8="NETAMOUNT";

        private static final String CREATE_TABLE_NAME="CREATE TABLE "+
                TABLE_NAME_ONE+"("+col1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+col2+
                " VARCHAR(255),"+col8+" INTEGER);";
        private static final String DROP_TABLE_NAME="DROP TABLE IF EXISTS "+TABLE_NAME_ONE;
        private Context context;

        public static final String TABLE_NAME_TWO="TRANSACTIONS";
        public static final String col3="ID";
        public static final String col4="NAME";
        public static final String col5="REASON";
        public static final String col6="AMOUNT";
        public static final String col7="TYPE";
        public static final String col9="DATE";

        private static final String CREATE_TABLE_REASON = "create table "
                + TABLE_NAME_TWO + " ("
                + col3 + " integer primary key autoincrement, "
                + col4 + " VARCHAR(255), "
                + col5 + " VARCHAR(255), "
                + col6 + " INTEGER,"
                + col7 + " VARCHAR(10),"
                + col9 + " VARCHAR(20), "
                + " FOREIGN KEY ("+col4+") REFERENCES "+TABLE_NAME_ONE+"("+col2+"));";
        private static final String DROP_TABLE_REASON="DROP TABLE IF EXISTS "+TABLE_NAME_TWO;

        public MyHelper(Context context){
            super(context,DATABASE_NAME,null,1);
            this.context=context;
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            try{
                db.execSQL(CREATE_TABLE_NAME);
                db.execSQL(CREATE_TABLE_REASON);
                }
            catch(android.database.SQLException e){
                Log.d("message",e.toString());
            }
        }

        public void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion){
            try{
                db.execSQL(DROP_TABLE_REASON);
                db.execSQL(DROP_TABLE_NAME);
                onCreate(db);
            }catch(android.database.SQLException e){

            }
        }
    }
}