<%@ attribute name="oca" required="false" rtexprvalue="true" type="org.springframework.samples.parchisoca.game.Oca"
 description="Oca to be rendered" %>
<canvas id="canvas" width="${oca.width}" height="${oca.height}"></canvas>
<img id="source" src="${oca.background}" style="display:none">

<img id="piece_BLUE" src="resources/static/resources/images/piece_BLUE.png" style="display:none">
<img id="piece_YELLOW" src=""resources/static/resources/images/piece_YELLOW.png" style="display:none">
<img id="piece_GREEN" src=""resources/static/resources/images/piece_GREEN.png" style="display:none">
<img id="piece_red" src=""resources/static/resources/images/piece_RED.png" style="display:none">

<script>
var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");
var image = document.getElementById('source');


 ctx.beginPath();
 ctx.lineWidth = "5";
 ctx.rect( 0, 0, ${oca.width}, ${oca.height});
 ctx.stroke();






</script>