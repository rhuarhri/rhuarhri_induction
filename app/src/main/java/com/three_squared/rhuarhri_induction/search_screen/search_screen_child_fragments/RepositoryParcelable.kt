package com.three_squared.rhuarhri_induction.search_screen.search_screen_child_fragments

import android.os.Parcel
import android.os.Parcelable

/*
A parcelable class is used to convert an object into a byte stream so it can be past to a
fragment
 */
class RepositoryParcelable(val id : String?, val name : String?,
                           val visibility : String?, val description : String?) : Parcelable {


    constructor(parcel: Parcel) : this(parcel.readString(), parcel.readString(),
        parcel.readString(), parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(this.id)
        parcel.writeString(this.name)
        parcel.writeString(this.visibility)
        parcel.writeString(this.description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RepositoryParcelable> {
        override fun createFromParcel(parcel: Parcel): RepositoryParcelable {
            return RepositoryParcelable(parcel)
        }

        override fun newArray(size: Int): Array<RepositoryParcelable?> {
            return arrayOfNulls(size)
        }
    }
}