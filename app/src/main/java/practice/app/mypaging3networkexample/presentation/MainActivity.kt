package practice.app.mypaging3networkexample.presentation

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import practice.app.mypaging3networkexample.Injection
import practice.app.mypaging3networkexample.data.model.Repo
import practice.app.mypaging3networkexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), MainAdapter.MainAdapterListener {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mainViewModel: MainViewModel
    private var searchJob: Job? = null
    private val mainAdapter by lazy {
        MainAdapter(this)
    }

    companion object {
        private const val LAST_SEARCH_QUERY: String = "last_search_query"
        private const val DEFAULT_QUERY = "Android"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel =
            ViewModelProvider(
                this,
                Injection.provideMainViewmodelFactory()
            ).get(MainViewModel::class.java)

        setupViewBinding()

        initWidget()
        initListener()

        val query = savedInstanceState?.getString(LAST_SEARCH_QUERY) ?: DEFAULT_QUERY
        search(query)
        initSearch(query)

    }

    private fun initListener() {
        mainAdapter.addLoadStateListener { loadState ->

            binding.mainListRecyclerView.isVisible =
                loadState.source.refresh is LoadState.NotLoading

            binding.mainProgressBar.isVisible = loadState.source.refresh is LoadState.Loading

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(LAST_SEARCH_QUERY, binding.mainSearchRepo.text.trim().toString())
    }

    private fun initSearch(query: String) {
        binding.mainSearchRepo.setText(query)
    }

    private fun setupViewBinding() {
        binding = ActivityMainBinding.inflate(layoutInflater)

        val view = binding.root
        setContentView(view)
    }

    private fun initWidget() {

        val decoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        binding.mainListRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mainAdapter.withLoadStateHeaderAndFooter(
                header = MainLoadStateAdapter(),
                footer = MainLoadStateAdapter()
            )
            addItemDecoration(decoration)
        }

        binding.mainSearchRepo.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }

        binding.mainSearchRepo.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateRepoListFromInput()
                true
            } else {
                false
            }
        }

        lifecycleScope.launch {
            mainAdapter.loadStateFlow
                .distinctUntilChangedBy {
                    it.refresh
                }
                .filter {
                    it.refresh is LoadState.NotLoading
                }
                .collect {
                    binding.mainListRecyclerView.scrollToPosition(0)
                }
        }

    }

    private fun updateRepoListFromInput() {
        binding.mainSearchRepo.text.trim().takeIf { it.isNotEmpty() }
            .let {
                search(it.toString())
            }
    }

    private fun search(queryString: String) {

        searchJob?.cancel()

        searchJob = lifecycleScope.launch {
            mainViewModel.searchRepos(queryString)
                ?.collectLatest { repoPagingData: PagingData<Repo> ->
                    mainAdapter.submitData(repoPagingData)
                }
        }

    }

    override fun onClicked(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}