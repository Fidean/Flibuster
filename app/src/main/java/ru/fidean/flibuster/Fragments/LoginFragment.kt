package ru.fidean.flibuster.Fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.login_fragment.*
import kotlinx.android.synthetic.main.search_fragment.*
import ru.fidean.flibuster.ViewModels.LoginViewModel
import ru.fidean.flibuster.R
import ru.fidean.flibuster.ViewModels.LoginState

private const val TAG = "LoginFragmentTAG"

class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        loginButton.setOnClickListener {
            viewModel.login(loginEditText.text.toString(),passwordEditText.text.toString())
        }
        viewModel.state.observe(viewLifecycleOwner) {
            when (it) {
                is LoginState.ErrorState -> {
                    Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_LONG)
                        .show()
                }
                is LoginState.LoadingState -> {

                }
                is LoginState.Succses -> {
                    requireActivity().bottomView.menu.removeItem(R.id.loginFragment)
                    val action =
                        LoginFragmentDirections.actionLoginFragmentToBookListFragment()
                    NavHostFragment.findNavController(this).navigate(action)
                }
            }
        }
    }

}