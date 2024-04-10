package com.aura.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transfer(
    @SerialName("sender") val senderId: String,
    @SerialName("recipient") val recipientId: String,
    @SerialName("amount") val amount: Double
)
