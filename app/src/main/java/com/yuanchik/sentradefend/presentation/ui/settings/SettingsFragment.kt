package com.yuanchik.sentradefend.presentation.ui.settings

import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.yuanchik.sentradefend.databinding.FragmentSettingsBinding
import com.yuanchik.sentradefend.presentation.viewmodel.App
import com.yuanchik.sentradefend.utils.AnimationHelper
import com.yuanchik.sentradefend.utils.PreferencesHelper
import javax.inject.Inject


class SettingsFragment : Fragment() {
    private var binding2: FragmentSettingsBinding? = null
    private val binding get() = binding2!!

    @Inject
    lateinit var sharedPrefs: SharedPreferences

    private lateinit var switchNotifications: Switch
    private lateinit var switchAutoScan: Switch
    private lateinit var switchAutoUpdate: Switch
    private lateinit var tvTheme: TextView
    private lateinit var tvApiKeys: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding2 = FragmentSettingsBinding.inflate(inflater, container, false)

        (requireActivity().application as App).appComponent.inject(this)

        val value = sharedPrefs.getBoolean("auto_check", false)
        Toast.makeText(requireContext(), "AutoCheck = $value", Toast.LENGTH_SHORT).show()

        switchNotifications = binding.switchNotifications
        switchAutoScan = binding.switchScheduledScan
        switchAutoUpdate = binding.switchAutoUpdate
        tvTheme = binding.tvTheme
        tvApiKeys = binding.tvApiKeys

        switchNotifications.isChecked = PreferencesHelper.getBoolean(requireContext(), "notifications")
        switchAutoScan.isChecked = PreferencesHelper.getBoolean(requireContext(), "autoscan")
        switchAutoUpdate.isChecked = PreferencesHelper.getBoolean(requireContext(), "autoupdate")


        switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            PreferencesHelper.saveBoolean(requireContext(), "notifications", isChecked)
        }

        switchAutoScan.setOnCheckedChangeListener { _, isChecked ->
            PreferencesHelper.saveBoolean(requireContext(), "autoscan", isChecked)
        }

        switchAutoUpdate.setOnCheckedChangeListener { _, isChecked ->
            PreferencesHelper.saveBoolean(requireContext(), "autoupdate", isChecked)
        }

        tvTheme.setOnClickListener {
            Toast.makeText(requireContext(), "Выбранные настройки темы", Toast.LENGTH_SHORT).show()
            // Позже будет диалог выбора темы
        }

        tvApiKeys.setOnClickListener {
            Toast.makeText(requireContext(), "Задействованные настройки ключа API", Toast.LENGTH_SHORT).show()
            // Позже будет отдельный экран или диалог
        }

        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentSettings,
            requireActivity(),
            3
        )
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding2 = null
    }
}