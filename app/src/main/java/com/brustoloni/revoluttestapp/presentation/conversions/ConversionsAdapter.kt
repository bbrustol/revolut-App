package com.brustoloni.revoluttestapp.presentation.conversions

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.itau.cartoes.infrastructure.CurrencyTextWatcher
import kotlinx.android.synthetic.main.rate_item.view.*
import com.brustoloni.revoluttestapp.data.model.CurrencysModel
import com.brustoloni.revoluttestapp.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import java.math.BigDecimal


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

        Log.d("aaaa", "mFactor-> " + mFactor.toString())
        val currency = mCurrenciesLast.get(position)

        var mCurrencyTextWatcher = CurrencyTextWatcher(holder.itemView.rate_item_ed_currency, object :
            CurrencyTextWatcher.CurrencyTextWatcherListener {
            override fun onValueChanged(value: Double) {
                if (holder.adapterPosition == 0) {
                    mFactor = value.toBigDecimal()
                    Log.d("aaaa", mFactor.toString())
                }
            }
        })

        holder.itemView.apply {
            //            rate_item_image_flag
//            rate_item_tv_currency_name

            if (position == 0) {
                rate_item_button.visibility = View.GONE
            } else {
                rate_item_button.visibility = View.GONE
            }

            rate_item_button.alpha = 0f
            rate_item_tv_currency_locale.text = currency.code
            rate_item_ed_currency.setText(calcNewValue(currency.value))
            rate_item_ed_currency.addTextChangedListener(mCurrencyTextWatcher)
            rate_item_ed_currency.setOnKeyListener { view, i, keyEvent ->
                rate_item_ed_currency.setSelection(
                    rate_item_ed_currency.text!!.length,
                    rate_item_ed_currency.text!!.length
                )

                notifyItemRangeChanged(1,mCurrenciesLast.size)

                false
            }

            setOnClickListener {
                var newCurrenyPos = mCurrenciesLast.get(holder.adapterPosition)
                mFactor = mCurrenciesLast.get(holder.adapterPosition).value.toBigDecimal()

                clickSubject.onNext(mCurrenciesLast.get(holder.adapterPosition).code)

                mFactor = mCurrencyTextWatcher.currencyToDecimal(it.rate_item_ed_currency.text.toString())

                it.rate_item_ed_currency.requestFocus()

                mCurrenciesLast.removeAt(holder.adapterPosition)
                mCurrenciesLast.add(0, newCurrenyPos)
                newValues(0, holder.adapterPosition)
            }
        }
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
        mFlagFirst = false
        notifyItemRangeChanged(1,mCurrenciesLast.size)
    }


    fun newValues(to:Int, from:Int) {
        notifyItemMoved(from, to)
        mRecyclerView.scrollToPosition(0)
    }
}

class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView)

