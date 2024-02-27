package com.wantech.gdsc_msu.feature_auth.sign_up.domain.usecase

class NavigateToSignInScreen (
    private val navigateToSignInScreen:() -> Unit
) {
    suspend operator fun invoke() = navigateToSignInScreen()
}