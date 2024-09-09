package com.hantash.coroutines.view.fragment.demonstration.viewmodel

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.hantash.coroutines.R
import com.hantash.coroutines.databinding.FragmentViewModelDemoBinding
import com.hantash.coroutines.model.ThreadInfoLogger.logThreadInfo
import com.techyourchance.coroutines.demonstrations.viewmodel.MyViewModel
import com.techyourchance.coroutines.demonstrations.viewmodel.MyViewModelFactory

class ViewModelDemoFragment : Fragment() {
    private lateinit var binding: FragmentViewModelDemoBinding
    private lateinit var myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myViewModel = ViewModelProvider(this, MyViewModelFactory()).get(MyViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentViewModelDemoBinding.inflate(layoutInflater, container, false)

        binding.btnTrackTime.setOnClickListener {
            logThreadInfo("button callback")
            myViewModel.toggleTrackElapsedTime()
        }

        myViewModel.elapsedTime.observe(viewLifecycleOwner, Observer { elapsedTime ->
            binding.tvElapsedTime.text = elapsedTime.toString()
        })

        myViewModel.isTrackingTime.observe(viewLifecycleOwner, Observer { isTrackingTime ->
            binding.btnTrackTime.text = if (isTrackingTime) { getString(R.string.tracking_stop) } else { getString(R.string.tracking_start) }
        })

        return binding.root
    }

    override fun onStop() {
        logThreadInfo("onStop()")
        super.onStop()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ViewModelDemoFragment()
    }
}