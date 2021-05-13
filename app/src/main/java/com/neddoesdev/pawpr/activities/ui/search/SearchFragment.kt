package com.neddoesdev.pawpr.activities.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
    lateinit var user_id: String
    lateinit var manager: CardStackLayoutManager
    val likes = ArrayList<String>()
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

        return root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        app = activity?.application as MainApp
        user_id = app.auth.currentUser!!.uid
    }

    fun isInRange() {
    }

    override fun onResume() {
        super.onResume()
        getLikes()
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
                        getProfiles()
                        app.database.child("likes").child(user_id).removeEventListener(this)
                    }
                })
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

                            if ( profile!!.userId != user_id && !likes.contains(profile!!.userId)  ) {
                                profiles.add(profile!!)
                            }
                        }
                    }
                    root.stack_view.adapter =
                        ProfileAdapter(profiles)
                    root.stack_view.adapter?.notifyDataSetChanged()

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
        val liked_profile =  profiles.get(manager.topPosition - 1)
        val liked_id = liked_profile.userId
        val childUpdates = HashMap<String, Any>()
        childUpdates["/likes/$user_id/$liked_id"] = true
        app.database.updateChildren(childUpdates)
        toast("You liked " + liked_profile.name)
    }
}
