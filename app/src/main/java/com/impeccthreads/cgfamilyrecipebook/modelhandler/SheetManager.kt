package com.impeccthreads.cgfamilyrecipebook.modelhandler

import java.io.IOException
import java.util.*

class SheetManager {

//    private val APPLICATION_NAME = "Google Sheets API Java Quickstart"
//    private val JSON_FACTORY: JsonFactory = JacksonFactory.getDefaultInstance()
//    private val TOKENS_DIRECTORY_PATH = "tokens"
//
//    /**
//     * Global instance of the scopes required by this quickstart.
//     * If modifying these scopes, delete your previously saved tokens/ folder.
//     */
//    private val SCOPES: List<String> =
//        Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY)
//    private val CREDENTIALS_FILE_PATH = "/credentials.json"

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
//    @Throws(IOException::class)
//    private fun getCredentials(HTTP_TRANSPORT: NetHttpTransport): Credential? {
//        // Load client secrets.
//        val `in`: InputStream =
//            SheetManager::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
//                ?: throw FileNotFoundException("Resource not found: $CREDENTIALS_FILE_PATH")
//        val clientSecrets =
//            GoogleClientSecrets.load(JSON_FACTORY, InputStreamReader(`in`))
//
//        // Build flow and trigger user authorization request.
//        val flow = (GoogleAuthorizationCodeFlow.Builder(
//            HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES
//        )
//            .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH))).accessType =
//            "offline")
//            .build()
//        val receiver = (LocalServerReceiver.Builder().port = 8888).build()
//        return AuthorizationCodeInstalledApp(flow, receiver).authorize("user")
//    }

//    fun getSheetData() {
//        val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
//        val spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms"
//        val range = "Class Data!A2:E"
//        val service =
//            Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName(APPLICATION_NAME)
//                .build()
//        val response: ValueRange = service.spreadsheets().values()[spreadsheetId, range]
//            .execute()
//        val values: List<List<Any>> =
//            response.getValues()
//        if (values == null || values.isEmpty()) {
//            println("No data found.")
//        } else {
//            println("Name, Major")
//            for (row in values) {
//                // Print columns A and E, which correspond to indices 0 and 4.
//                System.out.printf("%s, %s\n", row[0], row[4])
//            }
//        }
//    }

//    companion object {
//        /**
//         * Prints the names and majors of students in a sample spreadsheet:
//         * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
//         */
//
//        @Throws(IOException::class, GeneralSecurityException::class)
//        @JvmStatic
//        fun main(sheetManager: SheetManager, args: Array<String>) {
//            // Build a new authorized API client service.
//            val HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport()
//            val spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms"
//            val range = "Class Data!A2:E"
//            val service =
//                Sheets.Builder(HTTP_TRANSPORT,
//                    sheetManager.JSON_FACTORY,
//                    sheetManager.getCredentials(HTTP_TRANSPORT)
//                )
//                    .setApplicationName(sheetManager.APPLICATION_NAME)
//                    .build()
//            val response: ValueRange = service.spreadsheets().values()[spreadsheetId, range]
//                .execute()
//            val values: List<List<Any>> =
//                response.getValues()
//            if (values == null || values.isEmpty()) {
//                println("No data found.")
//            } else {
//                println("Name, Major")
//                for (row in values) {
//                    // Print columns A and E, which correspond to indices 0 and 4.
//                    System.out.printf("%s, %s\n", row[0], row[4])
//                }
//            }
//        }
//    }
}