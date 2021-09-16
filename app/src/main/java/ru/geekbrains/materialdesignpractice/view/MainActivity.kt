package ru.geekbrains.materialdesignpractice.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.geekbrains.materialdesignpractice.R
import ru.geekbrains.materialdesignpractice.view.picture.PODFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PODFragment.newInstance())
                .commit()
        }
    }
}