package net.simplifiedcoding.ui

import android.view.View
import net.simplifiedcoding.data.Author

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClicked(view: View, author: Author)
}