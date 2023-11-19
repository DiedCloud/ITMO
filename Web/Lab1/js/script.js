function send_query(r, x, y) {
    const req = new XMLHttpRequest();
    
    const urlParams = new URLSearchParams({"R": r, "X": x, "Y": y});
    req.open("GET", "./actions/point.php?" + urlParams.toString(), true);

    req.onreadystatechange = () => {
        if (req.readyState === 4) {
            if (req.status === 200 || req.status === 400) {
                let res = req.responseText.split(';');
                addToTable(res[0], res[1], res[2], res[3], res[4], res[5]);
            } else {
                console.error("Error loading page / " + req.statusText + " " + req.readyState + "\n");
            }
        }
    };

    req.send(null);
}

function addToTable(r, x, y, result, exAt, exTime) {
    let row = document.getElementById("stat-table-body").insertRow();
    var cell;

    cell = row.insertCell(0);
    cell.innerHTML = r;
    cell.className = "stat-table table-cell";
    
    cell = row.insertCell(1);
    cell.innerHTML = x;
    cell.className = "stat-table table-cell";

    cell = row.insertCell(2);
    cell.innerHTML = y;
    cell.className = "stat-table table-cell";

    cell = row.insertCell(3);
    cell.innerHTML = result;
    cell.className = "stat-table table-cell";

    cell = row.insertCell(4);
    cell.innerHTML = exAt;
    cell.className = "stat-table table-cell";

    cell = row.insertCell(5);
    cell.innerHTML = exTime;
    cell.className = "stat-table table-cell";
}