$(document).ready(function(){
    var user;
    $.get({
        url: '/PocetniREST/rest/users/currentUser',
        cache: false,
        success: function(data){
            user = data;
            console.log(user);
            $('#firstName').val(user.firstName);
            $('#lastName').val(user.lastName);
            $('#username').val(user.username);
            if(user.gender == 'male'){
                $('#male').attr('checked', true);
            }
            else{
                $('#female').attr('checked', true);
            }
        }
    });

    $('#confirmPassword').keyup(function(){
        let newPassword = $('#newPassword').val();
        let confirmPassword = $('#confirmPassword').val();

        if(confirmPassword == "" && newPassword == ""){
            $('#oldPassword').removeClass('is-invalid');
        }
        if(confirmPassword != newPassword){
            $('#confirmPassword').addClass('is-invalid');
        }
        if(confirmPassword === newPassword || confirmPassword == ""){
            $('#confirmPassword').removeClass('is-invalid');
        }
    });

    $('#newPassword').keyup(function(){
        let newPassword = $('#newPassword').val();
        let confirmPassword = $('#confirmPassword').val();

        if(confirmPassword == "" && newPassword == ""){
            $('#oldPassword').removeClass('is-invalid');
        }
        if(confirmPassword != newPassword && confirmPassword != ""){
            $('#confirmPassword').addClass('is-invalid');
        }
        if(confirmPassword === newPassword){
            $('#confirmPassword').removeClass('is-invalid');
        }
    });

    $('form').submit(function(){
        event.preventDefault();

        let firstName = $('#firstName').val();
        let lastName = $('#lastName').val();
        let username = user.username;
        let oldPassword = $('#oldPassword').val();
        let newPassword = $('#newPassword').val();
        let confirmPassword = $('#confirmPassword').val();
        let gender = "";
        
        if($('#female').is(':checked')){
            gender = "female";
        }
        if($('#male').is(':checked')){
            gender = "male";
        }

        if(oldPassword == "" && newPassword != ""){
            $('#oldPassword').addClass('is-invalid');
            $('#msg').text("Please confirm your old password.");
            $('#msg').show().delay(2000).fadeOut(2000);
        }
        else if(confirmPassword != newPassword){
            $('#msg').text("Passwords do not match.");
            $('#msg').show().delay(2000).fadeOut(2000);
        }
        else{
            $.ajax({
                url: '/PocetniREST/rest/users/myProfile',
                type: 'POST',
                data: JSON.stringify({username: username, oldPassword: oldPassword, newPassword: newPassword, confirmPassword: confirmPassword, firstName: firstName, lastName: lastName, gender: gender}),
                contentType: 'application/json',
                success: function(data){
                    $('#info').text(data);
                    $('#info').show().delay(2000).fadeOut(2000);
                },
                error: function(jqxhr){
                    $('#msg').text(jqxhr.responseText);
                    $('#msg').show().delay(2000).fadeOut(2000);
                }
            }); 
        }  

    });
});