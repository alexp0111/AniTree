package com.ikbo0621.anitree.util

object FireStoreCollection {
    val USER = "user"
    val TREE = "tree"
    val INNER_PATH = "trees"
}

object ParserConstants {
    val BASIC_URL = "https://www.anime-planet.com/"
    val SEARCH_INPUT_DELAY: Long = 2000 // for coroutine delay
    val TIMEOUT: Int = 5000 // for Jsoup request
}

object SharedPrefConstants {
    val LOCAL_SHARED_PREF = "local_shared_pref"
    val USER_SESSION = "user_session"
}

object RegistrationParams {
    val PASSWORD_LENGTH = 6
}
