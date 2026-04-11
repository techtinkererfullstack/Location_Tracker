package com.example.locationtracking

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.locationtracking.databinding.ActivitySigninSignupBinding
import com.example.locationtracking.repo.UserRepository
import com.example.locationtracking.viewmodel.AuthViewModel

class SigninSignup : AppCompatActivity() {
    private lateinit var binding: ActivitySigninSignupBinding

    private val repo = UserRepository()

    private val viewModel: AuthViewModel by viewModels<AuthViewModel> {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AuthViewModel(repo) as T
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySigninSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.BTNSignin.setOnClickListener {
            val email = binding.ETEmail.text.toString().trim()
            val password = binding.ETPassword.text.toString().trim()

            // 1. Reset visual errors
            binding.ETEmail.error = null
            binding.ETPassword.error = null

            // 2. Validation Chain
            when {
                email.isEmpty() -> {
                    binding.ETEmail.error = "Please enter your email"
                    binding.ETEmail.requestFocus()
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.ETEmail.error = "Invalid email format"
                    binding.ETEmail.requestFocus()
                }
                password.isEmpty() -> {
                    binding.ETPassword.error = "Please enter your password"
                    binding.ETPassword.requestFocus()
                }
                else -> {
                    // 3. Trigger the Login
                    viewModel.login(email, password)
                }
            }
        }


        binding.BTNSignup.setOnClickListener {
            val email = binding.ETEmail.text.toString().trim()
            val password = binding.ETPassword.text.toString().trim()

            // 1. Reset errors (to clear previous attempts)
            binding.ETEmail.error = null
            binding.ETPassword.error = null

            // 2. Perform Validations
            when {
                email.isEmpty() -> {
                    binding.ETEmail.error = "Email is required"
                    binding.ETEmail.requestFocus()
                }
                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    binding.ETEmail.error = "Please enter a valid email address"
                    binding.ETEmail.requestFocus()
                }
                password.isEmpty() -> {
                    binding.ETPassword.error = "Password is required"
                    binding.ETPassword.requestFocus()
                }
                password.length < 6 -> {
                    binding.ETPassword.error = "Password must be at least 6 characters"
                    binding.ETPassword.requestFocus()
                }
                else -> {
                    // 3. Proceed to ViewModel if all checks pass
                    viewModel.register(email, password)
                }
            }
        }

        viewModel.registerResult.observe(this) { (success, message) ->
            if (success) {
                Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Registration Filed", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.loginResult.observe(this) { (success, message) ->
            if (success) {
                Toast.makeText(this, "Logged In Successful", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Logged In Filed", Toast.LENGTH_SHORT).show()
            }
        }


    }
}