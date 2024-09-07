package com.hantash.coroutines.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hantash.coroutines.databinding.ActivityMainBinding
import com.hantash.coroutines.view.fragment.demonstration.BasicCoroutineDemoFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            binding.frameContent.id,
            BasicCoroutineDemoFragment.newInstance(),
        )
        transaction.commit()
    }
}