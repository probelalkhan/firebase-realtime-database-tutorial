package net.simplifiedcoding.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*
import net.simplifiedcoding.data.Author
import net.simplifiedcoding.data.NODE_AUTHORS
import java.lang.Exception

class AuthorsViewModel : ViewModel() {

    private val dbAuthors = FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)

    private val _authors = MutableLiveData<List<Author>>()
    val authors: LiveData<List<Author>>
        get() = _authors

    private val _author = MutableLiveData<Author>()
    val author: LiveData<Author>
        get() = _author

    private val _result = MutableLiveData<Exception?>()
    val result: LiveData<Exception?>
        get() = _result

    fun addAuthor(author: Author) {
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

    private val childEventListener = object : ChildEventListener {
        override fun onCancelled(error: DatabaseError) {}

        override fun onChildMoved(snapshot: DataSnapshot, p1: String?) {}

        override fun onChildChanged(snapshot: DataSnapshot, p1: String?) {
            val author = snapshot.getValue(Author::class.java)
            author?.id = snapshot.key
            _author.value = author
        }

        override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
            val author = snapshot.getValue(Author::class.java)
            author?.id = snapshot.key
            _author.value = author
        }

        override fun onChildRemoved(snapshot: DataSnapshot) {
            val author = snapshot.getValue(Author::class.java)
            author?.id = snapshot.key
            author?.isDeleted = true
            _author.value = author
        }
    }

    fun getRealtimeUpdates() {
        dbAuthors.addChildEventListener(childEventListener)
    }


    fun fetchFilteredAuthors(index: Int) {
        val dbAuthors =
            when (index) {
                1 ->
                    //#1 SELECT * FROM Authors
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)

                2 ->
                    //#2 SELECT * FROM Authors WHERE id = ?
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                        .child("-M-3fFw3GbovXWguSjp8")

                3 ->
                    //#3 SELECT * FROM Authors WHERE city = ?
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                        .orderByChild("city")
                        .equalTo("Hyderabad")

                4 ->
                    //#4 SELECT * FROM Authors LIMIT 2
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                        .limitToFirst(2)

                5 ->
                    //#5 SELECT * FROM Authors WHERE votes < 500
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                        .orderByChild("votes")
                        .endAt(500.toDouble())

                6 ->
                    //#6 SELECT * FROM Artists WHERE name LIKE "A%"
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                        .orderByChild("name")
                        .startAt("A")
                        .endAt("A\uf8ff")

                7 ->
                    //#7 SELECT * FROM Artists Where votes < 500 AND city = Bangalore
                    FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
                else -> FirebaseDatabase.getInstance().getReference(NODE_AUTHORS)
            }

        dbAuthors.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val authors = mutableListOf<Author>()
                    for (authorSnapshot in snapshot.children) {
                        val author = authorSnapshot.getValue(Author::class.java)
                        author?.id = authorSnapshot.key
                        author?.let { authors.add(it) }
                    }
                    _authors.value = authors
                }
            }
        })
    }


    fun fetchAuthors() {
        dbAuthors.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val authors = mutableListOf<Author>()
                    for (authorSnapshot in snapshot.children) {
                        val author = authorSnapshot.getValue(Author::class.java)
                        author?.id = authorSnapshot.key
                        author?.let { authors.add(it) }
                    }
                    _authors.value = authors
                }
            }
        })
    }

    fun updateAuthor(author: Author) {
        dbAuthors.child(author.id!!).setValue(author)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }
    }

    fun deleteAuthor(author: Author) {
        dbAuthors.child(author.id!!).setValue(null)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    _result.value = null
                } else {
                    _result.value = it.exception
                }
            }
    }

    override fun onCleared() {
        super.onCleared()
        dbAuthors.removeEventListener(childEventListener)
    }
}