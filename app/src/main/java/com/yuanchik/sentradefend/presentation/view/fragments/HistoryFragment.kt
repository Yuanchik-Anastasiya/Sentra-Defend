package com.yuanchik.sentradefend.presentation.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.databinding.FragmentHistoryBinding
import com.yuanchik.sentradefend.utils.AnimationHelper

class HistoryFragment : Fragment() {
    private var binding1: FragmentHistoryBinding? = null
    private val binding get() = binding1!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding1 = FragmentHistoryBinding.inflate(inflater, container, false)

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentHistory,
            requireActivity(),
            2
        )
        return binding.root
    }
}