package com.yuanchik.sentradefend.presentation.ui.settings

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.materialswitch.MaterialSwitch
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

    private lateinit var switchNotifications: MaterialSwitch
    private lateinit var switchAutoScan: MaterialSwitch
    private lateinit var switchAutoUpdate: MaterialSwitch
    private lateinit var tvTheme: TextView
    private lateinit var tvApiKeys: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding2 = FragmentSettingsBinding.inflate(inflater, container, false)

        (requireActivity().application as App).appComponent.inject(this)

//        val value = sharedPrefs.getBoolean("auto_check", false)
//        Toast.makeText(requireContext(), "AutoCheck = $value", Toast.LENGTH_SHORT).show()

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
            val editText = EditText(requireContext()).apply {
                hint = "Введите API ключ"
                setText(PreferencesHelper.getString(requireContext(), "api_key"))
            }

            AlertDialog.Builder(requireContext())
                .setTitle("API ключ")
                .setView(editText)
                .setPositiveButton("Сохранить") { _, _ ->
                    val apiKey = editText.text.toString()
                    PreferencesHelper.saveString(requireContext(), "api_key", apiKey)
                    Toast.makeText(requireContext(), "Ключ сохранён", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Отмена", null)
                .show()
        }

        binding.btnReset.setOnClickListener {
            switchNotifications.isChecked = false
            switchAutoScan.isChecked = false
            switchAutoUpdate.isChecked = false

            PreferencesHelper.saveBoolean(requireContext(), "notifications", false)
            PreferencesHelper.saveBoolean(requireContext(), "autoscan", false)
            PreferencesHelper.saveBoolean(requireContext(), "autoupdate", false)

            Toast.makeText(requireContext(), "Настройки сброшены", Toast.LENGTH_SHORT).show()
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