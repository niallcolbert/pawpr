package com.neddoesdev.pawpr.activities.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.neddoesdev.pawpr.R
import com.neddoesdev.pawpr.adapters.ProfileAdapter
import com.neddoesdev.pawpr.main.MainApp
import com.neddoesdev.pawpr.models.ProfileModel
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction
import kotlinx.android.synthetic.main.fragment_search.view.*
import org.jetbrains.anko.support.v4.toast

class SearchFragment : Fragment(), CardStackListener {
    lateinit var app: MainApp
    lateinit var root: View

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_search, container, false)
        activity?.title = getString(R.string.search)

        root.stack_view.setLayoutManager(CardStackLayoutManager(activity))

        setSwipeRefresh()

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
    }

    fun setSwipeRefresh() {
        root.swiperefresh.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener {
            override fun onRefresh() {
                root.swiperefresh.isRefreshing = true
                getProfiles()
            }
        })
    }

    fun checkSwipeRefresh() {
        if (root.swiperefresh.isRefreshing) root.swiperefresh.isRefreshing = false
    }

    fun isInRange() {
    }

    override fun onResume() {
        super.onResume()
        getProfiles()
    }

    fun getProfiles() {
        val profiles = ArrayList<ProfileModel>()
        app.database.child("profile")
                .addValueEventListener(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        toast("error : ${error.message}")
                    }

                    override fun onDataChange(snapshot: DataSnapshot) {
                        val children = snapshot.children
                        children.forEach {

                            it.children.forEach {
                                var profile = it.
                                getValue<ProfileModel>(ProfileModel::class.java)

                                if ( profile!!.userId != app.auth.currentUser!!.uid ) {
                                    profiles.add(profile!!)
                                }
                            }
                        }
                        root.stack_view.adapter =
                            ProfileAdapter(profiles)
                        root.stack_view.adapter?.notifyDataSetChanged()
                        checkSwipeRefresh()

                        app.database.child("profile").removeEventListener(this)
                    }
                })
    }

    override fun onCardDragging(direction: Direction, ratio: Float) {
        toast(direction.toString())
    }

    override fun onCardSwiped(direction: Direction) {
        toast(direction.toString())
        Log.d("CardStackView", "onCardSwiped:  $direction")
        if(direction.toString() == "right") likeProfile() else dislikeProfile()
    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View, position: Int) {
    }

    override fun onCardDisappeared(view: View, position: Int) {
        toast("poof")
    }

    fun likeProfile() {
        toast("liked")
    }

    fun dislikeProfile () {
        toast("disliked")
    }
}
