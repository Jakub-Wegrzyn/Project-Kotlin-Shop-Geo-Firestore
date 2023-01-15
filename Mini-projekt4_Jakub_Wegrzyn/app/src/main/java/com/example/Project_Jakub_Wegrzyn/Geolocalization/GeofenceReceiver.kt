package com.example.Project_Jakub_Wegrzyn.Geolocalization

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.Project_Jakub_Wegrzyn.R
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver : BroadcastReceiver() {
    private val CHANNEL_ID = "channel_id_example"
    private val notificationId = 101

    override fun onReceive(context: Context, intent: Intent) {
        when(intent?.action){
            "mini_projekt_1_jakub_wegrzyn_2_ACTION_SEND" ->{
                val geoEvent = GeofencingEvent.fromIntent(intent)
                println("GEOEVENT: " + geoEvent.triggeringGeofences)
                val triggering = geoEvent.triggeringGeofences
                for( geo in triggering){
                    Log.i("geofence", "Geofence z id: ${geo.requestId} aktywny.")
                }
                if(geoEvent.geofenceTransition ==
                    Geofence.GEOFENCE_TRANSITION_ENTER){
                    createNotificationChannel(context)
//                    var id = geoEvent.triggeringGeofences.get(0).requestId.toString().substring(3)
                    var tytuł = "Jesteś w pobliżu sklepu!"
                    var opis = "Odwiedź nas!"
                    sendNotification(context, tytuł, opis)
                }else if(geoEvent.geofenceTransition ==
                    Geofence.GEOFENCE_TRANSITION_EXIT){
                    createNotificationChannel(context)
//                    var id = geoEvent.triggeringGeofences.get(0).requestId.toString().substring(3)
                    var tytuł = "Wyszedłeś poza obręb sklepu"
                    var opis = "Odwiedź nas ponownie!"
                    sendNotification(context, tytuł, opis)
                }else{
                    Log.e("geofences", "Error.")
                }
            }
        }


    }

    private fun createNotificationChannel(context: Context){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(context: Context, tytuł: String, opis: String){
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.done)
            .setContentTitle(tytuł)
            .setContentText(opis)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)){
            notify(notificationId, builder.build())
        }
    }


}