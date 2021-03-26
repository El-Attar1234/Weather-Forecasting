package com.example.forecasting.ui.favourite

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.forecasting.R
import com.example.forecasting.ui.helpers.ScopedFragment
import com.example.forecasting.ui.weather.current.CurrentWeatherFragmentDirections
import com.example.forecasting.ui.weather.current.CurrentWeatherFragmentDirections.detailsAction
import com.example.forecasting.utilities.Event
import kotlinx.android.synthetic.main.favourite_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

private const val Favourite_DETAILS_REQUEST = 100

class FavouriteFragment : ScopedFragment(), KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: FavouriteViewModelFactory by instance()

    private lateinit var viewModel: FavouriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favourite_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as? AppCompatActivity)?.supportActionBar?.show()
        val navBar: CoordinatorLayout? =
            (activity as? AppCompatActivity)?.findViewById(R.id.coordinatorLayout_activity)
        navBar!!.visibility = View.VISIBLE
        Log.i("favourite1", "onActivityCreated")
        viewModel = ViewModelProvider(this, viewModelFactory).get(FavouriteViewModel::class.java)
        val adapter = FavouriteListAdapter(viewModel)
        initializeUi(adapter)


    }

    private fun initializeUi(adapter: FavouriteListAdapter) {
        favourite_recyclerview.adapter = adapter
        favourite_recyclerview.layoutManager =
            LinearLayoutManager(activity)
        favourite_recyclerview.setHasFixedSize(true)
        launch {
            Log.i("favourite1", "befire calling vm")
            val currentWeather = viewModel.fetchFavouriteWeather.await()
            currentWeather.observe(
                this@FavouriteFragment.viewLifecycleOwner,
                Observer {
                    adapter.setList(it)
                    Log.i("favourite1", "my list" + it.size)
                }
            )
        }

        viewModel.favouriteDeletedId.observe(viewLifecycleOwner, Observer {

            it?.getContentIfNotHandled()?.let { it1 ->
                openDeletedDialog(it1)
            }

        })

        viewModel.favouriteDetailedId.observe(viewLifecycleOwner, Observer {
            it?.getContentIfNotHandled()?.let { it1 ->
                val detailsAction = FavouriteFragmentDirections.favouriteDetailsAction(
                    Favourite_DETAILS_REQUEST, 1, it1
                )
                findNavController().navigate(detailsAction)
            }
        })
    }

    override fun onStart() {
        super.onStart()
        Log.i("favourite1", "onStart: ")
        // initializeUi()
    }

    /*  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
          super.onActivityResult(requestCode, resultCode, data)
          if (requestCode == 100) {
              Log.i("FFFFFF", "onActivityResult: ")
              getActivity()?.getSupportFragmentManager()?.beginTransaction()
                  ?.detach(requireParentFragment())
                  ?.attach(requireParentFragment())
                  ?.commit();
              /*   viewModel =
                     ViewModelProvider(this, viewModelFactory).get(FavouriteViewModel::class.java)
                    launch {
                     Log.i("favourite1", "befire calling vm")

                     val currentWeather = viewModel.fetchFavouriteWeather.await()
                     currentWeather.observe(
                         this@FavouriteFragment.viewLifecycleOwner,
                         Observer {
                             Log.i("favourite1", "my list" + it.size)
                         }
                     )
                 }*/

          }
      }*/

    private fun openDeletedDialog(it: String) {

        val builder1: AlertDialog.Builder = AlertDialog.Builder(context)
        builder1.setMessage(getResourceValue("delete_label"))
        builder1.setCancelable(true)

        builder1.setPositiveButton(
            getResourceValue("ok_label"),
            DialogInterface.OnClickListener { dialog, id ->
                viewModel.deleteFavouriteLocation(it)
                dialog.cancel()

            })

        builder1.setNegativeButton(
            getResourceValue("no_label"),
            DialogInterface.OnClickListener { dialog, id -> dialog.cancel() })

        val alert11: AlertDialog = builder1.create()
        alert11.show()
    }


    fun getResourceValue(label:String):String{
        return requireContext().resources.getString(
            requireContext().resources.getIdentifier(
                label, "string",
                requireContext().packageName
            )
        )
    }
}