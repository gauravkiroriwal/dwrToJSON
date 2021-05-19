package com.example.dwrToJSON;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class Helper {
	
	private final String reference = "reference:";
	private final int refLen = 10;
	private Map<String,String> map;
	
	public String OdatatoJSON(String str) throws IOException
	{
		map = new HashMap<String, String>();
		 StringBuffer res = new StringBuffer();
		try {
		      //File myObj = new File("src/oDATAtoJSONConverter/odatafile.txt");
		   
		    //  System.out.println(  myObj.getAbsolutePath() );
		     // Scanner myReader = new Scanner(myObj);
			BufferedReader myReader = new BufferedReader(new StringReader(str));
			String data = null;
		      while ((data = myReader.readLine())!=null) {
		        processline(data);
		        //System.out.println(data);
		      }
		      processMapData();
		      res.append('{');
		      int s=map.size();
		      for (Map.Entry e : map.entrySet())
		      {
		    	  res.append(validateAndReturnValue((String) e.getKey()));
		    	  res.append(':');
		    	 
		    	  res.append(map.get(e.getKey()));
		    	  if(s>1)
		    	  {
		    		  res.append(',');
		    		  s--;
		    	  }
		    	  res.append("\n");
		      }
		      res.append('}');
		      myReader.close();
		    } catch (FileNotFoundException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		return res.toString();
	}

	private void processMapData() {
		// TODO Auto-generated method stub
		 for (Map.Entry e : map.entrySet())
		 {
			 processMapValueForKey((String) e.getKey());
		 }
	}


	private void processMapValueForKey(String key) {
		// TODO Auto-generated method stub
		String value = map.get(key);
		StringBuilder sb = new StringBuilder(value);
		StringBuilder res = new StringBuilder();
		int l=value.length();
		if(l==0)
		{
			map.put(key, "\"\"");
			return;
		}
		int a=-1,b=-1;
		boolean flag=false;
		for(int i=0;i<l;i++)
		{
			
			if(isSpecialChar(value.charAt(i)))
			{
				flag=true;
				if(a==-1)
				{
					a=i;
				}
				else
				{
					b=i;
					String str = sb.substring(a+1, b);
					if(str.length()==0)
					{
						if(sb.charAt(a)==':'&&(sb.charAt(b)==','||sb.charAt(b)=='}'||sb.charAt(b)==']'))
						{
							res.append("\"\"");
						}
					}
					else
					{
						res.append(validateAndReturnValue(str));
					}
					a=b;
				}
				res.append(value.charAt(i));
			}

		}
		if(flag)
		{
			map.put(key, res.toString());
		}
		else
		{
			map.put(key, validateAndReturnValue(value));
		}
		
	}

	private String validateAndReturnValue(String str) {
		// TODO Auto-generated method stub
		 if(str.equals("null")||str.equals("true")||str.equals("false"))
			return str;
		else if (isInteger(str))
		return str;
		else
			return "\""+str+"\"";
	}

	private boolean isInteger(String str) {
		// TODO Auto-generated method stub
		int l =str.length();
		char c='#';
		for(int i=0;i<l;i++)
		{
			c=str.charAt(i);
			if(c<'0'||c>'9')
				return false;
		}
		return true;
	}

	private void processline(String data) {
		// TODO Auto-generated method stub
	    data = removeWhiteSpace(data);
		LineObject lineObject = convertToLineObject(data);
		if(map.containsKey(lineObject.getKey()))
		{
			// error scenario
		}
		else
		{
			String value = processValue(lineObject.getValue());
			map.put(lineObject.getKey(),value);
		}		
	}

	private String removeWhiteSpace(String data) {
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		int l = data.length();
		for(int i=0;i<l;i++)
		{
			if(data.charAt(i)!=' '&&data.charAt(i)!='\t')
				sb.append(data.charAt(i));
		}
		return sb.toString();
	}

	private String processValue(String value) {
		// TODO Auto-generated method stub
		
		StringBuilder sb = new StringBuilder(value);
		StringBuilder res = new StringBuilder();
		
		int l=sb.length();
		for(int i=0;i<l;i++)
		{
			if(i+refLen<l && sb.substring(i,i+refLen).equals(reference))
			{
				StringBuilder sb2 = new StringBuilder();
				i+=refLen;
				while(i<l&&sb.charAt(i)!=','&&sb.charAt(i)!=']'&&sb.charAt(i)!='}')
				{
					sb2.append(sb.charAt(i));
					i++;
				}
				res.append(getValueUsingKey(sb2.toString())) ;
				i--;
			}
			else
			{
				res.append(sb.charAt(i));
			}
		}
		return res.toString();
	}

	private String getValueUsingKey(String string) {
		// TODO Auto-generated method stub
		String res = "";
		if(map.containsKey(string))
		{
			res = map.get(string);
			map.remove(string);
		}
		return res;
	}

	private LineObject convertToLineObject(String data) {
		// TODO Auto-generated method stub
		int l = data.length();
		int a=-1,b=-1;
		for(int i=0;i<l;i++)
		{
			char c =data.charAt(i);
			if(c=='=')
				a=i;
			if(c==':')
			{
				b=i;
				break;
			}
		}
		StringBuilder sb = new StringBuilder(data);
		LineObject lineObject = new LineObject();
		if(a!=-1)
		{
			lineObject.setKey(sb.substring(0,a));
		}
		if(b!=-1)
		{
			lineObject.setType(sb.substring(a+1,b));
			lineObject.setValue(sb.substring(b+1,l));
		}
		else
		{
			lineObject.setValue(sb.substring(a+1,l));
		}
		return lineObject;
	}
	
	private boolean isSpecialChar(char c)
	{
		if(c==':'||c==','||c==']'||c=='}'||c=='['||c=='{')
			return true;
		return false;
	}

}
