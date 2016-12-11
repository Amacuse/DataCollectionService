function del(id) {
    var result = confirm("Confirm the deletion");
    if (result) {
        $.ajax(jsRoutes.controllers.FieldController.deleteField(id)).
        done(function () {
            location.reload();
        }).fail(
            alert("Cannot delete now.Please try later")
        );
    }
}
