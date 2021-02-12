 <?php
require 'config/configStock.php';
/*
Date: 25/07/2019

Description: 
FindString3.php searches the user's name with a specific email in the "user" table of "Usuarios" database.

FindString3.php busca el nombre de usuario con un determinado email en la tabla "user" de la base de datos "Usuarios".

Created by: Hernán Medina

copyright 2019 ITeDA

*/

// Create connection
$con = new mysqli(SM_MYSQL_SERVER, SM_MYSQL_USER, SM_MYSQL_PASS, SM_MYSQL_DB);
// Check connection
if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
}

$email = $_POST["email"];

$statement = mysqli_prepare($con, "SELECT name FROM user WHERE email = ?");
$statement->bind_param( "s", $email );

$statement->execute();

$statement->bind_result($name);
$statement->fetch();

$response = array();
if (is_null($name)) //no esta en la base de datos
	$response["success"] = "false";
else {
	$response["name"] = $name;
	$response["success"] = "true";
}

$statement->close();
$con->close();

echo json_encode($response);
?> 
