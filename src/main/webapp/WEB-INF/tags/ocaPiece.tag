<%@ attribute name="size" required="true" rtexprvalue="true" 
 description="Size of the piece to show" %>
 <%@ attribute name="piece" required="true" rtexprvalue="true" type="org.springframework.samples.parchisoca.game.GamePiece"
 description="Piece to be rendered" %>
 <script>
 var canvas = document.getElementById("canvas");
 var ctx = canvas.getContext("2d");
 var image = document.getElementById('piece_red');
 

ctx.beginPath();
ctx.arc(${piece.getPositionXInPixelsOca(size)},${piece.getPositionYInPixelsOca(size)}, 10, 0, 2 * Math.PI);
console.log("piece: " +${piece.getPositionXInPixelsOca(size)} )
ctx.fillStyle = '${piece.getColorInHex()}';
ctx.fill();
ctx.strokeStyle = '#000000';
ctx.lineWidth = '2';
ctx.stroke();



 </script>
