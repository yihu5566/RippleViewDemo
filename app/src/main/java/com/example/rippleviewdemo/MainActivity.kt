package com.example.rippleviewdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn.setOnClickListener {
            Log.d("MainActivity","开始")
            for (i in 0..10 step 2)  Log.d("MainActivity",i.toString()) // 打印结果为: "13"
        }


    }
}
