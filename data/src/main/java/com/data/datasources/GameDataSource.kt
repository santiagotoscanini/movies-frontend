package com.data.datasources

import android.content.Context
import android.content.SharedPreferences
import com.data.models.Team
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class GameDataSource constructor(context: Context) {
    private companion object {
        const val PREFERENCES_NAME = "GameDataSource"
        const val TEAMS = "Teams"
    }

    private val preferences: SharedPreferences = context.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )

    fun addOrUpdateTeam(team: Team) {
        val savedTeams = getTeamsFromSharedPreferences()
        savedTeams[team.id] = team
        insertTeamIntoSharedPreferences(savedTeams)
    }

    fun getTeams(): HashMap<String, Team>{
        return getTeamsFromSharedPreferences()
    }

    private fun getTeamsFromSharedPreferences(): HashMap<String, Team> {
        val defaultValue = GsonBuilder().create().toJson(HashMap<String, Team>())
        val jsonTeams = preferences.getString(TEAMS, defaultValue)
        val token: TypeToken<HashMap<String, Team>> = object : TypeToken<HashMap<String, Team>>(){}
        return Gson().fromJson(jsonTeams, token.type)
    }

    private fun insertTeamIntoSharedPreferences(teams: HashMap<String, Team>) {
        val jsonTeams = Gson().toJson(teams)
        preferences.edit().putString(TEAMS, jsonTeams).apply()
    }
}
