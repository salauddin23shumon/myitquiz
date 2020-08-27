<?php

if ($_SERVER['REQUEST_METHOD']=='POST') {
    
    $id = $_POST['id'];

    require_once 'dbConnection.php';

    $sql = "SELECT user.username, user.email, user.photo, score.Computer, score.MySql, score.PHP, score.C_prog, score.CPP, score.cSharp, score.HTML, score.CSS, score.JS, score.DS, score.java, score.DLD FROM user INNER JOIN score ON (user.id = score.userID) where user.id = '$id'";

    $response = mysqli_query($con, $sql);

    $result = array();
    $result['read'] = array();

    if( mysqli_num_rows($response) === 1 ) {
        
        if ($row = mysqli_fetch_assoc($response)) {
 
             $h['username']    = $row['username'] ;
             $h['email']       = $row['email'] ;
             $h['photo']       = $row['photo'] ;
            
             array_push($result["read"], $h);
 
             $result["success"] = '1';
             echo json_encode($result);
        }
 
   }
   mysqli_close($con);
 
 }else {
 
     $result["success"] = "0";
     $result["message"] = "Error!";
     echo json_encode($result);
 
     
 }
 
 ?>