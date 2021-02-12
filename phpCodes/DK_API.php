<?php
require 'config/configStock.php';

$curl = curl_init();
	
	$client_id = "7f35f990-d3be-4d95-9d91-44d282081111";
	$client_secret = "R7bP0hY1iO1cH1rJ2tQ6iE3vU3kI4wT2gG8oT7gY6nL8sC3qF2";
	$grant_type = "refresh_token";
	$refresh_token = $_POST["refresh_token"];
	//$refresh_token = "l7g71Snax4J4NvYPqiUY3Cyz5IxWuOVkG8C6jTB8tq"; 
	
curl_setopt_array($curl, array(
  CURLOPT_URL => "https://sso.digikey.com/as/token.oauth2",
  CURLOPT_RETURNTRANSFER => true,
  CURLOPT_ENCODING => "",
  CURLOPT_MAXREDIRS => 10,
  CURLOPT_TIMEOUT => 30,
  CURLOPT_HTTP_VERSION => CURL_HTTP_VERSION_1_1,
  CURLOPT_CUSTOMREQUEST => "POST",
  CURLOPT_POSTFIELDS => "client_id=$client_id&client_secret=$client_secret&grant_type=$grant_type&refresh_token=$refresh_token",
  CURLOPT_HTTPHEADER => array(
    "Cache-Control: no-cache",
    "Content-Type: application/x-www-form-urlencoded",
    "Postman-Token: 96123ca9-dc25-49a7-8d98-ad73cd78bd9a"
  ),
));

$response = curl_exec($curl);
$err = curl_error($curl);

curl_close($curl);

if ($err) {
  echo "cURL Error #:" . $err;
} else {
  echo $response;
  $json = json_decode($response, true);
  
  $access_token  =	$json['access_token'];
  $refresh_token = 	$json['refresh_token'];
  $token_type    = 	$json['token_type'];
  $expires_in    =	$json['expires_in'];

	// MySQL Conexion
	$con = mysqli_connect(SM_MYSQL_SERVER, SM_MYSQL_USER, SM_MYSQL_PASS, SM_MYSQL_DB);
	// Chequea coneccion
	if($con == false){
		die("ERROR: No pudo conectarse con la DB. " . mysqli_connect_error());
	}
	
	// Ejecuta la actualizacion del registro    
	$statement = mysqli_prepare($con, "UPDATE dk_token SET access_token = ?, refresh_token = ? WHERE token_type = ?");
	mysqli_stmt_bind_param($statement, "sss", $access_token, $refresh_token ,$token_type);
	mysqli_stmt_execute($statement);
	$statement->close();
	
	if(mysqli_query($con, $statement)){
		echo "access_token actualizado.";
	} else {
		echo "ERROR: No se ejecuto $statement. " . mysqli_error($con);
	}
	// Cierra la conexion
	mysqli_close($con);

}
