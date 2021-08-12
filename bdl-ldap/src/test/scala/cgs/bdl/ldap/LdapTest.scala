package cgs.bdl.ldap

import com.novell.ldap.LDAPConnection
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.{BeforeAndAfterAll, FunSuite}

import scala.util.{Failure, Success, Try}


/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

@RunWith(classOf[JunitRunner])
class LdapTest extends AnyFunSuite with BeforeAndAfterAll {
	
	val ldap_host: String = "172.16.11.254"
	val ldap_port: Int = LDAPConnection.DEFAULT_PORT
	val ldap_basedn: String = "OU=研发中心,OU=河姆渡(上海),DC=homedo,DC=com"
	val ldap_username: String = "yiguanldap"
	val ldap_passwd: String = "BIGdata@1203"
	
	var triedCnxt: Try[LDAPConnection] = _
	
	test ("hmd ldap connect") {
		
		triedCnxt match {
			case Success(cnxt) =>
				println(s"=====> Get Ldap Connection: $cnxt")
			case Failure(exception) =>
				println(s"=====> Exception: ${exception.getMessage}")
		}
	}
	
	override protected def beforeAll(): Unit = {
		triedCnxt = Try {
			val cnxt: LDAPConnection = new LDAPConnection()
			cnxt.connect(ldap_host, ldap_port)
			cnxt.bind(LDAPConnection.LDAP_V3, ldap_basedn, ldap_passwd.getBytes("UTF-8"))
			cnxt
		}
		
		
	}
	
	override protected def afterAll(): Unit = super.afterAll()
}
