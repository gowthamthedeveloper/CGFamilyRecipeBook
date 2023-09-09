package com.impeccthreads.cgfamilyrecipebook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.dto.RecipeItem
import com.impeccthreads.cgfamilyrecipebook.utility.listen

class CategoryAdapter(val mContext: Context, var categoryList: ArrayList<RecipeItem>, val delegate: CategoryAdapterListener?): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    interface CategoryAdapterListener {
        fun didSelectViewAtPosition(position: Int)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun getItemList(): List<RecipeItem> {
        return categoryList
    }

    fun setItemList(categories: ArrayList<RecipeItem>) {
        this.categoryList = categories
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.item_category_list, parent, false)
        val viewHolder = CategoryViewHolder(cellForRow).listen { position, type ->
            delegate?.didSelectViewAtPosition(position)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList.get(position)
        holder.textViewCategoryName.text = category.title.capitalize()
        holder.imageViewRecipe.visibility = if(category.ingredeints.isNotEmpty() || category.methods.isNotEmpty()) View.VISIBLE else View.GONE
        holder.viewNewTag.visibility = if(category.isNewRecipe) View.VISIBLE else View.GONE
    }


    class CategoryViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val textViewCategoryName = view.findViewById<TextView>(R.id.textViewCategoryName) as TextView
        val viewNewTag = view.findViewById<TextView>(R.id.viewNewTag) as View
        val imageViewRecipe = view.findViewById<TextView>(R.id.imageViewRecipe) as ImageView
    }
}