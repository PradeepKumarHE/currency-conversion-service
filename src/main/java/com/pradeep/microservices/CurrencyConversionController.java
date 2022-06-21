package com.pradeep.microservices;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CurrencyConversionController {

	@GetMapping("/currency-exchange/from/{from}/to/{to}/quantity/{quantity}")
	public CurrecnyConversion retrieveExchangeValue(@PathVariable("from") String from, @PathVariable("to") String to,@PathVariable("quantity") BigDecimal quantity) {
		CurrecnyConversion currecnyConversion = getCalcualtedValueForQuantity(from, to, quantity);
		return currecnyConversion;	
	}

	private CurrecnyConversion getCalcualtedValueForQuantity(String from, String to, BigDecimal quantity) {
		HashMap<String, String> uriVariables = new HashMap<String, String>();
		uriVariables.put("from", from);
		uriVariables.put("to", to);
		ResponseEntity<CurrecnyConversion> responseEntity=new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/USD/to/INR", CurrecnyConversion.class,uriVariables);
		CurrecnyConversion currecnyConversion=responseEntity.getBody();
		return new CurrecnyConversion(currecnyConversion.getId(),from,to,quantity,currecnyConversion.getConverstionMultiple(),quantity.multiply(currecnyConversion.getConverstionMultiple()),currecnyConversion.getApplicationPortNumber());
	}

}
