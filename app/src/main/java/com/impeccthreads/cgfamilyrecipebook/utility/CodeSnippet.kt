package com.impeccthreads.cgfamilyrecipebook.utility

import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CodeSnippet {
    companion object {
        val TAG = "CodeSnippet"

        fun isExternalStorageReadOnly(): Boolean {
            val extStorageState = Environment.getExternalStorageState()
            return if (Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState) {
                true
            } else false
        }

        fun isExternalStorageAvailable(): Boolean {
            val extStorageState = Environment.getExternalStorageState()
            return if (Environment.MEDIA_MOUNTED == extStorageState) {
                true
            } else false
        }

        fun getDestinationFolder(folderName: String): File? {

            if (isExternalStorageAvailable() && !isExternalStorageReadOnly())
            {
                val root = File(Environment.getExternalStorageDirectory(), folderName)

                if (root.exists())
                {
                    return  root
                }
                else
                {
                    if (root.mkdirs())
                    {
                        return root
                    }
                }
            }

            return null
        }

        fun createFile(folderName: String, fileName: String): File? {

            var out: File? = null
            var outStreamWriter: OutputStreamWriter? = null
            var outStream: FileOutputStream? = null

            try {

                val root = getDestinationFolder(folderName)

                if (root != null)
                {
                    out = File(root, fileName)

                    if (!out.exists())
                    {
                        out.createNewFile()
                    }

                    outStream = FileOutputStream(out, true)
                    outStreamWriter = OutputStreamWriter(outStream)
                    outStreamWriter.flush()
                    outStreamWriter.close()

                    return out
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

        fun getDateFromDateString(dateString: String, format: String, timeZone: TimeZone?): Date? {
            val simpleDateFormat = SimpleDateFormat(format)
            if (null != timeZone) {
                simpleDateFormat.timeZone = timeZone
            }

            try {
                return simpleDateFormat.parse(dateString)
            } catch (var5: ParseException) {
                var5.printStackTrace()
                return null
            }

        }

        fun getFileFromExternalStorage(folderName: String, fileName: String): File? {
            var out: File? = null

            try {

                val root = getDestinationFolder(folderName)

                if (root != null)
                {
                    out = File(root, fileName)

                    if (out.exists())
                    {
                        return out
                    }
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

            return null
        }

    }
}