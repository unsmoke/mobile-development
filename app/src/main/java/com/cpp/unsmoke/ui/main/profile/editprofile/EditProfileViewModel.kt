package com.cpp.unsmoke.ui.main.profile.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cpp.unsmoke.data.remote.Result
import com.cpp.unsmoke.data.remote.responses.userprofile.UpdateUserDataResponse
import com.cpp.unsmoke.data.remote.responses.userprofile.UserDetailDataResponse
import com.cpp.unsmoke.repository.UserDataRepository
import java.io.File

class EditProfileViewModel(private val userDataRepository: UserDataRepository) : ViewModel() {
    fun getUserData(): LiveData<Result<UserDetailDataResponse>> {
        return userDataRepository.getUserData()
    }

    fun updateUserData(
        username: String,
        file: File?
    ): LiveData<Result<UpdateUserDataResponse>>{
        return userDataRepository.updateDataProfile(username, file)
    }

}