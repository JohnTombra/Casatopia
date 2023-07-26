package com.tombra.casatopia.admin_side.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tombra.casatopia._model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class AdminDatabase(private val context: Context) {


    //
//
    private val network: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val localSet: SharedPreferences.Editor = context.getSharedPreferences(
        "MySharedPref",
        Context.MODE_PRIVATE
    ).edit()
    private val localGet: SharedPreferences = context.getSharedPreferences(
        "MySharedPref",
        Context.MODE_PRIVATE
    )

    //
//
//
//
    fun uploadEstate(estate: Estate, callback: () -> Unit) {
        network.reference.child("Admins").child(estate.adminId!!).child("Estates")
            .child(estate.estateId!!).setValue(estate).addOnSuccessListener { result ->
            callback()
        }
    }

    //
//
    fun getAuthInfo(): Auth {
        val authenticated = localGet.getString("Authenticated", "false").toString()
        val authId = localGet.getString("AuthId", "NO AUTH ID AVAILABLE").toString()
        val authType = localGet.getString("AuthType", "NO AUTH ID AVAILABLE").toString()
        return Auth(authenticated, authId, authType)
    }

    fun getAllEstates(submit: (List<Estate>) -> Unit) {
        //for each admin
        //that is available
        network.reference.child("Admins").child(getAuthInfo().authId).child("Estates").get()
            .addOnSuccessListener { result ->
                var estates = listOf<Estate>()
                for (estate in result.children) {
                    estates += estate.getValue(Estate::class.java)!!
                }
                Log.d("REPOSITORY", "vellow $estates")
                submit(estates)
            }
    }


    fun getAllClients(submit: (List<User>) -> Unit) {
        //for each admin
        //that is available
        network.reference.child("Admins").child(getAuthInfo().authId).child("Clients").get()
            .addOnSuccessListener { result ->
                var clients = listOf<User>()
                for (client in result.children) {
                    clients += getClientProfile(client.getValue(String::class.java)!!, {})
                }
                submit(clients)
            }
    }


    fun getClientProfile(clientId: String, submit: (User) -> Unit): User = runBlocking {
        Log.d("Repository", "Getting list - 4")
        withContext(Dispatchers.IO) {
            Log.d("Repository", "Client id $clientId")
            val result =
                network.reference.child("Users").child(clientId).child("Profile").get().await()
            val client = result.getValue(User::class.java)!!
            Log.d("Repository", "Getting list- 6")
            submit(client)
            Log.d("Repository", "Getting list- 7")
            return@withContext client
        }
    }


    fun getAdminProfile(submit: (Admin) -> Unit) {
        Log.d("Repository", "Getting list - 4")

        network.reference.child("Admins").child(getAuthInfo().authId).child("Profile").get()
            .addOnSuccessListener { result ->
                val client = result.getValue(Admin::class.java)!!
                submit(client)
            }

    }


    fun getNotifications(submit: (List<Notification>) -> Unit) {
        //for each admin
        //that is available
        network.reference.child("Admins").child(getAuthInfo().authId).child("Notifications").get()
            .addOnSuccessListener { result ->
                var estates = listOf<Notification>()
                for (estate in result.children) {
                    estates += estate.getValue(Notification::class.java)!!
                }
                Log.d("REPOSITORY", "vellow $estates")
                submit(estates)
            }
    }


    fun getChats(adminId: String, submit: (List<Chat>) -> Unit) {
        network.reference.child("Admins").child(getAuthInfo().authId).child("Chats").child(adminId)
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


    fun sendMessage(adminId: String, chat: Chat, submit: () -> Unit) {
        network.reference.child("Admins").child(getAuthInfo().authId).child("Chats").child(adminId)
            .child(chat.timestamp).setValue(chat).addOnSuccessListener {
                //send to admin too
                sendMessageToClient(adminId, chat, submit)
            }
    }

    fun sendMessageToClient(clientId: String, chat: Chat, submit: () -> Unit) {
        network.reference.child("Users").child(clientId).child("Chats").child(chat.sender)
            .child(chat.timestamp).setValue(chat).addOnSuccessListener {
                //send to admin too
                submit()
            }
    }


    fun getTransactionList(submit: (List<Transaction>) -> Unit) {
        Log.d("Repository", "Getting list - 1")
        network.reference.child("Admins").child(getAuthInfo().authId).child("Transactions")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("Repository", "Getting list -2")
                    var transactions = listOf<Transaction>()
                    for (transaction in snapshot.children) {
                        transactions += transaction.getValue(Transaction::class.java)!!
                    }
                    Log.d("Repository", "Getting list - 3 $transactions")
                    submit(transactions)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


    fun getChatList(submit: (List<User>) -> Unit) {
        Log.d("Repository", "Getting list - 1")
        network.reference.child("Users").child(getAuthInfo().authId).child("Chats")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("Repository", "Getting list -2")
                    var admins = listOf<User>()
                    for (recipient in snapshot.children) {
                        admins += getClientProfile(recipient.key!!, {})
                    }

                    Log.d("Repository", "Getting list - 3 $admins")
                    submit(admins)

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


}