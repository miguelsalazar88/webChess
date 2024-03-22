function coloring(){
    const color = document.querySelectorAll('.box')
    color.forEach(color =>{
        getId = color.id;
        arr = Array.from(getId);
        arr.shift();
        aside = eval(arr.pop());
        aup = eval(arr.shift());
        a = aside + aup;

        if(a % 2 == 0){
            color.style.backgroundColor = 'rgb(181 136 99)';
        }

        if(a % 2 !== 0){
            color.style.backgroundColor = 'rgb(240 217 181)';
        }
    })
}

coloring();