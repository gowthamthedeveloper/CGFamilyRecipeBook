package com.impeccthreads.cgfamilyrecipebook.activity.recipes

import android.R.attr.password
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.dosavillage.cxtdebug.adapter.AddTableItemAdapter
import com.google.firebase.database.FirebaseDatabase
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.application.BaseActivity
import com.impeccthreads.cgfamilyrecipebook.application.DatabaseTable
import com.impeccthreads.cgfamilyrecipebook.dto.CookingRecipeDetails
import kotlinx.android.synthetic.main.activity_edit_cooking_recipe.*


class EditCookingRecipeMethodActivity : BaseActivity(), AddTableItemAdapter.AddTableItemAdapterListener {

    companion object {
        val EDITED_COOKING_RECIPE_DETAILS = "EDITED_COOKING_RECIPE_DETAILS"
    }

    var cookingRecipeDetails : CookingRecipeDetails? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_cooking_recipe)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (intent.hasExtra(RecipeDetailActivity.EDIT_COOKING_RECIPE_DETAIL))
        {
            cookingRecipeDetails = intent.getParcelableExtra(RecipeDetailActivity.EDIT_COOKING_RECIPE_DETAIL)

            this.title = "Edit " + cookingRecipeDetails!!.recipeName
        }

        ingredientsTextTypeSwitch?.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked)
            {
                textViewIngredients.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
            }
            else
            {
                textViewIngredients.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
            }

            textViewIngredients.maxLines = 50
            textViewIngredients.isSingleLine = false
            textViewIngredients.setSelection(textViewMethods.getText().length);
        })

        methodTextTypeSwitch?.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked)
            {
                textViewMethods.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
            }
            else
            {
                textViewMethods.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
            }

            textViewMethods.maxLines = 50
            textViewMethods.isSingleLine = false
            textViewMethods.setSelection(textViewMethods.getText().length);
        })

        noteTextTypeSwitch?.setOnCheckedChangeListener({ _, isChecked ->
            if (isChecked)
            {
                textViewTips.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES)
            }
            else
            {
                textViewTips.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS)
            }

            textViewTips.maxLines = 50
            textViewTips.isSingleLine = false
            textViewTips.setSelection(textViewMethods.getText().length);
        })


        updateEditCookingRecipeFieldValues()
    }

    override fun onBackPressed() {
        updateRecipeItem()
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

    fun updateEditCookingRecipeFieldValues() {

        if (cookingRecipeDetails!!.Chef.toUpperCase() == "GOWTHAM") {
            textViewChef.setText("The Great Chef Gowtham's Recipe".toUpperCase())
        } else {
            textViewChef.setText("Chef ${cookingRecipeDetails!!.Chef}'s Recipe".toUpperCase())
        }

        textViewPreparationTime.setText(cookingRecipeDetails!!.preparationTime)
        textViewIngredients.setText(cookingRecipeDetails!!.ingredeints)
        textViewMethods.setText(cookingRecipeDetails!!.methods)
        textViewTips.setText(cookingRecipeDetails!!.note)
    }

    fun showProgressBar() {
        loadingpanelmask_editCookingRecipereport.visibility = View.VISIBLE
        loadingPanel_editCookingRecipereport.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        loadingpanelmask_editCookingRecipereport.visibility = View.GONE
        loadingPanel_editCookingRecipereport.visibility = View.GONE
    }

    fun updateRecipeDetails() {

        cookingRecipeDetails!!.preparationTime = textViewPreparationTime.text.toString()
        cookingRecipeDetails!!.ingredeints = textViewIngredients.text.toString().trim()
        cookingRecipeDetails!!.methods = textViewMethods.text.toString().trim()
        cookingRecipeDetails!!.note = textViewTips.text.toString().trim()
    }


    fun btnSaveCookingRecipeDetailsOnClick(view: View) {

        updateRecipeItem()
    }

    fun updateRecipeItem() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Confirmation")

        builder.setMessage("Are you sure do you want to save the recipe?")

        builder.setPositiveButton("YES") { dialog, which ->

            cookingRecipeDetails!!.key.let {

                if (it != null && it.isNotEmpty())
                {
                    updateRecipeDetails()

                    showProgressBar()

                    //Update Cooking Schedule
                    val updateCafeSlotRef = FirebaseDatabase.getInstance().getReference(
                        DatabaseTable.cookingrecipedetails.toString() + "/" + it!!
                    )
                    updateCafeSlotRef.updateChildren(cookingRecipeDetails!!.toHashMap())

                    hideProgressBar()

                    val result = Intent()
                    result.putExtra(EDITED_COOKING_RECIPE_DETAILS, cookingRecipeDetails!!)
                    setResult(Activity.RESULT_OK, result)
                    finish()

                    showToast("${cookingRecipeDetails!!.recipeName} is updated successfully")
                }
            }
        }


        builder.setNegativeButton("No"){ dialog, which ->

        }

        val dialog: AlertDialog = builder.create()

        dialog.show()

    }

    //LISTENER METHODS
    override fun didSelectViewAtPosition(position: Int) {

    }
}
