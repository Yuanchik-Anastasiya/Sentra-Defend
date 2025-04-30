package com.yuanchik.sentradefend.presentation.ui.scan

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.databinding.FragmentScanURLBinding
import com.yuanchik.sentradefend.presentation.viewmodel.API
import com.yuanchik.sentradefend.presentation.viewmodel.VirusTotalService
import com.yuanchik.sentradefend.utils.AnimationHelper
import kotlinx.coroutines.launch


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
                lifecycleScope.launch {
                    try {
                        val response = VirusTotalService.api.scanUrl(
                            apiKey = API.KEY_VT,
                            url = url
                        )
                        val scanId = response.data.id
                        Log.d("API", "Scan ID: $scanId")

                        val bundle = Bundle().apply {
                            putString("scan_id", scanId)
                            putString("url", url)
                        }

                        val fragment = ScanResultFragment().apply {
                            arguments = bundle
                        }

                        parentFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit()

                    } catch (e: Exception) {
                        Toast.makeText(
                            requireContext(),
                            "Ошибка запроса: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Введите URL", Toast.LENGTH_SHORT).show()
            }
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
