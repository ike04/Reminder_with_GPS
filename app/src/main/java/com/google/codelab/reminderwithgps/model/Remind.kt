package com.google.codelab.reminderwithgps.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class Remind : RealmObject() {
    @PrimaryKey
    var id: Long? = null

    var title: String = ""
    var memo: String? = null
    var lat: Double = 0.0
    var lng: Double = 0.0
    var dateTime: Date = Date()
    var isDone: Boolean = false
}
