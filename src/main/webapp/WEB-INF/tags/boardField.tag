<%@ attribute name="size" required="true" rtexprvalue="true" 
 description="Size of the field to show" %>
 <%@ attribute name="field" required="true" rtexprvalue="true" type="org.springframework.samples.parchisoca.game.BoardField"
 description="Field to be rendered" %>
 <script>
 var canvas = document.getElementById("canvas");
 var ctx = canvas.getContext("2d");
 var image = document.getElementById('${field.type}_${field.color}');
 ctx.beginPath();
 ctx.lineWidth = "2";
 ctx.strokeStyle = "green";
 ctx.rect(${field.getPositionXluInPixels(size)},${field.getPositionYluInPixels(size)},${field.getPositionXrbInPixels(size)},${field.getPositionYrbInPixels(size)});
 ctx.fillStyle = "${field.color}";
 ctx.fill();

ctx.font="10px Georgia";
ctx.textAlign="center"; 
ctx.textBaseline = "middle"; 
ctx.fillStyle = "#000000";
ctx.fillText("${field.getId()}",${field.getPositionXluInPixels(size)} + ${field.getPositionXrbInPixels(size)}/2, ${field.getPositionYluInPixels(size)} + ${field.getPositionYrbInPixels(size)}/2);



ctx.stroke();
 
 </script>
