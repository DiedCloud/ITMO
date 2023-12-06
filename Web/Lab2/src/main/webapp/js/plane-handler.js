import {isRValid} from "./validation.js";

const form = document.getElementById("form");
const planeForm = document.getElementById("plane-form");

const plane = document.getElementById('plane');
plane.addEventListener('click', handleGraphClick);

function handleGraphClick (evt) {
    if (!isRValid()) {
        let placeholder = document.getElementById("error-p");
        placeholder.innerHTML = "Choose R for adding point with your mouse";
        return;
    }
    const formData = new FormData(form);
    const r = +formData.get("R");

    const rect = plane.getBoundingClientRect();
    const x = (evt.clientX - rect.left - 200) / 160 * r;
    const y = (- (evt.clientY - rect.top) + 200) / 160 * r;
    console.log(r);
    console.log(x);
    console.log(y);

    document.getElementById("hiddenR").value = r;
    document.getElementById("hiddenX").value = x;
    document.getElementById("hiddenY").value = y;
    planeForm.submit();
}

export function drawPoint(r, x, y, res){
    let x_cord = x / r * 160;
    let y_cord = y / r * 160;
    console.log(r, x, y, x_cord, y_cord, res);

    let point = document.createElement('div');
    point.className = "point " + (res ? "point-hit" : "point-miss");

    point.style.left = (200 + x_cord).toString() + 'px';
    point.style.top = (200 - y_cord).toString() + 'px';
    document.querySelector('.plane').appendChild(point);
}