package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.AsteroidListener
import com.udacity.asteroidradar.ItemAdapter
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private var adapter: ItemAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        val manager = LinearLayoutManager(activity);

        binding.asteroidRecycler.layoutManager=manager
        adapter = ItemAdapter(AsteroidListener { id ->
            viewModel.onAsteroidClicked(id)
        })

        viewModel.navigateToAsteroidData.observe(viewLifecycleOwner, Observer { asteroid ->
            asteroid?.let {

                this.findNavController().navigate(
                    MainFragmentDirections.actionShowDetail(asteroid))
                viewModel.onAsteroidNavigated()
            }
        })
        viewModel.asteroids.observe(viewLifecycleOwner, Observer<List<Asteroid>> { asteroids ->
            Log.i("MainFragment", "onViewCreated: ${asteroids}")
            asteroids?.apply {
                adapter?.videos = asteroids
            }
        })


        binding.asteroidRecycler.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.asteroids.observe(viewLifecycleOwner, Observer<List<Asteroid>> { asteroids ->
//            Log.i("MainFragment", "onViewCreated: ${asteroids}")
//            asteroids?.apply {
//                adapter?.videos = asteroids
//            }
//        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_rent_menu -> viewModel.todayAsteroid.observe(viewLifecycleOwner) {
                adapter?.videos = it
            }
            R.id.show_all_menu -> viewModel.weelAsteroid.observe(viewLifecycleOwner) {
                adapter?.videos = it
            }
            R.id.show_buy_menu -> viewModel.asteroids.observe(viewLifecycleOwner) {
                adapter?.videos = it
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
