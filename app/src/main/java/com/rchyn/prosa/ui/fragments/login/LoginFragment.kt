package com.rchyn.prosa.ui.fragments.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.rchyn.prosa.R
import com.rchyn.prosa.components.LoadingDialog
import com.rchyn.prosa.databinding.FragmentLoginBinding
import com.rchyn.prosa.ui.activities.MainActivity
import com.rchyn.prosa.utils.UiText
import com.rchyn.prosa.utils.hideSoftKeyboard
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding as FragmentLoginBinding

    private val loginViewModel: LoginViewModel by activityViewModels()
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
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            setupToLogin()
        }

        binding.tvRegister.setOnClickListener {
            navigateToRegister()
        }

    }

    private fun setupToLogin() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()

        var emailCorrect = true
        var passwordCorrect = true

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

        if (emailCorrect && passwordCorrect) {
            loadingDialog = LoadingDialog(requireContext())
            loginViewModel.login(email, password).observe(viewLifecycleOwner) { state ->
                when {
                    state.isSuccess -> {
                        loadingDialog.dismissLoading()
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
                        } ?: ""
                        act.showSnackBar(msg)

                    }
                }
            }
            requireActivity().currentFocus?.let {
                hideSoftKeyboard(requireContext(), it)
            }
        }
    }

    private fun navigateToRegister() {
        val direction = LoginFragmentDirections.actionLoginNavToRegisterNav()
        findNavController().navigate(direction)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}