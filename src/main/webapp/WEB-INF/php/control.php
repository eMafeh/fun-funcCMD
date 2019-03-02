<?php
require_once 'mysql.php';
require_once 'mysqlUtil.php';
require_once 'util.php';
require_once 'allType.php';
require_once 'service.php';
require_once 'exception.php';
/**
 * Created by IntelliJ IDEA.
 * User: QianRui
 * Date: 2019/2/26
 * Time: 14:40
 */

//header('Content-Type:application:json;charset=GBK');
header('Access-Control-Allow-Origin:*');
header('Access-Control-Allow-Methods:POST');
header('Access-Control-Allow-Headers:x-requested-with,content-type');
//$___keys = array_keys($_REQUEST);
//foreach ($___keys as $key) $_REQUEST[$key] = iconv('utf-8', 'gbk', $_REQUEST[$key]);

$control = 'control_' . $_REQUEST['method'];
try {
    $control();
} catch (Throwable $e) {
    echo $e;
}

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
        $array = queryList($tableName, GifType::class);
        echo javaJson($array);
    }
}

function control_insertGif()
{
    $url = $_REQUEST['url'];
    echo $url;
    echo insertGif($url);
}

function control_badGif()
{
    $url = $_REQUEST['url'];
    echo $url;
    echo markSmall($url);
}

function control_login()
{
    $userId = $_COOKIE['userId'];
    $ip = getIp();
    $userType = getUser($userId, $ip);
    $userId = $userType->id;
    setcookie('userId', $userId);
    echo 'const User=';
    echo javaJson($userType);
    insertOp($userId, $ip, 'visit');
}

function control_addGrade()
{
    $userId = $_COOKIE['userId'];
    $num = $_REQUEST['num'];
    addGrade($userId,$num);

}