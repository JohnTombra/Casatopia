package com.tombra.casatopia._model

data class Maintenance(
    val timestamp: String = "",
    val message: String = "",
    val sender: String = "",
    val receiver: String = "",
    val estate: String = "",
    val acknowledged: Boolean = false,
)
