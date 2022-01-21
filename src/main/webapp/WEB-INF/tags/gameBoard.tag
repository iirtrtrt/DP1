<%@ attribute name="gameBoard" required="false" rtexprvalue="true" type="org.springframework.samples.parchisoca.model.game.GameBoard"
 description="Gameboard to be rendered" %>
<canvas id="canvas" width="${gameBoard.width}" height="${gameBoard.height}"></canvas>
<img id="source" src="${gameBoard.background}">

<script>
var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");
var image = document.getElementById('source');

ctx.drawImage(image, 0, 0, ${gameBoard.width}, ${gameBoard.height});
</script>
