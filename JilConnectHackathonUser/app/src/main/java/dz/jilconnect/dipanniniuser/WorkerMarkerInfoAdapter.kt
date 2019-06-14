package dz.jilconnect.dipanniniuser

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker


class WorkerMarkerInfoAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {

    @SuppressLint("SetTextI18n")
    override fun getInfoContents(marker: Marker?): View {
        val view = (context as Activity).layoutInflater.inflate(R.layout.infowindow, null)

        val nameTv = view.findViewById<TextView>(R.id.name)
        val phoneTv = view.findViewById<TextView>(R.id.phone)
        val distanceTv = view.findViewById<TextView>(R.id.distance)
        val moneyTv = view.findViewById<TextView>(R.id.moneyTv)

        val workerInfo = marker?.tag as WorkersResult

        with(workerInfo) {
            nameTv.text = name
            phoneTv.text = phone
            distanceTv.text = "$distance km"
            moneyTv.text = cost
        }

        return view
    }

    override fun getInfoWindow(p0: Marker?): View? {
        return null
    }
}