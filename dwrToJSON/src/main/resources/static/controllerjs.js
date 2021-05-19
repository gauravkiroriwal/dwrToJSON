const url = "/convert";

function onPageLoad()
{
    document.getElementById("textArea1").value =getExampleValue(); 
}
function convert()
{ 
    document.getElementById("textArea2").value = "";
	const req = new XMLHttpRequest();
	req.open("POST",url,true);
    req.setRequestHeader("Content-Type", "text/plain");
    var params =document.querySelector('#textArea1').value;
	req.send(params);
	req.onload = ()=>{
		if(req.status ===200)
		{
            document.getElementById("textArea2").value = req.response;
		}
		else
		{
			console.log('error');
		}
	}
}

function getExampleValue()
{
    var val ="c0-param0=string:Value1\n"+
              "c0-e1=string:Value2\n"+
              "c0-e2=string:Value3\n"+
              "c0-e3=string:Value4\n"+
              "c0-param1=Array:[reference:c0-e1,reference:c0-e2,reference:c0-e3]";
    return val;
}