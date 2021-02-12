<?php
require 'config/configStock.php';
/*

Date: 25/07/2019

Description: 

By: Hernán Medina

copyright 2019 ITeDA

*/
    /* MySQL Conexion*/
    $con = mysqli_connect(SM_MYSQL_SERVER, SM_MYSQL_USER, SM_MYSQL_PASS, SM_MYSQL_DB);
    // Chequea coneccion
    if($con == false){
        die("ERROR: No pudo conectarse con la DB. " . mysqli_connect_error());
    }
    
    $newPass = $_POST["newPass"];
    $email = $_POST["email"];

    //$statement = "UPDATE user SET password='$newPass' WHERE email='$email'";
    // Ejecuta la actualizacion del registro    
    $statement = mysqli_prepare($con, "UPDATE user SET password = ? WHERE email = ?");
    mysqli_stmt_bind_param($statement, "ss", $newPass, $email);
    mysqli_stmt_execute($statement);
    $statement->close();
    
    if(mysqli_query($con, $statement)){
        echo "Registro actualizado.";
    } else {
        echo "ERROR: No se ejecuto $statement. " . mysqli_error($con);
    }
    // Cierra la conexion
    mysqli_close($con);
?>
