<?php

session_start();
try{
	$username1 = $_POST['username'];
	$password1 = $_POST['password'];
	include "conn.php";

	$c = 0;
	error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);
	$query = "select * from login where username = '".$username1."' and password = '".$password1."'";
	$data = $con->query($query);
	$num_rows1 = $data->num_rows;
	if ($num_rows1 > 0) {
		$c = 1;
	}


	
	if ($c == 1) {
		error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);
		$jsonObj->username = $username1;
		$jsonObj->response = "1";
		$statusData=mysqli_fetch_row($data);
		$jsonObj->status = $statusData[3];
		$myJSON = json_encode($jsonObj);
		$_SESSION['user'] = $username1;
		print_r($myJSON);


	}

	else{
		print_r("0,Invalid");
	}
}
catch(Exception $e){
	$jsonObj->response = "0";

}

?>