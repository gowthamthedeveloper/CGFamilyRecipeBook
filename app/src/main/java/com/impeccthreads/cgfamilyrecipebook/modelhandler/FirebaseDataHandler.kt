package com.impeccthreads.cgfamilyrecipebook.modelhandler

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.impeccthreads.cgfamilyrecipebook.application.AppCacheManager
import com.impeccthreads.cgfamilyrecipebook.application.DatabaseTable
import com.impeccthreads.cgfamilyrecipebook.dto.CookingScheduleDetails
import com.impeccthreads.cgfamilyrecipebook.dto.CookingRecipeDetails
import com.impeccthreads.cgfamilyrecipebook.dto.RecipeItem

class FirebaseDataHandler(val delegate: FirebaseDataHandlerListener?) {

    interface FirebaseDataHandlerListener {
        fun apiHitFailure()
        fun cookingRecipeDetailsListReceived()
        fun cookingScheduleListReceived()
    }

    companion object {
        fun getRecipeCategory(): ArrayList<RecipeItem> {

            var recipeItemList: ArrayList<RecipeItem> = arrayListOf()

            for (item in AppCacheManager.allCookingRecipeList)
            {
                val hasRecipeItem = recipeItemList.filter { it.title == item.title }

                if (hasRecipeItem.count() == 0)
                {
                    val recipeItem = RecipeItem()
                    recipeItem.key = item.key.toString()
                    recipeItem.title = item.title
                    recipeItem.subTitle = item.subTitle
                    recipeItem.recipeName = item.recipeName
                    recipeItem.isNewRecipe = isCategoryHasTrendingRecipe(item.title)

                    recipeItemList.add(recipeItem)
                }
            }

            recipeItemList = ArrayList(recipeItemList.sortedBy { it.title })

            return recipeItemList
        }

        fun getAllRecipeItemList(): ArrayList<RecipeItem> {

            var recipeItems: ArrayList<RecipeItem> = arrayListOf()

            for (item in AppCacheManager.allCookingRecipeList)
            {
                val hasRecipeItem = recipeItems.filter { it.recipeName == item.recipeName }

                if (hasRecipeItem.count() == 0)
                {
                    val recipeItem = RecipeItem()
                    recipeItem.title = item.recipeName
                    recipeItem.recipeName = item.recipeName
                    recipeItem.isNewRecipe = item.isNewRecipe
                    recipeItems.add(recipeItem)
                }
            }

            recipeItems = ArrayList(recipeItems.sortedBy { it.recipeName })

            return recipeItems
        }

        fun isRecipeCategoryAlreadyAdded(categoryName: String): Boolean {

            val filterCategory = AppCacheManager.allCookingRecipeList.filter { it.title.toLowerCase() == categoryName.toLowerCase() }

            if (filterCategory.count() > 0)
            {
                return true
            }

            return false
        }

        fun isRecipeAlreadyAdded(recipeName: String): Boolean {

            val filterRecipe = AppCacheManager.allCookingRecipeList.filter { it.recipeName.toLowerCase() == recipeName.toLowerCase() }

            if (filterRecipe.count() > 0)
            {
                return true
            }
            return false
        }

        fun getAllRecipeItem(): ArrayList<String> {

            var recipeItems: ArrayList<String> = arrayListOf()

            for (recipeItem in AppCacheManager.allCookingRecipeList)
            {
                if (recipeItem.recipeName.isNotEmpty())
                {
                    if (!recipeItems.contains(recipeItem.recipeName))
                    {
                        recipeItems.add(recipeItem.recipeName)
                    }
                }
            }

            recipeItems = ArrayList(recipeItems.sorted())

            return recipeItems
        }

        fun isCategoryHasTrendingRecipe(category: String): Boolean {
            var cookingRecipeDetailsList: ArrayList<CookingRecipeDetails> = arrayListOf()

            cookingRecipeDetailsList = AppCacheManager.allCookingRecipeList.filter { it.title!!.toLowerCase() == category.toLowerCase() } as ArrayList<CookingRecipeDetails>

            cookingRecipeDetailsList = ArrayList(cookingRecipeDetailsList.sortedBy { it.recipeName })

            var hasTrendingRecipe = false

            for (recipeItem in cookingRecipeDetailsList)
            {
                if (recipeItem.isNewRecipe)
                {
                    hasTrendingRecipe = true
                    break
                }
            }

            return hasTrendingRecipe
        }

        fun getRecipeItemForCategory(category: String): ArrayList<CookingRecipeDetails> {
            var cookingRecipeDetailsList: ArrayList<CookingRecipeDetails> = arrayListOf()

            cookingRecipeDetailsList = AppCacheManager.allCookingRecipeList.filter { it.title!!.toLowerCase() == category.toLowerCase() } as ArrayList<CookingRecipeDetails>

            cookingRecipeDetailsList = ArrayList(cookingRecipeDetailsList.sortedBy { it.recipeName })

            return cookingRecipeDetailsList
        }

        fun getRecipeItemForRecipeName(recipeName: String): CookingRecipeDetails {

            var cookingRecipeDetailsList: ArrayList<CookingRecipeDetails> = arrayListOf()

            cookingRecipeDetailsList = AppCacheManager.allCookingRecipeList.filter { it.recipeName!!.toLowerCase() == recipeName.toLowerCase() } as ArrayList<CookingRecipeDetails>

            return cookingRecipeDetailsList[0]
        }

        fun updateCookingScheduleInList(cookingScheduleDetails: CookingScheduleDetails, isDelete: Boolean) {

            for (i in 0 until AppCacheManager.allCookingScheduleDetailsList.count())
            {
                if (AppCacheManager.allCookingScheduleDetailsList[i].key == cookingScheduleDetails!!.key)
                {
                    if (isDelete)
                    {
                        AppCacheManager.allCookingScheduleDetailsList.removeAt(i)
                    }
                    else
                    {
                        AppCacheManager.allCookingScheduleDetailsList[i] = cookingScheduleDetails!!
                    }

                    break
                }
            }
        }

        fun updateCookingRecipeItemInList(cookingRecipeDetails: CookingRecipeDetails, isDelete: Boolean) {

                for (i in 0 until AppCacheManager.allCookingRecipeList.count())
                {
                    if (cookingRecipeDetails!!.key == AppCacheManager.allCookingRecipeList[i].key)
                    {
                        if (isDelete)
                        {
                            AppCacheManager.allCookingRecipeList.removeAt(i)
                        }
                        else
                        {
                            AppCacheManager.allCookingRecipeList[i] = cookingRecipeDetails!!
                        }

                        break
                    }
                }

        }
    }

    fun getCookingRecipeDetailsList() {

        FirebaseDatabase.getInstance().getReference(DatabaseTable.cookingrecipedetails.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                var allCookingCookingRecipeDetailsList: ArrayList<CookingRecipeDetails> = arrayListOf()

                for (item: DataSnapshot in dataSnapshot.children) {
                    val recipeItem = CookingRecipeDetails(item as DataSnapshot)
                    allCookingCookingRecipeDetailsList.add(recipeItem)
                }

                AppCacheManager.allCookingRecipeList = allCookingCookingRecipeDetailsList

                delegate?.cookingRecipeDetailsListReceived()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                delegate?.apiHitFailure()
            }
        })
    }

    fun getCookingScheduleDetailsList() {

        FirebaseDatabase.getInstance().getReference(DatabaseTable.cookingscheduledetails.toString()).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                var allCookingScheduleDetailsList: ArrayList<CookingScheduleDetails> = arrayListOf()

                for (item: DataSnapshot in dataSnapshot.children) {
                    val cookingSchedule = CookingScheduleDetails(item as DataSnapshot)
                    allCookingScheduleDetailsList.add(cookingSchedule)
                }

                allCookingScheduleDetailsList = ArrayList(allCookingScheduleDetailsList.sortedByDescending { it.scheduledatetime!! })

                AppCacheManager.allCookingScheduleDetailsList = allCookingScheduleDetailsList

                delegate?.cookingScheduleListReceived()
            }

            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                delegate?.apiHitFailure()
            }
        })
    }

}