package com.yuanchik.sentradefend.presentation.ui.scan

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.databinding.FragmentScanResultBinding
import com.yuanchik.sentradefend.entity.ScanResultEntity
import com.yuanchik.sentradefend.presentation.viewmodel.API
import com.yuanchik.sentradefend.presentation.viewmodel.ScanResultViewModel
import com.yuanchik.sentradefend.data.remote.VirusTotalService
import com.yuanchik.sentradefend.presentation.ui.MainActivity
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
                    binding.scanStatus.text = getString(R.string.data_error)
                    return
                }

                binding.scanStatus.text = getString(R.string.check, url)
                binding.scanDetails.text = getString(R.string.expectation)

                lifecycleScope.launch {
                    getScanResultWithPolling(scanId, url)
                }
            }

            "file" -> {
                val scanId = arguments?.getString("scan_id")
                val fileName = arguments?.getString("file_name")

                if (scanId == null || fileName == null) {
                    binding.scanStatus.text = getString(R.string.file_error)
                    return
                }

                binding.scanStatus.text = getString(R.string.checking_the_file, fileName)
                binding.scanDetails.text = getString(R.string.expectation)

                lifecycleScope.launch {
                    getScanResultWithPolling(scanId, fileName)
                }
            }

            "package" -> {
                val packageName = arguments?.getString("package_name")
                val appName = arguments?.getString("app_name")

                if (packageName == null || appName == null) {
                    binding.scanStatus.text = getString(R.string.package_error)
                    return
                }

                binding.scanStatus.text = getString(R.string.checking_the_application, appName)
                binding.scanDetails.text = getString(R.string.analysis, packageName)

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

                val locale = Locale.getDefault().language
                val summary = if (locale == "en") {
                    """
                        ✅ Harmless: ${stats.harmless}
                        ❗ Malicious: ${stats.malicious}
                        ⚠️ Suspicious: ${stats.suspicious}
                        ❓ Not defined: ${stats.undetected}
                    """.trimIndent()
                } else {
                    """
                        ✅ Безвреден: ${stats.harmless}
                        ❗ Вредоносен: ${stats.malicious}
                        ⚠️ Подозрительный: ${stats.suspicious}
                        ❓ Не определено: ${stats.undetected}
                    """.trimIndent()
                }

                _binding?.scanDetails?.text = summary

                val resultText = when {
                    stats.malicious > 0 -> getString(R.string.dangerous)
                    stats.suspicious > 0 -> getString(R.string.suspicious)
                    else -> getString(R.string.safery)
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

                // 🔔 Показываем нотификацию
                val isSafe = stats.malicious == 0 && stats.suspicious == 0
                showScanNotification(isSafe, displayName)

                return

            } catch (e: Exception) {
                _binding?.scanStatus?.text = getString(R.string.result_error)
                _binding?.scanDetails?.text = e.message ?: getString(R.string.unknown_error)
                return
            } finally {
                _binding?.progressBar?.visibility = View.GONE
            }
        }

        _binding?.scanStatus?.text = getString(R.string.there_is_no_response)
        _binding?.scanDetails?.text = getString(R.string.try_again_later)
        _binding?.progressBar?.visibility = View.GONE
    }

    private suspend fun scanPackage(packageName: String, appName: String) {
        _binding?.progressBar?.visibility = View.VISIBLE

        try {
            val pm = requireContext().packageManager
            val appInfo = pm.getApplicationInfo(packageName, 0)
            val apkFile = File(appInfo.sourceDir)
            val apkBytes = apkFile.readBytes()

            val requestFile =
                apkBytes.toRequestBody("application/vnd.android.package-archive".toMediaTypeOrNull())
            val apkPart = MultipartBody.Part.createFormData("file", "$appName.apk", requestFile)

            val response = VirusTotalService.api.scanFile(
                apiKey = API.KEY_VT,
                file = apkPart
            )

            val scanId = response.data.id

            getScanResultWithPolling(scanId, appName)

        } catch (e: Exception) {
            _binding?.scanStatus?.text = getString(R.string.scan_error)
            _binding?.scanDetails?.text = e.message ?: getString(R.string.unknown_error)
        } finally {
            _binding?.progressBar?.visibility = View.GONE
        }
    }

    // 🔔 Метод отправки уведомлений
    private fun showScanNotification(isSafe: Boolean, displayName: String) {
        val context = requireContext()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            putExtra("open_history", true) // <- добавляем флаг
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val builder = NotificationCompat.Builder(context, "scan_result_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(if (isSafe) "✅ Безопасно!" else "⚠️ Угроза!")
            .setContentText("$displayName — ${if (isSafe) "Угроз не обнаружено" else "Обнаружены угрозы"}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        notificationManager.notify(1001, builder.build())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}