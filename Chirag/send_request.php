<?php


$username1 = $_POST['username'];
$lat_long = $_POST['location'];
$message = $_POST['message'];

$distG = 0;
$distName = "";


function getDistance($lat1, $lon1, $lat2, $lon2, $dname,$unit="K") {

  $theta = $lon1 - $lon2;
  $dist = sin(deg2rad($lat1)) * sin(deg2rad($lat2)) +  cos(deg2rad($lat1)) * cos(deg2rad($lat2)) * cos(deg2rad($theta));
  $dist = acos($dist);
  $dist = rad2deg($dist);
  $miles = $dist * 60 * 1.1515;
  $unit = strtoupper($unit);

  if ($unit == "K") {
	  	$val = $miles * 1.609344;
	  	if ($GLOBALS['distG'] == 0) {
			$GLOBALS['distG'] = $val;
			$GLOBALS['distName'] = $dname; 
	  	}
	  	if($GLOBALS['distG'] > $val){
	  		$GLOBALS['distG'] = $val;
	  		$GLOBALS['distName'] = $dname;
	  	}
	    

		  
		}
}

include "conn.php";


$location1 = explode(",",$lat_long);
$lati = $location1[0];
$long = $location1[1];

$query = "select * from garage_location where status=0 and location IS NOT NULL";
$data = $con->query($query);
$status_raw = $data->num_rows;
		
if ($status_raw > 0) {
	while ($row=mysqli_fetch_row($data))
	{
		// $arrayjson = $arrayjson."".$row[0].",".$row[1].",".$row[2].",".$row[3].",".$row[4]."";
		error_reporting(E_ALL & ~E_WARNING & ~E_NOTICE);
		$location2 = explode(",",$row[2]);
		$latid = $location2[0];
		$longd = $location2[1];
		getDistance($lati,$long,$latid,$longd,$row[1]);

	}


	$query = "insert into request(`username`,`location`,`message`,`work_status`,`flag`,`work_by`,`distance`) values ('$username1','$lat_long','$message',0,0,'$distName',$distG)";
	$data = $con->query($query);

	$query1 = "update garage_location set status=1 where username = '$distName'";
	$con->query($query1);

	if ($distName != "") {
		$query = "select * from login where username = '$distName'";
		$data = $con->query($query);
		$row=mysqli_fetch_row($data);
		$js->response = 1;
		$js->distance = $distG;
		$js->uname = $distName;
		$js->name = $row[4];
		$js->mobile = $row[5];
		$js->img_name = $row[6];
		print_r(json_encode($js));
	}else{
		$js->response = 0;
		print_r(json_encode($js));
	}


}

else{
		$js->response = 0;
		print_r(json_encode($js));

}







?>