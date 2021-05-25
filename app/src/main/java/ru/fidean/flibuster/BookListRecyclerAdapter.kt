package ru.fidean.flibuster

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import ru.fidean.flibuster.Fragments.BookListFragment
import ru.fidean.flibuster.Fragments.BookListFragmentDirections

private const val TAG = "RecyclerAdapterTAG"

class BookListRecyclerAdapter(var list: List<Book>, var fragment: BookListFragment) :
    RecyclerView.Adapter<BookListRecyclerAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val autor: TextView
        val title: TextView
        val series: TextView
        val genres: TextView
        val autorText: TextView
        val seriesText: TextView
        val genresText: TextView

        init {
            autor = itemView.findViewById(R.id.autor)
            title = itemView.findViewById(R.id.title)
            series = itemView.findViewById(R.id.series)
            genres = itemView.findViewById(R.id.genres)
            autorText = itemView.findViewById(R.id.autorText)
            seriesText = itemView.findViewById(R.id.seriesText)
            genresText = itemView.findViewById(R.id.genresText)
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
        if (list[position].autor.isEmpty()) {
            holder.autorText.visibility = View.GONE
            holder.autor.visibility = View.GONE
        } else {
            holder.autor.text = list[position].autor
        }
        if (list[position].series.isEmpty()) {
            holder.seriesText.visibility = View.GONE
            holder.series.visibility = View.GONE
        } else {
            holder.series.text = list[position].series
        }
        holder.genres.text = list[position].genre.joinToString()

        holder.itemView.setOnClickListener {
            Log.d(TAG, list[position].id.toString())
            val action =
                BookListFragmentDirections.actionBookListFragmentToBookFragment(list[position].id)
            findNavController(fragment).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


}