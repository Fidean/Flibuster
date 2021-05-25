package ru.fidean.flibuster.ViewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.fidean.flibuster.LoginData
import ru.fidean.flibuster.ServerAPI

private const val TAG = "LoginViewModelTAG"

sealed class LoginState {
    object LoadingState : LoginState()
    object Succses : LoginState()
    class ErrorState(var message: String) : LoginState()
}

class LoginViewModel : ViewModel() {
    var state = MutableLiveData<LoginState>().apply { postValue(LoginState.LoadingState) }

    fun login(username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val doc = Jsoup.connect("http://flibusta.site/").get()
            val formBuildID = doc.select("input[name=form_build_id]").first().attr("id").toString()
            Log.d(TAG, formBuildID)
            val postLogin = ServerAPI.api.login(
                username,
                password,
                "Вход в систему",
                formBuildID,
                "user_login_block",
                "http://flibusta.site/openid/authenticate?destination=node",
                ""
            )
            postLogin.enqueue(object: Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    if (response.code() == 302){
                        LoginData.cookie = response.headers().get("Set-Cookie").toString().substringBefore(";")
                        state.postValue(LoginState.Succses)
                    }

                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                }

            })
        }
    }

}