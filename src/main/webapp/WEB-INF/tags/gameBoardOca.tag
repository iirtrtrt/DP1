<%@ attribute name="gameBoardOca" required="false" rtexprvalue="true" type="org.springframework.samples.petclinic.game.GameBoard"
 description="Gameboard to be rendered" %>
<canvas id="canvas" width="${gameBoard.width}" height="${gameBoard.height}"></canvas>
<img id="source" src="resources/static/resources/images/board_oca.jpg" >

<img id="BLUE_PIECE" src="resources/static/resources/images/piece_BLUE.png" style="display:none">
<img id="YELLOW_PIECE" src="resources/static/resources/images/piece_YELLOW.png" style="display:none">
<img id="GREEN_PIECE" src="resources/static/resources/images/piece_GREEN.png" style="display:none">
<img id="RED_PIECE" src="resources/static/resources/images/piece_RED.png" style="display:none">

<script>
var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");
var image = document.getElementById('source');

ctx.drawImage(image, 0, 0, ${gameBoard.width}, ${gameBoard.height});
</script>