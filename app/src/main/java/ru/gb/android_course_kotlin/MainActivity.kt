package ru.gb.android_course_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.gb.android_course_kotlin.databinding.ActivityMainBinding
import ru.gb.android_course_kotlin.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }
}