package com.brustoloni.revoluttestapp.presentation.conversions

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.rate_item.view.*
import com.brustoloni.revoluttestapp.data.model.CurrencysModel
import com.brustoloni.revoluttestapp.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.math.BigDecimal
import android.support.v7.widget.SimpleItemAnimator

class ConversionsAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var mCurrenciesLast: ArrayList<CurrencysModel> = arrayListOf()
    private var mFlagFirst = true
    private lateinit var mRecyclerView: RecyclerView
    private var mFactor: BigDecimal = 1.0.toBigDecimal()

    private val clickSubject = PublishSubject.create<String>()
    val clickEvent: Observable<String> = clickSubject

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView = recyclerView
    }

    override fun getItemCount(): Int {
        return mCurrenciesLast.size
    }

    // Inflates the item views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.rate_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currency = mCurrenciesLast.get(position)

        holder.itemView.apply {
            if (position == 0) {
                rate_item_button.visibility = View.GONE
            }else {
                rate_item_button.visibility = View.VISIBLE
            }
            rate_item_tv_currency_locale.text = currency.code
            rate_item_ed_currency.setText(calcNewValue(currency.value))
            rate_item_ed_currency.setOnKeyListener { view, i, keyEvent ->
                mFactor = currencyToDecimal(rate_item_ed_currency?.text.toString())
                notifyItemRangeChanged(1, mCurrenciesLast.size)

                rate_item_ed_currency.setSelection(
                    rate_item_ed_currency.text!!.length,
                    rate_item_ed_currency.text!!.length
                )

                false
            }

            rate_item_button.setOnClickListener {
                val newCurrenyPos = mCurrenciesLast.get(holder.adapterPosition)
                mFactor = mCurrenciesLast.get(holder.adapterPosition).value.toBigDecimal()

                clickSubject.onNext(mCurrenciesLast.get(holder.adapterPosition).code)

                mFactor = currencyToDecimal(mRecyclerView.findViewHolderForAdapterPosition(holder.adapterPosition)?.itemView?.rate_item_ed_currency?.text.toString())

                mRecyclerView.findViewHolderForAdapterPosition(holder.adapterPosition)
                    ?.itemView?.rate_item_ed_currency?.rate_item_ed_currency?.requestFocus()

                mCurrenciesLast.removeAt(holder.adapterPosition)
                mCurrenciesLast.add(0, newCurrenyPos)
                newValues(0, holder.adapterPosition)
            }

        }
    }

    fun currencyToDecimal(currencyValue: String): BigDecimal {

        var value = currencyValue.replace("[(a-z)|(A-Z)|($,. )]".toRegex(), "")
        val len = value.length
        value = value.substring(0,len-2) +"."+ value.substring(len-2,len)
        return value.toBigDecimal()
    }


    private fun calcNewValue(value: Float): String {
        return "%.2f".format(value.toBigDecimal() * mFactor)
    }

    fun updateData(
        newData: ArrayList<CurrencysModel>,
        base: String
    ) {
        if (mFlagFirst || (mCurrenciesLast.size != newData.size)) {
            mCurrenciesLast = newData
        }

        for ((index, currency) in mCurrenciesLast.withIndex()) {
            for (new in newData) {
                if (currency.code == new.code){
                    mCurrenciesLast.get(index).value = new.value
                    break
                }
            }
        }
        (mRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        notifyItemRangeChanged(1,mCurrenciesLast.size)

        mFlagFirst = false
    }


    fun newValues(to:Int, from:Int) {
        (mRecyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = true
        notifyItemMoved(from, to)
        mRecyclerView.scrollToPosition(0)
        mRecyclerView.findViewHolderForAdapterPosition(from)?.itemView?.rate_item_button?.visibility = View.VISIBLE
        mRecyclerView.findViewHolderForAdapterPosition(0)?.itemView?.rate_item_button?.visibility = View.GONE
    }
}

class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)

