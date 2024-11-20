package com.example.chatapp.network

import android.content.Context
import android.util.Log
import com.example.chatapp.R
import com.example.chatapp.model.Conversation
import com.example.chatapp.model.User
import okhttp3.FormBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.json.JSONArray
import org.json.JSONObject

object API {
    private val client = OkHttpClient()

    fun createUser(context: Context,username:String,email:String, password:String,):Boolean{
        val builder = Request.Builder()
        builder.url("${context.getString(R.string.url)}create-user")
        val json = """
            {
                "username": "$email",
                "credentials": [
                    {
                        "temporary": false,
                        "type": "password",
                        "value": "$password"
                    }
                ],
                "attributes": 
                    {"imageUrl": "https://static-00.iconduck.com/assets.00/person-icon-1901x2048-a9h70k71.png"}
                ,
                "email": "$email",
                "emailVerified": false,
                "enabled": true,
                "firstName": "${username.split(" ")[0]}",
                "lastName": "${if (username.contains(" ")) username.substring(username.indexOf(" ")+1) else ""}"
            }
        """
        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(),json)
        builder.post(requestBody)
        val request = builder.build()
        try {
            val response = client.newCall(request).execute()
            return response.isSuccessful
        }catch (e:Exception){
            return false
        }
    }

    fun getAuthenticatedToken(context: Context, username: String, password: String):String{
        val builder = Request.Builder()
        builder.url("${context.getString(R.string.url)}get-access-token")
        val formBody = FormBody.Builder()
            .add("username", username)
            .add("password", password)
            .build()
        val request = builder.post(formBody).build()
        try {
            val response = client.newCall(request).execute()
            if (response.isSuccessful){
                val json = JSONObject(response.body!!.string())
                return json.getString("access_token")
            } else return ""
        }catch (e:Exception){
            return ""
        }
    }

    fun getAuthenticatedUser(context: Context, token: String):User{
        var user = User()

        val builder = Request.Builder()
        builder.url("${context.getString(R.string.url)}api/users/get-authenticated-user?forceResync=false")
        builder.addHeader("Authorization", "Bearer $token")
        val request = builder.build()
        try {
            val response = client.newCall(request).execute()
            val json = JSONObject(response.body!!.string())
            user = readUserFromJson(json)
        } catch (_:Exception){

        }
        return user
    }

    fun getAllConversation(context: Context,token:String):ArrayList<Conversation> {
        val arrayList = arrayListOf<Conversation>()
        Log.e("token",token)
        val builder = Request.Builder()
        builder.addHeader("Authorization", "Bearer $token")
        builder.url("${context.getString(R.string.url)}api/conversations")
        val request = builder.build()
        try {
            val response = client.newCall(request).execute()
            val json = JSONArray(response.body!!.string())
            for (i in 0..json.length()-1){
                arrayList.add(readConversationFromJson(jsonObject = json.getJSONObject(i)))
            }
        } catch (e: Exception) {
            Log.e("error", e.message!!)
        }

        return arrayList
    }


    fun readUserFromJson(jsonObject: JSONObject):User{
        val user = User()
        if (jsonObject.has("email")) user.email = jsonObject.getString("email")
        user.userName = jsonObject.getString("firstName")+" "+jsonObject.getString("lastName")
        user.imageUrl = jsonObject.getString("imageUrl")
        user.id = jsonObject.getString("publicId")
        if (jsonObject.has("lastSeen")) user.lastSeen = jsonObject.getString("lastSeen")
        return user
    }

    fun readConversationFromJson(jsonObject: JSONObject):Conversation{
        val conversation = Conversation()

        conversation.name = jsonObject.getString("name")
        conversation.id = jsonObject.getString("publicId")
        val memberList = jsonObject.getJSONArray("members")
        for (i in 0..memberList.length()-1){
            conversation.memberList.add(readUserFromJson(memberList.getJSONObject(i)))
        }

        return conversation
    }
}