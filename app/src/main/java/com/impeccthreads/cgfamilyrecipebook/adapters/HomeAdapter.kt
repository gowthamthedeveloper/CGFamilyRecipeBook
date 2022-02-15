package com.impeccthreads.cgfamilyrecipebook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.utility.listen

class HomeAdapter(val mContext: Context, var modules: ArrayList<String>, val delegate: HomeAdapterListener?): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    interface HomeAdapterListener {

        fun didSelectViewAtPosition(position: Int)
    }

    override fun getItemCount(): Int {
        return modules.size
    }

    fun getModuleList(): List<String> {
        return modules
    }

    fun setModuleList(tables: ArrayList<String>) {
        this.modules = tables
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.item_home_list, parent, false)
        val viewHolder = HomeViewHolder(cellForRow).listen { position, type ->
            delegate?.didSelectViewAtPosition(position)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {

        val moduleName = modules.get(position)

        holder.textViewTableName.text = moduleName.capitalize()
    }


    class HomeViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val textViewTableName = view.findViewById<TextView>(R.id.textViewTableName) as TextView

    }
}