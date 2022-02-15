package com.impeccthreads.cgfamilyrecipebook.modelhandler;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class SheetDataHandler {

//    // The ID of the spreadsheet to retrieve data from.
//    String spreadsheetId = "16ETQMlDpyyhanlhjIuLeJph753-hCz40GpRR3M8VqXo";
//
//    // The A1 notation of the values to retrieve.
//    String range = "Sheet2";
//
//    // How values should be represented in the output.
//    // The default render option is ValueRenderOption.FORMATTED_VALUE.
//    String valueRenderOption = "FORMATTED_VALUE";
//
//    // How the input data should be interpreted.
//    String valueInputOption = "USER_ENTERED";
//
//    // How dates, times, and durations should be represented in the output.
//    // This is ignored if value_render_option is
//    // FORMATTED_VALUE.
//    // The default dateTime render option is [DateTimeRenderOption.SERIAL_NUMBER].
//    String dateTimeRenderOption = "";
//
//    public static Sheets createSheetsService() throws IOException, GeneralSecurityException {
//        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//
//        // TODO: Change placeholder below to generate authentication credentials. See
//        // https://developers.google.com/sheets/quickstart/java#step_3_set_up_the_sample
//        //
//        // Authorize using one of the following scopes:
//        //   "https://www.googleapis.com/auth/drive"
//        //   "https://www.googleapis.com/auth/drive.file"
//        //   "https://www.googleapis.com/auth/drive.readonly"
//        //   "https://www.googleapis.com/auth/spreadsheets"
//        //   "https://www.googleapis.com/auth/spreadsheets.readonly"
//        GoogleCredential credential = null;
//
//        return new Sheets.Builder(httpTransport, jsonFactory, credential)
//                .setApplicationName("Google-SheetsSample/0.1")
//                .build();
//    }
//
//    public void getSheetData() throws IOException, GeneralSecurityException {
//        Sheets sheetsService = createSheetsService();
//        Sheets.Spreadsheets.Values.Get request =
//                sheetsService.spreadsheets().values().get(spreadsheetId, range);
//        request.setValueRenderOption(valueRenderOption);
////        request.setDateTimeRenderOption(dateTimeRenderOption);
//
//        ValueRange response = request.execute();
//
//        System.out.println(response);
//
//        int numRows = response.getValues() != null ? response.getValues().size() : 0;
//        System.out.printf("%d rows retrieved.", numRows);
//    }
//
//    public void  updateSheetData() throws IOException, GeneralSecurityException {
//        Sheets sheetsService = createSheetsService();
//
//        ValueRange requestBody = new ValueRange();
//
//        Sheets.Spreadsheets.Values.Update request =
//                sheetsService.spreadsheets().values().update(spreadsheetId, range, requestBody);
//        request.setValueInputOption(valueInputOption);
//
//        UpdateValuesResponse response = request.execute();
//
//        // TODO: Change code below to process the `response` object:
//        System.out.println(response);
//    }
}
