 <?php
require 'config/configStock.php';
/**
 *Date: 25/07/2019
 *Description:
 *EmailValidation.php inserts a specific email in the "user" table of "Usuarios" database.
 *EmailValidation.php inserta un email determinado en la tabla "user" de la base de datos "Usuarios".
 *Created by: Hernán Medina
 *Copyright 2019 ITeDA
 */


if(isset($_POST['submit'])){

	$email= $_POST["email"];
	//generate a random hash
	$randomCode = md5(uniqid(rand()));
	//print the email and random code on the screen
	//echo "Email: $email <br> Random Code: $randomCode <br>";
	// Create connection
	$con = new mysqli(SM_MYSQL_SERVER, SM_MYSQL_USER, SM_MYSQL_PASS, SM_MYSQL_DB);
	// Check connection
	if ($con->connect_error) {
	die("Connection failed: " . $con->connect_error);
	}

	//AS: Qu'est ce que?
	mysql_select_db('user');
 
        // Set the SQL Query
        //$statement = "INSERT INTO user (CONFIRMATIONCODE, email) VALUES ('$randomCode','$email');";      
	$statement = mysqli_prepare($con, "INSERT INTO user (CONFIRMATIONCODE, email) VALUES (?, ?)");
        //ligar parámetros para marcadores
	mysqli_stmt_bind_param($statement, "is", $randomCode, $email);
        //ejecutar ejecutar la consulta
	mysqli_stmt_execute($statement);

         
        // Execute the SQL Query with the mysql_query function 
        // which takes the query as an argument in string format and 
        // also the connection object 
        $result_set = mysql_query($statement, $con); 
 
        // Close the connection by passing the connection object in the
        // mysql_close function
        $statement->close();
        mysql_close($con); 
        
//if the result is returned true
	if($result_set){
		$to = $email;
		$subject = "Your confirmation link here";
		$header = "Stock Manager";
		$message = "tu link de confirmación: \r\n";
		$message .= "Clickeá sobre el link para activar tu cuenta.";
		$message .= "http://localhost/EmailValidation.php?randomCode=$randomCode";
		$sentmail = mail($to, $subject, $message, $header);
	}
	else{
		echo "Check your database connection, it's probably broken";
	}

	if($sentmail){
		echo "Your confirmacion link has been sent to your email address."; 
	}else{
		echo "Cannot send confirmacion link to your email address";
	}
}

?>
