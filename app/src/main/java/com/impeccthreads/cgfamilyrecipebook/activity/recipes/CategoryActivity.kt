package com.impeccthreads.cgfamilyrecipebook.activity.recipes

import android.app.Activity
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.adapters.CategoryAdapter
import com.impeccthreads.cgfamilyrecipebook.application.BaseActivity
import com.impeccthreads.cgfamilyrecipebook.dto.RecipeItem
import com.impeccthreads.cgfamilyrecipebook.modelhandler.FirebaseDataHandler
import kotlinx.android.synthetic.main.activity_category.*


class CategoryActivity : BaseActivity(), CategoryAdapter.CategoryAdapterListener, FirebaseDataHandler.FirebaseDataHandlerListener { //RecipeDataHandler.RecipeDataHandlerListener,

    companion object {
        val PICK_ADDED_COOKING_RECIPE_CATEGORY_REQUEST_CODE = 11
        val RECIPE_ITEM_DETAILS = "RECIPE_ITEM_DETAILS"
        val IS_ADDING_RECIPE_CATEGORY = "IS_ADDING_RECIPE_CATEGORY"
    }

    var recipeItemList: ArrayList<RecipeItem> = arrayListOf()
    var filteredRecipeItemList: ArrayList<RecipeItem> = arrayListOf()

    var categoryList: ArrayList<RecipeItem> = arrayListOf()
//    var dataHandler = RecipeDataHandler(this, this)
    var firebaseDataHandler = FirebaseDataHandler(this)
    var isSearchEnabled: Boolean = false
    var addActionBarItem: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        this.title = "Recipe Category List"

//        val sheetQuickStart =  SheetQuickStart()
//        sheetQuickStart.getSheetData()

//        requestPermission()

        showProgressBar()
        firebaseDataHandler.getCookingRecipeDetailsList()


//        if (dataHandler.checkDocumentAvailable())
//        {
//            showProgressBar()
//            dataHandler.readRecipeBookXlsSheetData()
//        }
//        else
//        {
//            loadRecipeItemByCategory()
////            showProgressBar()
////            dataHandler.downloadFileForURL()
//        }

//        recipeItemSearchBar.queryHint = "Search"
//        recipeItemSearchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//            override fun onQueryTextSubmit(query: String?): Boolean {
//                return false
//            }
//            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText != null) {
//                    val adaptor = (listViewCategoryList.adapter as CategoryAdapter)
//                    val filteredAry = ArrayList(categoryList.filter{ it.toLowerCase().contains(newText.toLowerCase()) })
//                    adaptor.setItemList(filteredAry)
//                }
//                return true
//            }
//        })

    }

    fun getCookingRecipeDetailsFromAsset() {
//        var jsonString: String = assets.list("cookingrecipedetails.json").bufferedReader().use { it.readText() }

        //
//        val typeToken = object : TypeToken<List>() {}.type
//        val authors = Gson().fromJson<List>(json, typeToken)

//        val allCookingCookingRecipeDetailsList = object : TypeToken<ArrayList<CookingRecipeDetails>>() {}.type
//        AppCacheManager.allCookingRecipeList = Gson().fromJson(jsonString, allCookingCookingRecipeDetailsList)
//        cookingRecipeDetailsListReceived()
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        super.onPrepareOptionsMenu(menu)
        addActionBarItem = menu.findItem(R.id.action_add)
        val icon = resources.getDrawable(R.drawable.add)
        icon.setColorFilter(resources.getColor(R.color.white), PorterDuff.Mode.SRC_IN)
        addActionBarItem?.icon = icon
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        val searchViewItem = menu.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(searchViewItem) as SearchView
        searchViewItem.setOnMenuItemClickListener{
            isSearchEnabled = true
            updateActionBarButtonVisibilityWhenSearchEnabled()
            true
        }
        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchView.clearFocus()
                /*   if(list.contains(query)){
                    adapter.getFilter().filter(query);
                }else{
                    Toast.makeText(MainActivity.this, "No Match found",Toast.LENGTH_LONG).show();
                }*/return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                isSearchEnabled = true
                updateActionBarButtonVisibilityWhenSearchEnabled()

                val adaptor = (listViewCategoryList.adapter as CategoryAdapter)
                filteredRecipeItemList = ArrayList(recipeItemList.filter{ it.recipeName.toLowerCase().contains(newText.toLowerCase()) })
                adaptor.setItemList(filteredRecipeItemList)

                return false
            }
        })

        searchView.setOnCloseListener(SearchView.OnCloseListener {
            isSearchEnabled = false
            updateActionBarButtonVisibilityWhenSearchEnabled()

            val adaptor = (listViewCategoryList.adapter as CategoryAdapter)
            adaptor.setItemList(categoryList)

            false
        })

        return super.onCreateOptionsMenu(menu)
    }

    fun updateActionBarButtonVisibilityWhenSearchEnabled() {
//        addActionBarItem?.setVisible(!isSearchEnabled)
//        supportActionBar?.setDisplayShowTitleEnabled(!isSearchEnabled)
//        supportActionBar?.setHomeButtonEnabled(!isSearchEnabled)
//        supportActionBar?.setDisplayHomeAsUpEnabled(!isSearchEnabled)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

//        if (id == R.id.action_sync) {
//            showProgressBar()
//            dataHandler.downloadFileForURL()
//            return true
//        }
//        if (id == android.R.id.home) {
//            finish()
//            return true
//        }
//        else if (id == R.id.action_filter) {
//            this.showToast("Coming Soon..!")
//            return true
//        }

        if (id == R.id.action_add) {
            if (!isSearchEnabled) {
                val intent = Intent(this, AddCookingRecipeActivity::class.java)
                intent.putExtra(IS_ADDING_RECIPE_CATEGORY, true)
                startActivityForResult(intent, PICK_ADDED_COOKING_RECIPE_CATEGORY_REQUEST_CODE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_ADDED_COOKING_RECIPE_CATEGORY_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                //ISNEEDED
                Log.d("Category Activity", "Category Added")
//                            showProgressBar()
//                            firebaseDataHandler.getCookingRecipeDetailsList()

            }
        }
    }

    fun loadRecipeItemByCategory() {
        if (categoryList.count() == 0)
        {
            textViewNoRecipe.visibility = View.VISIBLE
        }
        else
        {
            textViewNoRecipe.visibility = View.GONE
            listViewCategoryList.layoutManager = LinearLayoutManager(applicationContext, RecyclerView.VERTICAL, false)
            listViewCategoryList.adapter = CategoryAdapter(this, categoryList, this)
        }
    }


    //Recycler Listener
    override fun didSelectViewAtPosition(position: Int) {

        if (isSearchEnabled)
        {
            var intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra(RECIPE_ITEM_DETAILS, filteredRecipeItemList[position])
            startActivity(intent)
        }
        else
        {
            var intent = Intent(this, RecipeActivity::class.java)
            intent.putExtra(RECIPE_ITEM_DETAILS, categoryList[position])
            startActivity(intent)
        }

    }

    fun hideProgressBar() {
        loadingpanelmask_category.visibility = View.GONE
        loadingpanel_category.visibility = View.GONE
    }

    fun showProgressBar() {
        loadingpanelmask_category.visibility = View.VISIBLE
        loadingpanel_category.visibility = View.VISIBLE
    }

//    override fun recipeItemListReceived() {
//        recipeItemList = RecipeDataHandler.getAllRecipeItem()
//        categoryList = RecipeDataHandler.getRecipeCategory()
//        loadRecipeItemByCategory()
//    }

    override fun apiHitFailure() {
        hideProgressBar()
    }

    override fun cookingRecipeDetailsListReceived() {
        hideProgressBar()
        reloadRecyclerView()
    }

    fun reloadRecyclerView() {
        recipeItemList = FirebaseDataHandler.getAllRecipeItemList()
        categoryList = FirebaseDataHandler.getRecipeCategory()
        loadRecipeItemByCategory()
    }

    override fun cookingScheduleListReceived() {
    }
}