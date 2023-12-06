<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:useBean id="points" class="model.UserAreaData" scope="session" />
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.time.format.DateTimeFormatter" %>

<!DOCTYPE html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="./css/style.css">
    <link rel="stylesheet" href="./css/plane.css">
    <title>Web Lab 2</title>
</head>
<body>
<header id="title">
    <h3>Frolov Kirill Dmitrievich, P3206, var1741</h3>
</header>
<main>
    <table class="main-table">
        <thead>
        <tr>
            <th class="table-cell"></th>
            <th class="table-cell"></th>
            <th class="table-cell"></th>
            <th class="table-cell"></th>
            <th class="table-cell"></th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td class="table-cell"></td>
            <td class="table-cell" colspan="2" rowspan="2">
                <table class="stat-table">
                    <thead>
                    <tr>
                        <th class="stat-table table-cell" scope="col">R</th>
                        <th class="stat-table table-cell" scope="col">X</th>
                        <th class="stat-table table-cell" scope="col">Y</th>
                        <th class="stat-table table-cell" scope="col">Result</th>
                        <th class="stat-table table-cell" scope="col">Time executed</th>
                        <th class="stat-table table-cell" scope="col">Execution time</th>
                    </tr>
                    </thead>
                    <tbody id="stat-table-body">
                        <c:forEach var="areaData" items="${points.areaDataList}">
                            <tr>
                                <td class="stat-table table-cell">${areaData.r}</td>
                                <td class="stat-table table-cell">${areaData.x}</td>
                                <td class="stat-table table-cell">${areaData.y}</td>
                                <td class="stat-table table-cell">${areaData.result ? "Hit / Success" : "Miss / Fail"}</td>
                                <td class="stat-table table-cell">${areaData.calculatedAt.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))}</td>
                                <td class="stat-table table-cell">${areaData.calculationTime}ns</td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </td>
            <td class="table-cell">
                <div class="plane" id="plane">
                    <div id="q_1"></div>
                    <div id="q_2"></div>
                    <div id="q_3"></div>
                    <div id="q_4"></div>
                    <div class="x-axis"></div>
                    <div class="y-axis"></div>
                    <div class="arrow x-arrow"></div>
                    <div class="arrow y-arrow"></div>
                    <script src="./js/plane-ticks.js"></script>
                    <script type="module" src="./js/plane-handler.js"></script>
                    <script type="module">
                        import {drawPoint} from "./js/plane-handler.js";
                        <c:forEach var="areaData" items="${points.areaDataList}">
                            drawPoint(${areaData.r}, ${areaData.x}, ${areaData.y}, ${areaData.result});
                        </c:forEach>
                    </script>
                </div>
                <form action="${pageContext.request.contextPath}/controller" method="POST" id="plane-form">
                    <input type="hidden" name="R" value="1" id="hiddenR">
                    <input type="hidden" name="X" value="1" id="hiddenX">
                    <input type="hidden" name="Y" value="1" id="hiddenY">
                </form>
                <br/>

                <form action="${pageContext.request.contextPath}/controller" method="POST" id="form">
                    <table>
                        <thead>
                        <tr>
                            <th scope="col"></th>
                            <th scope="col"></th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>
                                <label>R</label>
                                <br/>
                                <input type="checkbox" id="r1" name="R" value="1"/>
                                <label for="r1">1</label>
                                <input type="checkbox" id="r2" name="R" value="2"/>
                                <label for="r2">2</label>
                                <input type="checkbox" id="r3" name="R" value="3"/>
                                <label for="r3">3</label>
                                <input type="checkbox" id="r4" name="R" value="4"/>
                                <label for="r4">4</label>
                                <input type="checkbox" id="r5" name="R" value="5"/>
                                <label for="r5">5</label>
                                <br/>
                            </td>
                            <td>
                                <p id="error-p"></p>
                            </td>
                        </tr>
                        <tr>
                            <td id="x-field">
                                <label>X: </label>
                                <br/>
                                <input type="radio" id="x1" name="X" value="-2"/>
                                <label for="x1">-2</label>
                                <br/>
                                <input type="radio" id="x2" name="X" value="-1.5"/>
                                <label for="x2">-1.5</label>
                                <br/>
                                <input type="radio" id="x3" name="X" value="-1"/>
                                <label for="x3">-1</label>
                                <br/>
                                <input type="radio" id="x4" name="X" value="-0.5"/>
                                <label for="x4">-0.5</label>
                                <br/>
                                <input type="radio" id="x5" name="X" value="0" checked="checked" required/>
                                <label for="x5">0</label>
                                <br/>
                                <input type="radio" id="x6" name="X" value="0.5"/>
                                <label for="x6">0.5</label>
                                <br/>
                                <input type="radio" id="x7" name="X" value="1"/>
                                <label for="x7">1</label>
                                <br/>
                                <input type="radio" id="x8" name="X" value="1.5"/>
                                <label for="x8">1.5</label>
                                <br/>
                                <input type="radio" id="x9" name="X" value="2"/>
                                <label for="x9">2</label>
                                <br/>
                            </td>
                            <td>
                                <label for="y-field">Y</label>
                                <br/>
                                <input type="text" placeholder="(-3; 3)" name="Y" id="y-field" required/>
                                <br/>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <input type="submit" value="Добавить" name="add" id="submit_button">
                </form>
                <script type="module" src="./js/validation.js"></script>
            </td>
            <td class="table-cell"></td>
        </tr>
        </tbody>
    </table>
</main>
<footer>
</footer>
<script type="module" src="./js/script.js"></script>
</body>