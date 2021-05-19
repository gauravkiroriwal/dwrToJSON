package com.example.dwrToJSON;

import java.io.IOException;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class controller {
	
	 @CrossOrigin 
	 @RequestMapping(
		        method = { RequestMethod.POST },
		        value = { "/convert" })
		  
		    public String conver(@RequestBody String str) throws IOException
		    {
		
		     //   return str;
		        Helper h = new Helper();
		      String res = h.OdatatoJSON(str);
		      return res;
		    }
}
