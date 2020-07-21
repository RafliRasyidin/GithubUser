package com.rasyidin.githubuserconsumerapp.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rasyidin.githubuserconsumerapp.R
import com.rasyidin.githubuserconsumerapp.model.User
import kotlinx.android.synthetic.main.item_user_list.view.*

class FavoriteAdapter(private val activity: Activity) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFavorite = ArrayList<User>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)
        return FavoriteViewHolder(mView)
    }

    override fun getItemCount(): Int = this.listFavorite.size

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])
    }

    inner class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            with(itemView) {
                Glide.with(context)
                    .load(user.avatars)
                    .into(imgUser)
                tvUsername.text = user.login
                tvType.text = user.type

                itemView.setOnClickListener {
                    Toast.makeText(activity, "${user.login}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}