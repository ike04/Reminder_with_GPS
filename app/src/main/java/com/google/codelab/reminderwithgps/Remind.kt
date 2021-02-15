package com.google.codelab.reminderwithgps

import java.util.*

data class Remind(
    var title: String = "",
    var memo: String? = null,
    var lat: Double = 0.0,
    var lng: Double = 0.0,
    var dateTime: Date = Date(),
    var isDone: Boolean = false
)
