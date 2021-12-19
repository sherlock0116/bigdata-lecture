import org.apache.commons.codec.binary.Base64
import org.junit.runner.RunWith
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatestplus.junit.JUnitRunner

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

@RunWith(classOf[JUnitRunner])
class CodecSuite extends AnyFunSuiteLike {
	
	
	test ("base64") {
		val pswd: String = "hao123&*%~}:"
		val encryption: String = base64Encryption(pswd)
		val decryption: String = base64Decryption(encryption)
		println(s"加密: ${encryption}")
		println(s"解密: ${decryption}")
	}
	
	def base64Encryption(str: String): String = {
		val base64: Base64 = new Base64()
		base64.encodeToString(str.getBytes)
	}
	
	def base64Decryption(encryption: String): String = {
		val base64: Base64 = new Base64()
		new String(base64.decode(encryption))
	}
}
