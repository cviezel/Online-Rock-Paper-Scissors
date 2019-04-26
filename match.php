<?php
$username1 = $_POST["username1"];
$username2 = $_POST["username2"];

#connecting
$dbname = "jsuhr2_rps";
$dbpassword = "aet5Aixoohai";

$dbuser = "jsuhr2";
$host = "mysql.cs.binghamton.edu";
$cid = mysqli_connect($host, $dbuser, $dbpassword, $dbname);

$sql = "select * from users where username=\"" . addslashes($username2) . "\"";
$result  = mysqli_query($cid, $sql);
$numRows = $result->num_rows;

//user exists
if($numRows){
	//check if match exists
	$sql2 = "select * from matches where user1=\"" . addslashes($username1) . "\" and user2=\"" . addslashes($username2) . "\"";
	$result2  = mysqli_query($cid, $sql2);
	$numRows2 = $result2->num_rows;
	
	if($numRows2){
		echo "1";
	}

	$sql3 = "select * from matches where user1=\"" . addslashes($username2) . "\" and user2=\"" . addslashes($username1) . "\"";
	$result3  = mysqli_query($cid, $sql3);
	$numRows3 = $result3->num_rows;
	
	if($numRows3){
		echo "2";
	}

	if(!($numRows2 || $numRows3)){
		//create new match
		$sql4 = "insert into matches (user1, user2, move1, move2)
					values (\"" . addslashes($username1) . "\", \"" .
					addslashes($username2) . "\", 0, 0)";
		$result4 = mysqli_query($cid, $sql4);
	}
	echo "Success";
} else{
	//user doesn't exist
	echo "Failed";
}
?>
