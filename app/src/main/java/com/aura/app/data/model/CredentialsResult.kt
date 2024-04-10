package com.aura.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Represents the result of a login attempt.
 * @property granted Whether the login attempt was successful.
 */
@Serializable
data class CredentialsResult(
    @SerialName("granted") val granted: Boolean,
)

