<?php
/**
 *Date: 25/07/2019
 *Description:
 *index.php sends emails from chmedina1994 gmail account.
 *index.php envia emails desde cuenta de gmail chmedina1994 (debe modificarse a la cuenta de ingenieria iteda.cnea.gov.ar).
 *Created by: Hernán Medina
 *Copyright 2019 ITeDA
 */

// Import PHPMailer classes into the global namespace
// These must be at the top of your script, not inside a function
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;

// Load Composer's autoloader
require 'vendor/autoload.php';
require 'config/configEmail.php';

//Crear una instancia de PHPMailer
$mail = new PHPMailer(true);

try {

//Esto es para activar el modo depuración. En entorno de pruebas lo mejor es 2, en producción siempre 0
// 0 = off (producción)
// 1 = client messages
// 2 = client and server messages
    $mail->SMTPDebug = 2;                                       // Enable verbose debug output
    
    //Definir que vamos a usar SMTP
    $mail->isSMTP();   
    
    //Ahora definimos gmail como servidor que aloja nuestro SMTP
    $mail->Host   = $host;			// Specify main and backup SMTP servers
    
//Tenemos que usar gmail autenticados, así que esto a TRUE
    $mail->SMTPAuth   = true;                                   // Enable SMTP authentication
    
//Definimos la cuenta que vamos a usar. Dirección completa de la misma
    $mail->Username   = $username;               // SMTP username
    //Introducimos nuestra contraseña de gmail
    //$mail->Password   = "vasoverde1";                           // SMTP password
	$mail->Password   = $password;                           
//Definimos el remitente (dirección y, opcionalmente, nombre)
    $mail->SetFrom($from_email, $app_name);
    
//Definmos la seguridad como TLS
    $mail->SMTPSecure = $smtp_secure;                        
    
//El puerto será el 587 ya que usamos encriptación TLS
    $mail->Port       = $port;                                    // TCP port to connect to
    
    $email = $_POST["email"];
    //$user = $_POST["---------"];
    $mail->AddAddress($email);
    $subject = $_POST["subject"];
    $body=$_POST["body"];
       
    //$mail->AddAddress('chmedina1994@gmail.com', 'Herny');// Add a recipient
    //$subject = 'Here is the subject';
    //$body = 'This is the HTML message body <b>in bold!</b>';

    //Esta línea es por si queréis enviar copia a alguien (dirección y, opcionalmente, nombre)
//$mail->AddReplyTo('replyto@correoquesea.com','El de la réplica');

    // Content
    $mail->isHTML(true);                                  // Set email format to HTML
    
//Definimos el tema del email
    //$mail->Subject = $subject;
    $mail->Body    = $body;
    $mail->Subject= $subject;
    $mail->AltBody = 'This is the body in plain text for non-HTML mail clients';

//Para enviar un correo formateado en HTML lo cargamos con la siguiente función. Si no, puedes meterle directamente una cadena de texto.
//$mail->MsgHTML(file_get_contents('correomaquetado.html'), dirname(ruta_al_archivo));

//Y por si nos bloquean el contenido HTML (algunos correos lo hacen por seguridad) una versión alternativa en texto plano (también será válida para lectores de pantalla)
//$mail->AltBody = 'This is a plain-text message body';

    $mail->send();
    $mail->ClearAddresses();
    
    echo 'Message has been sent';
} catch (Exception $e) {
    echo "Message could not be sent. Mailer Error: {$mail->ErrorInfo}";
} 
?>