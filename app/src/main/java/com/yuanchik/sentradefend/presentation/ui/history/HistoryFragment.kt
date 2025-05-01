package com.yuanchik.sentradefend.presentation.ui.history

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.data.AppDatabase
import com.yuanchik.sentradefend.databinding.FragmentHistoryBinding
import com.yuanchik.sentradefend.entity.ScanResultEntity
import com.yuanchik.sentradefend.presentation.viewmodel.HistoryViewModel
import com.yuanchik.sentradefend.presentation.viewmodel.HistoryViewModelFactory
import com.yuanchik.sentradefend.repository.ScanHistoryRepository
import com.yuanchik.sentradefend.utils.AnimationHelper

class HistoryFragment : Fragment(R.layout.fragment_history) {
    private var binding1: FragmentHistoryBinding? = null
    private val binding get() = binding1!!

    private lateinit var adapter: ScanHistoryAdapter
    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding1 = FragmentHistoryBinding.inflate(inflater, container, false)

        // Подключение DAO и репозитория
        val dao = AppDatabase.getDatabase(requireContext()).scanResultDao()
        val repository = ScanHistoryRepository(dao)
        val factory = HistoryViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        adapter = ScanHistoryAdapter(mutableListOf<ScanResultEntity>())

        binding.rvScanHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvScanHistory.adapter = adapter

        // Подписка на Flow
        lifecycleScope.launchWhenStarted {
            viewModel.scanResults.collect { list ->
                adapter.updateList(list)
            }
        }

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
