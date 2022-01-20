<%@ attribute name="size" required="true" rtexprvalue="true" 
 description="Size of the piece to show" %>
 <%@ attribute name="piece" required="true" rtexprvalue="true" type="org.springframework.samples.parchisoca.model.game.GamePiece"
 description="Piece to be rendered" %>
 <script>
 var canvas = document.getElementById("canvas");
 var ctx = canvas.getContext("2d");
 

ctx.beginPath();
ctx.arc(${piece.getPositionXInPixels(size)},${piece.getPositionYInPixels(size)}, 10, 0, 2 * Math.PI);
ctx.fillStyle = '${piece.getColorInHex()}';
ctx.fill();
ctx.strokeStyle = '#000000';
ctx.lineWidth = '2';
ctx.stroke();




 </script>
