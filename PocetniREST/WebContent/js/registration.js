$(document).ready(function(){

    let role = 'GUEST';
    var urlParams = new URLSearchParams(window.location.search);
    if(urlParams.has('host')){
        role = 'HOST';
        $('#title').html('Host registration');
        $('#submit').attr('value', 'Register new host');
    }    

    $('#username').keyup(function(){
        let username = $('#username').val();
        $('#username').removeClass('is-invalid');

        if(username != ""){
            $.get({
                url: '/PocetniREST/rest/users/' + username,
                success: function(user){
                    console.log(user);
                    if(user != undefined){
                        $('#username').addClass('is-invalid');
                        $('#msg').text('This username is already taken.');
                        $('#msg').show().delay(2000).fadeOut(2000);
                    }
                }
            });
        } 
    });

    $('#confirmPassword').keyup(function(){
        let password = $('#password').val();
        let confirm = $('#confirmPassword').val();

        if(confirm != password){
            $('#confirmPassword').addClass('is-invalid');
        }
        if(confirm === password || confirm == ""){
            $('#confirmPassword').removeClass('is-invalid');
        }
    });

    $('#password').keyup(function(){
        let password = $('#password').val();
        let confirm = $('#confirmPassword').val();

        if(confirm != password && confirm != ""){
            $('#confirmPassword').addClass('is-invalid');
        }
        if(confirm === password){
            $('#confirmPassword').removeClass('is-invalid');
        }
    });

    $('form').submit(function(){
        event.preventDefault();

        let firstName = $('#firstName').val();
        let lastName = $('#lastName').val();
        let username = $('#username').val();
        let password = $('#password').val();
        let confirm = $('#confirmPassword').val();
        let gender = "";
        
        if($('#female').is(':checked')){
            gender = "female";
        }
        if($('#male').is(':checked')){
            gender = "male";
        }

        if($('#username').hasClass('is-invalid')){
            $('#msg').text("This username is already taken.");
            $('#msg').show().delay(2000).fadeOut(2000);
        }
        else if(confirm != password){
            $('#msg').text("Passwords do not match.");
            $('#msg').show().delay(2000).fadeOut(2000);
        }
        else{
            $.ajax({
                url: '/PocetniREST/rest/users/register/' + role,
                type: 'POST',
                data: JSON.stringify({username: username, password: password, confirmPassword: confirm, firstName: firstName, lastName: lastName, gender: gender}),
                contentType: 'application/json',
                success: function(){
                    if(role == 'HOST'){
                        window.location.href = "http://localhost:8080/PocetniREST/html/usersAdmin.html";
                    }
                    else{
                        window.location.href = "http://localhost:8080/PocetniREST/html/login.html";
                    }
                },
                error: function(jqxhr){
                    $('#msg').text(jqxhr.responseText);
                    $('#msg').show().delay(2000).fadeOut(2000);
                }
            }); 
        }  
    });
});