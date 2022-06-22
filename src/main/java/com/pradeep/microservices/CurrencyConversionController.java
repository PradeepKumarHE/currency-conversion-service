package com.pradeep.microservices;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {

	@Autowired
	private CurrencyExchangeProxy currencyExchangeProxy;

	@GetMapping("/currency-exchange/from/{from}/to/{to}/quantity/{quantity}")
	public CurrecnyConversion retrieveExchangeValueFromRestTemplate(@PathVariable("from") String from, @PathVariable("to") String to,@PathVariable("quantity") BigDecimal quantity) {
		CurrecnyConversion currecnyConversion = getDataFromRestTemplate(from, to, quantity);
		return new CurrecnyConversion(currecnyConversion.getId(),from,to,quantity,currecnyConversion.getConverstionMultiple(),quantity.multiply(currecnyConversion.getConverstionMultiple()),currecnyConversion.getApplicationPortNumber()+" From Rest Template");	
	}
	
	@GetMapping("/currency-exchange-feign/from/{from}/to/{to}/quantity/{quantity}")
	public CurrecnyConversion retrieveExchangeValueFromFeign(@PathVariable("from") String from, @PathVariable("to") String to,@PathVariable("quantity") BigDecimal quantity) {
		CurrecnyConversion currecnyConversion = currencyExchangeProxy.retrieveExchangeValue(from, to);
		return new CurrecnyConversion(currecnyConversion.getId(),from,to,quantity,currecnyConversion.getConverstionMultiple(),quantity.multiply(currecnyConversion.getConverstionMultiple()),currecnyConversion.getApplicationPortNumber()+" From Feign");	
	}

	private CurrecnyConversion getDataFromRestTemplate(String from, String to, BigDecimal quantity) {
		HashMap<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CurrecnyConversion> responseEntity=new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/USD/to/INR", CurrecnyConversion.class,uriVariables);
		return responseEntity.getBody();
	}
	
	

}
