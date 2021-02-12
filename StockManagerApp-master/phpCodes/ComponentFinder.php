 <?php
require 'config/configStock.php';
 /*

Date: 25/07/2019

Description: 
FindString.php gets certain data of the "stock" table of "Usuarios" database.

FindString.php obtiene ciertos datos de la tabla "stock" de la base de datos "Usuarios".

Created by: Hernán Medina

copyright 2019 ITeDA

*/

// Create connection
$con = new mysqli(SM_MYSQL_SERVER, SM_MYSQL_USER, SM_MYSQL_PASS, SM_MYSQL_DB);
// Check connection
if ($con->connect_error) {
    die("Connection failed: " . $con->connect_error);
}

$mNumber = $_POST["etMFNumber"];

//$query = "SELECT quantity, location FROM stock WHERE m_number = ?";
//$statement = $con->prepare($query);
$statement = mysqli_prepare($con, "SELECT stock_id, description, quantity, location FROM stock WHERE m_number = ?");
$statement->bind_param( "s", $mNumber );
$statement->execute();

$statement->bind_result($stock_id, $description, $quantity, $location);
$statement->fetch();

$response = array();

if (is_null($quantity)) //no esta en la base de datos
	$response["success"] = "false";
else {
 	$response["quantity"] = $quantity;
 	$response["description"] = $description;
 	$response["location"] = $location;
	$response["stock_id"] = $stock_id;
	$response["success"] = "true";
}

$statement->close();
$con->close();

echo json_encode($response);
?>
