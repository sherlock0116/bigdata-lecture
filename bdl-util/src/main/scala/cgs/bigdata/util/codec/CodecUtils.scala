package cgs.bigdata.util.codec

import org.apache.commons.codec.binary.Base64
import org.apache.commons.codec.digest.DigestUtils

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object CodecUtils {
	
	private val base64 = new Base64()
	
	def md5Hex(str: String): String = DigestUtils.md5Hex(str)
	
	def base64Encryption(str: String): String = base64.encodeToString(str.getBytes)
	
	def base64Decryption(encryption: String): String = new String(base64.decode(encryption))
}
