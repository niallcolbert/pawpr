package com.neddoesdev.pawpr.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.neddoesdev.pawpr.R
import com.neddoesdev.pawpr.main.MainApp
import com.neddoesdev.pawpr.models.ProfileModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.card_profile.view.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileAdapter constructor(private var profiles: List<ProfileModel>) :
    RecyclerView.Adapter<ProfileAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.card_profile,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val profile = profiles[holder.adapterPosition]
        holder.bind(profile)
    }

    override fun getItemCount(): Int = profiles.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("SetTextI18n")
        fun bind(profile: ProfileModel) {
            itemView.profile_name.text = profile.name
            itemView.profile_breed.text = "Breed: " + profile.breed
            itemView.profile_bio.text = "Bio: " + profile.bio
            itemView.profile_gender.text = if (profile.gender == "male") "Good Boy" else "Good Girl"
            FirebaseStorage.getInstance().reference.child("photos/${profile.userId}.jpg").downloadUrl.addOnSuccessListener {
                Picasso.get().load(it)
                    .into(itemView.profile_image)
            }.addOnFailureListener {
            }

        }
    }
}