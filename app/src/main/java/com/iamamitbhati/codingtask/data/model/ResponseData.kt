package com.iamamitbhati.codingtask.data.model


data class Pet(
    var imageUrl: String? = null,
    var title: String? = null,
    var contentUrl: String? = null,
    var dateAdded: String? = null
)

data class Setting(
    var isChatEnabled: Boolean = false,
    var isCallEnabled: Boolean = false,
    var workHours: String? = null
)