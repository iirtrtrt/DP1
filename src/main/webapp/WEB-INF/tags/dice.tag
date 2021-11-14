<%@ attribute name="oca" required="false" rtexprvalue="true" type="org.springframework.samples.parchisoca.game.Oca"
 description="Oca to be rendered" %>
<div class="dice">
    <img id="diceImg" src="" class="border border-1 border-primary" width="100" height="100">
</div>
<button class="js-click">Roll Dice</button>

<script>
    RollDice = {
        init: function () {
            this.bindEvents();
            this.roll();
            this.render();
        },

        roll: function () {
            var sides = 6;
            var randomRoll = Math.floor(Math.random() * sides) + 1;
            return randomRoll;
        },

        printNumber: function (number) {
            console.log(number);
            if (number == 1) {
                $('#diceImg').prop('src', "/resources/images/1.png");
            } else if (number == 2) {
                $('#diceImg').prop('src', "/resources/images/2.png");
            } else if (number == 3) {
                $('#diceImg').prop('src', "/resources/images/3.png");
            } else if (number == 4) {
                $('#diceImg').prop('src', "/resources/images/4.png");
            } else if (number == 5) {
                $('#diceImg').prop('src', "/resources/images/5.png");
            } else if (number == 6) {
                $('#diceImg').prop('src', "/resources/images/6.png");
            } else {
                $('#diceImg').prop('src', "");
            }
        },

        bindEvents: function () {
            $('.js-click').on('click', function () {
                var result = RollDice.roll();
                RollDice.printNumber(result);
                if ($('.dice').hasClass('animated')) {
                    $('.dice').removeClass('animated')
                } else {
                    $('.dice').addClass('animated')
                }
            });
        },

        render: function () {
            var result = RollDice.roll();
            RollDice.printNumber(result);
        }
    }

    RollDice.init();

</script>
