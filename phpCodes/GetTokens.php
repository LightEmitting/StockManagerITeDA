<?php

$curl = curl_init();
//modificar el campo code e ingresarlo en http://localhost/GetTokens.php
//obtener access_token y refresh_token
	$code = "_JIoM2-d3fozGenyrKXlW6VC6Nwz1JsJKZfP3wAD";
	$client_id = "7f35f990-d3be-4d95-9d91-44d282081111";
	$client_secret = "R7bP0hY1iO1cH1rJ2tQ6iE3vU3kI4wT2gG8oT7gY6nL8sC3qF2";
	$redirect_uri = "https%3A%2F%2Flocalhost";
	$grant_type = "authorization_code";
	
curl_setopt_array($curl, array(
  CURLOPT_URL => "https://sso.digikey.com/as/token.oauth2",
  CURLOPT_RETURNTRANSFER => true,
  CURLOPT_ENCODING => "",
  CURLOPT_MAXREDIRS => 10,
  CURLOPT_TIMEOUT => 30,
  CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
  CURLOPT_CUSTOMREQUEST => "POST",
  CURLOPT_POSTFIELDS => "code=$code&client_id=$client_id&client_secret=$client_secret&redirect_uri=$redirect_uri&grant_type=$grant_type",
  CURLOPT_HTTPHEADER => array(
    "Cache-Control: no-cache",
    "Content-Type: application/x-www-form-urlencoded",
    "Postman-Token: 45d58886-70f5-4827-b648-fc6d930d486c"
  ),
));

$response = curl_exec($curl);
$err = curl_error($curl);

curl_close($curl);

if ($err) {
  echo "cURL Error #:" . $err;
} else {
  echo $response;
}