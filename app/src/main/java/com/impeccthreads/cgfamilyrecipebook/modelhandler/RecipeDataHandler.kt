package com.impeccthreads.cgfamilyrecipebook.modelhandler

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.impeccthreads.cgfamilyrecipebook.application.AppCacheManager
import com.impeccthreads.cgfamilyrecipebook.application.Constants
import com.impeccthreads.cgfamilyrecipebook.application.DatabaseTable
import com.impeccthreads.cgfamilyrecipebook.dto.CookingScheduleDetails
import com.impeccthreads.cgfamilyrecipebook.dto.CookingRecipeDetails
import com.impeccthreads.cgfamilyrecipebook.utility.PreferenceHelper
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException

class RecipeDataHandler(val mContext: Context, val delegate: RecipeDataHandlerListener?)  {

    interface RecipeDataHandlerListener {
        fun hideProgressBar()
        fun showProgressBar()
        fun recipeItemListReceived()
        fun cookingScheduleListReceived()
    }

    companion object {
        val TAG = "RecipeDataHandler"
        val recipeDocumentNameWithExtension = "Recipe_Book.xls"
        val sheetPublicUrl = "https://docs.google.com/spreadsheets/d/e/2PACX-1vSCDtu1nujvPPI-5yUIzDeBDKOnK_D7djAmzMY1uO7krtVtlVl938VTRBTjaBBfGQdZE5_2I81LsiBD/pub?output=xlsx"

        fun getRecipeCategory(): ArrayList<String> {

            var categories: ArrayList<String> = arrayListOf()

            for (recipeItem in AppCacheManager.allCookingRecipeList)
            {
                if (recipeItem.title.isNotEmpty())
                {
                    if (!categories.contains(recipeItem.title))
                    {
                        categories.add(recipeItem.title)
                    }
                }
            }

            categories = ArrayList(categories.sorted())

            return categories
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

        fun getRecipeItemForCategory(category: String): ArrayList<CookingRecipeDetails> {
            var cookingRecipeDetailsList: ArrayList<CookingRecipeDetails> = arrayListOf()

            cookingRecipeDetailsList = AppCacheManager.allCookingRecipeList.filter { it.title!!.toLowerCase() == category.toLowerCase() && it.recipeName.isNotEmpty()} as ArrayList<CookingRecipeDetails>

            cookingRecipeDetailsList = ArrayList(cookingRecipeDetailsList.sortedBy { it.recipeName })

            return cookingRecipeDetailsList
        }

        fun getRecipeItemForRecipeName(recipeName: String): CookingRecipeDetails {

            var cookingRecipeDetailsList: ArrayList<CookingRecipeDetails> = arrayListOf()

            cookingRecipeDetailsList = AppCacheManager.allCookingRecipeList.filter { it.recipeName!!.toLowerCase() == recipeName.toLowerCase() } as ArrayList<CookingRecipeDetails>

            return cookingRecipeDetailsList[0]
        }

    }


    fun showToast(msg: String) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
    }

    fun checkDocumentAvailable(): Boolean {
        val folderDir = File(mContext.getExternalFilesDir(Constants.AppKeys.DESTINATION_FOLDER_NAME).toString())

        if (folderDir.exists())
        {
            val fileDir = File(folderDir, recipeDocumentNameWithExtension)
            return fileDir.exists()
        }

        return false
    }

    fun deleteRecipeDocument() {
        val folderDir = File(mContext.getExternalFilesDir(Constants.AppKeys.DESTINATION_FOLDER_NAME).toString())

        if (folderDir.exists())
        {
            val fileDir = File(folderDir, recipeDocumentNameWithExtension)

            if (fileDir.exists())
            {
                fileDir.delete()
            }
        }

    }

    fun createFolderDirectory() {

        val folderDir = File(mContext.getExternalFilesDir(Constants.AppKeys.DESTINATION_FOLDER_NAME).toString())

        if (!folderDir.exists()) {

            try {
                folderDir.mkdirs()
            }
            catch (e: IOException) {
                e.printStackTrace()
            }
        }
        else
        {
            print("Directory Exist")
        }

    }

    fun getRecipeFile(): File? {
        val folderDir = File(mContext.getExternalFilesDir(Constants.AppKeys.DESTINATION_FOLDER_NAME).toString())

        if (folderDir.exists()) {

            val fileDir = File(folderDir, recipeDocumentNameWithExtension)

            if (fileDir.exists())
            {
                return fileDir
            }
        }

        return  null
    }

    fun downloadFileForURL() {
        createFolderDirectory()
        deleteRecipeDocument()

        var request = DownloadManager.Request(Uri.parse(sheetPublicUrl))
            .setTitle(recipeDocumentNameWithExtension)
            .setDescription("Downloading...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            .setAllowedOverMetered(true)
//            .setDestinationInExternalPublicDir(mContext.getExternalFilesDir(Constants.AppKeys.DESTINATION_FOLDER_NAME).toString(), recipeDocumentNameWithExtension)
            .setDestinationInExternalFilesDir(mContext, Constants.AppKeys.DESTINATION_FOLDER_NAME, recipeDocumentNameWithExtension)

        var downloadManager = mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        var downloadedFileId = downloadManager.enqueue(request)

        var downloadBroadcastReceiver = object: BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

                if (id == downloadedFileId)
                {
                    if (PreferenceHelper.isRecipeBookDownloaded())
                    {
                        PreferenceHelper.setRecipeBookSynced(true)
                        showToast(Constants.AlertMessages.recipeBookUpdated)
                    }
                    else
                    {
                        PreferenceHelper.setRecipeBookDownloaded(true)
                        showToast(Constants.AlertMessages.recipeBookDownloaded)
                    }

                    Log.d(TAG, checkDocumentAvailable().toString())
                    readRecipeBookXlsSheetData()
                    delegate?.hideProgressBar()
                    delegate?.recipeItemListReceived()
                }
            }
        }

        mContext.registerReceiver(downloadBroadcastReceiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }


    fun readRecipeBookXlsSheetData() {

        getRecipeFile().let {

            if (it != null) {

                var cookingRecipeDetailsList: ArrayList<CookingRecipeDetails> = arrayListOf()

                try {

                    val workbook = XSSFWorkbook(it!!.absolutePath)
                    workbook.missingCellPolicy = Row.RETURN_NULL_AND_BLANK

//            val workbook = XSSFWorkbook(contentResolver.openInputStream(uri))

                    val sheet = workbook.getSheet(Constants.recipeSheetName)
                    val rows = sheet.iterator()

                    var title = ""
                    var subTitle = ""

                    while (rows.hasNext()) {
                        val currentRow = rows.next()

                        if (currentRow.rowNum == 0)
                        {
                            continue
                        }

                        Log.d(TAG, "Row Index: ${currentRow.rowNum}")

                        val recipeItem = CookingRecipeDetails()

                        val cellsInRow = currentRow.iterator()

                        loop@ while (cellsInRow.hasNext()) {

                            val currentCell = cellsInRow.next()
                            Log.d(TAG, "Column Index: ${currentCell.columnIndex}")

                            when(currentCell.columnIndex)
                            {
                                0 -> {
                                    if (currentCell.getStringCellValue().isNotEmpty())
                                    {
                                        title = currentCell.getStringCellValue()
                                    }

                                    recipeItem.title = title
                                }
                                1 -> {
                                    if (currentCell.getStringCellValue().isNotEmpty())
                                    {
                                        subTitle = currentCell.getStringCellValue()
                                    }

                                    recipeItem.subTitle = subTitle
                                }
                                2 -> {
                                    if (currentCell.getStringCellValue().isEmpty())
                                    {
                                        break@loop
                                    }
                                    else
                                    {
                                        recipeItem.recipeName = currentCell.getStringCellValue()
                                    }
                                }
                                3 -> {
                                    recipeItem.isVeg = currentCell.booleanCellValue
                                }
                                4 -> {
                                    recipeItem.session = currentCell.getStringCellValue()
                                }
                                5 -> {
                                    recipeItem.preparationTime = currentCell.getStringCellValue()
                                }
                                6 -> {
                                    recipeItem.combination = currentCell.getStringCellValue()
                                }
                                7 -> {
                                    recipeItem.ingredeints = currentCell.getStringCellValue()
                                }
                                8 -> {
                                    recipeItem.methods = currentCell.getStringCellValue()
                                }
                                9 -> {
                                    recipeItem.note = currentCell.getStringCellValue()
                                }
                                10 -> {
                                    recipeItem.isNewRecipe = currentCell.booleanCellValue
                                }
                            }

                            val cell = currentRow.getCell(currentRow.rowNum, Row.RETURN_NULL_AND_BLANK)

                            if (cell != null) {
                                when(cell.cellType) {
                                    Cell.CELL_TYPE_BLANK ->
                                    {
                                        Log.d(TAG, "CELL_TYPE_BLANK")
                                    }
                                    else ->
                                    {
                                        Log.d(TAG, cell.cellType.toString())
                                    }
                                }
                            }

                        }

                        if (!recipeItem.recipeName.isNullOrEmpty())
                        {
                            cookingRecipeDetailsList.add(recipeItem)
                        }

                    }

                    AppCacheManager.allCookingRecipeList = cookingRecipeDetailsList
//                    uploadCookingRecipeDetails()
                    delegate?.hideProgressBar()
                    delegate?.recipeItemListReceived()
                }
                catch (e: FileNotFoundException) {
                    println(e.message)
                }
            }
        }


    }

    fun uploadCookingRecipeDetails() {

        for (recipeItem in AppCacheManager.allCookingRecipeList)
        {
            FirebaseDatabase.getInstance().getReference(DatabaseTable.cookingrecipedetails.toString()).push().setValue(recipeItem!!.toHashMap())
        }
    }


    fun readCookingScheduleXlsSheetData() {

        getRecipeFile().let {

            if (it != null) {

                var cookingScheduleDetailsList: ArrayList<CookingScheduleDetails> = arrayListOf()

                try {

                    val workbook = XSSFWorkbook(it!!.absolutePath)
//                    val workbook = XSSFWorkbook()

                    workbook.missingCellPolicy = Row.RETURN_NULL_AND_BLANK

//            val workbook = XSSFWorkbook(contentResolver.openInputStream(uri))

                    val sheet = workbook.getSheet(Constants.cookingScheduleSheetName)
                    val rows = sheet.iterator()

                    while (rows.hasNext()) {
                        val currentRow = rows.next()

                        if (currentRow.rowNum == 0)
                        {
                            continue
                        }

                        Log.d(TAG, "Row Index: ${currentRow.rowNum}")

                        val cookingSchedule = CookingScheduleDetails()

                        val cellsInRow = currentRow.iterator()

                        loop@ while (cellsInRow.hasNext()) {

                            val currentCell = cellsInRow.next()
                            Log.d(TAG, "Column Index: ${currentCell.columnIndex}")
                            currentCell.cellType = 1

                            when(currentCell.columnIndex)
                            {
                                0 -> {
                                    if (currentCell.stringCellValue.isEmpty())
                                    {
                                        break@loop
                                    }
                                    else
                                    {
                                        cookingSchedule.key = currentCell.stringCellValue
                                    }
                                }
                                1 -> {
                                    cookingSchedule.family = currentCell.stringCellValue
                                }
                                2 -> {
                                    cookingSchedule.scheduledate = currentCell.stringCellValue
                                }
                                3 -> {
                                    cookingSchedule.breakfast = currentCell.stringCellValue
                                }
                                4 -> {
                                    cookingSchedule.lunch = currentCell.stringCellValue
                                }
                                5 -> {
                                    cookingSchedule.evening = currentCell.stringCellValue
                                }
                                6 -> {
                                    cookingSchedule.dinner = currentCell.stringCellValue
                                }
                                7 -> {
                                    cookingSchedule.others = currentCell.stringCellValue
                                }
                            }

                            val cell = currentRow.getCell(currentRow.rowNum, Row.RETURN_NULL_AND_BLANK)

                            if (cell != null) {
                                when(cell.cellType) {
                                    Cell.CELL_TYPE_BLANK ->
                                    {
                                        Log.d(TAG, "CELL_TYPE_BLANK")
                                    }
                                    else ->
                                    {
                                        Log.d(TAG, cell.cellType.toString())
                                    }
                                }
                            }

                        }

                        if (cookingSchedule.family.isNotEmpty())
                        {
                            cookingScheduleDetailsList.add(cookingSchedule)
                        }

                    }

                    AppCacheManager.allCookingScheduleDetailsList = cookingScheduleDetailsList
//                    uploadCookingScheduleDetails()
                    delegate?.hideProgressBar()
                    delegate?.cookingScheduleListReceived()
                }
                catch (e: FileNotFoundException) {
                    println(e.message)
                }
            }
        }

    }

    fun uploadCookingScheduleDetails() {

        for (cookingSchedule in AppCacheManager.allCookingScheduleDetailsList)
        {
            FirebaseDatabase.getInstance().getReference(DatabaseTable.cookingscheduledetails.toString()).push().setValue(cookingSchedule!!.toHashMap())
        }
    }

}