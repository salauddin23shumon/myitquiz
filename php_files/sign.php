<?php
include_once 'dbConnection.php';
ob_start();

$username = $_POST['username'];
$email = $_POST['email'];
$password = $_POST['password'];


   
$password = password_hash($password, PASSWORD_DEFAULT);
$path = "https://myitquizbd.000webhostapp.com/uploads/";
$targetDir = "uploads/";
$fileName = basename($_FILES["img"]["name"]);
$targetFilePath = $targetDir . $fileName;
$finalFleName=$path . $fileName;



if(isset($_POST["submit"]) && !empty($_FILES["img"]["name"])){
	if(move_uploaded_file($_FILES["img"]["tmp_name"], $targetFilePath)){
		$q3 = mysqli_query($con, "INSERT INTO user VALUES  (NULL,'$username' ,'$email', '$password', '$finalFleName')") or die('data insertion failed');
	}
}


if ($q3) {
	$last_id = mysqli_insert_id($con);
	$q4 = mysqli_query($con, "INSERT INTO score (userID, Computer, MySql, PHP, C_prog, cSharp, CPP, DS, CSS, HTML, JS, java, DLD) VALUES ('$last_id', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)");
    if(q4){
		session_start();
		$_SESSION["username"] = $username;
		header("location:account.php?q=1");
	}
} else {
    header("location:index.php?q7=Username already exists. Please choose another&name=$name&username=$username&gender=$gender&phno=$phno&branch=$branch&rollno=$rollno");
}
ob_end_flush();
?>