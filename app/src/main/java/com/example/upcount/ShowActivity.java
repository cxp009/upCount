package com.example.upcount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ShowActivity extends AppCompatActivity {

    private ListView mLvShow;
    //以下两行用于数据库声明
    private SQLiteDatabase mDbCount;
    private Cursor Count_cursor;
    ArrayList<UpList> upLists = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        mLvShow = findViewById(R.id.lv_show);
        //对存储于手机本地的记录进行读取
        mDbCount = openOrCreateDatabase("upCount.db", Context.MODE_PRIVATE, null);
        mDbCount.execSQL("CREATE TABLE IF NOT EXISTS count (_id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR, time VARCHAR, count VARCHAR)");
        Count_cursor = mDbCount.rawQuery("SELECT * FROM count WHERE _id >= ?", new String[]{"1"});
        while (Count_cursor.moveToNext()){
            String username = Count_cursor.getString(Count_cursor.getColumnIndex("username"));
            //if语句：使界面只展示目前登录用户的爬楼梯记录
            if (username.equals(LoginActivity.Username)){
                UpList upList = new UpList();
                upList.setUsername(username);
                upList.setTime(Count_cursor.getString(Count_cursor.getColumnIndex("time")));
                upList.setCount(Count_cursor.getString(Count_cursor.getColumnIndex("count")));
                upLists.add(upList);
            }
        }
        ListAdapter listAdapter = new ListAdapter(ShowActivity.this, upLists);
        mLvShow.setAdapter(listAdapter);
    }

    @Override
    protected void onDestroy() {
        //退出程序时销毁数据库连接
        super.onDestroy();
        Count_cursor.close();
        mDbCount.close();
    }
}
