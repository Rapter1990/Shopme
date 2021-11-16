package com.shopme.admin.order;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.repository.OrderDetailRepository;
import com.shopme.common.entity.order.OrderDetail;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class OrderDetailRepositoryTests {

	@Autowired 
	private OrderDetailRepository repo;

	@Test
	public void testFindWithCategoryAndTimeBetween() throws ParseException {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = dateFormatter.parse("2021-09-01");
		Date endTime = dateFormatter.parse("2021-09-30");

		List<OrderDetail> listOrderDetails = repo.findWithCategoryAndTimeBetween(startTime, endTime);

		assertThat(listOrderDetails.size()).isGreaterThan(0);

		for (OrderDetail detail : listOrderDetails) {
			
			System.out.printf("%-30s | %d | %10.2f| %10.2f | %10.2f \n", 
					detail.getProduct().getCategory().getName(),
					detail.getQuantity(), 
					detail.getProductCost(),
					detail.getShippingCost(), 
					detail.getSubtotal()
					);
		}
		
	}

	@Test
	public void testFindWithProductAndTimeBetween() throws ParseException {
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		Date startTime = dateFormatter.parse("2021-10-01");
		Date endTime = dateFormatter.parse("2021-10-30");

		List<OrderDetail> listOrderDetails = repo.findWithProductAndTimeBetween(startTime, endTime);

		assertThat(listOrderDetails.size()).isGreaterThan(0);

		for (OrderDetail detail : listOrderDetails) {
			
			System.out.printf("%-100s | %d | %10.2f| %10.2f | %10.2f \n", 
					detail.getProduct().getShortName(),
					detail.getQuantity(), 
					detail.getProductCost(),
					detail.getShippingCost(), 
					detail.getSubtotal()
					);
		}
	}	
}
