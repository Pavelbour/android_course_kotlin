package ru.gb.android_course_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import ru.gb.android_course_kotlin.databinding.ActivityMainBinding
import ru.gb.android_course_kotlin.ui.contacts.ContactsFragment
import ru.gb.android_course_kotlin.ui.history.HistoryFragment
import ru.gb.android_course_kotlin.ui.main.MainFragment
import ru.gb.android_course_kotlin.ui.maps.MapsFragment
import ru.gb.android_course_kotlin.ui.maps.SearchFragment

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.main_menu_menu_history -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .add(R.id.container, HistoryFragment.newInstance())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            R.id.main_menu_contacts -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .add(R.id.container, ContactsFragment.newInstance())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            R.id.main_menu_map -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .add(R.id.container, SearchFragment.newInstance())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}