package com.yuanchik.sentradefend.presentation.ui.scan

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.databinding.FragmentScanURLBinding
import com.yuanchik.sentradefend.presentation.viewmodel.API
import com.yuanchik.sentradefend.presentation.viewmodel.VirusTotalService
import com.yuanchik.sentradefend.utils.AnimationHelper
import kotlinx.coroutines.launch


@RequiresApi(Build.VERSION_CODES.O)
class ScanURLFragment : Fragment() {

    private var binding4: FragmentScanURLBinding? = null
    private val binding get() = binding4!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding4 = FragmentScanURLBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Запуск анимации появления фрагмента
        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentScanUrl,
            requireActivity(),
            5
        )

        binding.btnScan.setOnClickListener {
            val url = binding.inputUrl.text.toString().trim()

            if (url.isEmpty()) {
                Snackbar.make(binding.root, "Введите URL", Snackbar.LENGTH_LONG).show()
                return@setOnClickListener
            }

            scanUrlAndNavigate(url)
        }
    }

    private fun scanUrlAndNavigate(url: String) {
        binding.progressBar.visibility = View.VISIBLE

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val response = VirusTotalService.api.scanUrl(
                    apiKey = API.KEY_VT,
                    url = url
                )

                val scanId = response.data.id

                val bundle = Bundle().apply {
                    putString("scan_id", scanId)
                    putString("url", url)
                }

                parentFragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        ScanResultFragment().apply { arguments = bundle })
                    .addToBackStack(null)
                    .commit()

            } catch (e: Exception) {
                Snackbar.make(binding.root, "Ошибка запроса: ${e.message}", Snackbar.LENGTH_LONG)
                    .show()
            } finally {
                binding.progressBar.visibility = View.GONE
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding4 = null
    }
}
