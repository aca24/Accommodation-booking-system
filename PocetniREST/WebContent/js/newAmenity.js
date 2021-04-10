$(document).ready(function(){

    let id = -1;
    var urlParams = new URLSearchParams(window.location.search);
    if(urlParams.has('id')){
        id = urlParams.get('id');
        $('#title').html('Update amenity');
        $.get({
            url: '/PocetniREST/rest/amenities/'+ id,
            success: function(amenity){
                $('#name').val(amenity.name);
            }
        });
    }
    else{
        $('#title').html('New amenity');
    }

    $('#name').keyup(function(){
        $('#name').removeClass('is-invalid');
    });

    $('form').submit(function(){
        event.preventDefault();

        let name = $('#name').val();
        $('#name').removeClass('is-invalid');

        if(name == ""){
            $('#name').addClass('is-invalid');
            $('#msg').text("This field is required.");
            $('#msg').show().delay(2000).fadeOut(2000);
        }
        if(name != ""){
            $.post({
                url: '/PocetniREST/rest/amenities?id=' + id,
                data: JSON.stringify({name: name}),
                contentType: 'application/json',
                success: function(role){
                    window.location.href = "http://localhost:8080/PocetniREST/html/amenitiesAdmin.html";
                    
                },
                error: function(jqxhr){
                    $('#msg').text(jqxhr.responseText);
                    $('#msg').show().delay(2000).fadeOut(2000);
                }
            });
        }

        

    });

});