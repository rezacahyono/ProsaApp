package com.rchyn.prosa.ui.fragments.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.rchyn.prosa.R
import com.rchyn.prosa.components.LoadingDialog
import com.rchyn.prosa.databinding.FragmentRegisterBinding
import com.rchyn.prosa.ui.activities.MainActivity
import com.rchyn.prosa.utils.UiText
import com.rchyn.prosa.utils.hideSoftKeyboard
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding as FragmentRegisterBinding

    private val registerViewModel: RegisterViewModel by viewModels()

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var act: MainActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        act = activity as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnBack.setOnClickListener {
                navigateToLogin()
            }
            tvLogin.setOnClickListener {
                navigateToLogin()
            }
            btnRegister.setOnClickListener {
                setupToRegister()
            }
        }
    }


    private fun setupToRegister() {
        val name = binding.edtFullName.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()


        var nameCorrect = true
        var emailCorrect = true
        var passwordCorrect = true

        if (name.isBlank()) {
            nameCorrect = false
            binding.layoutEdtFullName.apply {
                error = getString(
                    R.string.field_cant_be_empty,
                    getString(R.string.full_name)
                )
                isErrorEnabled = true
            }
        } else {
            binding.layoutEdtFullName.apply {
                error = null
                isErrorEnabled = false
            }
        }
        if (email.isBlank()) {
            emailCorrect = false
            binding.layoutEdtEmail.apply {
                error = getString(
                    R.string.field_cant_be_empty,
                    getString(R.string.email)
                )
                isErrorEnabled = true
            }
        } else {
            binding.layoutEdtEmail.apply {
                error = null
                isErrorEnabled = false
            }
        }
        if (password.isBlank()) {
            passwordCorrect = false
            binding.layoutEdtPassword.apply {
                error = getString(
                    R.string.field_cant_be_empty,
                    getString(R.string.password)
                )
                isErrorEnabled = true
            }
        } else {
            binding.layoutEdtPassword.apply {
                error = null
                isErrorEnabled = false
            }
        }

        if (nameCorrect && emailCorrect && passwordCorrect) {
            loadingDialog = LoadingDialog(requireContext())
            registerViewModel.register(name, email, password).observe(viewLifecycleOwner) { state ->
                when {
                    state.isSuccess -> {
                        loadingDialog.dismissLoading()
                        act.showSnackBar(getString(R.string.message_success_register))
                        navigateToLogin()
                    }
                    state.isLoading -> {
                        loadingDialog.startLoading()
                    }
                    state.isError -> {
                        loadingDialog.dismissLoading()
                        val msg = state.messageError?.let { uiText ->
                            when (uiText) {
                                is UiText.DynamicString ->
                                    uiText.value
                                is UiText.StringResource ->
                                    getString(uiText.id)
                            }
                        } ?: getString(R.string.message_failed_register)
                        act.showSnackBar(msg)
                    }
                }
            }
            requireActivity().currentFocus?.let {
                hideSoftKeyboard(requireContext(), it)
            }
        }
    }


    private fun navigateToLogin() {
        findNavController().popBackStack()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}