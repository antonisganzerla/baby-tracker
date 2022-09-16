package com.sgztech.babytracker.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sgztech.babytracker.R
import com.sgztech.babytracker.model.Register
import java.time.format.DateTimeFormatter
import java.util.*

class RegisterAdapter(
    private val registers: List<Register>,
) : RecyclerView.Adapter<RegisterAdapter.RegisterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RegisterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.register_card_view, parent, false)
        return RegisterViewHolder(view)
    }

    override fun onBindViewHolder(holder: RegisterViewHolder, position: Int) {
        holder.bind(registers[position])
    }

    override fun getItemCount(): Int = registers.size

    inner class RegisterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivAction: ImageView = itemView.findViewById(R.id.ivAction)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        private val tvSubtitle: TextView = itemView.findViewById(R.id.tvSubtitle)
        private val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        private val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale("pt", "BR"))

        fun bind(register: Register) {
            ivAction.setImageDrawable(itemView.context.getDrawable(register.icon))
            tvTitle.text = register.name
            tvSubtitle.text = register.description
            tvTime.text = register.time.format(formatter)
        }
    }
}
