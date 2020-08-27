<?php

if($_SERVER['REQUEST_METHOD'] == 'POST'){
	
	require_once  'dbConnection.php';

    $id = $_POST['id'];
	$username = $_POST['username'];
    $email = $_POST['email'];
    $photo = $_POST['photo'];
	
	$com = $_POST['Computer'];
    $mysql = $_POST['MySql'];
    $php = $_POST['PHP'];
    $c = $_POST['C_prog'];
    $cs = $_POST['cSharp'];
    $cpp = $_POST['CPP'];
    $ds = $_POST['DS'];
    $css = $_POST['CSS'];
    $html = $_POST['HTML'];
    $js = $_POST['JS'];
    $java = $_POST['java'];
    $dld = $_POST['DLD'];
	
	///this is for the image id
	$str= "AA000";
	$num = $str.trim($id);
    $path = "uploads/$num.jpeg";
    $finalPath = "https://myitquizbd.000webhostapp.com/".$path;

	
	/// query to check same name or mail already exist or not
	$query = " SELECT username, email FROM user WHERE username = '{$username}' and id != '{$id}' or email = '{$email}' and id != '{$id}' ";
	$result = mysqli_query($con, $query);
	if(mysqli_num_rows($result) > 0){
		$response['success'] = 0;
		$response["message"] = " username or email already exist ";
		echo json_encode($response);
	}
	else{	
		$sql = " UPDATE user INNER JOIN score ON (user.id = score.userID) SET user.username = '$username', user.email = '$email', user.photo = '$finalPath', score.Computer='$com', score.MySql='$mysql', score.PHP='$php', score.C_prog='$c', score.CPP='$cpp', score.cSharp='$cs', score.HTML='$html', score.CSS='$css', score.JS='$js', score.DS='$ds', score.java='$java', score.DLD='$dld' WHERE user.id = '$id' ";
	
		if(mysqli_query($con, $sql)) {
			
			if ( file_put_contents( $path, base64_decode($photo) ) ) {
				
			  $response["success"] = "1";
			  $response["message"] = "Profile updated successfully";
			  echo json_encode($response);
			}
		}
		else{
		    $response["success"] = "0";
		    $response["message"] = "error! update failed";
		    echo json_encode($response);
		}
	}	
	mysqli_close($con);
}
?>


