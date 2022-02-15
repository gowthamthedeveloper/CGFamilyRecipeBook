package com.impeccthreads.cgfamilyrecipebook.activity.recipes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dosavillage.cxtdebug.adapter.AddTableItemAdapter
import com.google.firebase.database.FirebaseDatabase
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.application.*
import com.impeccthreads.cgfamilyrecipebook.dto.CookingRecipeDetails
import com.impeccthreads.cgfamilyrecipebook.dto.FieldValueDto
import com.impeccthreads.cgfamilyrecipebook.dto.RecipeItem
import com.impeccthreads.cgfamilyrecipebook.modelhandler.FirebaseDataHandler
import kotlinx.android.synthetic.main.activity_add_cooking_recipe.*

class AddCookingRecipeActivity : BaseActivity(), AddTableItemAdapter.AddTableItemAdapterListener {

    companion object {
        val ADDED_COOKING_RECIPE_DETAILS = "ADDED_COOKING_RECIPE_DETAILS"
    }

    var cookingRecipeFieldValues: ArrayList<FieldValueDto> = arrayListOf()
    var recipeItemDetails : RecipeItem? = null
    var isAddingRecipeCategory = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_cooking_recipe)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(CategoryActivity.IS_ADDING_RECIPE_CATEGORY))
        {
            isAddingRecipeCategory = intent!!.getBooleanExtra(CategoryActivity.IS_ADDING_RECIPE_CATEGORY, false)
        }

        if (isAddingRecipeCategory)
        {
            this.title = Constants.addCookingRecipeCategory

            cookingRecipeFieldValues = Constants.getAddCookingRecipeCategoryFieldValueList()
        }
        else
        {
            this.title = Constants.addCookingRecipeItem

            cookingRecipeFieldValues = Constants.getAddCookingRecipeFieldValueList()

            if (intent.hasExtra(CategoryActivity.RECIPE_ITEM_DETAILS))
            {
                recipeItemDetails = intent.getParcelableExtra(CategoryActivity.RECIPE_ITEM_DETAILS)
            }

            updateAddCookingRecipeFieldValues()
        }

        configureRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun updateAddCookingRecipeFieldValues() {

        for (i in cookingRecipeFieldValues.indices) {

            when (cookingRecipeFieldValues[i].name)
            {
                CookingRecipeDetailsTable.title.toString() ->
                    cookingRecipeFieldValues[i].value = recipeItemDetails!!.title.toString()
                CookingRecipeDetailsTable.subTitle.toString() ->
                    cookingRecipeFieldValues[i].value = recipeItemDetails!!.subTitle
            }
        }
    }

    fun configureRecyclerView() {

        listViewAddCookingRecipeItem.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
        listViewAddCookingRecipeItem.adapter = AddTableItemAdapter(this, cookingRecipeFieldValues, this)
    }

    fun showProgressBar() {
        loadingpanelmask_addCookingRecipereport.visibility = View.VISIBLE
        loadingPanel_addCookingRecipereport.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        loadingpanelmask_addCookingRecipereport.visibility = View.GONE
        loadingPanel_addCookingRecipereport.visibility = View.GONE
    }

    fun generateCookingRecipeRequest() : CookingRecipeDetails? {

        (listViewAddCookingRecipeItem.adapter as AddTableItemAdapter).getEnteredAttributes().let {

            if (it != null)
            {
                val request = CookingRecipeDetails()

                if (isAddingRecipeCategory)
                {
                    request.recipeName = ""
                    request.isVeg = false
                    request.combination = ""
                    request.preparationTime = ""
                }

                request.ingredeints = ""
                request.methods = ""
                request.note = ""
                request.isNewRecipe = false

                for (fieldValue in it!!)
                {
                    when (fieldValue.name)
                    {
                        CookingRecipeDetailsTable.title.toString() ->
                            request.title = fieldValue.value
                        CookingRecipeDetailsTable.subTitle.toString() ->
                            request.subTitle = fieldValue.value
                        CookingRecipeDetailsTable.recipeName.toString() ->
                            request.recipeName = fieldValue.value
                        CookingRecipeDetailsTable.foodType.toString() ->
                            if (fieldValue.value  == FoodType.veg.toString()){
                                request.isVeg = true
                            }
                            else
                            {
                                request.isVeg = false
                            }
                        CookingRecipeDetailsTable.session.toString() ->
                            request.session = fieldValue.value
                        CookingRecipeDetailsTable.combination.toString() ->
                            request.combination = fieldValue.value
                        CookingRecipeDetailsTable.preparationTime.toString() ->
                            request.preparationTime = fieldValue.value
                    }
                }


                return request
            }
        }

        return null
    }

    fun isValidationSuccess(recipeDetails: CookingRecipeDetails): Boolean {

        if (isAddingRecipeCategory)
        {
            if (FirebaseDataHandler.isRecipeCategoryAlreadyAdded(recipeDetails.title))
            {
                showToast(Constants.AlertMessages.recipeCategoryDuplicate)
                return false
            }
        }
        else
        {
            if (FirebaseDataHandler.isRecipeAlreadyAdded(recipeDetails.recipeName))
            {
                showToast(Constants.AlertMessages.recipeNameDuplicate)
                return false
            }
        }

        return true
    }

    fun btnSaveCookingRecipeDetailsOnClick(view: View) {

        generateCookingRecipeRequest().let {

            if (it != null) {

                if (isValidationSuccess(it)) {
                    Log.d("Recipe Request", it!!.toString())

                    val builder = AlertDialog.Builder(this)

                    builder.setTitle("Confirmation")

                    builder.setMessage("Are you sure do you want to save recipe?")

                    builder.setPositiveButton("YES") {dialog, which ->
                        // Do something when user press the positive button

                        showProgressBar()

                        //Add Cooking Recipe
                        FirebaseDatabase.getInstance().getReference(DatabaseTable.cookingrecipedetails.toString()).push().setValue(it!!.toHashMap())

                        val result = Intent()
                        setResult(Activity.RESULT_OK, result)

                        hideProgressBar()
                        finish()
                    }


                    builder.setNegativeButton("No"){dialog,which ->

                    }

                    val dialog: AlertDialog = builder.create()

                    dialog.show()
                }
            }
        }

    }

    //LISTENER METHODS
    override fun didSelectViewAtPosition(position: Int) {

    }
}
