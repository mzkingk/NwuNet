package com.shuaiqing.nwunet.util

import java.security.MessageDigest

object MD5 {
    /**
     * MD5 加密并返回十六进制结果
     */
    fun md5(text: String): String {
        val messageDigest = MessageDigest.getInstance("MD5")
        messageDigest.update(text.toByteArray())
        val bytes = messageDigest.digest()
        return parseHex(bytes)
    }

    private fun parse(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(b)
        }
        return sb.toString()
    }

    private fun parseHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            val tmp = (b.toInt() and 0xff).toString(16)
            if (tmp.length == 1) {
                sb.append("0")
            }
            sb.append(tmp)
        }
        return sb.toString()
    }
}