package com.example.app_controle_financeiro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.app_controle_financeiro.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpBottomNavigation()

    }

    private fun setUpBottomNavigation() {
        val navView: BottomNavigationView = binding.bottomNavigationView
        val navController = findNavController(R.id.fragment)
        navView.setupWithNavController(navController)
    }
}