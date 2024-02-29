package com.example.chatapplicationpracticefinal

import androidx.room.PrimaryKey

data class MessageModel(
    var message:String? ="",
    var senderId:String? ="",
    var timeStamp: Long? =0
)
