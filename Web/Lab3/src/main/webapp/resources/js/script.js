const VALID_CLASS_ID = "valid";
const INVALID_CLASS_ID = "invalid";

const form = document.getElementById("form");
const plane = document.getElementById('plane');
plane.addEventListener('click', handleGraphClick);

function handleGraphClick (evt) {
    const r = +document.forms["form"].elements["form:r-field"].value;

    const rect = plane.getBoundingClientRect();
    const x = (evt.clientX - rect.left - 200) / 160 * r;
    const y = (- (evt.clientY - rect.top) + 200) / 160 * r;
    console.log(r);
    console.log(x);
    console.log(y);

    document.getElementById("plain-form:hiddenR").value = r;
    document.getElementById("plain-form:hiddenX").value = x;
    document.getElementById("plain-form:hiddenY").value = y;

    updateBeanValues();
    location.reload();
}