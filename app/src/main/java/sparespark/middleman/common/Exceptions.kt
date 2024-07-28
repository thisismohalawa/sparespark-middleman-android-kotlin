package sparespark.middleman.common

import java.io.IOException

const val NO_INTERNET_CONNECTION = "No Internet Connection."
const val INVALID_DATA_ENTRY = "Invalid Data Entry"

class NoConnectivityException : IOException(NO_INTERNET_CONNECTION)
class InvalidDataEntryException :IOException(INVALID_DATA_ENTRY)
