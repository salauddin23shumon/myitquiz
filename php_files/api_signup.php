<?php

if ($_SERVER['REQUEST_METHOD'] == 'POST'){

	require_once  'dbConnection.php';
	
	$username = $_POST['username'];
    $email = $_POST['email'];
    $password = $_POST['password'];
    $photo = $_POST['photo'];
	
	///this is for the image id
	$str= "AA000";
	$query1 = "SELECT id FROM user";
	$result1 = mysqli_query($con,$query1);	
	$id = mysqli_num_rows($result1);
	$id++;
	$num = $str.$id;

	
    $path = "uploads/$num.jpeg";
    $finalPath = "https://myitquizbd.000webhostapp.com/".$path;

    $password = password_hash($password, PASSWORD_DEFAULT); ////password hashing
	
	$query = "SELECT username,email from user where username = '{$username}' or email = '{$email}'";
	$result = mysqli_query($con, $query);
	if(mysqli_num_rows($result) > 0){
		$response['success'] = 0;
		$response['message'] = " username or email already exist ";
		echo json_encode($response);
	}else{									    
		$query3 = "INSERT INTO user (username, email, password, photo) VALUES ('$username', '$email', '$password', '$finalPath')";
		if ( mysqli_query($con, $query3) ) {
			$last_id = mysqli_insert_id($con);
			$query4 = "INSERT INTO score (userID, Computer, MySql, PHP, C_prog, cSharp, CPP, DS, CSS, HTML, JS, java, DLD) VALUES ('$last_id', 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)";
			if(mysqli_query($con, $query4)){
				if ( file_put_contents( $path, base64_decode($photo) ) ) {
				$response['success'] = 1; //// returning the id of recently added row
				$response['message'] = " $last_id ";
				echo json_encode($response);	
				}
			}
		} 
		else {
			$response['success'] = 0;
			$response['message'] = " Data Insertion Failed";
			echo json_encode($response);			
		}			
	}
	mysqli_close($con);	
	
}

?>