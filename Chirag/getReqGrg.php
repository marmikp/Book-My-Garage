<?php

$distName = $_POST['username1'];

include "conn.php";
error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);
if ($_POST['req'] == "1") {
	$query = "select * from request where work_by = '$distName' and `work_status` = 0 and `flag` = 0 ORDER BY `id` DESC";
	$data = $con->query($query);
	$status_raw = $data->num_rows;
	if($status_raw>0){
		$row=mysqli_fetch_row($data);
		$query1 = "select * from login where `username` = '$row[1]'";
		$data1 = $con->query($query1);
		$row1=mysqli_fetch_row($data1);

		$location2 = explode(",",$row[2]);
		$js->let = $location2[0];
		$js->long = $location2[1];
		
		$js->response = 1;
		$js->distance = $row[7];
		$js->uname_clt = $row1[1];
		$js->name_clt = $row1[4];
		$js->mobile = $row1[5];
		$js->img_name = $row1[6];
		$js->msg = $row[3];
		print_r(json_encode($js));
	}
	else{
		$js->response = 0;
		print_r(json_encode($js));
	}

}else if($_POST['req'] == "0" && $_POST['adm'] == "1"){
	$query = "select * from garage_location where `username` = '$distName'";
	$data = $con->query($query);
	$status_raw = $data->num_rows;
	if($status_raw>0){
		$row=mysqli_fetch_row($data);
		$query1 = "select * from login where `username` = '$distName'";
		$data1 = $con->query($query1);
		$row1=mysqli_fetch_row($data1);

		$location2 = explode(",",$row[2]);
		$js->let = $location2[0];
		$js->long = $location2[1];
		
		$js->response = 1;
		$js->email = $row1[1];
		$js->name_clt = $row1[4];
		$js->mobile = $row1[5];
		$js->img_name = $row1[6];
		print_r(json_encode($js));
	}
	else{
		$js->response = 0;
		print_r(json_encode($js));
	}
}

else if($_POST['req'] == "0" && $_POST['adm'] == "0"){
	
		$row=mysqli_fetch_row($data);
		$query1 = "select * from login where `username` = '$distName'";
		$data1 = $con->query($query1);
		$row1=mysqli_fetch_row($data1);

		
		$js->response = 1;
		$js->email = $row1[1];
		$js->name_clt = $row1[4];
		$js->mobile = $row1[5];
		$js->img_name = $row1[6];
		print_r(json_encode($js));
	
}

?>