package ru.fidean.flibuster

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView

class BookListRecyclerAdapter(var list: List<Book>,var fragment: BookListFragment) :
    RecyclerView.Adapter<BookListRecyclerAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val autor: TextView
        val title: TextView
        val series: TextView

        init {
            autor = itemView.findViewById(R.id.autor)
            title = itemView.findViewById(R.id.title)
            series = itemView.findViewById(R.id.series)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.book_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = list[position].title
        holder.autor.text = list[position].autor
        holder.series.text = list[position].series
        val bundle = bundleOf("BookID" to list[position].id)
        holder.itemView.setOnClickListener {
            findNavController(fragment).navigate(R.id.action_bookListFragment_to_bookFragment, bundle)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}