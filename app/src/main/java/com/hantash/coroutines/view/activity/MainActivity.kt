package com.hantash.coroutines.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hantash.coroutines.databinding.ActivityMainBinding
import com.hantash.coroutines.view.fragment.exercise.exercise6.Exercise6Fragment
import com.hantash.coroutines.view.fragment.solution.exercise5.Exercise5SolutionFragment
import com.hantash.coroutines.view.fragment.solution.exercise6.Exercise6SolutionFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(
            binding.frameContent.id,
            Exercise6SolutionFragment.newInstance(),
        )
        transaction.commit()
    }
}