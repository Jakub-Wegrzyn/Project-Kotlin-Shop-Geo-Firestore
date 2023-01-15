package com.example.Project_Jakub_Wegrzyn.FireStoreDataProduct

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.Project_Jakub_Wegrzyn.databinding.SingleRowBinding

class ProductAdapter(val productViewModel: ProductViewModel) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: SingleRowBinding) : RecyclerView.ViewHolder(binding.root)

    private val productList = ArrayList<Product>()
    private var arrayOfChecked = ArrayList<Product>()


    fun setProducts(list: List<Product>){
        productList.clear()
        productList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        //wywoływane w momencie kiedy tworzony jest nowy viewHolder do dodania do listy.
        //w tym miejscu wskazujemy na plik XML z układem tego elementu na liscie
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = SingleRowBinding.inflate(inflater, parent, false)

        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        //wywołanie w momencie kiedy poszczegolny View Holder jest doczepiany do listy
        //w tym miejscu wskazujemy na dane jakie powinny się znaleźć w elementach układu tej pozycji na liscie

        holder.binding.ctv1!!.isChecked = false
        arrayOfChecked.clear()


        val currentProduct = productList[position]
        holder.binding.nazwaRoot.text = currentProduct.nazwa
        holder.binding.cenaRoot.text = String.format("%.2f", currentProduct.cena) + " zł"
        holder.binding.iloscRoot.text = currentProduct.ilosc.toString() + " szt."

        holder.binding.edtxIloscZakupiona.text = null
        holder.binding.edtxIloscZakupiona.isEnabled = true

        holder.binding.ctv1.setOnClickListener {

            if(holder.binding.edtxIloscZakupiona.text.toString() == ""){
                Toast.makeText(holder.binding.ctv1.context, "Wprowadz ilość", Toast.LENGTH_SHORT).show()
                holder.binding.ctv1!!.isChecked = false
            }
            else{
                if (holder.binding.ctv1.isChecked) {
                    holder.binding.edtxIloscZakupiona.isEnabled = false
                    val ilosc_kupiona = holder.binding.edtxIloscZakupiona.text.toString()
                    if(ilosc_kupiona == ""){
                        currentProduct.ilosc = 1
                        arrayOfChecked.add(currentProduct)
                    }
                    else{
                        currentProduct.ilosc = ilosc_kupiona.toInt()
                        arrayOfChecked.add(currentProduct)
                    }

                } else {
                    holder.binding.edtxIloscZakupiona.isEnabled = true
                    var value :Int ?= null

                    arrayOfChecked.forEach{
                            item ->
                        if(item.uid == currentProduct.uid)
                        {
                            value == arrayOfChecked.indexOf(item)
                        }

                    }
                    if (value != null) {
                        arrayOfChecked.removeAt(value)
                    }
                }
            }
        }

    }

    internal fun getCheckedArray(): ArrayList<Product> {
        return arrayOfChecked
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    fun delete(array: ArrayList<Product>) {
        array.forEach{
            item ->
                productList.remove(item)
                productViewModel.delete(item)
        }
        notifyDataSetChanged()
    }

}