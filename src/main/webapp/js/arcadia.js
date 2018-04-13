/**
 * 
 */
var ambito="";
function validar()
{
	var email=document.getElementById("email").value;
	var emailC=document.getElementById("emailC").value;
	if (email=="")
		{
		alert("Por favor, indique una dirección de correo electrónico.");
		return false;
		}
	if (email==emailC)
		return true;
	else
		{
		alert("Las direcciones de correo electrónico no coinciden.");
		return false;
		}
}
function procesar(peticion)
{
	var json=JSON.parse(peticion);
	var contexto=document.getElementById("contexto");
	contexto.value=JSON.stringify(json.context);
	var chat=document.getElementById("textoChat");
	chat.innerHTML+="<span style=\"text:red\">AEAT:</span>"+json.output.text[0]+"<br>";
}
function getAjax (url,success)
{
    var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP");
    xhr.open('GET', url);
    xhr.onreadystatechange = function() {
        if (xhr.readyState>3 && xhr.status==200) { success(xhr.responseText); }
    };
    xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
    xhr.send();
    return xhr;
}
function postAjax(url, data, success) {
    var params = typeof data == 'string' ? data : Object.keys(data).map(
            function(k){ return encodeURIComponent(k) + '=' + encodeURIComponent(data[k]) }
        ).join('&');

    var xhr = window.XMLHttpRequest ? new XMLHttpRequest() : new ActiveXObject("Microsoft.XMLHTTP");
    xhr.open('POST', url);
    xhr.onreadystatechange = function() {
        if (xhr.readyState>3 && xhr.status==200) { success(xhr.responseText); }
    };
    xhr.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
    xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=UTF-8');
    xhr.send(params);
    return xhr;
}
function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}
function asignarAmbito(valor)
{
	ambito=valor;
	document.title="Asistente Virtual - "+ambito;
	document.getElementById("titulo").innerHTML="Bienvenido al Asistente Virtual de la AEAT para "+ambito;
}
function calcularAmbito()
{
	asignarAmbito(getParameterByName("ambito",location.href));
}
function enviarMensaje()
{
	var chat=document.getElementById("textoChat");
	var msg=document.getElementById("textoMensaje").value;
	var contexto=document.getElementById("contexto").value;
	chat.innerHTML+="<span style=\"text:blue\">Usted:</span>"+msg+"<br>";
	postAjax('/AsistenteVirtual/AsistenteVirtual', 'ambito='+ambito+'&mensaje='+msg+"&contexto="+contexto, function(data){ procesar(data); });
	
}
function iniciarChat()
{
	var chat=document.getElementById("textoChat");
	postAjax('/AsistenteVirtual/AsistenteVirtual', 'ambito='+ambito+'&mensaje=', function(data){ procesar(data); });
	
}
function crearAsistente()
{
	var divContenido=document.getElementById("contenedorder");
	divContenido.innerHTML="";
	var divAv=document.createElement("div");
	var divAvatar=document.createElement("div");
	var divChat=document.createElement("div");
	var divMensaje=document.createElement("div");
	var botonMensaje=document.createElement("input");
	var textoMensaje=document.createElement("input");
	var textoChat=document.createElement("div");
	
	divAvatar.innerHTML="<img src=\"/AsistenteVirtual/img/avatarIVA02Neutra.gif\">";
	
	textoChat.setAttribute("id","textoChat");
	textoChat.setAttribute("style","margin: 0px; width: 368px; height: 284px;");
	divChat.appendChild(textoChat);
	textoMensaje.setAttribute("type","text");
	textoMensaje.setAttribute("id","textoMensaje");
	divMensaje.appendChild(textoMensaje);
	botonMensaje.setAttribute("type","button");
	botonMensaje.setAttribute("onClick","enviarMensaje();");
	botonMensaje.setAttribute("value","Enviar Mensaje");
	divMensaje.appendChild(botonMensaje);
	divAv.setAttribute("id","AsistenteVirtual");
	divContenido.appendChild(divAv);
	divAv.appendChild(divAvatar);
	divAv.appendChild(divChat);
	divAv.appendChild(divMensaje);
	
	divMensaje.appendChild(textoMensaje);
	divMensaje.appendChild(botonMensaje);

	iniciarChat();
}
function iniciarAsistente()
{
	if (!validar()) return false;
	
	crearAsistente();
}
calcularAmbito();