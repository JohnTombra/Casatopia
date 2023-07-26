package com.tombra.casatopia._model

data class Auth(
    val authenticated: String,
    val authId: String,
    val accountType: String,
)
