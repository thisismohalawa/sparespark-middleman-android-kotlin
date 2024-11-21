package sparespark.middleman.core

import java.io.IOException

const val NO_INTERNET_CONNECTION = "No Internet Connection."
const val UNAUTHORIZED = "unauthorized."

class NoConnectivityException : IOException(NO_INTERNET_CONNECTION)
class UnAuthorizedException : Exception(UNAUTHORIZED)
