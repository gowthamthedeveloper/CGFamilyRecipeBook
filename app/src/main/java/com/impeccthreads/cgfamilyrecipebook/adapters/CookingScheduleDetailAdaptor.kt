package com.impeccthreads.cgfamilyrecipebook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.dto.FieldValueListDto
import com.impeccthreads.cgfamilyrecipebook.utility.listen

class CookingScheduleDetailAdaptor(val mContext: Context, var item: FieldValueListDto, val delegate: CookingScheduleDetailAdaptorListener?): RecyclerView.Adapter<CookingScheduleDetailAdaptor.CookingScheduleDetailViewHolder>() {

    interface CookingScheduleDetailAdaptorListener {

        fun didSelectViewAtPosition(position: Int)
    }

    fun setItemList(updateItem: FieldValueListDto) {
        this.item = updateItem
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return item.fieldValues.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookingScheduleDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.item_cooking_schedule_detail, parent, false)
        val viewHolder = CookingScheduleDetailViewHolder(cellForRow).listen { position, type ->
            delegate?.didSelectViewAtPosition(position)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CookingScheduleDetailViewHolder, position: Int) {

        val fieldValue = item.fieldValues[position]

        holder.textViewTitle.text = fieldValue.name
        holder.textViewValue.text = fieldValue.value

    }


    class CookingScheduleDetailViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val textViewTitle = view.findViewById<TextView>(R.id.textViewTitle) as TextView
        val textViewValue = view.findViewById<TextView>(R.id.textViewValue) as TextView
    }
}