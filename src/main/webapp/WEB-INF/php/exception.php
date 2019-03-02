<?php
/**
 * Created by IntelliJ IDEA.
 * User: QianRui
 * Date: 2019/2/28
 * Time: 11:07
 * @param boolean $b 为false 抛出异常
 * @param string $msg 异常信息
 */
function javaAssert(bool $b, string $msg)
{
    if (!$b) {
        throw new RuntimeException($msg);
    }
}