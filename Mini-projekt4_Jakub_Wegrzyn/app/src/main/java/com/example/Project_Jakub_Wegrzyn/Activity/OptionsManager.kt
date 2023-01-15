package com.example.Project_Jakub_Wegrzyn.Activity

import android.content.Context
import androidx.datastore.preferences.createDataStore
import androidx.datastore.preferences.edit
import androidx.datastore.preferences.preferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class OptionsManager(context: Context) {

    private val dataStore = context.createDataStore(name = "user_prefs")

    companion object {
        val WPISZ_TEKST_KEY = preferencesKey<String>("WPISZ_TEKST")
        val KOLOR_KEY = preferencesKey<Boolean>("COLOR")
    }

    suspend fun storeData(wpisz_tekst: String, isColor: Boolean) {
        dataStore.edit {
            it[WPISZ_TEKST_KEY] = wpisz_tekst
            it[KOLOR_KEY] = isColor
        }
    }

    val wpisanyTekstFlow: Flow<String> = dataStore.data.map {
        val wpisany_tekst = it[WPISZ_TEKST_KEY] ?: ""
        wpisany_tekst

    }

    val KolorFlow: Flow<Boolean> = dataStore.data.map {
        it[KOLOR_KEY] ?: false
    }

}