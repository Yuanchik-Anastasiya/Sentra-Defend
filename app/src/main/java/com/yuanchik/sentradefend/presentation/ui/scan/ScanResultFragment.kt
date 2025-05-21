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
import com.yuanchik.sentradefend.data.remote.VirusTotalService
import com.yuanchik.sentradefend.utils.AnimationHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@RequiresApi(Build.VERSION_CODES.O)
class ScanResultFragment : Fragment(R.layout.fragment_scan_result) {

    private val viewModel: ScanResultViewModel by activityViewModels()

    private var _binding: FragmentScanResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentScanResultBinding.inflate(inflater, container, false)

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentScanResult,
            requireActivity(),
            4
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val scanType = arguments?.getString("scan_type") ?: "url"

        when (scanType) {
            "url" -> {
                val scanId = arguments?.getString("scan_id")
                val url = arguments?.getString("url")

                if (scanId == null || url == null) {
                    binding.scanStatus.text = "Ошибка: нет данных"
                    return
                }

                binding.scanStatus.text = "Проверка URL: $url"
                binding.scanDetails.text = "Ожидание результата..."

                lifecycleScope.launch {
                    getScanResultWithPolling(scanId, url)
                }
            }

            "file" -> {
                val scanId = arguments?.getString("scan_id")
                val fileName = arguments?.getString("file_name")

                if (scanId == null || fileName == null) {
                    binding.scanStatus.text = "Ошибка: нет данных файла"
                    return
                }

                binding.scanStatus.text = "Проверка файла: $fileName"
                binding.scanDetails.text = "Ожидание результата..."

                lifecycleScope.launch {
                    getScanResultWithPolling(scanId, fileName)
                }
            }

            "package" -> {
                val packageName = arguments?.getString("package_name")
                val appName = arguments?.getString("app_name")

                if (packageName == null || appName == null) {
                    binding.scanStatus.text = "Ошибка: нет данных пакета"
                    return
                }

                binding.scanStatus.text = "Проверка приложения: $appName"
                binding.scanDetails.text = "Анализ по packageName: $packageName"

                lifecycleScope.launch {
                    scanPackage(packageName, appName)
                }
            }
        }
    }

    /**
     * Повторно опрашивает API, пока не появятся реальные результаты
     */
    private suspend fun getScanResultWithPolling(scanId: String, displayName: String) {
        _binding?.progressBar?.visibility = View.VISIBLE

        var attempt = 0
        val maxAttempts = 5
        val delayBetweenAttempts = 4000L

        while (attempt < maxAttempts) {
            try {
                val response = VirusTotalService.api.getScanResult(
                    apiKey = API.KEY_VT,
                    scanId = scanId
                )

                val stats = response.data.attributes.stats

                if (stats.harmless + stats.malicious + stats.suspicious + stats.undetected == 0) {
                    attempt++
                    delay(delayBetweenAttempts)
                    continue
                }

                val summary = """
                ✅ Безвреден: ${stats.harmless}
                ❗ Вредоносен: ${stats.malicious}
                ⚠️ Подозрительный: ${stats.suspicious}
                ❓ Не определено: ${stats.undetected}
            """.trimIndent()

                _binding?.scanDetails?.text = summary

                val resultText = when {
                    stats.malicious > 0 -> "⚠️ Опасный"
                    stats.suspicious > 0 -> "⚠️ Подозрительный"
                    else -> "✅ Безопасный"
                }

                _binding?.scanStatus?.text = "$resultText: $displayName"

                val currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
                val currentDate = LocalDate.now().format(
                    DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())
                )

                val result = ScanResultEntity(
                    content = displayName,
                    date = currentDate,
                    time = currentTime,
                    result = resultText
                )

                viewModel.insertScanResult(result)
                return

            } catch (e: Exception) {
                _binding?.scanStatus?.text = "Ошибка получения результата"
                _binding?.scanDetails?.text = e.message ?: "Неизвестная ошибка"
                return
            } finally {
                _binding?.progressBar?.visibility = View.GONE
            }
        }

        _binding?.scanStatus?.text = "⏳ Ответ не получен"
        _binding?.scanDetails?.text = "Попробуйте повторно позже"
        _binding?.progressBar?.visibility = View.GONE
    }

    private suspend fun scanPackage(packageName: String, appName: String) {
        _binding?.progressBar?.visibility = View.VISIBLE

        try {
            val pm = requireContext().packageManager
            val appInfo = pm.getApplicationInfo(packageName, 0)
            val apkFile = File(appInfo.sourceDir)
            val apkBytes = apkFile.readBytes()

            val requestFile = apkBytes.toRequestBody("application/vnd.android.package-archive".toMediaTypeOrNull())
            val apkPart = MultipartBody.Part.createFormData("file", "$appName.apk", requestFile)

            val response = VirusTotalService.api.scanFile(
                apiKey = API.KEY_VT,
                file = apkPart
            )

            val scanId = response.data.id

            // Далее — та же логика, как в других типах сканирования:
            getScanResultWithPolling(scanId, appName)

        } catch (e: Exception) {
            _binding?.scanStatus?.text = "Ошибка сканирования"
            _binding?.scanDetails?.text = e.message ?: "Неизвестная ошибка"
        } finally {
            _binding?.progressBar?.visibility = View.GONE
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}