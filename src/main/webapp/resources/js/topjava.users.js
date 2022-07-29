const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl,
    toggleEnabled:toggleEnabled
};

function toggleEnabled(checkbox) {
    const enabled = checkbox[0].checked;
    const userId = checkbox[0].closest('tr').id;
    //https://stackoverflow.com/questions/38945677/using-jquery-patch-to-make-partial-update
    $.ajax({
        type: "PATCH",
        url: "rest/"+ ctx.ajaxUrl + userId,
        contentType: 'application/json',
        data: JSON.stringify(enabled),
        processData: false,
    }).done(function () {
        checkbox.attr("data-is-enabled", enabled);
        console.log("User is: " + JSON.stringify(enabled))
        updateTable();
    });
}

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );
});