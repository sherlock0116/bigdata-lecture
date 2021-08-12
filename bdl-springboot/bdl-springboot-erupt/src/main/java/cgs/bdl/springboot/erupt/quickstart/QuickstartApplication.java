package cgs.bdl.springboot.erupt.quickstart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import xyz.erupt.core.annotation.EruptScan;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */

@SpringBootApplication
@EntityScan
@EruptScan
public class QuickstartApplication {

	public static void main (String[] args) {
		SpringApplication.run(QuickstartApplication.class, args);
	}
}
