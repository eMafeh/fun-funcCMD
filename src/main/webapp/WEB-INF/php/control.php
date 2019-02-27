<?php
require 'mysql.php';
require 'ip.php';
/**
 * Created by IntelliJ IDEA.
 * User: QianRui
 * Date: 2019/2/26
 * Time: 14:40
 */

header('Content-Type:application:json;charset=UTF-8');
header('Access-Control-Allow-Origin:*');
header('Access-Control-Allow-Methods:POST');
header('Access-Control-Allow-Headers:x-requested-with,content-type');


$control = 'control_' . $_REQUEST['method'];
$control();

function control_gifList()
{
    header('Cache-Control:max-age=5,must-revalidate');
    $tableName = 'gif';
    $maxModified = strtotime(lastTime($tableName));
    $lastModified = strtotime($_SERVER['HTTP_IF_MODIFIED_SINCE']);
    if ($lastModified >= $maxModified) {
        header('HTTP/1.1 304 Not Modified');
    } else {
        header('Last-Modified: ' . gmdate('D, d M Y H:i:s', $maxModified) . ' GMT');
        $json = queryListNotValid($tableName);
        echo json_encode($json);
    }
}

function control_badGif()
{
    $url = $_REQUEST['url'];
    echo $url;
    $count = deleteGif($url);
    echo '<br/>影响' . $count;
}

function control_login()
{
    login(getIp());
}