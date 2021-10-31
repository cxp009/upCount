package com.example.upcount;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {

    //声明控件
    private Button mBtnRecord, mBtnQuery;
    private TextView mTvUsername;
    private EditText mEtCount,mEtCost;
    //用于获取系统时间
    private Date date;
    //设置时间显示格式
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    //用于在存储时间并显示
    private String time;
    //数据库声明
    private SQLiteDatabase mDbCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定控件
        mBtnRecord = findViewById(R.id.btn_main_record);
        mBtnQuery = findViewById(R.id.btn_main_query);
        mEtCost = findViewById(R.id.et_main_cost);
        mEtCount = findViewById(R.id.et_main_count);
        mTvUsername = findViewById(R.id.tv_main_username);
        //初始赋值
        mTvUsername.setText(LoginActivity.Username);
        //记录账单
        mBtnRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取当前时间
                date = new Date(System.currentTimeMillis());
                time = sdf.format(date);
                Toast.makeText(getApplicationContext(), "Current Time:" + time, Toast.LENGTH_SHORT).show();
                //获取用户输入的金额
                Editable cost = mEtCost.getText();
                String count = mEtCount.getText().toString();
                //SQLite数据库处理
                mDbCount = openOrCreateDatabase("upCount.db",  Context.MODE_PRIVATE, null);
                mDbCount.execSQL("CREATE TABLE IF NOT EXISTS count (_id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR, time VARCHAR,cost VARCHAR, count VARCHAR)");
                mDbCount.execSQL("INSERT INTO count VALUES (NULL, ?, ?, ?)",new Object[]{LoginActivity.Username, time,cost, count});
                mDbCount.close();
            }
        });
        mBtnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //从数据库拉取此用户的所有账单数据
                Intent intent = new Intent(getApplicationContext(), ShowActivity.class);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "数据查询成功！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //用户点击返回键，则销毁当前Activity
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在销毁当前Activity前，先将LoginActivity销毁
        LoginActivity.loginActivity.finish();
        finish();
    }
}
