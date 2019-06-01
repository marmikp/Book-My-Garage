<?php

$username1 = $_POST['username1'];
$garage_uname = $_POST['g_u'];
$asked = $_POST['asked'];
error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);
include 'conn.php';
$query = "update `request` set `flag` = $asked, `work_status` = $asked, `ask_complete` = $asked where `work_by` = '$username1' and `username`='$garage_uname'";
$con->query($query);
$query1 = "";
$response = "";
if ($asked == 1) {
	$query1 = "update `garage_location` set `status` = 0 where username = '$username1'";
	$response = "Thank You!";
}
else{
	$query1 = "update `garage_location` set `status` = 1 where username = '$username1'";	
	$response = "We will contact you shortly!";
}
$con->query($query1);

$js->status = 1;
$js->response = $response;
print_r(json_encode($js));

?>