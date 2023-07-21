package com.tombra.casatopia._model

data class Auth(
    val authenticated: Boolean,
    val authId: String,
    val accountType: String,
)
