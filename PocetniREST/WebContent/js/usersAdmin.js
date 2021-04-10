$(document).ready(function () {
    $.get({
        url: '/PocetniREST/rest/users/adminSearch',
        success: function(users) {
            createUsers(users);
        }
    });

    $("#myInput").keyup(function() {
        var value = $(this).val().toLowerCase();
        $("#myTable tr").filter(function() {
          $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
        });
      });
});

function createUsers(users) {
    for (var user of users) {
        $("#myTable").append('<tr><td>' + user.username +'</td><td>' + user.firstName + '</td><td>' + user.lastName + 
        '</td><td>' + user.gender + '</td><td>' + user.role.toLowerCase() + '</td><td>' + user.blocked + '</td></tr>');
    }
};