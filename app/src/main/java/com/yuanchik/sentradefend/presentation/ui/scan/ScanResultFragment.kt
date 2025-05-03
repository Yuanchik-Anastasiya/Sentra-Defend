package com.yuanchik.sentradefend.presentation.ui.scan

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.databinding.FragmentScanResultBinding
import com.yuanchik.sentradefend.entity.ScanResultEntity
import com.yuanchik.sentradefend.presentation.viewmodel.API
import com.yuanchik.sentradefend.presentation.viewmodel.ScanResultViewModel
import com.yuanchik.sentradefend.presentation.viewmodel.VirusTotalService
import com.yuanchik.sentradefend.utils.AnimationHelper
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


class ScanResultFragment : Fragment(R.layout.fragment_scan_result) {
    private val viewModel: ScanResultViewModel by activityViewModels()

    private var binding3: FragmentScanResultBinding? = null
    private val binding get() = binding3!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding3 = FragmentScanResultBinding.inflate(inflater, container, false)

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentScanResult,
            requireActivity(),
            4
        )

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scanId = arguments?.getString("scan_id")
        val url = arguments?.getString("url")

        if (scanId == null || url == null) {
            binding.scanStatus.text = "Ошибка: нет данных"
            return
        }

        binding.scanStatus.text = "Проверка URL: $url"
        binding.scanDetails.text = "Ожидаем результат..."

        lifecycleScope.launch {
            try {
                val response = VirusTotalService.api.getScanResult(
                    apiKey = API.KEY_VT,
                    scanId = scanId
                )

                val stats = response.data.attributes.stats

                val summary = """
                    ✅ Безвреден: ${stats.harmless}
                    ❗ Вредоносен: ${stats.malicious}
                    ⚠️ Подозрительный: ${stats.suspicious}
                    ❓ Не определено: ${stats.undetected}
                """.trimIndent()

                binding.scanDetails.text = summary

                val resultText = when {
                    stats.malicious > 0 -> "⚠️ Опасный URL"
                    stats.suspicious > 0 -> "⚠️ Подозрительный URL"
                    else -> "✅ Безопасный URL"
                }

                binding.scanStatus.text = resultText

                
                val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                val currentDate = LocalDate.now()
                    .format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault()))

                val result = ScanResultEntity(
                    date = currentDate,
                    time = currentTime,
                    result = resultText
                )

                viewModel.insertScanResult(result)

            } catch (e: Exception) {
                binding.scanStatus.text = "Ошибка получения результата"
                binding.scanDetails.text = e.message
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding3 = null
    }
}