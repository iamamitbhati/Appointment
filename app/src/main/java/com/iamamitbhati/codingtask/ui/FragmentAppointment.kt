package com.iamamitbhati.codingtask.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.iamamitbhati.codingtask.R
import com.iamamitbhati.codingtask.viewmodel.ViewModelFactory
import com.iamamitbhati.codingtask.domain.alertView
import com.iamamitbhati.codingtask.data.model.Pet
import com.iamamitbhati.codingtask.databinding.FragmentHomeBinding
import com.iamamitbhati.codingtask.extension.setVisibility
import com.iamamitbhati.codingtask.repository.AppointmentRepository
import com.iamamitbhati.codingtask.repository.AppointmentRepositoryImpl
import com.iamamitbhati.codingtask.repository.Resource
import com.iamamitbhati.codingtask.ui.FragmentPetDetail.Companion.CONTENT_URL
import com.iamamitbhati.codingtask.ui.FragmentPetDetail.Companion.TAG
import com.iamamitbhati.codingtask.ui.adapter.PetListAdapter
import com.iamamitbhati.codingtask.viewmodel.MainViewModel
import java.util.*


class FragmentAppointment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val appointmentRepository: AppointmentRepository = AppointmentRepositoryImpl()
    private val mainViewModel: MainViewModel by viewModels { ViewModelFactory(appointmentRepository) }
    private lateinit var petListAdapter: PetListAdapter
    private var petList = ArrayList<Pet>()
    private var workingHrs: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObserver()
        getProducts()
    }

    private fun setupView() {
        with(binding) {
            petListAdapter = PetListAdapter(petList) { pet ->
                val bundle = Bundle()
                bundle.putString(CONTENT_URL, pet.contentUrl)
                val webViewFragment = FragmentPetDetail()
                webViewFragment.arguments = bundle
                val fragmentTransaction = parentFragmentManager.beginTransaction()
                fragmentTransaction.add(R.id.fragment_container_view, webViewFragment)
                fragmentTransaction.addToBackStack(TAG)
                fragmentTransaction.commit()
            }
            recyclerView.adapter = petListAdapter

            callBtn.setOnClickListener {
                showAlert()

            }
            chatBtn.setOnClickListener {
                showAlert()
            }
        }
    }

    private fun showAlert() {
        val isInHrs = workingHrs?.let { mainViewModel.checkTime(it, Calendar.getInstance()) } ?: false
        val alertMsg = if (isInHrs)
            getString(R.string.in_hrs)
        else
            getString(R.string.outside_hrs)
        alertView(alertMsg, requireContext())
    }

    private fun setupObserver() {
        mainViewModel.petList.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressbar.visibility = View.GONE
                    it.data?.let { list ->
                        petList.clear()
                        petList.addAll(list)
                    }
                    notifyAdapter()
                }
                is Resource.Failed -> {
                    onRetrieveListError(it.errorMessage)
                }
            }
        }

        mainViewModel.config.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.progressbar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressbar.visibility = View.GONE
                    it.data?.let { config ->
                        with(binding) {
                            callBtn.setVisibility(config.isCallEnabled)
                            chatBtn.setVisibility(config.isChatEnabled)
                            workingHourText.setVisibility(true)
                            workingHrs = config.workHours
                            workingHourText.text =
                                context?.getString(R.string.ofc_hrs, config.workHours)
                        }
                    }
                }
                is Resource.Failed -> {
                    onRetrieveListError(it.errorMessage)
                }
            }
        }
    }

    private fun getProducts() {
        mainViewModel.fetchAllData()
    }

    /**
     * In order to update complete list
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun notifyAdapter() {
        petListAdapter.notifyDataSetChanged()
    }


    private fun onRetrieveListError(errorMessage: String?) {
        val showMessage = errorMessage ?: getString(R.string.post_error)
        val errorSnackbar = Snackbar.make(binding.recyclerView, showMessage, Snackbar.LENGTH_LONG)
        errorSnackbar.setAction(R.string.retry, errorClickListener)
        errorSnackbar.show()
    }

    private val errorClickListener = View.OnClickListener { getProducts() }

}