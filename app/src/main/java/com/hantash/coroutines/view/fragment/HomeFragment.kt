package com.hantash.coroutines.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.hantash.coroutines.databinding.FragmentHomeBinding
import com.hantash.coroutines.model.ScreenDemoEnum
import com.hantash.coroutines.view.DemoAdapter
import com.hantash.coroutines.view.Listener


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var demoAdapter: DemoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        initUI()

        return binding.root
    }

    private fun initUI() {
        demoAdapter = DemoAdapter(ScreenDemoEnum.entries.toList(), object: Listener {
            override fun onScreenClicked(screenDemoEnum: ScreenDemoEnum) {
                showScreen(screenDemoEnum)
            }
        })

        binding.rvScreens.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = demoAdapter
            addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL))
        }
    }

    private fun showScreen(screenDemoEnum: ScreenDemoEnum) {
        when(screenDemoEnum) {
            ScreenDemoEnum.UI_THREAD_DEMO -> {}
            ScreenDemoEnum.BACKGROUND_THREAD_DEMO -> {}
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HomeFragment()
    }
}