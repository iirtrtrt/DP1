<%@ attribute name="oca" required="false" rtexprvalue="true" type="org.springframework.samples.parchisoca.model.game.Oca"
 description="Oca to be rendered" %>
<canvas id="canvas" width="${oca.width}" height="${oca.height}"></canvas>
<img id="source" src="${oca.background}" style="display:none">

<script>
var canvas = document.getElementById("canvas");
var ctx = canvas.getContext("2d");
var image = document.getElementById('source');

ctx.drawImage(image, 0, 0, ${oca.width}, ${oca.height});
</script>
