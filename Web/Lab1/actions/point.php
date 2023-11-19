<?php

$response = $r = $x = $y = $executed_at = $execution_time = $result = "";

$start_exec = microtime(1);

function check_r($r): bool
{
    return $r > 1 and $r < 4;
}

function check_x($x): bool
{
    return $x > -5 and $x < 5;
}

function check_y($y): bool
{
    return $y > -5 and $y < 5;
}

function check_hit($r, $x, $y): string
{
    // 1-st quarter - noting
    if ($x >= 0 and $y >= 0)
        return "Miss";
    // 2-nd quarter - sector of the circle
    else if ($x <= 0 and $y >= 0 and (pow($x, 2) + pow($y, 2) <= pow($r, 2)))
        return "Success";
    // 3-rd quarter - triangle
    else if ($x <= 0 and $y <= 0 and abs($x) + abs($y) <= $r)
        return "Success";
    // 4-th quarter - square
    else if ($x >= 0 and $y <= 0 and abs($x) <= $r && abs($y) <= $r)
        return "Success";
    // everything else is miss
    else return "Miss";
}

if ($_SERVER["REQUEST_METHOD"] == "GET") {
    $r = $_GET["R"];
    $x = $_GET["X"];
    $y = $_GET["Y"];

    if (check_x($x) and check_y($y) and check_r($r)) {
        $result = check_hit($r, $x, $y);
    } else {
        $result = "Wrong data";
        http_response_code(400);
    }

    $executed_at = date(DATE_RFC2822);
    $execution_time = (microtime(1) - $start_exec) * 1000;

    $response .= $r .= ";";
    $response .= $x .= ";";
    $response .= $y .= ";";
    $response .= $result .= ";";
    $response .= $executed_at .= ";";
    $response .= number_format($execution_time, 6) . " ms";

    echo $response;
}