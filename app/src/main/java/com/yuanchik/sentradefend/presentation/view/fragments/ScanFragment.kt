package com.yuanchik.sentradefend.presentation.view.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.databinding.FragmentScanBinding
import com.yuanchik.sentradefend.presentation.ui.scan.ScanResultFragment
import com.yuanchik.sentradefend.presentation.ui.scan.ScanURLFragment
import com.yuanchik.sentradefend.presentation.viewmodel.API
import com.yuanchik.sentradefend.data.remote.VirusTotalService
import com.yuanchik.sentradefend.utils.AnimationHelper
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


@RequiresApi(Build.VERSION_CODES.O)
class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private val FILE_PICK_CODE = 1001

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
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ScanAppsFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.scanFils.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                type = "*/*"
                addCategory(Intent.CATEGORY_OPENABLE)
            }
            startActivityForResult(intent, FILE_PICK_CODE)
        }

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentScan,
            requireActivity(),
            1
        )

        return binding.root
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == FILE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return

            val fileName = getFileName(requireContext(), uri)

            lifecycleScope.launch {
                try {
                    val fileBytes = requireContext().contentResolver
                        .openInputStream(uri)?.readBytes()
                        ?: throw Exception("Не удалось прочитать файл")

                    val requestFile =
                        fileBytes.toRequestBody("application/octet-stream".toMediaTypeOrNull())
                    val filePart = MultipartBody.Part.createFormData("file", fileName, requestFile)

                    val response = VirusTotalService.api.scanFile(
                        apiKey = API.KEY_VT,
                        file = filePart
                    )

                    val scanId = response.data.id

                    val fragment = ScanResultFragment().apply {
                        arguments = bundleOf(
                            "scan_type" to "file",
                            "scan_id" to scanId,
                            "file_name" to fileName
                        )
                    }

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()

                } catch (e: Exception) {
                    Toast.makeText(
                        requireContext(),
                        "Ошибка сканирования: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun getFileName(context: Context, uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = context.contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    result = it.getString(it.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            }
        }
        if (result == null) {
            result = uri.path?.substringAfterLast('/')
        }
        return result ?: "Неизвестный файл"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}