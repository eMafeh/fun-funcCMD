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

try {
    $method = $_REQUEST['method'];
    if (method_exists(Control::class, $method)) {
        $fire_args = array();
        $reflection = new ReflectionMethod(Control::class, $method);
        foreach ($reflection->getParameters() AS $arg) {
            $name = $arg->name;
            if ($_REQUEST[$name])
                $fire_args[$name] = $_REQUEST[$name];
            else
                $fire_args[$name] = null;
        }
        $fire_args['ip'] = getIp();
        $reflection->invokeArgs(new Control(), $fire_args);
    } else
        echo 'method not exist:' . $method;
} catch (Throwable $e) {
    echo $e;
}

class Control
{
    function imageList($userId)
    {
        if (is_null($userId)) $userId = 'sys';
        header('Cache-Control:max-age=5,must-revalidate');
        $maxModified = strtotime(imageLastTime($userId));
        $lastModified = strtotime($_SERVER['HTTP_IF_MODIFIED_SINCE']);
        if ($lastModified >= $maxModified) {
            header('HTTP/1.1 304 Not Modified');
        } else {
            header('Last-Modified: ' . gmdate('D, d M Y H:i:s', $maxModified) . ' GMT');
            $array = imageList($userId);
            echo 'const ImageList=';
            echo javaJson($array);
        }
    }

    function gd()
    {
        echo javaJson(gd_info());
    }

    function uploadFile($imgBase64, $userId)
    {
        if (preg_match('/^(data:\s*image\/(\w+);base64,)/', $imgBase64, $result)) {
            $path = DIRECTORY_SEPARATOR . 'img' . DIRECTORY_SEPARATOR . md5($imgBase64) . strlen($imgBase64) . '.' . $result[2];
            if (!file_exists('..' . $path)) {
                $data = base64_decode(str_replace($result[1], '', $imgBase64));
                file_put_contents('..' . $path, $data);
            }
            echo javaJson(insertBackImg('http://qianrui.fun' . $path, $userId));
        } else echo '{}';
    }

    function visit($userId, $ip)
    {
        $userType = getUser($userId, $ip);
        $_REQUEST['userId'] = $userId = $userType->id;
        setcookie('userId', $userId, time() + 60 * 60 * 24 * 365, "/");
        echo 'const RemoteUser=';
        echo javaJson($userType);
        insertOp($userId, $ip, 'visit', '');
    }

    function addGrade($userId, $num, $ip)
    {
        echo ' user:' . $userId . ' num:' . $num . ' success:' . addGrade($userId, $num);
        insertOp($userId, $ip, 'addGrade', $num);
    }

    function updateUser($userId, $ip, $field, $value)
    {
        javaAssert(property_exists(UserType::class, $field), '$field is not exist');
        echo ' user:' . $userId . ' ' . $field . ':' . $value;
        echo ' success:' . updateUtil('user', $userId, $field, $value);
        insertOp($userId, $ip, $field, $value);
    }
}

