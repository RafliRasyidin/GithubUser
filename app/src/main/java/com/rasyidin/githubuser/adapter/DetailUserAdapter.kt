package com.rasyidin.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rasyidin.githubuser.R
import com.rasyidin.githubuser.model.User
import kotlinx.android.synthetic.main.item_user.view.*

class DetailUserAdapter : RecyclerView.Adapter<DetailUserAdapter.DetailViewHolder>() {

    private val mData = ArrayList<User>()

    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return DetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    inner class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(userDetail: User) {
            with(itemView) {
                Glide.with(context)
                    .load(userDetail.avatars)
                    .into(imgUserDetail)
                tvName.text = userDetail.name
                tvCompany.text = userDetail.company
                tvFollowers.text = userDetail.followers.toString()
                tvFollowing.text = userDetail.following.toString()
                tvLocation.text = userDetail.location
                textFollowers.text = resources.getString(R.string.followers)
                textFollowing.text = resources.getString(R.string.following)

            }
        }
    }
}