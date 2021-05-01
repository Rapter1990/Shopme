package com.shopme.admin.brand;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.shopme.admin.repository.BrandRepository;
import com.shopme.admin.service.BrandService;
import com.shopme.common.entity.Brand;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
public class BrandServiceTests {

	@MockBean private BrandRepository repo;

	@InjectMocks private BrandService service;

	@Test
	public void testCheckUniqueInNewModeReturnDuplicate() {
		Integer id = null;
		String name = "Acer";
		Brand brand = new Brand(name);

		Mockito.when(repo.findByName(name)).thenReturn(brand);

		String result = service.checkUnique(id, name);
		assertThat(result).isEqualTo("Duplicate");
	}

	@Test
	public void testCheckUniqueInNewModeReturnOK() {
		Integer id = null;
		String name = "AMD";

		Mockito.when(repo.findByName(name)).thenReturn(null);

		String result = service.checkUnique(id, name);
		assertThat(result).isEqualTo("OK");
	}

	@Test
	public void testCheckUniqueInEditModeReturnDuplicate() {
		Integer id = 1;
		String name = "Canon";
		Brand brand = new Brand(id, name);

		Mockito.when(repo.findByName(name)).thenReturn(brand);

		String result = service.checkUnique(2, "Canon");
		assertThat(result).isEqualTo("Duplicate");
	}

	@Test
	public void testCheckUniqueInEditModeReturnOK() {
		Integer id = 1;
		String name = "Acer";
		Brand brand = new Brand(id, name);

		Mockito.when(repo.findByName(name)).thenReturn(brand);

		String result = service.checkUnique(id, "Acer Ltd");
		assertThat(result).isEqualTo("OK");
	}	
}
