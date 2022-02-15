package com.impeccthreads.cgfamilyrecipebook.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.impeccthreads.cgfamilyrecipebook.R
import com.impeccthreads.cgfamilyrecipebook.dto.CookingScheduleDetails
import com.impeccthreads.cgfamilyrecipebook.utility.listen

class CookingScheduleListAdapter(val mContext: Context, var cookingScheduleDetailsList: ArrayList<CookingScheduleDetails>, val delegate: CookingScheduleListAdapterListener?): RecyclerView.Adapter<CookingScheduleListAdapter.CookingScheduleListViewHolder>() {

    interface CookingScheduleListAdapterListener {

        fun didSelectViewAtPosition(position: Int)
    }

    override fun getItemCount(): Int {
        return cookingScheduleDetailsList.size
    }

    fun setItemList(updateCookingScheduleDetails: ArrayList<CookingScheduleDetails>) {
        this.cookingScheduleDetailsList = updateCookingScheduleDetails
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CookingScheduleListViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.item_cooking_schedule_list, parent, false)
        val viewHolder = CookingScheduleListViewHolder(cellForRow).listen { position, type ->
            delegate?.didSelectViewAtPosition(position)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CookingScheduleListViewHolder, position: Int) {

        val cafeSlotDetail = cookingScheduleDetailsList.get(position)

        holder.textViewDate.text = cafeSlotDetail.scheduledate
//        holder.textViewBreakfast.text = cafeSlotDetail.breakfast
//        holder.textViewLunch.text = cafeSlotDetail.lunch
//        holder.textViewEvening.text = cafeSlotDetail.evening
//        holder.textViewDinner.text = cafeSlotDetail.dinner
//        holder.textViewOthers.text = cafeSlotDetail.others
    }


    class CookingScheduleListViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        val textViewDate = view.findViewById<TextView>(R.id.textViewDate) as TextView
//        val textViewBreakfast = view.findViewById<TextView>(R.id.textViewBreakfast) as TextView
//        val textViewLunch = view.findViewById<TextView>(R.id.textViewLunch) as TextView
//        val textViewEvening = view.findViewById<TextView>(R.id.textViewEvening) as TextView
//        val textViewDinner = view.findViewById<TextView>(R.id.textViewDinner) as TextView
//        val textViewOthers = view.findViewById<TextView>(R.id.textViewOthers) as TextView

    }
}