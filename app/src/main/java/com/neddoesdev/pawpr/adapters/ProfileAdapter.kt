package com.neddoesdev.pawpr.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.neddoesdev.pawpr.R
import com.neddoesdev.pawpr.models.ProfileModel
import kotlinx.android.synthetic.main.card_profile.view.*

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

        fun bind(profile: ProfileModel) {
            itemView.profile_name.text = profile.name
            itemView.profile_breed.text = profile.breed
            itemView.profile_bio.text = profile.bio
            itemView.profile_gender.text = profile.gender
        }
    }
}