package com.neddoesdev.pawpr.activities.ui.message

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.neddoesdev.pawpr.R
import com.neddoesdev.pawpr.adapters.ProfileAdapter
import com.neddoesdev.pawpr.main.MainApp
import com.neddoesdev.pawpr.models.ProfileModel
import kotlinx.android.synthetic.main.fragment_message.view.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import org.jetbrains.anko.support.v4.toast

class MessageFragment : Fragment() {
    lateinit var app: MainApp
    lateinit var root: View
    lateinit var user_id: String
    val likes = ArrayList<String>()
    val friends = ArrayList<ProfileModel>()


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_message, container, false)
        activity?.title = getString(R.string.message)

        root.recyclerView.setLayoutManager(LinearLayoutManager(activity))

        root.no_friends_label.setText(R.string.no_friends)

        setSwipeRefresh()

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        user_id = app.auth.currentUser!!.uid
        getLikes()
    }

    override fun onResume() {
        super.onResume()
        getLikes()
    }

    fun setSwipeRefresh() {
        root.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.swiperefresh.isRefreshing = true
            }
        })
    }

    fun checkSwipeRefresh() {
        if (root.swiperefresh.isRefreshing) root.swiperefresh.isRefreshing = false
    }

    fun getLikes() {
        likes.clear()
        app.database.child("likes").child(user_id)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        toast("error : ${error.message}")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val children = snapshot.children
                        children.forEach {
                            likes.add(it.key.toString())
                        }
                        getFriends()
                        app.database.child("likes").child(user_id).removeEventListener(this)
                    }
                })
    }

    fun getFriends() {
        friends.clear()
        root.no_friends_label.setText(R.string.no_friends)

        app.database.child("profile")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        toast("error : ${error.message}")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val children = snapshot.children
                        children.shuffled().forEach {

                            it.children.forEach {
                                var profile = it.
                                getValue<ProfileModel>(ProfileModel::class.java)

                                if ( profile!!.userId != user_id && likes.contains(profile!!.userId)  ) {
                                   checkFriendStatus(profile)
                                }
                            }
                        }

                        app.database.child("profile").removeEventListener(this)
                    }
                })
    }

    fun checkFriendStatus(profile: ProfileModel) {
        app.database.child("likes").child(profile.userId)
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        toast("error : ${error.message}")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val children = snapshot.children
                        children.forEach {
                            if (it.key.toString() == user_id && !friends.contains(profile)) friends.add(profile)
                        }
                        if(!friends.isEmpty()) root.no_friends_label.setText("")
                        root.recyclerView.adapter =
                                ProfileAdapter(friends)
                        root.recyclerView.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()
                        app.database.child("likes").child(profile.userId).removeEventListener(this)
                    }
                })
    }
}