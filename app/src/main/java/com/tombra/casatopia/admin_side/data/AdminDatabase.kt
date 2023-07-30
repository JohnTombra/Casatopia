package com.tombra.casatopia.admin_side.data

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tombra.casatopia._model.*
import com.tombra.casatopia.user_side.data.UserDatabase
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
        return UserDatabase(context).getAuthInfo()
    }

    fun getAllEstates(submit: (List<Estate>) -> Unit) {
        //for each admin
        //that is available

        network.reference.child("Admins").child(getAuthInfo().authId).child("Estates").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var estates = listOf<Estate>()
                for (estate in snapshot.children) {
                    estates += estate.getValue(Estate::class.java)!!
                }
                Log.d("REPOSITORY", "vellow $estates")
                submit(estates)
            }
            override fun onCancelled(error: DatabaseError) {

            }

        })


    }


    fun getAllClients(submit: (List<User>) -> Unit) {
        //for each admin
        //that is available

        network.reference.child("Admins").child(getAuthInfo().authId).child("Clients").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                var clients = listOf<User>()
                for (client in snapshot.children) {
                    clients += getClientProfile(client.getValue(String::class.java)!!, {})
                }
                submit(clients)
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })


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

                network.reference.child("Admins").child(getAuthInfo().authId).child("Chats")
                    .child(adminId)
                    .child("total").get().addOnSuccessListener {
                        //     Log.d("REPOSITORY", "SUCCESS 1")
                        if (it.exists()) {

                            var old = it.getValue(Int::class.java)!!
                            var new = old + 1
                            //   Log.d("REPOSITORY", "SUCCESS X $old")
                            network.reference.child("Admins").child(getAuthInfo().authId)
                                .child("Chats").child(adminId)
                                .child("total").setValue(new)
                            //    Log.d("REPOSITORY", "SUCCESS 2 $new")
                        } else {
                            network.reference.child("Admins").child(getAuthInfo().authId)
                                .child("Chats").child(adminId)
                                .child("total").setValue(1)
                            network.reference.child("Admins").child(getAuthInfo().authId)
                                .child("Chats").child(adminId)
                                .child("read").setValue(0)
                            //  Log.d("REPOSITORY", "SUCCESS 3")
                        }
                    }



                sendMessageToClient(adminId, chat, submit)
            }
    }


    fun uploadDocument(document: Document, submit: () -> Unit) {
        network.reference.child("Admins").child(getAuthInfo().authId).child("Documents").child(document.timestamp)
            .setValue(document).addOnSuccessListener {
                //send to admin too
                submit()
            }
    }



    fun getDocuments(submit: (List<Document>) -> Unit) {
        network.reference.child("Admins").child(getAuthInfo().authId).child("Documents")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var chats = listOf<Document>()
                    for (chat in snapshot.children) {
                        chats += chat.getValue(Document::class.java)!!
                    }
                    submit(chats)
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }



    fun sendMessageToClient(clientId: String, chat: Chat, submit: () -> Unit) {
        network.reference.child("Users").child(clientId).child("Chats").child(chat.sender)
            .child(chat.timestamp).setValue(chat).addOnSuccessListener {
                //send to admin too


                network.reference.child("Users").child(clientId).child("Chats").child(chat.sender)
                    .child("total").get().addOnSuccessListener {

                        if (it.exists()) {
                            var old = it.getValue(Int::class.java)!!
                            var new = old + 1
                            network.reference.child("Users").child(clientId).child("Chats")
                                .child(chat.sender)
                                .child("total").setValue(new)
                        } else {
                            network.reference.child("Users").child(clientId).child("Chats")
                                .child(chat.sender)
                                .child("total").setValue(1)
                            network.reference.child("Users").child(clientId).child("Chats")
                                .child(chat.sender)
                                .child("read").setValue(0)
                        }
                    }




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
        network.reference.child("Admins").child(getAuthInfo().authId).child("Chats")
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

                }
            })
    }









    fun listenForAllUnread(callback: (Int) -> Unit) {
        Log.d("REPOSITORY", "Listening.......1")



        getChatList {
            var map = mutableMapOf<String, Int>()
            for (id in it) {
                Log.d("REPOSITORY", "Listening.......3y")
                listenForSingleUnread(id.userId) { data1, data2 ->

                    map.put(data1,data2)


                    //    list[0] = data2

                    callback(map.values.sum())
                    //         Log.d("REPOSITORY", "Global $global")
                }
            }
        }
    }


    //get this for all chats and then calculate the difference between all

    fun listenForSingleUnread(chatId: String, callback: (String, Int) -> Unit) {
        Log.d("REPOSITORY", "Listening.......3z")


        var total = 0
        var seen = 0




        network.reference.child("Admins/${getAuthInfo().authId}/Chats/${chatId}/total")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    Log.d("REPOSITORY", "Listening.......3z1")
                    if (snapshot.exists()) {
                        Log.d("REPOSITORY", "Listening.......3z2")

                        total = snapshot.getValue(Int::class.java)!!

                        //  if(seen != 0){
                        callback(chatId,total - seen)
                        //}


                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })


        network.reference.child("Admins/${getAuthInfo().authId}/Chats/${chatId}/read")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d("REPOSITORY", "Listening.......3z3")
                    if (snapshot.exists()) {
                        Log.d("REPOSITORY", "Listening.......3z4")

                        seen = snapshot.getValue(Int::class.java)!!


                        //  if(total != 0){


                        callback(chatId,total - seen)
                        //}


                        Log.d("REPOSITORY", "Global $total - seen")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })


    }










    fun checkTotal(chatId: String, callback: (String,Int) -> Unit){
        network.reference.child("Admins/${getAuthInfo().authId}/Chats/${chatId}/total")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        callback(chatId,snapshot.getValue(Int::class.java)!!)
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
    }

    fun updateRead(chatId: String,toInt: Int){
        network.reference.child("Users/${getAuthInfo().authId}/Chats/${chatId}/read").setValue(toInt)
    }








}