package com.rasyidin.githubuser.model

data class User (
    var name: String? = null,
    var login: String? = null,
    var location: String? = null,
    var company: String? = null,
    var followers: Int? = 0,
    var following: Int? = 0,
    var avatars: String? = null,
    var type: String? = null
)


