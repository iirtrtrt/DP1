<%@ attribute name="size" required="true" rtexprvalue="true" 
 description="Size of the field to show" %>
<%@ attribute name="field" required="true" rtexprvalue="true" type="org.springframework.samples.parchisoca.model.game.BoardField"
 description="Field to be rendered" %>

<script>
var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");

ctx.beginPath();
ctx.lineWidth = "1";
ctx.strokeStyle = "#964B00"; // brown
ctx.rect(${field.getPositionXluInPixels(size)},${field.getPositionYluInPixels(size)},${field.getPositionXrbInPixels(size)},${field.getPositionYrbInPixels(size)});
ctx.fillStyle = "${field.color}";
ctx.fill();

ctx.font="bold 15px Georgia";
ctx.fillStyle = "#000000";

if (${field.getNumber()} == 0) {
    ctx.textAlign="center"; 
    ctx.textBaseline = "middle";
    ctx.fillText("START!",${field.getPositionXluInPixels(size)} + ${field.getPositionXrbInPixels(size)}/2, ${field.getPositionYluInPixels(size)} + ${field.getPositionYrbInPixels(size)}/2);
    ctx.stroke();
} else if (${field.getNumber()} == 1 || ${field.getNumber()} == 5 || ${field.getNumber()} == 9 || ${field.getNumber()} == 14 || ${field.getNumber()} == 18 || ${field.getNumber()} == 23 || ${field.getNumber()} == 27 || ${field.getNumber()} == 32 || ${field.getNumber()} == 36 || ${field.getNumber()} == 41 || ${field.getNumber()} == 45 || ${field.getNumber()} == 50 || ${field.getNumber()} == 54 || ${field.getNumber()} == 59) {
    ctx.textAlign="left"; 
    ctx.textBaseline = "top";
    ctx.fillText("${field.getNumber()}",${field.getPositionXluInPixels(size)} + 5, ${field.getPositionYluInPixels(size)} + 5);
    ctx.textAlign="center"; 
    ctx.textBaseline = "middle";
    ctx.fillText("GOOSE",${field.getPositionXluInPixels(size)} + ${field.getPositionXrbInPixels(size)}/2, ${field.getPositionYluInPixels(size)} + ${field.getPositionYrbInPixels(size)}/2);
    ctx.stroke();
} else if (${field.getNumber()} == 6 || ${field.getNumber()} == 12) {
    ctx.textAlign="left"; 
    ctx.textBaseline = "top";
    ctx.fillText("${field.getNumber()}",${field.getPositionXluInPixels(size)} + 5, ${field.getPositionYluInPixels(size)} + 5);
    ctx.textAlign="center"; 
    ctx.textBaseline = "middle";
    ctx.fillText("BRIDGE",${field.getPositionXluInPixels(size)} + ${field.getPositionXrbInPixels(size)}/2, ${field.getPositionYluInPixels(size)} + ${field.getPositionYrbInPixels(size)}/2);
    ctx.stroke();
} else if (${field.getNumber()} == 26 || ${field.getNumber()} == 53) {
    ctx.textAlign="left"; 
    ctx.textBaseline = "top";
    ctx.fillText("${field.getNumber()}",${field.getPositionXluInPixels(size)} + 5, ${field.getPositionYluInPixels(size)} + 5);
    ctx.textAlign="center"; 
    ctx.textBaseline = "middle";
    ctx.fillText("DICE",${field.getPositionXluInPixels(size)} + ${field.getPositionXrbInPixels(size)}/2, ${field.getPositionYluInPixels(size)} + ${field.getPositionYrbInPixels(size)}/2);
    ctx.stroke();
} else if (${field.getNumber()} == 19 || ${field.getNumber()} == 31 || ${field.getNumber()} == 42 || ${field.getNumber()} == 52) {
    ctx.textAlign="left"; 
    ctx.textBaseline = "top";
    ctx.fillText("${field.getNumber()}",${field.getPositionXluInPixels(size)} + 5, ${field.getPositionYluInPixels(size)} + 5);
    ctx.textAlign="center"; 
    ctx.textBaseline = "middle";
    ctx.fillText("STUN",${field.getPositionXluInPixels(size)} + ${field.getPositionXrbInPixels(size)}/2, ${field.getPositionYluInPixels(size)} + ${field.getPositionYrbInPixels(size)}/2);
    ctx.stroke();
} else if (${field.getNumber()} == 58) {
    ctx.fillStyle = "#FFFFFF";
    ctx.textAlign="left"; 
    ctx.textBaseline = "top";
    ctx.fillText("${field.getNumber()}",${field.getPositionXluInPixels(size)} + 5, ${field.getPositionYluInPixels(size)} + 5);
    ctx.textAlign="center"; 
    ctx.textBaseline = "middle";
    ctx.fillText("DEATH",${field.getPositionXluInPixels(size)} + ${field.getPositionXrbInPixels(size)}/2, ${field.getPositionYluInPixels(size)} + ${field.getPositionYrbInPixels(size)}/2);
    ctx.stroke();
} else if (${field.getNumber()} == 63) {
    ctx.textAlign="left"; 
    ctx.textBaseline = "top";
    ctx.fillText("${field.getNumber()}",${field.getPositionXluInPixels(size)} + 5, ${field.getPositionYluInPixels(size)} + 5);
    ctx.textAlign="center"; 
    ctx.textBaseline = "middle";
    ctx.fillText("GOAL!",${field.getPositionXluInPixels(size)} + ${field.getPositionXrbInPixels(size)}/2, ${field.getPositionYluInPixels(size)} + ${field.getPositionYrbInPixels(size)}/2);
    ctx.stroke();
} else {
    ctx.textAlign="center"; 
    ctx.textBaseline = "middle";
    ctx.fillText("${field.getNumber()}",${field.getPositionXluInPixels(size)} + ${field.getPositionXrbInPixels(size)}/2, ${field.getPositionYluInPixels(size)} + ${field.getPositionYrbInPixels(size)}/2);
    ctx.stroke();
}

ctx.beginPath();
ctx.lineWidth = "5";
ctx.strokeStyle = '#000000';
ctx.rect(0, 0, 800, 800);
ctx.stroke();

ctx.beginPath();
ctx.lineWidth = "2.5";
ctx.moveTo(0, ${size} * 7);
ctx.lineTo(${size} * 7, ${size} * 7);

ctx.moveTo(${size} * 7, ${size} * 7);
ctx.lineTo(${size} * 7, ${size} * 1);

ctx.moveTo(${size} * 7, ${size} * 1);
ctx.lineTo(${size} * 1, ${size} * 1);

ctx.moveTo(${size} * 1, ${size} * 1);
ctx.lineTo(${size} * 1, ${size} * 6);

ctx.moveTo(${size} * 1, ${size} * 6);
ctx.lineTo(${size} * 6, ${size} * 6);

ctx.moveTo(${size} * 6, ${size} * 6);
ctx.lineTo(${size} * 6, ${size} * 2);

ctx.moveTo(${size} * 6, ${size} * 2);
ctx.lineTo(${size} * 2, ${size} * 2);

ctx.moveTo(${size} * 2, ${size} * 2);
ctx.lineTo(${size} * 2, ${size} * 5);

ctx.moveTo(${size} * 2, ${size} * 5);
ctx.lineTo(${size} * 5, ${size} * 5);

ctx.moveTo(${size} * 5, ${size} * 5);
ctx.lineTo(${size} * 5, ${size} * 3);

ctx.moveTo(${size} * 5, ${size} * 3);
ctx.lineTo(${size} * 3, ${size} * 3);

ctx.moveTo(${size} * 3, ${size} * 3);
ctx.lineTo(${size} * 3, ${size} * 4);

ctx.moveTo(${size} * 3, ${size} * 4);
ctx.lineTo(${size} * 4, ${size} * 4);
ctx.stroke();
</script>
