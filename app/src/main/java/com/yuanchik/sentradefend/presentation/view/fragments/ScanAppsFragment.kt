package com.yuanchik.sentradefend.presentation.view.fragments

import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.presentation.ui.scan.adapter.AppAdapter
import com.yuanchik.sentradefend.databinding.FragmentScanAppsBinding
import com.yuanchik.sentradefend.presentation.ui.scan.model.AppInfo
import com.yuanchik.sentradefend.presentation.ui.scan.ScanResultFragment

@RequiresApi(Build.VERSION_CODES.O)
class ScanAppsFragment : Fragment() {

    private var _binding: FragmentScanAppsBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AppAdapter
    private val appList = mutableListOf<AppInfo>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentScanAppsBinding.inflate(inflater, container, false)

        adapter = AppAdapter(appList) {
            // Колбэк при изменении выбранных элементов (можно менять состояние кнопки)
        }

        binding.appsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.appsRecyclerView.adapter = adapter

        binding.searchEditText.addTextChangedListener { editable ->
            adapter.filter.filter(editable.toString())
        }

        binding.scanSelectedAppsButton.setOnClickListener {
            scanSelectedApps()
        }

        loadInstalledApps()

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadInstalledApps() {
        val pm = requireContext().packageManager
        val packages = pm.getInstalledPackages(0)

        appList.clear()
        for (packageInfo in packages) {
            val appInfo = packageInfo.applicationInfo ?: continue

            // Пропускаем системные приложения
            if ((appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0) continue

            val appName = pm.getApplicationLabel(appInfo).toString()
            val icon = pm.getApplicationIcon(appInfo)
            val apkPath = appInfo.sourceDir

            appList.add(
                AppInfo(
                    appName = appName,
                    packageName = packageInfo.packageName,
                    icon = icon,
                    apkPath = apkPath
                )
            )
        }

        appList.sortBy { it.appName.lowercase() }
        adapter.notifyDataSetChanged()
    }

    private fun scanSelectedApps() {
        val selectedApps = adapter.getFilteredList().filter { it.isSelected }

        if (selectedApps.isEmpty()) {
            Toast.makeText(requireContext(), getString(R.string.choice), Toast.LENGTH_SHORT).show()
            return
        }

        val firstApp = selectedApps.first()

        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, ScanResultFragment().apply {
                arguments = bundleOf(
                    "scan_type" to "package",
                    "package_name" to firstApp.packageName,
                    "app_name" to firstApp.appName
                )
            })
            .addToBackStack(null)
            .commit()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}