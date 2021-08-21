package br.com.lucas.santanderbootcamp.todolist.extensions

import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.*

private val locale = Locale("pt", "BR")

fun Date.formatDateToPtBr() : String = SimpleDateFormat("dd/MM/yyyy", locale).format(this)