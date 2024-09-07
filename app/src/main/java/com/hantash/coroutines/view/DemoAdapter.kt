package com.hantash.coroutines.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hantash.coroutines.databinding.LayoutScreenDemoBinding
import com.hantash.coroutines.model.ScreenDemoEnum

class DemoAdapter(
    var data: List<ScreenDemoEnum>,
    var listener: Listener
) : RecyclerView.Adapter<DemoAdapter.DemoViewHolder>() {
    private var context: Context? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DemoViewHolder {
        if (context == null) context = parent.context

        val binding = LayoutScreenDemoBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )

        return DemoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DemoViewHolder, position: Int) {
        holder.binding.tvName.text = data[position].description
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class DemoViewHolder(
        val binding: LayoutScreenDemoBinding
    ): RecyclerView.ViewHolder(
        binding.root
    )
}
interface Listener {
    fun onScreenClicked(screenDemoEnum: ScreenDemoEnum)
}