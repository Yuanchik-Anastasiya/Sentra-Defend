package com.yuanchik.sentradefend.presentation.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.databinding.FragmentScanBinding
import com.yuanchik.sentradefend.utils.AnimationHelper


class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentScan,
            requireActivity(),
            1
        )
        return binding.root
    }
}