package com.google.codelab.reminderwithgps.utils

import android.util.Patterns.EMAIL_ADDRESS
import com.google.codelab.reminderwithgps.R

object ValidationUtils {
    private const val MINIMUM_LENGTH = 6
    private const val NUM_REGEX = "^[0-9]+$"

    fun checkLogin(email: String, password: String): Int? {
        if (email.isEmpty() || password.isEmpty()) {
            return R.string.empty_text
        } else if (isMinPassword(password)) {
            return R.string.min_password
        }
        return null
    }

    /**
     * @param:email,password,passwordConfirm
     * @return:Int?
     * サインアップ情報のバリデーションチェックを行う。
     * サインアップ情報が有効の場合：nullを返す
     * 　　　　　　　　　無効の場合：エラーメッセージを返す
     */
    fun checkSignUp(
        email: String,
        password: String,
        passwordConfirm: String
    ): Int? {
        if (email.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
            return R.string.empty_text
        } else if (!isValidEmail(email)) {
            return R.string.invalid_email
        } else if (isMinPassword(password)) {
            return R.string.min_password
        } else if (isMatchPassword(password, passwordConfirm)) {
            return R.string.not_match_password
        }
        return null
    }


    private fun isValidEmail(email: String?): Boolean {
        return EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isMinPassword(password: String): Boolean {
        if (password.length >= MINIMUM_LENGTH) {
            return false
        }
        return true
    }

    private fun isMatchPassword(password: String, passwordConfirm: String): Boolean {
        if (password.equals(passwordConfirm)) {
            return false
        }
        return true
    }

}
