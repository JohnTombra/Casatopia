package com.tombra.casatopia._model

data class Withdrawal(
    val timestamp: String = "",
    val amount: Int = 0,
    val bankName: String = "",
    val accountName: String = "",
    val accountNumber: String = ""
)
