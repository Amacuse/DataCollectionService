function showOptions(elem) {
    if (elem.value == "Radio button" || elem.value == "Check box" || elem.value == "Combo box") {
        document.getElementById('options').style.display = "block";
    } else {
        document.getElementById('options').style.display = "none";
    }
}

function showHideField() {
    $(document).ready(function () {
        $(".ex .hide").click(function () {
            $(this).parents(".ex").hide("slow");
        });
    });
}


