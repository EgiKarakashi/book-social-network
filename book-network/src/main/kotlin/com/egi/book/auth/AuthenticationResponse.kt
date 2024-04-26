package com.egi.book.auth

import com.fasterxml.jackson.annotation.JsonProperty

data class AuthenticationResponse(
    @JsonProperty("token")
    var token: String = "",
)
