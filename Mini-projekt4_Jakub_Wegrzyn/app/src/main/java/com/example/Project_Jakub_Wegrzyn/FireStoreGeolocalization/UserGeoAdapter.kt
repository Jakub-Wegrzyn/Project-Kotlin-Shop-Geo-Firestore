package com.example.Project_Jakub_Wegrzyn.FireStoreGeolocalization

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.Project_Jakub_Wegrzyn.databinding.SingleRowShopBinding
import kotlin.collections.ArrayList

class UserGeoAdapter(val userGeoViewModel: UserGeoViewModel) : RecyclerView.Adapter<UserGeoAdapter.UserGeoViewHolder>() {

    inner class UserGeoViewHolder(val binding: SingleRowShopBinding) : RecyclerView.ViewHolder(binding.root)

    private val placesGeoArray = ArrayList<PlacesGeo>()

    fun setUserGeoPlaces(it: List<PlacesGeo>) {
        placesGeoArray.clear()
        placesGeoArray.addAll(it)
        notifyDataSetChanged()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserGeoViewHolder {
        //wywoływane w momencie kiedy tworzony jest nowy viewHolder do dodania do listy.
        //w tym miejscu wskazujemy na plik XML z układem tego elementu na liscie
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = SingleRowShopBinding.inflate(inflater, parent, false)

        return UserGeoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserGeoViewHolder, position: Int) {
        val currentProduct = placesGeoArray[position]

        holder.binding.txtNazwaSklepu.text = currentProduct.nazwaMiejsca.toString()
        holder.binding.txtOpis.text = currentProduct.opis.toString()
        holder.binding.txtPromien.text = currentProduct.promien?.toInt().toString()
        holder.binding.txtDlugosc.text = currentProduct.longitude.toString()
        holder.binding.txtSzerokosc.text = currentProduct.latitude.toString()
    }

    override fun getItemCount(): Int {
        return placesGeoArray.size
    }





}