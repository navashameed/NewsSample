package com.sample.news.view.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sample.news.R
import com.sample.news.databinding.NewsListFragmentBinding
import com.sample.news.model.Rows
import com.sample.news.util.ViewModelFactory
import com.sample.news.view.adapter.NewsListAdapter
import com.sample.news.viewmodel.NewsViewModel
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class NewsListFragment : Fragment() {

    lateinit var recyclerView: RecyclerView
    lateinit var toolbar: Toolbar
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    lateinit var newsListAdapter: NewsListAdapter

    var navController: NavController? = null

    lateinit var newsListFragmentBinding: NewsListFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: NewsViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return if (::newsListFragmentBinding.isInitialized) {
            newsListFragmentBinding.root
        } else {
            newsListFragmentBinding =
                DataBindingUtil.inflate(inflater, R.layout.news_list_fragment, container, false)
            recyclerView = newsListFragmentBinding.newsRecyclerview

            newsListAdapter = NewsListAdapter()

            with(recyclerView) {
                adapter = newsListAdapter
                layoutManager = LinearLayoutManager(activity)
            }

            toolbar = newsListFragmentBinding.toolbar as Toolbar

            swipeRefreshLayout = newsListFragmentBinding.swipeToRefresh

            (activity as AppCompatActivity).setSupportActionBar(toolbar)

            with((activity as AppCompatActivity).supportActionBar) {
                this?.setDisplayHomeAsUpEnabled(false)
                this?.title = ""
            }

            toolbar.setNavigationOnClickListener { activity?.onBackPressed() }

            viewModel = ViewModelProvider(
                activity as AppCompatActivity,
                viewModelFactory
            ).get(NewsViewModel::class.java)

            swipeRefreshLayout.setOnRefreshListener {
                fetchNews()
            }

            initViewModelObservers()
            fetchNews()

            return newsListFragmentBinding.root

        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
    }

    fun fetchNews(){
        viewModel.fetchNewsList()
    }

    fun showProgressBar(show: Boolean) {
        swipeRefreshLayout.isRefreshing = show
    }

    private fun initViewModelObservers() {
        viewModel.loadingProgressDialogObservable.observe(this, Observer {
            showProgressBar(it)
        })

        viewModel.errorObservable.observe(this, Observer {
            Toast.makeText(activity, "Error in loading", Toast.LENGTH_LONG).show()
        })

        viewModel.newsListObservable.observe(this, Observer {

            recyclerView.visibility = if (it.rows.size > 0) View.VISIBLE else View.GONE

            it.title?.let { title ->
                (activity as AppCompatActivity).supportActionBar?.title = title
            }

            newsListAdapter.setItemsAndListener(
                it.rows,
                object : NewsListAdapter.OnItemClickListener {
                    override fun onItemClick(item: Rows) {


                        //TODO If more details are needed use @Parcelise on Rows class and pass the whole object
                        // OR we can also pass the position and get the item from viewmodel as viewmodel is shared between fragments.
                        val bundle =
                            bundleOf(
                                "url" to item.imageHref,
                                "title" to item.title,
                                "summary_short" to item.description
                            )
                        navController?.navigate(
                            R.id.action_newsListFragment_to_newsDetailFragment,
                            bundle
                        )
                    }
                })
        })
    }

    fun hideKeyboardFrom(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }

}
