package com.impeccthreads.cgfamilyrecipebook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.application.FoodType
import com.impeccthreads.cgfamilyrecipebook.dto.CookingRecipeDetails
import com.impeccthreads.cgfamilyrecipebook.utility.listen

class RecipeItemAdapter(val mContext: Context, var cookingRecipeDetailsList: ArrayList<CookingRecipeDetails>, val delegate: RecipeItemAdapterListener?): RecyclerView.Adapter<RecipeItemAdapter.RecipeItemViewHolder>() {

    interface RecipeItemAdapterListener {
        fun didSelectViewAtPosition(position: Int)
    }

    override fun getItemCount(): Int {
        return cookingRecipeDetailsList.size
    }

    fun getItemList(): List<CookingRecipeDetails> {

        return cookingRecipeDetailsList
    }

    fun setItemList(cookingRecipeDetails: ArrayList<CookingRecipeDetails>) {
        this.cookingRecipeDetailsList = cookingRecipeDetails
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.item_recipe_item_list, parent, false)
        val viewHolder = RecipeItemViewHolder(cellForRow).listen { position, type ->
            delegate?.didSelectViewAtPosition(position)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecipeItemViewHolder, position: Int) {

        val recipeItem = cookingRecipeDetailsList.get(position)

        holder.textViewRecipeItemName.text = recipeItem.recipeName!!.capitalize()
        holder.imageViewNewTag.visibility = if(recipeItem.isNewRecipe) View.VISIBLE else View.GONE
        holder.imageViewRecipe.visibility = if(recipeItem.ingredeints.isNotEmpty() || recipeItem.methods.isNotEmpty()) View.VISIBLE else View.GONE

    }


    class RecipeItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val textViewRecipeItemName = view.findViewById<TextView>(R.id.textViewRecipeItemName) as TextView
        val imageViewNewTag = view.findViewById<TextView>(R.id.imageViewNewTag) as ImageView
        val imageViewRecipe = view.findViewById<TextView>(R.id.imageViewRecipe) as ImageView

    }
}