package com.yuanchik.sentradefend.presentation.view.fragments

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.pm.ApplicationInfo
import android.graphics.Color
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.presentation.ui.scan.adapter.AppAdapter
import com.yuanchik.sentradefend.databinding.FragmentScanAppsBinding
import com.yuanchik.sentradefend.presentation.ui.scan.model.AppInfo
import com.yuanchik.sentradefend.presentation.ui.scan.ScanResultFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

        animateNeonStroke(binding.searchInputLayout)

        adapter = AppAdapter(appList) {

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
        lifecycleScope.launch(Dispatchers.IO) {
            val pm = requireContext().packageManager
            val packages = pm.getInstalledPackages(0)

            val tempList = mutableListOf<AppInfo>()

            for (packageInfo in packages) {
                val appInfo = packageInfo.applicationInfo ?: continue
                if ((appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0) continue

                val appName = pm.getApplicationLabel(appInfo).toString()
                val icon = pm.getApplicationIcon(appInfo)
                val apkPath = appInfo.sourceDir

                tempList.add(
                    AppInfo(
                        appName = appName,
                        packageName = packageInfo.packageName,
                        icon = icon,
                        apkPath = apkPath
                    )
                )
            }

            tempList.sortBy { it.appName.lowercase() }

            withContext(Dispatchers.Main) {
                appList.clear()
                appList.addAll(tempList)
                adapter.notifyDataSetChanged()
            }
        }
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

    private fun animateNeonStroke(textInputLayout: TextInputLayout) {
        val neonColors = listOf(
            Color.parseColor("#00FFFF"), // бирюзовый
            Color.parseColor("#0088FF"), // голубой
            Color.parseColor("#0000FF"), // синий
            Color.parseColor("#4B0082"), // индиго
            Color.parseColor("#0088FF"), // назад к голубому
            Color.parseColor("#00FFFF")  // и снова бирюзовый
        )

        val animator = ValueAnimator.ofFloat(0f, (neonColors.size - 1).toFloat()).apply {
            duration = 6000L
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animation ->
                val position = animation.animatedValue as Float
                val index = position.toInt()
                val fraction = position - index
                val startColor = neonColors[index % neonColors.size]
                val endColor = neonColors[(index + 1) % neonColors.size]
                val animatedColor = ArgbEvaluator().evaluate(fraction, startColor, endColor) as Int
                textInputLayout.boxStrokeColor = animatedColor
            }
        }
        animator.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}