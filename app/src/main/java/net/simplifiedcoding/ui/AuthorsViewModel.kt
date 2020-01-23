package net.simplifiedcoding.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import net.simplifiedcoding.data.Author
import net.simplifiedcoding.data.NODE_AUTHORS
import java.lang.Exception

class AuthorsViewModel : ViewModel() {

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    fun addAuthor(author: Author) {
        val dbAuthors = FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
        author.id = dbAuthors.push().key
        dbAuthors.child(author.id!!).setValue(author)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }
    }

}