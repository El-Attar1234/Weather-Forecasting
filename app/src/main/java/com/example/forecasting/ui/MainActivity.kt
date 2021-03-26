package com.example.forecasting.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavHostController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.forecasting.NavGraphDirections
import com.example.forecasting.R
import com.example.forecasting.databinding.ActivityMainBinding
import com.example.forecasting.ui.favourite.MapsActivity
import com.example.forecasting.ui.weather_radar.RadarActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavHostController
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var binding:ActivityMainBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeUi()
        binding.fab.setOnClickListener {
            initializeDialog()
        }

    }


    private fun initializeDialog() {
        val inflater = LayoutInflater.from(this)
        val inflate_view: View = inflater.inflate(R.layout.statistics_graph, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(inflate_view)
        builder.setCancelable(true)
        builder.setTitle("     Weather Statistics")
        builder.setIcon(R.drawable.ic_mostly_cloudy)
        builder.create()
        builder.create().show()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.radarFragment -> {
                val intent = Intent(this, RadarActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.addAlarm -> {
                val detailsAction = NavGraphDirections.addNewAlarm(-1)
                navController.navigate(detailsAction)
                true
            }
            R.id.addFavourites -> {
                val intent = Intent(this, MapsActivity::class.java)
                intent.putExtra("case", "favourite")
                startActivity(intent)
                true
            }
            else -> {

                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun initializeUi() {
      binding.bottomNav.background = null
       binding.bottomNav.menu.getItem(2).isEnabled = false
        navHostFragment=
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.findNavController() as NavHostController
        binding.bottomNav.setupWithNavController(navController)
        setSupportActionBar(binding.toolbar)
        toolbar.setupWithNavController(navController)
    }


}