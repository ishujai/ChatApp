package com.example.chatapplicationpracticefinal

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MainAdapter(private val context: Context, private val arrayList: ArrayList<MainModel>) :
        RecyclerView.Adapter<MainAdapter.ViewHolder>() {

private var onItemClickListener: OnItemClickListener? = null

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.main_item, parent, false)
        return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {

                var user =arrayList[position]
                holder.name.text = arrayList[position].name
                holder.email.text = arrayList[position].email
                holder.uniqueid.text = arrayList[position].uniqueid
                holder.itemView.setOnClickListener {
                val intent=Intent(context,ChatActivity::class.java)
                intent.putExtra("uniqueid",user.uniqueid)
                context.startActivity(intent)
        }
        }

        override fun getItemCount(): Int {
        return arrayList.size
        }

        fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
        }

interface OnItemClickListener {
    fun onClick(model: MainModel)
}

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.textViewone)
        val email: TextView = itemView.findViewById(R.id.textViewtwo)
        val uniqueid: TextView = itemView.findViewById(R.id.uniqueid)
} }
