package mx.tecnm.cdhidalgo.e401_2024.Data_Class

import android.os.Parcel
import android.os.Parcelable

data class Producto(
    val imagen: String?="",
    val nombreCorto: String?="",
    val nombre: String?="",
    val precio: Double=0.0,
    val descripcion:String?="",
    val categoria:String?="") : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(imagen)
        parcel.writeString(nombreCorto)
        parcel.writeString(nombre)
        parcel.writeDouble(precio)
        parcel.writeString(descripcion)
        parcel.writeString(categoria)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Producto> {
        override fun createFromParcel(parcel: Parcel): Producto {
            return Producto(parcel)
        }

        override fun newArray(size: Int): Array<Producto?> {
            return arrayOfNulls(size)
        }
    }
}
