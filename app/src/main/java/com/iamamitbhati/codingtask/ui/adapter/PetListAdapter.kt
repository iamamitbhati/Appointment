package com.iamamitbhati.codingtask.ui.adapter

import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.iamamitbhati.codingtask.R
import com.iamamitbhati.codingtask.domain.loadImage
import com.iamamitbhati.codingtask.data.model.Pet
import com.iamamitbhati.codingtask.databinding.ItemPetBinding
import java.util.concurrent.Executors


class PetListAdapter(
    private val petList: ArrayList<Pet>,
    var onItemClick: ((Pet) -> Unit)? = null
) : RecyclerView.Adapter<PetListAdapter.AdapterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPetBinding.inflate(inflater, parent, false)
        return AdapterViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return petList.size
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        val pet = petList[position]
        holder.bind(pet, onItemClick)
    }

    class AdapterViewHolder(private val binding: ItemPetBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            pet: Pet,
            onItemClick: ((Pet) -> Unit)?
        ) {
            with(binding) {
                petText.text = pet.title
                val myExecutor = Executors.newSingleThreadExecutor()
                val myHandler = Handler(Looper.getMainLooper())
                petImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        root.context,
                        R.drawable.placeholder
                    )
                )
                myExecutor.execute {
                    val mImage = loadImage(pet.imageUrl)
                    myHandler.post {
                        petImage.setImageBitmap(mImage)
                    }
                }

                root.setOnClickListener {
                    onItemClick?.invoke(pet)
                }
            }

        }
    }
}