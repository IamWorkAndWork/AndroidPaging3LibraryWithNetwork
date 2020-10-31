package practice.app.mypaging3networkexample.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import practice.app.mypaging3networkexample.R
import practice.app.mypaging3networkexample.databinding.ViewholderFooterLoadingBinding

class MainLoadStateAdapter : LoadStateAdapter<MainLoadStateAdapter.MainLoadStateViewHolder>() {

    companion object {
        private val TAG by lazy {
            javaClass.simpleName
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): MainLoadStateViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_footer_loading, parent, false)
        val binding = ViewholderFooterLoadingBinding.bind(view)
        return MainLoadStateViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class MainLoadStateViewHolder(private val binding: ViewholderFooterLoadingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                Log.e(TAG, "error MainLoadStateViewHolder : ${loadState.error.localizedMessage}")
            }

            val isLoading = loadState is LoadState.Loading

            binding.footerLoadingProgressBar.isVisible = isLoading
        }

    }


}