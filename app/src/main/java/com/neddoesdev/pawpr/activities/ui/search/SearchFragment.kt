package com.neddoesdev.pawpr.activities.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
import kotlin.collections.ArrayList

class SearchFragment : Fragment(), CardStackListener {
    lateinit var app: MainApp
    lateinit var root: View
    lateinit var manager: CardStackLayoutManager
    val profiles = ArrayList<ProfileModel>()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_search, container, false)
        activity?.title = getString(R.string.search)

        manager = CardStackLayoutManager(activity, this)
        root.stack_view.setLayoutManager(manager)
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
        profiles.clear()
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
    }

    override fun onCardSwiped(direction: Direction, ) {
        if(direction.toString().toLowerCase() == "right") likeProfile()
    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View, position: Int) {
    }

    override fun onCardDisappeared(view: View, position: Int) {
    }

    fun likeProfile() {
        toast("You liked " + profiles.get(manager.topPosition - 1).name)
    }
}
