package net.simplifiedcoding.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_authors.*
import net.simplifiedcoding.R


class AuthorsFragment : Fragment() {

    private lateinit var viewModel: AuthorsViewModel
    private val adapter = AuthorsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this).get(AuthorsViewModel::class.java)
        return inflater.inflate(R.layout.fragment_authors, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        recycler_view_authors.adapter = adapter

        viewModel.fetchAuthors()

        viewModel.authors.observe(viewLifecycleOwner, Observer {
            adapter.setAuthors(it)
        })

        button_add.setOnClickListener {
            AddAuthorDialogFragment()
                .show(childFragmentManager, "")
        }
    }
}
