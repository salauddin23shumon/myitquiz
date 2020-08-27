<?php
session_start();
if (isset($_SESSION["username"])) {
    session_destroy();
}
include_once 'dbConnection.php';
$ref      = @$_GET['q'];
$username = $_POST['username'];
$password = $_POST['password'];

 $sql = "SELECT * FROM user WHERE username = '$username' ";

$response = mysqli_query($con, $sql);

if ( mysqli_num_rows($response) === 1 ) {
	
	$row = mysqli_fetch_assoc($response);

	if ( password_verify($password, $row['password']) ) { ////decrypting hash password
		
		$username = $row['username'];
		$email = $row['email'];
		$_SESSION["username"] = $username;
		header("location:account.php?q=1");

	} else {
		header("location:$ref?w=Wrong Password");        
	}
}
else{
	header("location:$ref?w=Wrong Username or Password");
}

?>