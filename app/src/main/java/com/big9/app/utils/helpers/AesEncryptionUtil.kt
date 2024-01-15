package com.big9.app.utils.helpers

object AesEncryptionUtil {

    /*private const val AES_ALGORITHM = "AES"
    private const val AES_TRANSFORMATION = "AES/CBC/PKCS5Padding"

    fun encrypt(data: String, key: String, iv: String): String {
        // Ensure IV is 16 bytes long
        val fixedIV = if (iv.length < 16) iv + " ".repeat(16 - iv.length) else iv.substring(0, 16)

        val keySpec = SecretKeySpec(key.toByteArray(Charsets.UTF_8), AES_ALGORITHM)
        val ivParameterSpec = IvParameterSpec(fixedIV.toByteArray(Charsets.UTF_8))

        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec)

        val encryptedBytes = cipher.doFinal(data.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
    }

    fun decrypt(encryptedData: String, key: String, iv: String): String {
        // Ensure IV is 16 bytes long
        val fixedIV = if (iv.length < 16) iv + " ".repeat(16 - iv.length) else iv.substring(0, 16)

        val keySpec = SecretKeySpec(key.toByteArray(Charsets.UTF_8), AES_ALGORITHM)
        val ivParameterSpec = IvParameterSpec(fixedIV.toByteArray(Charsets.UTF_8))

        val cipher = Cipher.getInstance(AES_TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec)

        val encryptedBytes = Base64.decode(encryptedData, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes, Charsets.UTF_8)
    }*/
}