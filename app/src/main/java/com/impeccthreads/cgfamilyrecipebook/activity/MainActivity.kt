package com.impeccthreads.cgfamilyrecipebook.activity

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.Scope
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.application.BaseActivity
import com.impeccthreads.cgfamilyrecipebook.application.Constants
import com.impeccthreads.cgfamilyrecipebook.dto.ItemWidth
import com.impeccthreads.cgfamilyrecipebook.dto.CookingRecipeDetails
import com.impeccthreads.cgfamilyrecipebook.utility.CodeSnippet
import com.impeccthreads.cgfamilyrecipebook.utility.PreferenceHelper
import org.apache.poi.ss.usermodel.IndexedColors
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream

class MainActivity : BaseActivity() {

    companion object {
        private const val TAG = "MainActivity"
        private const val REQUEST_SIGN_IN = 1
    }

    var cookingRecipeDetailsList: ArrayList<CookingRecipeDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestPermission()

        PreferenceHelper.isRecipeBookSynced().let {
            if (it!!)
            {

                //Download Recipe Book
                Log.d(TAG, "Is Doc Synced: $it!!")
                showToast("Document Synced")

//                val file = CodeSnippet.getFileFromExternalStorage(Constants.AppKeys.DESTINATION_FOLDER_NAME, Constants.recipeDocumentNameWithExtension)
//                readUserDetailXlsSheetData(Uri.fromFile(file))


            }
            else
            {
                showToast("Document Not Synced")

            }
        }

//        GoogleSignIn.getLastSignedInAccount(this)?.also { account ->
//            Log.d(TAG, "account=${account.displayName}")
//            val scopes = listOf(SheetsScopes.SPREADSHEETS_READONLY)
//            val credential = GoogleAccountCredential.usingOAuth2(this, scopes)
//            credential.selectedAccount = account.account
//
//            val jsonFactory = JacksonFactory.getDefaultInstance()
//            // GoogleNetHttpTransport.newTrustedTransport()
//            val httpTransport =  AndroidHttp.newCompatibleTransport()
//            val service = Sheets.Builder(httpTransport, jsonFactory, credential)
//                .setApplicationName(getString(R.string.app_name))
//                .build()
//
//
//            readSpreadSheet(service, Constants.recipeSheetId, "Sheet1")
//        }

//        requestSignIn()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        val id = item.getItemId()

//        if (id == R.id.action_sync) {
//            return true
//        }
//        else if (id == R.id.action_filter) {
//
//            this.showToast("Coming Soon..!")
//            return true
//        }

        return super.onOptionsItemSelected(item)

    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (requestCode == REQUEST_SIGN_IN) {
//            if (resultCode == RESULT_OK) {
//                GoogleSignIn.getSignedInAccountFromIntent(data)
//                    .addOnSuccessListener { account ->
//                        val scopes = listOf(SheetsScopes.SPREADSHEETS_READONLY)
//                        val credential = GoogleAccountCredential.usingOAuth2(this, scopes)
//                        credential.selectedAccount = account.account
//
//                        val jsonFactory = JacksonFactory.getDefaultInstance()
//                        // GoogleNetHttpTransport.newTrustedTransport()
//                        val httpTransport =  AndroidHttp.newCompatibleTransport()
//                        val service = Sheets.Builder(httpTransport, jsonFactory, credential)
//                            .setApplicationName(getString(R.string.app_name))
//                            .build()
//
//
//                        readSpreadSheet(service, Constants.recipeSheetId, "Sheet1")
//                    }
//                    .addOnFailureListener { e ->
//                        Log.d(TAG, e.localizedMessage)
//                    }            }
//        }
//    }

    fun getSignedInAccount() {

    }

//    private fun requestSignIn() {
//
//        val signInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            // .requestEmail()
//            .requestScopes(Scope(SheetsScopes.SPREADSHEETS_READONLY))
//            .build()
//        val client = GoogleSignIn.getClient(this, signInOptions)
//
//        startActivityForResult(client.signInIntent, REQUEST_SIGN_IN)
//    }
//
//    fun readSpreadSheet(service: Sheets, spreadsheetId : String, range : String) {
//        Log.d(TAG, service.toString())
//
//        val result = service.spreadsheets().values().get(spreadsheetId, range).execute()
//
//        if (result.getValues() != null)
//        {
//            val numRows = result.getValues().size
//            Log.d(TAG, numRows.toString())
//        }
//    }

    fun generateXSSFWorkbookXLSheet(menus: java.util.ArrayList<CookingRecipeDetails>) {

        val itemWidthList: java.util.ArrayList<ItemWidth> = arrayListOf()
        val outputFile = CodeSnippet.createFile(Constants.AppKeys.DESTINATION_FOLDER_NAME, Constants.recipeDocumentNameWithExtension)

        if (outputFile != null)
        {
            val workbook = XSSFWorkbook()
            val createHelper = workbook.getCreationHelper()

            val sheet = workbook.createSheet(Constants.recipeDocumentNameWithExtension)

            val headerFont = workbook.createFont()
            headerFont.fontName = "Serif"
            headerFont.setBold(true)
            headerFont.setColor(IndexedColors.BLUE.getIndex())

            val headerCellStyle = workbook.createCellStyle()
            headerCellStyle.setFont(headerFont)

            // Row for Header
            val headerRow = sheet.createRow(0)

            // Header
            for ((colIndex, fieldName) in Constants.recipeItemFields.withIndex()) {
                val cell = headerRow.createCell(colIndex)
                cell.setCellValue(fieldName.toUpperCase())
                cell.setCellStyle(headerCellStyle)

                val itemWidth = ItemWidth()
                itemWidth.fieldName = fieldName
                itemWidth.maxChar = fieldName.length
                itemWidthList.add(itemWidth)
            }

            for ((index, recipeItem) in menus.withIndex()) {
                val rowIndex = index + 1
                val row = sheet.createRow(rowIndex)

                for ((fieldIndex, fieldName) in Constants.recipeItemFields.withIndex())
                {
                    val cell = row.createCell(fieldIndex)
                    cell.columnIndex

                    val fieldValue = recipeItem.getValueForField(fieldName)

                    for ((index, itemWidth)in itemWidthList.withIndex())
                    {
                        if (itemWidth.fieldName == fieldName)
                        {
                            val previousFieldValueMaxChar = itemWidth.maxChar

                            if (previousFieldValueMaxChar < fieldValue.length)
                            {
                                itemWidthList[index].maxChar = fieldValue.length
                            }
                        }
                    }

                    row.createCell(fieldIndex).setCellValue(fieldValue)
                }
            }

            for (colIndex in Constants.recipeItemFields.indices) {

                for ((index, itemWidth)in itemWidthList.withIndex())
                {
                    if (itemWidth.fieldName == Constants.recipeItemFields[colIndex])
                    {
                        var width = ((itemWidth.maxChar * 1.14388).toInt() * 256) + 800

//                        if (width > 255)
//                        {
//                            width = 255
//                        }

                        sheet.setColumnWidth(colIndex, width)
                        continue
                    }
                }
            }

            val fileOut = FileOutputStream(outputFile)
            workbook.write(fileOut)
            fileOut.flush()
            fileOut.close()

            showToast("XLS File Created")
        }
        else
        {
//            hideProgressBar()
        }
    }


}
