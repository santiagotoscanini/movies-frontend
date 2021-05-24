package com.data.repositories

import com.data.Response
import com.data.models.Team

interface TeamRepository {
    fun getTeam(teamId: String, cBack: (Response<Team>) -> Unit)
}
