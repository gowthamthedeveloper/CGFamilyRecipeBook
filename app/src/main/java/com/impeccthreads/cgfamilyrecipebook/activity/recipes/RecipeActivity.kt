package com.impeccthreads.cgfamilyrecipebook.activity.recipes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.adapters.RecipeItemAdapter
import com.impeccthreads.cgfamilyrecipebook.application.BaseActivity
import com.impeccthreads.cgfamilyrecipebook.application.DatabaseTable
import com.impeccthreads.cgfamilyrecipebook.dto.CookingRecipeDetails
import com.impeccthreads.cgfamilyrecipebook.dto.RecipeItem
import com.impeccthreads.cgfamilyrecipebook.modelhandler.RecipeDataHandler
import kotlinx.android.synthetic.main.activity_recipe.*

class RecipeActivity : BaseActivity(), RecipeItemAdapter.RecipeItemAdapterListener {

    companion object {
        const val PICK_EDITED_COOKING_RECIPE_REQUEST_CODE = 15
        const val PICK_ADDED_COOKING_RECIPE_ITEM_REQUEST_CODE = 16

        val COOKING_RECIPE_DETAIL = "COOKING_RECIPE_DETAIL"
    }

    var recipeItemDetails : RecipeItem? = null
    var cookingRecipeDetailsList: ArrayList<CookingRecipeDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_recipe)

        if (intent.hasExtra(CategoryActivity.RECIPE_ITEM_DETAILS))
        {
            recipeItemDetails = intent.getParcelableExtra(CategoryActivity.RECIPE_ITEM_DETAILS)
            textViewCookingRecipeItemTitle.text = recipeItemDetails!!.title + " List"
        }

        cookingRecipeDetailsList = RecipeDataHandler.getRecipeItemForCategory(recipeItemDetails!!.title)

        if (cookingRecipeDetailsList.count() > 0)
        {
            listViewRecipeItemList.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            listViewRecipeItemList.adapter = RecipeItemAdapter(this, cookingRecipeDetailsList, this)
        }

        validateViewBtn()
    }


    //Recycler Listener
    override fun didSelectViewAtPosition(position: Int) {

        var intent = Intent(this, RecipeDetailActivity::class.java)
        intent.putExtra(COOKING_RECIPE_DETAIL, cookingRecipeDetailsList[position])
        startActivityForResult(intent, PICK_EDITED_COOKING_RECIPE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_EDITED_COOKING_RECIPE_REQUEST_CODE || requestCode == PICK_ADDED_COOKING_RECIPE_ITEM_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                reloadRecyclerView()
            }
        }
    }

    fun btnCookingRecipeItemBack(view: View) {
        finish()
    }

    fun btnDeleteRecipeCategoryOnClick(view: View) {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Confirmation")

        builder.setMessage("Are you sure do you want to delete this recipe category?")

        builder.setPositiveButton("YES"){dialog, which ->
            // Do something when user press the positive button

            recipeItemDetails!!.key.let {

                if (it != null && it.isNotEmpty())
                {

                    Log.d("Delete Recipe Key", it!!)

                    FirebaseDatabase.getInstance().getReference(DatabaseTable.cookingrecipedetails.toString() + "/" + it!!).removeValue()

                    finish()

                    showToast("${recipeItemDetails!!.title} Recipe Category Deleted")
                }
            }
        }

        builder.setNegativeButton("No"){dialog,which ->

        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

    fun btnAddCookingRecipeItemOnClick(view: View) {
        val intent = Intent(this, AddCookingRecipeActivity::class.java)
        intent.putExtra(CategoryActivity.RECIPE_ITEM_DETAILS, recipeItemDetails!!)
        startActivityForResult(intent, PICK_ADDED_COOKING_RECIPE_ITEM_REQUEST_CODE)
    }

    fun validateViewBtn() {
        if (cookingRecipeDetailsList.count() == 0)
        {
            btnDeleteRecipeCategory.visibility = View.VISIBLE
            textViewNoRecipe.visibility = View.VISIBLE
        }
        else
        {
            btnDeleteRecipeCategory.visibility = View.GONE
            textViewNoRecipe.visibility = View.GONE
        }
    }

    fun reloadRecyclerView() {
        cookingRecipeDetailsList = RecipeDataHandler.getRecipeItemForCategory(recipeItemDetails!!.title)
        (listViewRecipeItemList.adapter as RecipeItemAdapter).setItemList(cookingRecipeDetailsList)
        validateViewBtn()
    }
}