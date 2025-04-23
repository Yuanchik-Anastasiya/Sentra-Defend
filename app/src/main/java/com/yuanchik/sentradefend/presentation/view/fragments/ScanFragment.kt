package com.yuanchik.sentradefend.presentation.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.databinding.FragmentScanBinding
import com.yuanchik.sentradefend.presentation.ui.scan.ScanURLFragment
import com.yuanchik.sentradefend.utils.AnimationHelper


class ScanFragment : Fragment() {
    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)

        binding.scanUrl.setOnClickListener {

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ScanURLFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.scanApps.setOnClickListener {
            Toast.makeText(requireContext(), "Сканируем приложения", Toast.LENGTH_SHORT).show()
        }

        binding.scanFils.setOnClickListener {
            Toast.makeText(requireContext(), "Сканируем файлы", Toast.LENGTH_SHORT).show()
        }

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentScan,
            requireActivity(),
            1
        )
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}