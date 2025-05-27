package com.yuanchik.sentradefend.presentation.ui.settings

import android.app.AlertDialog
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.materialswitch.MaterialSwitch
import com.yuanchik.sentradefend.databinding.FragmentSettingsBinding
import com.yuanchik.sentradefend.App
import com.yuanchik.sentradefend.PreferencesManager
import com.yuanchik.sentradefend.R
import com.yuanchik.sentradefend.enum.AppTheme
import com.yuanchik.sentradefend.utils.AnimationHelper
import com.yuanchik.sentradefend.utils.PreferencesHelper
import javax.inject.Inject


class SettingsFragment : Fragment() {

    private var binding2: FragmentSettingsBinding? = null
    private val binding get() = binding2!!

    @Inject
    lateinit var preferencesManager: PreferencesManager

    private lateinit var tvTheme: TextView
    private lateinit var tvApiKeys: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding2 = FragmentSettingsBinding.inflate(inflater, container, false)

        (requireActivity().application as App).appComponent.inject(this)

        tvTheme = binding.tvTheme
        tvApiKeys = binding.tvApiKeys


        // Отображение текущей темы
        updateThemeText(preferencesManager.getSavedTheme())

        tvTheme.setOnClickListener {
            val options = arrayOf(getString(R.string.light), getString(R.string.dark),
                getString(R.string.system))
            val currentTheme = preferencesManager.getSavedTheme()

            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.choosing_a_theme))
                .setSingleChoiceItems(options, currentTheme.ordinal) { dialog, which ->
                    val selectedTheme = AppTheme.fromOrdinal(which)
                    preferencesManager.saveTheme(selectedTheme)
                    AppCompatDelegate.setDefaultNightMode(selectedTheme.mode)
                    updateThemeText(selectedTheme)
                    dialog.dismiss()
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }

        // API key диалог
        tvApiKeys.setOnClickListener {
            val editText = EditText(requireContext()).apply {
                hint = context.getString(R.string.entering_the_key)
                setText(PreferencesHelper.getString(requireContext(), "api_key"))
            }

            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.api_key))
                .setView(editText)
                .setPositiveButton(getString(R.string.save)) { _, _ ->
                    val apiKey = editText.text.toString()
                    PreferencesHelper.saveString(requireContext(), "api_key", apiKey)
                    Toast.makeText(requireContext(),
                        getString(R.string.key_saved), Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(getString(R.string.cancel), null)
                .show()
        }

        binding.btnReset.setOnClickListener {
            val defaultTheme = AppTheme.SYSTEM
            preferencesManager.saveTheme(defaultTheme)
            AppCompatDelegate.setDefaultNightMode(defaultTheme.mode)
            updateThemeText(defaultTheme)


            PreferencesHelper.saveString(requireContext(), "api_key", "")

            Toast.makeText(requireContext(), getString(R.string.settings_reset), Toast.LENGTH_SHORT).show()
        }


        AnimationHelper.performFragmentCircularRevealAnimation(
            binding.fragmentSettings,
            requireActivity(),
            3
        )

        return binding.root
    }

    private fun updateThemeText(theme: AppTheme) {
        binding.tvTheme.text = when (theme) {
            AppTheme.LIGHT -> getString(R.string.theme_light)
            AppTheme.DARK -> getString(R.string.theme_dark)
            AppTheme.SYSTEM -> getString(R.string.subject_system)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding2 = null
    }
}