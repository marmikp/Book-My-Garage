<?php

$user = $_POST['user'];
include "conn.php";
error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);
$query = "select * from request where username = '$user' and flag = 0 and work_status = 0 ORDER BY `id` DESC";
$data = $con->query($query);
$num = $data->num_rows;
if ($num > 0){
	$row=mysqli_fetch_row($data);
	$js->response = 1;
	$js->distance = $row[7];
	$js->asked = $row[8];
	$js->uname = $row[6];
	$q = "select * from login where username = '$row[6]'";
	$data1 = $con->query($q);
	$rowl=mysqli_fetch_row($data1);
	$js->name = $row[1];
	$js->nameg = $rowl[4];
	$js->mobile = $rowl[5];
	$js->img_name = $rowl[6];
	print_r(json_encode($js));
}
else{
	$js->response = 0;
	print_r(json_encode($js));
}

?>