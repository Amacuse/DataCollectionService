$(function () {
    hideOrShow();

    $('#type').change(function () {
        hideOrShow()
    });

});

function hideOrShow() {
    var type = $('#type').val();
    if (type == "Radio button" || type == "Check box" || type == "Combo box") {
        $('#options').fadeIn('slow');
    } else {
        $('#options').fadeOut('slow');
    }
}





