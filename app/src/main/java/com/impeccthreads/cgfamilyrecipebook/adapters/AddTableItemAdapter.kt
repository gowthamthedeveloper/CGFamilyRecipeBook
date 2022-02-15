package com.dosavillage.cxtdebug.adapter

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.application.AddTableFieldType
import com.impeccthreads.cgfamilyrecipebook.dto.FieldValueDto
import com.impeccthreads.cgfamilyrecipebook.utility.listen
import com.impeccthreads.cgfamilyrecipebook.utility.onChange
import com.impeccthreads.cgfamilyrecipebook.utility.showToast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class AddTableItemAdapter(val mContext: Context, var tableFields: ArrayList<FieldValueDto>, val delegate: AddTableItemAdapterListener?): RecyclerView.Adapter<AddTableItemAdapter.AddTableItemViewHolder>() {

    interface AddTableItemAdapterListener {

        fun didSelectViewAtPosition(position: Int)
    }

    var cal = Calendar.getInstance()


    override fun getItemCount(): Int {
        return tableFields.size
    }

    fun isValidationSuccess(): Boolean {

        for (tableField in tableFields)
        {
            if (tableField.isMandatory && tableField.value.isEmpty())
            {
                mContext.showToast("Enter " + tableField.name.toLowerCase() + " field", Toast.LENGTH_SHORT)
                return false
            }
        }

        return true
    }

    fun getEnteredAttributes(): ArrayList<FieldValueDto>? {

        if (isValidationSuccess()) {
//            var fieldValues = ArrayList<FieldValueDto>()
//
//            for (fieldValue in tableFields)
//            {
//                if (fieldValue.value.isNotEmpty())
//                {
//                    fieldValues.add(fieldValue)
//                }
//            }

            return tableFields
        }

        return null
    }

    fun setItemList(items: ArrayList<FieldValueDto>) {
        this.tableFields = items
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddTableItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)

        val cellForRow = layoutInflater.inflate(R.layout.item_add_table_field_value_list, parent, false)

        val viewHolder = AddTableItemViewHolder(cellForRow)

        viewHolder.listen { position, type ->
            delegate?.didSelectViewAtPosition(position)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: AddTableItemViewHolder, position: Int) {

//        Log.d( "Testing Custom",  " bind position - " + position + " adapter position - " + holder.adapterPosition + " isPrimary - " + tableFields[position].isPrimaryFilter)

        //Fill Updated values in Fields
        val fieldInfo = tableFields[position]
//        Log.d("AddTableItemViewHolder", "cell int ${position} ${tableFields[position].fieldType}")

        if (fieldInfo.isMandatory)
        {
            holder.editTextValue!!.setBackgroundResource(R.color.field_shade)
            holder.spinnerAddItemValue!!.setBackgroundResource(R.color.field_shade)
        }
        else
        {
            holder.editTextValue!!.setBackgroundResource(R.color.list_background_pressed)
            holder.spinnerAddItemValue!!.setBackgroundResource(R.color.list_background_pressed)
        }

        holder.textViewFieldName!!.text = fieldInfo.name
        holder.editTextValue!!.setText(fieldInfo.value)

        holder.editTextValue!!.onChange {
            tableFields[holder.adapterPosition].value = it
        }

        if (fieldInfo.fieldType == AddTableFieldType.date)
        {
            Log.d("Set Field Type", "Date Picker")
            holder.spinnerAddItemValue!!.visibility = View.GONE
            holder.editTextValue!!.visibility = View.VISIBLE

            val dateSetListener = object : DatePickerDialog.OnDateSetListener {
                override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                       dayOfMonth: Int) {
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateAndTimeInView(holder.editTextValue!!, fieldInfo.dateAndTimeFormat)
                }
            }

            holder.editTextValue!!.isFocusable = false
            holder.editTextValue!!.isFocusableInTouchMode = false

            holder.editTextValue!!.setOnClickListener {
                DatePickerDialog(mContext,
                        dateSetListener,
                        // set DatePickerDialog to point to today's date when it loads up
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
        else if (fieldInfo.fieldType == AddTableFieldType.time)
        {
            Log.d("Set Field Type", "Time Picker")
            holder.spinnerAddItemValue!!.visibility = View.GONE
            holder.editTextValue!!.visibility = View.VISIBLE

            val timeSetListener = object : TimePickerDialog.OnTimeSetListener {
                override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
                    cal.set(Calendar.HOUR_OF_DAY, p1)
                    cal.set(Calendar.MINUTE, p2)
                    updateDateAndTimeInView(holder.editTextValue!!, fieldInfo.dateAndTimeFormat)
                }
            }

            holder.editTextValue!!.isFocusable = false
            holder.editTextValue!!.isFocusableInTouchMode = false

            holder.editTextValue!!.setOnClickListener {
                TimePickerDialog(mContext,
                        timeSetListener,
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        fieldInfo.is24HoursFormat).show()
            }
        }
        else if (fieldInfo.fieldType == AddTableFieldType.dropdown)
        {
            Log.d("Set Field Type", "DropDown Field")
            holder.spinnerAddItemValue!!.visibility = View.VISIBLE
            holder.editTextValue!!.visibility = View.GONE

            holder.spinnerAddItemValue!!.adapter = ArrayAdapter(mContext, android.R.layout.simple_spinner_dropdown_item, fieldInfo.selectionList)
            holder.spinnerAddItemValue!!.setSelection(0)
            holder.spinnerAddItemValue!!.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                    tableFields[holder.adapterPosition].value = fieldInfo.selectionList[position]
                }
            }
        }
        else
        {
            Log.d("Set Field Type", "Default Field")
            holder.spinnerAddItemValue!!.visibility = View.GONE
            holder.editTextValue!!.visibility = View.VISIBLE

            holder.editTextValue!!.isFocusable = true
            holder.editTextValue!!.setOnClickListener(null)
        }

        holder.editTextValue!!.isEnabled = fieldInfo.isEditable
        holder.spinnerAddItemValue!!.isEnabled = fieldInfo.isEditable
    }

    fun updateDateAndTimeInView(editText: EditText, dateFormat: String) {
        val sdf = SimpleDateFormat(dateFormat, Locale.US)
        editText!!.setText(sdf.format(cal.getTime()))
    }

    class AddTableItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        var textViewFieldName: TextView? = null
        var editTextValue: EditText? = null
        var spinnerAddItemValue: Spinner? = null

        init {
            this.textViewFieldName = view.findViewById(R.id.textViewFieldName) as TextView
            this.editTextValue = view.findViewById(R.id.editTextValue) as EditText
            this.spinnerAddItemValue = view.findViewById(R.id.spinnerAddItemValue) as Spinner
        }
    }
}
