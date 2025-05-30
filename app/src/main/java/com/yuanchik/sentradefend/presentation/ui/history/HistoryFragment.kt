package com.yuanchik.sentradefend.presentation.ui.history

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.data.AppDatabase
import com.yuanchik.sentradefend.databinding.FragmentHistoryBinding
import com.yuanchik.sentradefend.presentation.viewmodel.HistoryViewModel
import com.yuanchik.sentradefend.repository.ScanHistoryRepository
import com.yuanchik.sentradefend.utils.AnimationHelper
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

        val dao = AppDatabase.getDatabase(requireContext()).scanResultDao()
        val repository = ScanHistoryRepository(dao)
        val factory = HistoryViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        adapter = ScanHistoryAdapter()
        binding.rvScanHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvScanHistory.adapter = adapter

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.scanResults.collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }

        binding.resetting.setOnClickListener {
            showClearHistoryDialog()
        }

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentHistory,
            requireActivity(),
            2
        )

        return binding.root
    }

    private fun showClearHistoryDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.сlearing_the_history))
            .setMessage(getString(R.string.question))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->
                viewModel.clearHistory()
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding1 = null
    }
}