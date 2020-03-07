<script>
    ! function(window, document, $) {
        "use strict";
        $('.code').mask('00000000');
        $('.phone').mask("(+00) 000-000-00000");
        $('.zip').mask("00000-000");
        $('.vat').mask("00000000000000");
        $('.num').mask("00000000000000");
        $('.euro').mask("00000000000", {reverse: true});
        $('.ton').mask("000000000000", {reverse: true});
        $('.raty').mask("0");

    }(window, document, jQuery);

</script>