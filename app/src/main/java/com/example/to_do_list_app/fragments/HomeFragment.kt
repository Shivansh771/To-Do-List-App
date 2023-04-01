package com.example.to_do_list_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.FragmentHostCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.to_do_list_app.R
import com.example.to_do_list_app.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class HomeFragment : Fragment(), AddTodoPopupFragment.DialogNextBtnClickListner {

    private lateinit var auth:FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding:FragmentHomeBinding
    private lateinit var popupFragment: AddTodoPopupFragment
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentHomeBinding.inflate(inflater,container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        regiterEvents()
    }

    private fun regiterEvents() {
        binding.addBtn.setOnClickListener{
            popupFragment= AddTodoPopupFragment()
            popupFragment.setListener(this)
            popupFragment.show(childFragmentManager,"Add todo pop up fragment")
        }
    }

    private fun init(view: View) {
        navController=Navigation.findNavController(view)
        auth= FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().reference.child("Tasks").child(auth.currentUser?.uid.toString())
    }

    override fun onSaveTask(todo: String, todoEt: AppCompatEditText) {
        databaseReference.push().setValue(todo).addOnCompleteListener { if(it.isSuccessful){
            Toast.makeText(context,"Todo Saved!!",Toast.LENGTH_SHORT).show()
            todoEt.text=null
        }else{
            Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
        }
            popupFragment.dismiss()
        }
    }

}