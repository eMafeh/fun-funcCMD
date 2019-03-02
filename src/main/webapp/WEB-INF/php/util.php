<?php
/**
 * Created by IntelliJ IDEA.
 * User: QianRui
 * Date: 2019/2/26
 * Time: 21:05
 */

function getIp()
{
    static $ip;
    if (!$ip) {
        //strcasecmp 比较两个字符，不区分大小写。返回0，>0，<0。
        if (getenv('HTTP_CLIENT_IP') && strcasecmp(getenv('HTTP_CLIENT_IP'), 'unknown')) {
            $ip = getenv('HTTP_CLIENT_IP');
        } elseif (getenv('HTTP_X_FORWARDED_FOR') && strcasecmp(getenv('HTTP_X_FORWARDED_FOR'), 'unknown')) {
            $ip = getenv('HTTP_X_FORWARDED_FOR');
        } elseif (getenv('REMOTE_ADDR') && strcasecmp(getenv('REMOTE_ADDR'), 'unknown')) {
            $ip = getenv('REMOTE_ADDR');
        } elseif (isset($_SERVER['REMOTE_ADDR']) && $_SERVER['REMOTE_ADDR'] && strcasecmp($_SERVER['REMOTE_ADDR'], 'unknown')) {
            $ip = $_SERVER['REMOTE_ADDR'];
        }
        $ip = preg_match('/[\d\.]{7,15}/', $ip, $matches) ? $matches [0] : '';
    }
    return $ip;
}

function javaJson($target)
{
    $json = json_encode($target, JSON_UNESCAPED_UNICODE);
    javaAssert(JSON_ERROR_NONE === json_last_error(), json_last_error_msg());
    return $json;
}