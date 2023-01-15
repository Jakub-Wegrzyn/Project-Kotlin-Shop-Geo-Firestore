package com.example.Project_Jakub_Wegrzyn.FireStoreDataUser

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.Project_Jakub_Wegrzyn.FireStoreDataProduct.Product
import com.example.Project_Jakub_Wegrzyn.databinding.SingleUserRowBinding

class UserAdapter(val context: Context, val userViewModel: UserViewModel) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: SingleUserRowBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val productList = ArrayList<Product>()
    private val arrayOfCheck = ArrayList<Product>()

    fun setProducts(list: List<Product>) {
        productList.clear()
        productList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        //wywoływane w momencie kiedy tworzony jest nowy viewHolder do dodania do listy.
        //w tym miejscu wskazujemy na plik XML z układem tego elementu na liscie

        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = SingleUserRowBinding.inflate(inflater, parent, false)

        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        //wywołanie w momencie kiedy poszczegolny View Holder jest doczepiany do listy
        //w tym miejscu wskazujemy na dane jakie powinny się znaleźć w elementach układu tej pozycji na liscie
        val currentProduct = productList[position]
        holder.binding.nazwaRoot.text = currentProduct.nazwa
        holder.binding.cenaRoot.text = String.format("%.2f", currentProduct.cena).plus(" zł")
        holder.binding.iloscRoot.setText(currentProduct.ilosc.toString())

        holder.binding.iloscRoot.isEnabled = true
        holder.binding.ctv1!!.isChecked = false
        arrayOfCheck.clear()

        holder.binding.ctv1.setOnClickListener {
            if (holder.binding.ctv1.isChecked) {
                holder.binding.iloscRoot.isEnabled = false
                val ilosc_wpisana = holder.binding.iloscRoot.text.toString()
                if(ilosc_wpisana == ""){
                    currentProduct.ilosc = 1
                    arrayOfCheck.add(currentProduct)
                }
                else {
                    currentProduct.ilosc = ilosc_wpisana.toInt()
                    arrayOfCheck.add(currentProduct)
                }
            }
            else {
                holder.binding.iloscRoot.isEnabled = true
                var value : Int? = null
                arrayOfCheck.forEach{
                        item ->
                    if(item.uid == currentProduct.uid)
                    {
                        value = arrayOfCheck.indexOf(item)
                    }
                }
                if (value != null) {
                    arrayOfCheck.removeAt(value!!)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        //powienien zwracać liczbe elementow na liscie
        return productList.size
    }

    internal fun getCenaKoszyka(): Double? {
        var wynik:Double ?= 0.0

        arrayOfCheck.forEach{
            entry ->
                val temp = entry.cena?.times(entry.ilosc!!)
                wynik = wynik?.plus(Math.round(temp!!))
            }

        return wynik
    }

    fun getCheckList(): List<Product> {
        return arrayOfCheck
    }
    internal fun delete(list: List<Product>) {
        userViewModel.delete(list)
        list.forEach { item ->
                productList.remove(item)
            }
        notifyDataSetChanged()
    }

    fun refresh() {
        notifyDataSetChanged()
    }
}



