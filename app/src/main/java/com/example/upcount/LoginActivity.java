package com.example.upcount;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText mEtUsername, mEtPassword;
    private Button mBtnLogin, mBtnRegister;
    //设置变量用于存储用户信息
    public static String Username = "";
    private String Password;
    public static Activity loginActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginActivity = LoginActivity.this;
        //将变量指向对应控件
        mEtUsername = findViewById(R.id.et_login_username);
        mEtPassword = findViewById(R.id.et_login_password);
        mBtnLogin = findViewById(R.id.btn_login_login);
        mBtnRegister = findViewById(R.id.btn_login_register);
        //点击注册，触发点击事件
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户输入的账号及密码，传送到服务器进行判断
                Username = mEtUsername.getText().toString();
                Password = mEtPassword.getText().toString();
                //确保用户输入不为空值
                if (Username.equals("")){
                    Toast.makeText(getApplicationContext(), "用户名不能为空！", Toast.LENGTH_SHORT).show();
                }else if(Password.equals("")){
                    Toast.makeText(getApplicationContext(), "密码不能为空！", Toast.LENGTH_SHORT).show();
                }else {
                    if(!queryUser(LoginActivity.Username).equals("")){
                        Toast.makeText(getApplicationContext(), "该用户已存在！", Toast.LENGTH_SHORT).show();
                    }else{
                        createUser(LoginActivity.Username, Password);
                        Toast.makeText(getApplicationContext(), "注册成功！", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
        //点击登录，触发点击事件
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取用户输入的账号及密码，传送到服务器进行判断
                Username = mEtUsername.getText().toString();
                Password = mEtPassword.getText().toString();
                //确保用户输入不为空
                if (Username.equals("")){
                    Toast.makeText(getApplicationContext(), "用户名不能为空！", Toast.LENGTH_SHORT).show();
                }else if(Password.equals("")){
                    Toast.makeText(getApplicationContext(), "密码不能为空！", Toast.LENGTH_SHORT).show();
                }else {
                    //调用用户信息查询方法
                    String rightPassword = queryUser(LoginActivity.Username);
                    if(rightPassword.equals("")){
                        Toast.makeText(getApplicationContext(), "该用户不存在，请注册！", Toast.LENGTH_SHORT).show();
                    }else{
                        if (Password.equals(rightPassword)){
                            Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(getApplicationContext(), "密码错误！", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    String queryUser(String Username){
        //查询结果
        String res = "";
        //数据库声明
        SQLiteDatabase mDbUser;
        Cursor Count_cursor;
        //对存储于手机本地的记录进行读取
        mDbUser = openOrCreateDatabase("upCount.db", Context.MODE_PRIVATE, null);
        mDbUser.execSQL("CREATE TABLE IF NOT EXISTS user (_id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR, password VARCHAR)");
        Count_cursor = mDbUser.rawQuery("SELECT * FROM user WHERE _id >= ?", new String[]{"1"});
        //若查询到当前用户，则退出
        while (Count_cursor.moveToNext()){
            String username = Count_cursor.getString(Count_cursor.getColumnIndex("username"));
            if (username.equals(Username)){
                res = Count_cursor.getString(Count_cursor.getColumnIndex("password"));
            }
        }
        //关闭数据库连接
        Count_cursor.close();
        mDbUser.close();
        return res;
    }

    void createUser(String Username, String Password){
        //数据库声明
        SQLiteDatabase mDbUser;
        //SQLite数据库处理
        mDbUser = openOrCreateDatabase("upCount.db",  Context.MODE_PRIVATE, null);
        mDbUser.execSQL("CREATE TABLE IF NOT EXISTS user (_id INTEGER PRIMARY KEY AUTOINCREMENT, username VARCHAR, password VARCHAR)");
        mDbUser.execSQL("INSERT INTO user VALUES (NULL, ?, ?)",new Object[]{Username, Password});
        //关闭数据库连接
        mDbUser.close();
    }
}
