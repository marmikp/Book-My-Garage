<?php


include "conn.php";
$query = "select * from garage_location";
$data = $con->query($query);

$num_rows1 = $data->num_rows;
if ($num_rows1 > 0) {
	$c = 1;
}
$i = 0;
$location = "";
error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);
if ($c == 1) {
	$location->status = 1;
	while($row=mysqli_fetch_row($data)){
		$location->$i = $row[2];
		$i = $i + 1;
	}
	
}
else{
	$location->status = 0;
}
print_r(json_encode($location));


?>