package sparespark.middleman.login

sealed class LoginEvent<out T> {
    data object OnAuthButtonClick : LoginEvent<Nothing>()
    data object OnStart : LoginEvent<Nothing>()
    data class OnGoogleSignInResult<out LoginResult>(val result: LoginResult) :
        LoginEvent<LoginResult>()
    data object OnDeleteAccTextClick : LoginEvent<Nothing>()
}