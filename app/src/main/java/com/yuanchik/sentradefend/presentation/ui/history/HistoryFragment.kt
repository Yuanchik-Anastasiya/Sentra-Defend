package com.yuanchik.sentradefend.presentation.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.data.ScanResult
import com.yuanchik.sentradefend.databinding.FragmentHistoryBinding
import com.yuanchik.sentradefend.utils.AnimationHelper

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private var binding1: FragmentHistoryBinding? = null
    private val binding get() = binding1!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ScanHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding1 = FragmentHistoryBinding.inflate(inflater, container, false)


        recyclerView = binding.rvScanHistory

        val dummyData = listOf(
            ScanResult("2 Jan 2024", "14:20", "5 threats"),
            ScanResult("2 Jan 2024", "10:12", "No threats"),
            ScanResult("1 Jan 2024", "18:43", "No threats"),
        )

        adapter = ScanHistoryAdapter(dummyData)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentHistory,
            requireActivity(),
            2
        )
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding1 = null
    }
}