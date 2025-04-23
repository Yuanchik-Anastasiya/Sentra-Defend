package com.yuanchik.sentradefend.presentation.ui.scan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.databinding.FragmentScanResultBinding
import com.yuanchik.sentradefend.utils.AnimationHelper


class ScanResultFragment : Fragment(R.layout.fragment_scan_result) {
    private var binding3: FragmentScanResultBinding? = null
    private val binding get() = binding3!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        // Здесь потом отобразится результат проверки

        binding3 = FragmentScanResultBinding.inflate(inflater, container, false)
        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentScanResult,
            requireActivity(),
            4
        )
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding3 = null
    }
}