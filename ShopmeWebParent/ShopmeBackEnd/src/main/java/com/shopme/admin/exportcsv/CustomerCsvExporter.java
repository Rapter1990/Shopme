package com.shopme.admin.exportcsv;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import com.shopme.admin.util.AbstractExporter;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.User;

public class CustomerCsvExporter extends AbstractExporter{

public void export(List<Customer> listCustomer, HttpServletResponse response) throws IOException {
		
		super.setResponseHeader(response, "text/csv", ".csv", "customers_");
		
		Writer writer = new OutputStreamWriter(response.getOutputStream(), "utf-8");
		writer.write('\uFEFF');
		
		ICsvBeanWriter csvWriter = new CsvBeanWriter(writer, 
				CsvPreference.STANDARD_PREFERENCE);

		String[] csvHeader = {"Customer ID", "First Name", "Last Name", "E-mail",  "City", "State", "Country" , "Enabled"};
		String[] fieldMapping = {"id", "firstName", "lastName", "email",  "city", "state" , "country" , "enabled"};

		csvWriter.writeHeader(csvHeader);

		for (Customer customer : listCustomer) {
			csvWriter.write(customer, fieldMapping);
		}

		csvWriter.close();
	}
}
