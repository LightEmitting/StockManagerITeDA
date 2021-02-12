<?php
require 'config/configStock.php';
/*

Date: 25/07/2019

Description:
Register.php registers each new user in the "user" table of "Usuarios" database.

Register.php registra cada nuevo usuario en la tabla "user" de la base de datos "Usuarios".

Created by: Hernán Medina

copyright 2019 ITeDA

*/
    //archivo que permite hacer la conexion a la base de datos

    //paramatros de mysqli_connect (host, nombre de usuario de la base de datos, contraseña de la base de datos, nombre de la base de datos)
    $con = mysqli_connect(SM_MYSQL_SERVER, SM_MYSQL_USER, SM_MYSQL_PASS, SM_MYSQL_DB);

    $name = $_POST["name"];
    $familyname = $_POST["familyname"];
    $user = $_POST["user"];
    $password = $_POST["password"];
    $email = $_POST["email"];

    $statement = mysqli_prepare($con, "INSERT INTO user (name, familyname, user, password, email) VALUES (?, ?, ?, ?, ?)");
        //ligar parámetros para marcadores
    mysqli_stmt_bind_param($statement, "sssss", $name, $familyname, $user, $password, $email);
        //ejecutar ejecutar la consulta
    mysqli_stmt_execute($statement);

    $response = array();
    $response["success"] = true;

    echo json_encode($response);
?>
