package com.yuanchik.sentradefend.presentation.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.databinding.FragmentSettingsBinding
import com.yuanchik.sentradefend.utils.AnimationHelper


class SettingsFragment : Fragment() {
    private var binding2: FragmentSettingsBinding? = null
    private val binding get() = binding2!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding2 = FragmentSettingsBinding.inflate(inflater, container, false)

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentSettings,
            requireActivity(),
            3
        )
        return binding.root
    }
}