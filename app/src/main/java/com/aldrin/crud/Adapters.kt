package com.aldrin.crud

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.lo_customer_update.view.*
import kotlinx.android.synthetic.main.lo_customers.view.*

class CustomerAdapter(mCtx : Context, val customers : ArrayList<Customer>) : RecyclerView.Adapter<CustomerAdapter.ViewHolder>() {

    val mCtx = mCtx

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCustomerName = itemView.txtCustomerName
        val txtMaxCredit = itemView.txtMaxCredit
        val btnUpdate = itemView.btnUpdate
        val btnDelete = itemView.btnDelete
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): CustomerAdapter.ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.lo_customers,p0,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return customers.size
    }

    override fun onBindViewHolder(p0: CustomerAdapter.ViewHolder, p1: Int) {
        val customer : Customer = customers[p1]
        p0.txtCustomerName.text = customer.customerName
        p0.txtMaxCredit.text = customer.maxCredit.toString()

        p0.btnDelete.setOnClickListener {
            val customerName = customer.customerName

            var alertDialog = AlertDialog.Builder(mCtx)
                .setTitle("Warning")
                .setMessage("Are You Sure to Delete : $customerName ?")
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                    if (MainActivity.dbHandler.deleteCustomer(customer.customerID)) {
                        customers.removeAt(p1)
                        notifyItemRemoved(p1)
                        notifyItemRangeChanged(p1,customers.size)
                        Toast.makeText(mCtx, "Customer $customerName Deleted", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(mCtx, "Error Deleting", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->  })
                .setIcon(R.drawable.ic_warning_black_24dp)
                .show()
        }

        p0.btnUpdate.setOnClickListener {
            val inflater = LayoutInflater.from(mCtx)
            val view = inflater.inflate(R.layout.lo_customer_update,null)

            val txtCustName : TextView = view.findViewById(R.id.editUpCustomerName)
            val txtMaxCredit : TextView = view.findViewById(R.id.editUpMaxCredit)

            txtCustName.text = customer.customerName
            txtMaxCredit.text = customer.maxCredit.toString()

            val builder = AlertDialog.Builder(mCtx)
                .setTitle("Update Customer Info")
                .setView(view)
                .setPositiveButton("Update", DialogInterface.OnClickListener { dialog, which ->
                    val isUpdate = MainActivity.dbHandler.updateCustomer(
                        customer.customerID.toString(),
                        view.editUpCustomerName.text.toString(),
                        view.editUpMaxCredit.text.toString())
                    if (isUpdate==true) {
                        customers[p1].customerName = view.editUpCustomerName.text.toString()
                        customers[p1].maxCredit = view.editUpMaxCredit.text.toString().toDouble()
                        notifyDataSetChanged()
                        Toast.makeText(mCtx, "Updated Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(mCtx, "Error Updating", Toast.LENGTH_SHORT).show()
                    }
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->  })
            val alert = builder.create()
            alert.show()
        }
    }

}