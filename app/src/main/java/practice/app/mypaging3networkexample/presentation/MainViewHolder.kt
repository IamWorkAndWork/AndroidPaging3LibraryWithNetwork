package practice.app.mypaging3networkexample.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import practice.app.mypaging3networkexample.R
import practice.app.mypaging3networkexample.data.model.Repo

class MainViewHolder(itemView: View, private val listener: MainAdapter.MainAdapterListener) :
    RecyclerView.ViewHolder(itemView) {

    companion object {
        fun create(parent: ViewGroup, listener: MainAdapter.MainAdapterListener): MainViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.viewholder_main, parent, false)
            return MainViewHolder(view, listener)
        }
    }

    private val name: TextView = itemView.findViewById(R.id.repo_name)
    private val description: TextView = itemView.findViewById(R.id.repo_description)
    private val stars: TextView = itemView.findViewById(R.id.repo_stars)
    private val language: TextView = itemView.findViewById(R.id.repo_language)
    private val forks: TextView = itemView.findViewById(R.id.repo_forks)

    private var repo: Repo? = null

    init {
        itemView.setOnClickListener {
            repo?.url
                ?.takeIf { it.isNotEmpty() }
                ?.let { url ->
                    listener.onClicked(url)
                }
        }
    }

    fun bind(repo: Repo?) {
        if (repo == null) {
            val resources = itemView.resources
            name.text = resources.getString(R.string.loading)
            description.visibility = View.GONE
            language.visibility = View.GONE
            stars.text = resources.getString(R.string.unknown)
            forks.text = resources.getString(R.string.unknown)
        } else {
            showRepoData(repo)
        }
    }

    private fun showRepoData(repo: Repo) {
        this.repo = repo
        name.text = repo.fullName

        var descriptionVisibility = View.GONE
        if (repo.description != null) {
            description.text = repo.description
            descriptionVisibility = View.VISIBLE
        }
        description.visibility = descriptionVisibility

        stars.text = repo.stars.toString()
        forks.text = repo.forks.toString()

        var languageVisibility = View.GONE
        if (!repo.language.isNullOrEmpty()) {
            val resources = this.itemView.context.resources
            language.text = resources.getString(R.string.language, repo.language)
            languageVisibility = View.VISIBLE
        }
        language.visibility = languageVisibility
    }
}