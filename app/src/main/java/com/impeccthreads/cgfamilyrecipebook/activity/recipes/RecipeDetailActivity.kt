package com.impeccthreads.cgfamilyrecipebook.activity.recipes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.application.BaseActivity
import com.impeccthreads.cgfamilyrecipebook.application.CookingRecipeDetailsTable
import com.impeccthreads.cgfamilyrecipebook.application.DatabaseTable
import com.impeccthreads.cgfamilyrecipebook.dto.CookingRecipeDetails
import com.impeccthreads.cgfamilyrecipebook.dto.RecipeItem
import com.impeccthreads.cgfamilyrecipebook.modelhandler.RecipeDataHandler
import kotlinx.android.synthetic.main.activity_recipe.*
import kotlinx.android.synthetic.main.activity_recipe_detail.*

class RecipeDetailActivity : BaseActivity() {

    companion object {
        const val PICK_EDITED_COOKING_RECIPE_REQUEST_CODE = 10
        const val ACTION_EDIT_COOKING_RECIPE = 1
        const val ACTION_DELETE_COOKING_RECIPE = 2
        const val ACTION_IS_NEW_RECIPE = 3
        const val ACTION_IS_OLD_RECIPE = 4

        val EDIT_COOKING_RECIPE_DETAIL = "EDIT_COOKING_RECIPE_DETAIL"
    }

    var recipeItemDetails : RecipeItem? = null
    var cookingRecipeDetails: CookingRecipeDetails? = null
    var isRecipeEdited: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_recipe_detail)

        if (intent.hasExtra(CategoryActivity.RECIPE_ITEM_DETAILS))
        {
            recipeItemDetails = intent.getParcelableExtra(CategoryActivity.RECIPE_ITEM_DETAILS)
            cookingRecipeDetails = RecipeDataHandler.getRecipeItemForRecipeName(recipeItemDetails!!.recipeName)
        }

        if (intent.hasExtra(RecipeActivity.COOKING_RECIPE_DETAIL))
        {
            cookingRecipeDetails = intent.getParcelableExtra(RecipeActivity.COOKING_RECIPE_DETAIL)
        }

        textViewCookingRecipeDetailTitle.text = cookingRecipeDetails!!.recipeName

        textViewCookingRecipePath.text = cookingRecipeDetails!!.title + " / " + cookingRecipeDetails!!.subTitle + " / " + cookingRecipeDetails!!.recipeName

        updateRecipeDetails()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

        if (id == android.R.id.home) {
            finish()
            return true
        }

        return super.onOptionsItemSelected(item)

    }

    fun updateRecipeDetails() {
        textViewIngredients.text = cookingRecipeDetails!!.ingredeints
        textViewMethods.text = cookingRecipeDetails!!.methods
        textViewTips.text = cookingRecipeDetails!!.note
        textViewPreparationTime.text = cookingRecipeDetails!!.preparationTime
    }

    fun showProgressBar() {
        loadingpanelmask_addCookingRecipereport.visibility = View.VISIBLE
        loadingPanel_addCookingRecipereport.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        loadingpanelmask_addCookingRecipereport.visibility = View.GONE
        loadingPanel_addCookingRecipereport.visibility = View.GONE
    }

    fun btnCookingRecipeDetailBack(view: View) {

        if (isRecipeEdited)
        {
            val result = Intent()
            setResult(Activity.RESULT_OK, result)
        }

        finish()

    }

    fun btnEditCookingRecipeOnClick(view: View) {

        val popupMenu = PopupMenu(this,view)

        popupMenu.menu.add(Menu.NONE, ACTION_EDIT_COOKING_RECIPE, 1,"Edit Recipe")
        popupMenu.menu.add(Menu.NONE, ACTION_DELETE_COOKING_RECIPE, 2,"Delete Recipe")

        if (cookingRecipeDetails!!.isNewRecipe)
        {
            popupMenu.menu.add(Menu.NONE, ACTION_IS_OLD_RECIPE, 3,"Normal Recipe")
        }
        else
        {
            popupMenu.menu.add(Menu.NONE, ACTION_IS_NEW_RECIPE, 3,"Trending Recipe")
        }

        popupMenu.setOnMenuItemClickListener({ item ->
            when(item.itemId) {
                ACTION_EDIT_COOKING_RECIPE -> {
                    val intent = Intent(this, EditCookingRecipeMethodActivity::class.java)
                    intent.putExtra(EDIT_COOKING_RECIPE_DETAIL, cookingRecipeDetails!!)
                    startActivityForResult(intent, PICK_EDITED_COOKING_RECIPE_REQUEST_CODE)
                }
                ACTION_DELETE_COOKING_RECIPE -> {
                    deleteCookingRecipeDetailAlert()
                }
                ACTION_IS_NEW_RECIPE -> {
                    updateRecipeNewStatus(true)
                }
                ACTION_IS_OLD_RECIPE -> {
                    updateRecipeNewStatus(false)
                }
            }
            true
        })

        popupMenu.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_EDITED_COOKING_RECIPE_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                data.let {

                    if (it != null) {

                        if (it!!.hasExtra(EditCookingRecipeMethodActivity.EDITED_COOKING_RECIPE_DETAILS))
                        {
                            cookingRecipeDetails = it!!.getParcelableExtra(EditCookingRecipeMethodActivity.EDITED_COOKING_RECIPE_DETAILS)

                            isRecipeEdited = true

                            //ISNEDDED
//                            FirebaseDataHandler.updateCookingRecipeItemInList(cookingRecipeDetails!!, false)

                            updateRecipeDetails()
                        }
                    }
                }

            }
        }
    }


    fun updateRecipeNewStatus(isNewRecipe: Boolean) {

        cookingRecipeDetails!!.key.let {

            if (it != null && it.isNotEmpty())
            {
                showProgressBar()

                val updateValues = HashMap<String, Any>()
                updateValues.put(CookingRecipeDetailsTable.isNewRecipe.toString(), isNewRecipe)

                val recipeItemRef = FirebaseDatabase.getInstance().getReference(DatabaseTable.cookingrecipedetails.toString() + "/" + it!!)
                recipeItemRef.updateChildren(updateValues)

                cookingRecipeDetails!!.isNewRecipe = isNewRecipe

                //ISNEEDED
//                FirebaseDataHandler.updateCookingRecipeItemInList(cookingRecipeDetails!!, false)

                isRecipeEdited = true

                hideProgressBar()

                if (isNewRecipe)
                {
                    showToast("${cookingRecipeDetails!!.recipeName} is set as trending.")
                }
                else
                {
                    showToast("${cookingRecipeDetails!!.recipeName} is set as normal.")
                }
            }
        }

    }

    fun deleteCookingRecipeDetailAlert() {

        val builder = AlertDialog.Builder(this)

        builder.setTitle("Confirmation")

        builder.setMessage("Are you sure do you want to delete this recipe?")

        builder.setPositiveButton("YES"){dialog, which ->
            // Do something when user press the positive button

            cookingRecipeDetails!!.key.let {

                if (it != null && it.isNotEmpty())
                {

                    Log.d("Delete Recipe Key", it!!)

                    showProgressBar()

                    FirebaseDatabase.getInstance().getReference(DatabaseTable.cookingrecipedetails.toString() + "/" + it!!).removeValue()

                    //ISNEEDED
//                FirebaseDataHandler.updateCookingRecipeItemInList(cookingRecipeDetails!!, true)

                    hideProgressBar()

                    val result = Intent()
                    setResult(Activity.RESULT_OK, result)
                    finish()

                    showToast("${cookingRecipeDetails!!.recipeName} Deleted")
                }
            }
        }

        builder.setNegativeButton("No"){dialog,which ->

        }

        val dialog: AlertDialog = builder.create()

        dialog.show()
    }

}
