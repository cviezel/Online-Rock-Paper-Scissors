<?php
$user1 = $_POST["user1"];
$move = $_POST["move"];
$user2 = $_POST["user2"];
$number = $_POST["number"]; //keeps track of which user sent the move

#connecting
$dbname = "jsuhr2_rps";
$dbpassword = "aet5Aixoohai";

$dbuser = "jsuhr2";
$host = "mysql.cs.binghamton.edu";
$cid = mysqli_connect($host, $dbuser, $dbpassword, $dbname);

if($number == 1){
	$sql1 = "update matches set move1=$move where user1=\"" . addslashes($user1) . "\" and user2=\"" . addslashes($user2) . "\";";
	$result1  = mysqli_query($cid, $sql1);
//	echo "User1 Moved";
} else{
	$sql2 = "update matches set move2=$move where user1=\"" . addslashes($user1) . "\" and user2=\"" . addslashes($user2) . "\";";
	$result2  = mysqli_query($cid, $sql2);
//	echo "User2 Moved";
}

$sql = "select * from matches where user1=\"" . addslashes($user1) . "\" and user2=\"" . addslashes($user2) . "\";";
$result  = mysqli_query($cid, $sql);

$row = mysqli_fetch_array($result, MYSQLI_ASSOC);
$move1 = $row["move1"];
$move2 = $row["move2"];

if($move1 != 0 && $move2 != 0){
	echo "$move1 $move2";
} else{
	echo "Failed";
}

?>
