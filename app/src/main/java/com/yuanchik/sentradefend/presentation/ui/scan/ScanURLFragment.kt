package com.yuanchik.sentradefend.presentation.ui.scan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.databinding.FragmentScanURLBinding
import com.yuanchik.sentradefend.utils.AnimationHelper


class ScanURLFragment : Fragment() {
    private var binding4: FragmentScanURLBinding? = null
    private val binding get() = binding4!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding4 = FragmentScanURLBinding.inflate(inflater, container, false)
        binding.btnScan.setOnClickListener {
            val url = binding.inputUrl.text.toString()
            if (url.isNotBlank()) {
                findNavController().navigate(R.id.action_to_result)
            } else {
                Toast.makeText(requireContext(), "Введите URL", Toast.LENGTH_SHORT).show()
            }
        }
binding.btnScan.setOnClickListener{
    parentFragmentManager.beginTransaction()
        .replace(R.id.fragment_container, ScanResultFragment())
        .addToBackStack(null)
        .commit()
}

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentScanUrl,
            requireActivity(),
            5
        )
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding4 = null
    }

}