package com.example.biladoniga_toscanini_tejerina.ranking.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.data.models.Movie
import com.example.biladoniga_toscanini_tejerina.R

class MovieAdapter(private val movies: List<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    class MovieViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val movieName: TextView = view.findViewById(R.id.movie_name)
        val position: TextView = view.findViewById(R.id.position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_movie, parent, false)

        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.movieName.text = movies[position].movieName
        holder.position.text = "${holder.view.resources.getString(R.string.position_num)}${position + 1}"
    }

    override fun getItemCount() = movies.size
}