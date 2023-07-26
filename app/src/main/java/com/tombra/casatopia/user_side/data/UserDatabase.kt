package com.tombra.casatopia.user_side.data

import android.content.ComponentCallbacks
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tombra.casatopia._model.*
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await

class UserDatabase(private val context: Context) {


    private val network: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val localSet: SharedPreferences.Editor =
        context.getSharedPreferences("MySharedPref", MODE_PRIVATE).edit()
    private val localGet: SharedPreferences =
        context.getSharedPreferences("MySharedPref", MODE_PRIVATE)


    fun searchProperty(query: String, submit: (List<Estate>) -> Unit) {

        //name
        //country
        //state
        //city
        //address

        network.reference.child("Estates").get().addOnSuccessListener { result ->
            var estates = listOf<Estate>()
            for (estate in result.children) {
                estates += estate.getValue(Estate::class.java)!!
            }
            estates.filter {
                it.estateName!!.contains(query) || it.country!!.contains(query) || it.state!!.contains(
                    query
                ) || it.city!!.contains(query) || it.address!!.contains(query)
            }
            submit(estates)
        }


        //if its a country get and stop
        //if its in a state get and stop


    }


    fun getAllEstates(submit: (List<Estate>) -> Unit) {
        //for each admin
        //that is available
        network.reference.child("Admins").get().addOnSuccessListener { result ->
            var estates = listOf<Estate>()


            for (admin in result.children) {
                for (estate in admin.child("Estates").children) {
                    val result = estate.getValue(Estate::class.java)
                    if (result!!.availability!!) {
                        estates += estate.getValue(Estate::class.java)!!
                    }
                }
            }
            Log.d("REPOSITORY", "yellow $estates")
            submit(estates)
        }
    }

    //
    fun getSingleEstate(estateId: String, submit: (Estate) -> Unit) {
        network.reference.child("Admins").get().addOnSuccessListener { result ->
            var estateValue: Estate? = null
            for (admin in result.children) {
                for (estate in admin.child("Estates").children) {
                    val result = estate.getValue(Estate::class.java)
                    if (result!!.estateId!! == estateId) {
                        estateValue = estate.getValue(Estate::class.java)!!
                        submit(estateValue)
                    }
                }
            }
        }
    }
//
//
//
//    private fun removeEstate(estateId: String, submit:()->Unit){
//        network.reference.child("Estates").child(estateId).removeValue().addOnSuccessListener { result ->
//            submit()
//        }
//    }
//
//
//
    fun getUserProfile(submit: (User) -> Unit){
        network.reference.child("Users").child(getAuthInfo().authId).child("Profile").get().addOnSuccessListener { result ->
            val user = result.getValue(User::class.java)!!
            submit(user)
        }
    }
//
//
//


    fun getAdminProfile(adminId: String, submit: (Admin) -> Unit): Admin = runBlocking {
        Log.d("Repository","Getting list - 4")
        withContext(Dispatchers.IO){
            Log.d("Repository","Getting list 5")
            val result =
                network.reference.child("Admins").child(adminId).child("Profile").get().await()
            val admin = result.getValue(Admin::class.java)!!
            Log.d("Repository","Getting list- 6")
            submit(admin)
            Log.d("Repository","Getting list- 7")
            return@withContext admin
        }
    }


    fun getAuthInfo(): Auth {
        val authenticated = localGet.getString("Authenticated", "false").toString()
        val authId = localGet.getString("AuthId", "").toString()
        val authType = localGet.getString("AuthType", "").toString()
        return Auth(authenticated, authId, authType)
    }

    //
//
    fun saveAuthInfo(auth: Auth) {
        localSet.putString("Authenticated", auth.authenticated.toString())
        localSet.putString("AuthId", auth.authId)
        localSet.putString("AuthType", auth.accountType)
        localSet.apply()
    }
//
//
//
//    fun getNotification(user: User, submit: (List<Estate>)-> Unit){
//        network.reference.child("Users").child(user.userId).child("Notifications").get().addOnSuccessListener { result ->
//            var estates = listOf<Estate>()
//            for (estate in result.children) {
//                estates += estate.getValue(Estate::class.java)!!
//            }
//            submit(estates)
//        }
//    }
//
//
//
//    fun myEstates(user: User, submit: (List<Estate>)-> Unit){
//        network.reference.child("Users").child(user.userId).child("MyEstates").get().addOnSuccessListener { result ->
//            var estates = listOf<Estate>()
//            for (estate in result.children) {
//                estates += estate.getValue(Estate::class.java)!!
//            }
//            submit(estates)
//        }
//    }
//
//
//
//
//
//


    fun acquireProperty(estateId: String, adminId: String, purchase: Purchase, submit: () -> Unit) {
        network.reference.child("Admins").child(adminId).child("Estates").child(estateId)
            .child("availability").setValue(false).addOnSuccessListener {
            network.reference.child("Admins").child(adminId).child("Estates").child(estateId)
                .child("purchase").setValue(purchase)
            submit()
        }
    }


    fun sendNotification(notification: Notification, Id: String, type: String, submit: () -> Unit) {
        network.reference.child(type).child(Id).child("Notifications").child(notification.timeStamp)
            .setValue(notification).addOnSuccessListener {
            submit()
        }
    }


    fun addTransactionsToAdmin(transaction: Transaction, adminId: String, submit: () -> Unit) {
        network.reference.child("Admins").child(adminId).child("Transactions")
            .child(transaction.transactionId).setValue(transaction).addOnSuccessListener {
            submit()
        }
    }


    fun addTransactionsToUser(transaction: Transaction, userId: String, submit: () -> Unit) {
        network.reference.child("Users").child(userId).child("Transactions")
            .child(transaction.transactionId).setValue(transaction).addOnSuccessListener {
            submit()
        }
    }


    fun addToClientsList(adminId: String, userId: String, submit: () -> Unit) {
        network.reference.child("Admins").child(adminId).child("Clients").child(userId)
            .setValue(userId).addOnSuccessListener {
            submit()
        }
    }


    fun getChatList(submit: (List<Admin>) -> Unit) {
        Log.d("Repository","Getting list - 1")
        network.reference.child("Users").child(getAuthInfo().authId).child("Chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("Repository","Getting list -2")
                        var admins = listOf<Admin>()
                        for (recipient in snapshot.children) {
                            admins += getAdminProfile(recipient.key!!,{})
                        }

                    Log.d("Repository","Getting list - 3 $admins")
                        submit(admins)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }






    fun getPropertyList(submit: (List<Transaction>) -> Unit) {
        Log.d("Repository","Getting list - 1")
        network.reference.child("Users").child(getAuthInfo().authId).child("Transactions")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("Repository","Getting list -2")
                    var transactions = listOf<Transaction>()
                    for (transaction in snapshot.children) {
                        transactions += transaction.getValue(Transaction::class.java)!!
                    }
                    Log.d("Repository","Getting list - 3 $transactions")
                    submit(transactions)
                }
                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }



    //
//
    fun getChats(adminId: String, submit: (List<Chat>) -> Unit) {
        network.reference.child("Users").child(getAuthInfo().authId).child("Chats").child(adminId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var chats = listOf<Chat>()
                    for (chat in snapshot.children) {
                        chats += chat.getValue(Chat::class.java)!!
                    }
                    submit(chats)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    //
//
    fun getAdminDetails(adminId: String, submit: (Admin) -> Unit) {
        network.reference.child("Admins").child(adminId).get().addOnSuccessListener { profile ->
            submit(profile.getValue(Admin::class.java)!!)
        }
    }
//
//

    fun sendMessage(adminId: String, chat: Chat, submit: () -> Unit) {
        network.reference.child("Users").child(getAuthInfo().authId).child("Chats").child(adminId)
            .child(chat.timestamp).setValue(chat).addOnSuccessListener {
            //send to admin too
            sendMessageToAdmin(adminId, chat, submit)
        }
    }


    fun sendMessageToAdmin(adminId: String, chat: Chat, submit: () -> Unit) {
        network.reference.child("Admins").child(adminId).child("Chats").child(chat.sender)
            .child(chat.timestamp).setValue(chat).addOnSuccessListener {
            //send to admin too
            submit()
        }
    }
//


}