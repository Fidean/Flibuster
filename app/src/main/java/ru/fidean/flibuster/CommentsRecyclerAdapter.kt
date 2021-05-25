package ru.fidean.flibuster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CommentsAdapterTAG"

class CommentsRecyclerAdapter(var list: List<Comment>) :
    RecyclerView.Adapter<CommentsRecyclerAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val autor: TextView
        val text: TextView

        init {
            autor = itemView.findViewById(R.id.commentAutor)
            text = itemView.findViewById(R.id.commentText)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.comment_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.autor.text = list[position].autor
        holder.text.text = list[position].text
    }

    override fun getItemCount(): Int {
        return list.size
    }


}