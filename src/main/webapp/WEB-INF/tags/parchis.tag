<%@ attribute name="parchis" required="false" rtexprvalue="true" type="org.springframework.samples.petclinic.game.Parchis"
 description="Parchis to be rendered" %>
<canvas id="canvas" width="${parchis.width}" height="${parchis.height}"></canvas>
<img id="source" src="${parchis.background}" style="display:none">

<img id="BLUE_PIECE" src="resources/images/piece_BLUE.png" style="display:none">
<img id="YELLOW_PIECE" src="resources/images/piece_YELLOW.png" style="display:none">
<img id="GREEN_PIECE" src="resources/images/piece_GREEN.png" style="display:none">
<img id="RED_PIECE" src="resources/images/piece_RED.png" style="display:none">


<img id="END_RED" src="resources/images/END_RED.png" style="display:none">
<img id="END_BLUE" src="resources/images/END_BLUE.png" style="display:none">
<img id="END_GREEN" src="resources/images/END_GREEN.png" style="display:none">
<img id="END_YELLOW" src="resources/images/END_YELLOW.png" style="display:none">
<img id="HORIZONTAL_WHITE" src="resources/images/HORIZONTAL.png" style="display:none">


<script>
var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");
var image = document.getElementById('source');

ctx.drawImage(image, 0, 0, ${parchis.width}, ${parchis.height});

ctx.beginPath();
ctx.arc(${parchis.width}/20 * 3.5, ${parchis.height}/20 * 3.5 , 100, 0, 2 * Math.PI);
ctx.fillStyle = '#e32908';
ctx.fill();
ctx.strokeStyle = '#e32908';
ctx.stroke();


ctx.beginPath();
ctx.arc(${parchis.width}/20 * 16.5, ${parchis.height}/20 * 3.5 , 100, 0, 2 * Math.PI);
ctx.fillStyle = '#0890e3';
ctx.fill();
ctx.strokeStyle = '#0890e3';
ctx.stroke();

ctx.beginPath();
ctx.arc(${parchis.width}/20 * 16.5, ${parchis.height}/20 * 16.5 , 100, 0, 2 * Math.PI);
ctx.fillStyle = '#dbe117';
ctx.fill();
ctx.strokeStyle = '#dbe117';
ctx.stroke();

ctx.beginPath();
ctx.arc(${parchis.width}/20 * 3.5, ${parchis.height}/20 * 16.5 , 100, 0, 2 * Math.PI);
ctx.fillStyle = '#26ca0c';
ctx.fill();
ctx.strokeStyle = '#26ca0c';
ctx.stroke();


</script>
