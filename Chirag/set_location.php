<?php

$username1 = $_POST['username1'];

include "conn.php";
$c=0;
$query = "select * from garage_temp_location where username = '".$username1."'";
$data = $con->query($query);

$num_rows1 = $data->num_rows;
if ($num_rows1 > 0) {
	$c = 1;
}
$location = "";
error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);
if ($c == 1) {
	$row=mysqli_fetch_row($data);
	$location2 = explode(",",$row[2]);
	$location->status = 1;
	$location->let = $location2[0];
	$location->long = $location2[1];
}
else{
	$location->status = 0;
}
print_r(json_encode($location));


?>