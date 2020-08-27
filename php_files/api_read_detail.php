<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {
    
	require_once  'dbConnection.php';

    $id = $_POST['id'];


    $sql = "SELECT user.username, user.email, user.photo, score.Computer, score.MySql, score.PHP, score.C_prog, score.CPP, score.cSharp, score.HTML, score.CSS, score.JS, score.DS, score.java, score.DLD FROM user INNER JOIN score ON (user.id = score.userID) WHERE user.id = '$id'";

    $response = mysqli_query($con, $sql);

    $result = array();
    $result['read'] = array();

    if( mysqli_num_rows($response) == 1 ) {
        
        if ($row = mysqli_fetch_assoc($response)) {
 
             $index['username']    = $row['username'] ;
             $index['email']       = $row['email'] ;
             $index['photo']       = $row['photo'] ;
			 
             $index['Computer']    = $row['Computer'] ;
             $index['MySql']       = $row['MySql'] ;
             $index['PHP']         = $row['PHP'] ;
             $index['CSS']         = $row['CSS'] ;
             $index['C_prog']      = $row['C_prog'] ;
             $index['cSharp']      = $row['cSharp'] ;
             $index['CPP']         = $row['CPP'] ;
             $index['DS']          = $row['DS'] ;
             $index['JS']          = $row['JS'] ;
             $index['HTML']        = $row['HTML'] ;
             $index['java']        = $row['java'] ;
             $index['DLD']         = $row['DLD'] ;
            
             array_push($result["read"], $index);
 
             $result["success"] = '1';
             echo json_encode($result);
        }
 
   }else {
 
     $result["success"] = "0";
     $result["message"] = "Error!";
     echo json_encode($result);
	}
	
	mysqli_close($con);
 }
 
 ?>