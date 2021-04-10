$(document).ready(function(){

    // redirekcija
    $.get({
        url: '/PocetniREST/rest/users/role',
        success: function(role){
            if(role == 'GUEST'){
                window.location.href = "http://localhost:8080/PocetniREST/html";
            }
            else if(role == 'HOST'){
                window.location.href = "http://localhost:8080/PocetniREST/html";
            }
            else if(role == 'ADMIN'){
                window.location.href = "http://localhost:8080/PocetniREST/html";
            }
        }
    });

    $('form').submit(function(){
        event.preventDefault();

        let username = $('#username').val();
        let password = $('#password').val();
        let rememberMe = $('#rememberMe').is(':checked');
        $('#username').removeClass('is-invalid');
        $('#password').removeClass('is-invalid');

        if(username == ""){
            $('#username').addClass('is-invalid');
        }
        if(password == ""){
            $('#password').addClass('is-invalid');
        }
        if(username != "" && password != ""){
            $.post({
                url: '/PocetniREST/rest/users',
                data: JSON.stringify({username: username, password: password, rememberMe: rememberMe}),
                contentType: 'application/json',
                success: function(role){
                    if(role == 'GUEST'){
                        window.location.href = "http://localhost:8080/PocetniREST/html";
                    }
                    else if(role == 'HOST'){
                        window.location.href = "http://localhost:8080/PocetniREST/html";
                    }
                    else if(role == 'ADMIN'){
                        window.location.href = "http://localhost:8080/PocetniREST/html";
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