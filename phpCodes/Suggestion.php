<?php
require 'config/configStock.php';
/*
Date: 25/07/2019

Description: 
Suggestion.php allows to load suggestions to the "suggestions" table of "Usuarios" database.

Suggestion.php permite cargar sugerencias a la tabla "suggestions" de la base de datos "Usuarios".

Created by: Hernán Medina

copyright 2019 ITeDA

*/

    //paramatros de mysqli_connect (host, nombre de usuario de la base de datos, contraseña de la base de datos, nombre de la base de datos)
    $con = mysqli_connect(SM_MYSQL_SERVER, SM_MYSQL_USER, SM_MYSQL_PASS, SM_MYSQL_DB);
    
    $suggestion = $_POST["suggestion"];
    $user = $_POST["user"];
    //$timestamp = $_POST["timestamp"];

    $statement = mysqli_prepare($con, "INSERT INTO suggestions (suggestion,user) VALUES (?,?)");
        //ligar parámetros para marcadores
    mysqli_stmt_bind_param($statement, "ss", $suggestion, $user);
        //ejecutar ejecutar la consulta
    mysqli_stmt_execute($statement);
    
    $response = array();
    $response["success"] = true;  
    
    echo json_encode($response);
?>
