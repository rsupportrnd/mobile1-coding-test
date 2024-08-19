package com.rsupport.mobile1.test.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.rsupport.mobile1.test.adapter.PhotoContentsAdapter
import com.rsupport.mobile1.test.databinding.ActivityMainBinding
import com.rsupport.mobile1.test.ui.state.UiState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: MainViewModel by viewModels()
    private var adapter: PhotoContentsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()
        subscribe()
        viewModel.requestApi()
    }

    private fun setupRecyclerView() {
        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        binding.activityMainRecyclerview.addItemDecoration(decoration)
        adapter = PhotoContentsAdapter()
        binding.activityMainRecyclerview.adapter = adapter
    }

    private fun subscribe() {
        viewModel.state.observe(this) { state ->
            when (state) {
                is UiState.Uninitialized -> {}

                is UiState.Loading -> {

                }

                is UiState.Empty -> {


                }

                is UiState.Success -> {
                    adapter?.submitList(state.data)
                }

                is UiState.Error -> {


                }
            }
        }
    }
}