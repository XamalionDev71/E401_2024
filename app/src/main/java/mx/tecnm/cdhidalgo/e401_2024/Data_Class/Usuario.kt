package mx.tecnm.cdhidalgo.e401_2024.Data_Class

import android.os.Parcel
import android.os.Parcelable

data class Usuario(
    var correo:String?="",
    var nombre:String?="",
    var apaterno:String?="",
    var amaterno:String?="",
    var perfil_cliente:Boolean=false,
    var perfil_admin:Boolean=false
    ) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(correo)
        parcel.writeString(nombre)
        parcel.writeString(apaterno)
        parcel.writeString(amaterno)
        parcel.writeByte(if (perfil_cliente) 1 else 0)
        parcel.writeByte(if (perfil_admin) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Usuario> {
        override fun createFromParcel(parcel: Parcel): Usuario {
            return Usuario(parcel)
        }

        override fun newArray(size: Int): Array<Usuario?> {
            return arrayOfNulls(size)
        }
    }
}