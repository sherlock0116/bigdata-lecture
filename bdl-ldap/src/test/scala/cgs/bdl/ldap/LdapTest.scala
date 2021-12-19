package cgs.bdl.ldap

import org.apache.directory.ldap.client.api.LdapNetworkConnection
import org.junit.runner.RunWith
import org.scalatest.BeforeAndAfterAll
import org.scalatest.funsuite.AnyFunSuite
import org.scalatestplus.junit.JUnitRunner

import scala.util.{Failure, Success, Try}


/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

@RunWith(classOf[JUnitRunner])
class LdapTest extends AnyFunSuite with BeforeAndAfterAll {
	
	// 172.16.220.230
	val ldap_host: String = "ads.homedo.com"
	val ldap_port: Int = 389
	val ldap_dn: String = "OU=研发中心,OU=河姆渡(上海)"
	val ldap_username: String = "yiguanldap"
	val ldap_passwd: String = "BIGdata@1203"
	
	test ("hmd ldap simple connect") {
		val triedCnxt: Try[LdapNetworkConnection] = Try {
			val cnxt: LdapNetworkConnection = new LdapNetworkConnection(ldap_host, ldap_port)
			cnxt
		}
		triedCnxt match {
			case Success(cnxt) =>
				println(s"=====> Get Ldap Connection: $cnxt")
				cnxt.close()
			case Failure(exception) =>
				println(s"=====> Exception: ${exception.getMessage}")
		}
		
	}
	
}
