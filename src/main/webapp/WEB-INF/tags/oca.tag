<%@ attribute name="oca" required="false" rtexprvalue="true" type="org.springframework.samples.parchisoca.game.Oca"
 description="Oca to be rendered" %>
<canvas id="canvas" width="${oca.width}" height="${oca.height}"></canvas>
<img id="source" src="${oca.background}" style="display:none">

<img id="piece_BLUE" src="/resources/images/piece_BLUE.png" style="display:none">
<img id="piece_YELLOW" src="/resources/images/piece_YELLOW.png" style="display:none">
<img id="piece_GREEN" src="/resources/images/piece_GREEN.png" style="display:none">
<img id="piece_red" src="/resources/images/piece_RED.png" style="display:none">

<script>
var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");
var image = document.getElementById('source');
ctx.drawImage(image, 0, 0, ${oca.width}, ${oca.height});

ctx.beginPath();
ctx.lineWidth = "5";
ctx.rect( 0, 0, ${oca.width}, ${oca.height});
ctx.stroke();

ctx.beginPath();
ctx.arc(${oca.width}/20 * 3.5, ${oca.height}/20 * 3.5 , 100, 0, 2 * Math.PI);
ctx.fillStyle = '#e32908';
ctx.fill();
ctx.strokeStyle = '#e32908';
ctx.stroke();

ctx.beginPath();
ctx.arc(${oca.width}/20 * 16.5, ${oca.height}/20 * 3.5 , 100, 0, 2 * Math.PI);
ctx.fillStyle = '#0890e3';
ctx.fill();
ctx.strokeStyle = '#0890e3';
ctx.stroke();

ctx.beginPath();
ctx.arc(${oca.width}/20 * 16.5, ${oca.height}/20 * 16.5 , 100, 0, 2 * Math.PI);
ctx.fillStyle = '#dbe117';
ctx.fill();
ctx.strokeStyle = '#dbe117';
ctx.stroke();

ctx.beginPath();
ctx.arc(${oca.width}/20 * 3.5, ${oca.height}/20 * 16.5 , 100, 0, 2 * Math.PI);
ctx.fillStyle = '#26ca0c';
ctx.fill();
ctx.strokeStyle = '#26ca0c';
ctx.stroke();
</script>