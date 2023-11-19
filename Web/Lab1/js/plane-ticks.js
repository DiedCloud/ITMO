for (let i = -2; i <= 2; i++){
    if (i === 0){
        continue;
    }
    let x_tick = document.createElement('div');
    x_tick.className = 'x-tick';
    x_tick.style.left = (i * 80 + 200 - 1.5) + 'px';
    x_tick.style.top = '197px';
    document.querySelector('.plane').appendChild(x_tick);
}

for (let i = -2; i <= 2; i++){
    if (i === 0){
        continue;
    }
    let y_tick = document.createElement('div');
    y_tick.className = 'y-tick';
    y_tick.style.left = '197px';
    y_tick.style.top = (i * 80 + 200 - 1.5) + 'px';
    document.querySelector('.plane').appendChild(y_tick);
}