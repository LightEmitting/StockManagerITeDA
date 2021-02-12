 <?php
require 'config/configStock.php';
/*

Date: 25/07/2019

Description: 
FindString4.php gets all the data related to DigiKey token loads of the "dk_token" table of "Usuarios" database.

FindString4.php obtiene toda la información respecto al DigiKey token de la tabla "dk_token" de la base de datos "Usuarios".

Created by: Hernán Medina

copyright 2019 ITeDA

*/

// Create connection
$con = new mysqli(SM_MYSQL_SERVER, SM_MYSQL_USER, SM_MYSQL_PASS, SM_MYSQL_DB);
// Check connection
if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
}

$token_type = $_REQUEST["token_type"];

//$query = "SELECT * FROM dk_token WHERE token_type = ? ";
//$statement = $con->prepare($query);
$statement = mysqli_prepare($con, "SELECT * FROM dk_token WHERE token_type = ? ");

$statement->bind_param( "s", $token_type);

$statement->execute();

$statement->bind_result($access_token, $refresh_token, $url, $client_id, $client_secret, $token_type, $grant_type, $expires_in, $timestamp);
$statement->fetch();

$response = array();
if (is_null($client_secret))
{
	$response["success"] = "false";
	$response["client_secret"] = $client_secret;
} else {
	$response["access_token"] = $access_token;
	$response["refresh_token"] = $refresh_token;
	$response["url"] = $url;	
	$response["client_id"] = $client_id;
	$response["client_secret"] = $client_secret;
	$response["token_type"] = $token_type;
	$response["grant_type"] = $grant_type;
	$response["expires_in"] = $expires_in;
	$response["timestamp"] = $timestamp;
	$response["success"] = "true";
	}

$statement->close();
$con->close();

echo json_encode($response);
?> 
