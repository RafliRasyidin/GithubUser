package com.rasyidin.githubuser.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rasyidin.githubuser.R
import com.rasyidin.githubuser.model.User
import com.rasyidin.githubuser.ui.activity.DetailUserActivity
import kotlinx.android.synthetic.main.item_user_list.view.*

class ListUserAdapter : RecyclerView.Adapter<ListUserAdapter.UserViewHolder>() {

    private val mData = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val mView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)
        return UserViewHolder(mView)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userItem: User) {
            with(itemView) {
                Glide.with(context)
                    .load(userItem.avatars)
                    .into(imgUser)

                tvUsername.text = userItem.login
                tvType.text = userItem.type

                itemView.setOnClickListener {
                    Intent(context, DetailUserActivity::class.java).apply {
                        putExtra(DetailUserActivity.EXTRA_USERNAME, userItem.login)
                    }.run {
                        context.startActivity(this)
                    }
                }
            }
        }
    }
}